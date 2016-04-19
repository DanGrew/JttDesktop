/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.handling;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import api.sources.ExternalApi;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import model.users.JenkinsUser;
import model.users.JenkinsUserImpl;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;
import utility.TestCommon;

/**
 * {@link JenkinsFetcherImpl} test.
 */
public class JenkinsFetcherImplTest {

   private JenkinsJob jenkinsJob;
   private JenkinsUser jenkinsUser;
   private JenkinsDatabase database;
   @Mock private ExternalApi externalApi;
   @Mock private JenkinsFetcherDigest digest;
   private JenkinsFetcher systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      database = new JenkinsDatabaseImpl();
      jenkinsJob = new JenkinsJobImpl( "JenkinsJob" );
      jenkinsUser = new JenkinsUserImpl( "JenkinsUser" );
      database.store( jenkinsJob );
      database.store( jenkinsUser );
      systemUnderTest = new JenkinsFetcherImpl( database, externalApi, digest );
   }//End Method
   
   @Test public void shouldAttachFetcherToDigest(){
      verify( digest ).attachSource( systemUnderTest );
   }//End Method

   @Test public void shouldUpdateJobBuilding() {
      jenkinsJob.buildStateProperty().set( BuildState.Built );
      Assert.assertEquals( BuildState.Built, jenkinsJob.buildStateProperty().get() );
      
      String response = TestCommon.readFileIntoString( getClass(), "building-state.json" );
      Mockito.when( externalApi.getLastBuildBuildingState( jenkinsJob ) ).thenReturn( response );
      systemUnderTest.updateBuildState( jenkinsJob );
      Assert.assertEquals( BuildState.Building, jenkinsJob.buildStateProperty().get() );
      
      InOrder digestOrdering = inOrder( digest, externalApi );
      digestOrdering.verify( digest ).fetching( JenkinsFetcherDigest.BUILD_STATE, jenkinsJob );
      digestOrdering.verify( externalApi ).getLastBuildBuildingState( jenkinsJob );
      digestOrdering.verify( digest ).parsing( JenkinsFetcherDigest.BUILD_STATE, jenkinsJob );
      digestOrdering.verify( digest ).updated( JenkinsFetcherDigest.BUILD_STATE, jenkinsJob );
   }//End Method
   
   @Test public void shouldUpdateJobBuilt() {
      jenkinsJob.buildStateProperty().set( BuildState.Building );
      Assert.assertEquals( BuildState.Building, jenkinsJob.buildStateProperty().get() );
      
      String response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
      Mockito.when( externalApi.getLastBuildBuildingState( jenkinsJob ) ).thenReturn( response );
      systemUnderTest.updateBuildState( jenkinsJob );
      Assert.assertEquals( BuildState.Built, jenkinsJob.buildStateProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateJobDetailsWhenBuilt() {
      String buildingResponse = TestCommon.readFileIntoString( getClass(), "built-state.json" );
      Assert.assertNotNull( buildingResponse );
      Mockito.when( externalApi.getLastBuildBuildingState( jenkinsJob ) ).thenReturn( buildingResponse );
      
      String response = TestCommon.readFileIntoString( getClass(), "job-details.json" );
      Assert.assertNotNull( response );
      Mockito.when( externalApi.getLastBuildJobDetails( jenkinsJob ) ).thenReturn( response );
      
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      systemUnderTest.updateJobDetails( jenkinsJob );
      Assert.assertEquals( 22, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.SUCCESS, jenkinsJob.lastBuildStatusProperty().get() );
      
      InOrder digestOrdering = inOrder( digest, externalApi );
      digestOrdering.verify( digest ).fetching( JenkinsFetcherDigest.JOB_DETAIL, jenkinsJob );
      digestOrdering.verify( externalApi ).getLastBuildJobDetails( jenkinsJob );
      digestOrdering.verify( digest ).parsing( JenkinsFetcherDigest.JOB_DETAIL, jenkinsJob );
      digestOrdering.verify( digest ).updated( JenkinsFetcherDigest.JOB_DETAIL, jenkinsJob );
   }//End Method
   
   @Test public void shouldNotUpdateJobDetailsWhenBuilding() {
      String buildingResponse = TestCommon.readFileIntoString( getClass(), "building-state.json" );
      Assert.assertNotNull( buildingResponse );
      Mockito.when( externalApi.getLastBuildBuildingState( jenkinsJob ) ).thenReturn( buildingResponse );

      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      systemUnderTest.updateJobDetails( jenkinsJob );
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      
      verify( externalApi, Mockito.times( 0 ) ).getLastBuildJobDetails( Mockito.any() );
      
      InOrder digestOrdering = inOrder( digest, externalApi );
      digestOrdering.verify( digest ).fetching( JenkinsFetcherDigest.BUILD_STATE, jenkinsJob );
      digestOrdering.verify( externalApi ).getLastBuildBuildingState( jenkinsJob );
      digestOrdering.verify( digest ).parsing( JenkinsFetcherDigest.BUILD_STATE, jenkinsJob );
      digestOrdering.verify( digest ).updated( JenkinsFetcherDigest.BUILD_STATE, jenkinsJob );
   }//End Method
   
   @Test public void shouldUpdateJobDetailsWhenBuildingAndCurrentBuildNumberIsNotAfterLastBuildNumber() {
      String buildingResponse = TestCommon.readFileIntoString( getClass(), "building-state-24.json" );
      when( externalApi.getLastBuildBuildingState( jenkinsJob ) ).thenReturn( buildingResponse );
      String jobDetailsResponse = TestCommon.readFileIntoString( getClass(), "job-details-23.json" );
      when( externalApi.getLastBuildJobDetails( jenkinsJob ) ).thenReturn( jobDetailsResponse );

      jenkinsJob.lastBuildNumberProperty().set( 22 );
      systemUnderTest.updateJobDetails( jenkinsJob );
      assertEquals( 23, jenkinsJob.lastBuildNumberProperty().get() );
      assertEquals( 24, jenkinsJob.currentBuildNumberProperty().get() );
   }//End Method
   
   @Test public void shouldNotUpdateJobDetailsWhenBuildingAndCurrentBuildNumberIsOneAfterLastBuildNumber() {
      String buildingResponse = TestCommon.readFileIntoString( getClass(), "building-state-24.json" );
      when( externalApi.getLastBuildBuildingState( jenkinsJob ) ).thenReturn( buildingResponse );
      String jobDetailsResponse = TestCommon.readFileIntoString( getClass(), "job-details-23.json" );
      when( externalApi.getLastBuildJobDetails( jenkinsJob ) ).thenReturn( jobDetailsResponse );

      jenkinsJob.lastBuildNumberProperty().set( 23 );
      jenkinsJob.lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
      systemUnderTest.updateJobDetails( jenkinsJob );
      assertEquals( 23, jenkinsJob.lastBuildNumberProperty().get() );
      assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      assertEquals( 24, jenkinsJob.currentBuildNumberProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateJobDetailsWhenBuildingAndLastBuildNumberIsDefault() {
      String buildingResponse = TestCommon.readFileIntoString( getClass(), "building-state-24.json" );
      when( externalApi.getLastBuildBuildingState( jenkinsJob ) ).thenReturn( buildingResponse );
      String jobDetailsResponse = TestCommon.readFileIntoString( getClass(), "job-details-23.json" );
      when( externalApi.getLastBuildJobDetails( jenkinsJob ) ).thenReturn( jobDetailsResponse );

      assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      systemUnderTest.updateJobDetails( jenkinsJob );
      assertEquals( 23, jenkinsJob.lastBuildNumberProperty().get() );
   }//End Method
   
   @Test public void shouldNotUpdateJobDetailsWhenNullJob() {
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      systemUnderTest.updateJobDetails( null );
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      
      verify( externalApi, times( 0 ) ).getLastBuildJobDetails( Mockito.any() );
      verify( externalApi, times( 0 ) ).getLastBuildBuildingState( Mockito.any() );
      
      verify( digest ).attachSource( systemUnderTest );
      verifyNoMoreInteractions( digest );
   }//End Method
   
   @Test public void shouldFetchAllJobs(){
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list.json" );
      Assert.assertNotNull( response );
      Mockito.when( externalApi.getJobsList() ).thenReturn( response );
      
      systemUnderTest.fetchJobs();
      Assert.assertEquals( 8, database.jenkinsJobs().size() );
      Assert.assertEquals( jenkinsJob.nameProperty().get(), database.jenkinsJobs().get( 0 ).nameProperty().get() );
      Assert.assertEquals( "ClassicStuff", database.jenkinsJobs().get( 1 ).nameProperty().get() );
      Assert.assertEquals( "CommonProject", database.jenkinsJobs().get( 2 ).nameProperty().get() );
      Assert.assertEquals( "JenkinsTestTracker", database.jenkinsJobs().get( 3 ).nameProperty().get() );
      Assert.assertEquals( "MySpecialProject", database.jenkinsJobs().get( 4 ).nameProperty().get() );
      Assert.assertEquals( "Silly Project", database.jenkinsJobs().get( 5 ).nameProperty().get() );
      Assert.assertEquals( "SomeOtherTypeOfProject", database.jenkinsJobs().get( 6 ).nameProperty().get() );
      Assert.assertEquals( "Zebra!", database.jenkinsJobs().get( 7 ).nameProperty().get() );
      
      InOrder digestOrdering = inOrder( digest, externalApi );
      digestOrdering.verify( digest ).fetching( JenkinsFetcherDigest.JOBS );
      digestOrdering.verify( externalApi ).getJobsList();
      digestOrdering.verify( digest ).parsing( JenkinsFetcherDigest.JOBS );
      digestOrdering.verify( digest ).updated( JenkinsFetcherDigest.JOBS );
   }//End Method
   
   @Test public void shouldNotFetchTestResultsForNullJob(){
      verify( digest ).attachSource( systemUnderTest );
      systemUnderTest.updateTestResults( null );
      verifyZeroInteractions( externalApi, digest );
   }//End Method
   
   @Test public void shouldFetchTestResultsWrapped(){
      String response = TestCommon.readFileIntoString( getClass(), "single-test-case.json" );
      Assert.assertNotNull( response );
      Mockito.when( externalApi.getLatestTestResultsWrapped( jenkinsJob ) ).thenReturn( response );
      
      systemUnderTest.updateTestResults( jenkinsJob );
      Assert.assertEquals( 1, database.testClasses().size() );
      Assert.assertEquals( 1, database.testClasses().get( 0 ).testCasesList().size() );
   }//End Method
   
   @Test public void shouldFetchTestResultsUnwrapped(){
      String response = TestCommon.readFileIntoString( getClass(), "single-test-case-suites-only.json" );
      Assert.assertNotNull( response );
      Mockito.when( externalApi.getLatestTestResultsWrapped( jenkinsJob ) ).thenReturn( "{ }" );
      Mockito.when( externalApi.getLatestTestResultsUnwrapped( jenkinsJob ) ).thenReturn( response );
      
      systemUnderTest.updateTestResults( jenkinsJob );
      Assert.assertEquals( 1, database.testClasses().size() );
      Assert.assertEquals( 1, database.testClasses().get( 0 ).testCasesList().size() );
   }//End Method
   
   @Test public void shouldFetchTestResultsForEnabledTestResults(){
      systemUnderTest.updateTestResults( jenkinsJob );
      verify( externalApi ).getLatestTestResultsWrapped( jenkinsJob );
      verify( externalApi ).getLatestTestResultsUnwrapped( jenkinsJob );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectNullDatabaseInConstructor(){
      systemUnderTest = new JenkinsFetcherImpl( null, externalApi );
   }//End Method

   @Test( expected = IllegalArgumentException.class ) public void shouldRejectNullApiInConstructor(){
      systemUnderTest = new JenkinsFetcherImpl( database, null );
   }//End Method
   
   @Test public void shouldFetchAllUsers(){
      String response = TestCommon.readFileIntoString( getClass(), "users-list.json" );
      Assert.assertNotNull( response );
      Mockito.when( externalApi.getUsersList() ).thenReturn( response );
      
      systemUnderTest.fetchUsers();
      Assert.assertEquals( 6, database.jenkinsUsers().size() );
      Assert.assertEquals( jenkinsUser.nameProperty().get(), database.jenkinsUsers().get( 0 ).nameProperty().get() );
      Assert.assertEquals( "Dan Grew", database.jenkinsUsers().get( 1 ).nameProperty().get() );
      Assert.assertEquals( "jenkins", database.jenkinsUsers().get( 2 ).nameProperty().get() );
      Assert.assertEquals( "He who shall not be named", database.jenkinsUsers().get( 3 ).nameProperty().get() );
      Assert.assertEquals( "The Stig", database.jenkinsUsers().get( 4 ).nameProperty().get() );
      Assert.assertEquals( "Jeffrey", database.jenkinsUsers().get( 5 ).nameProperty().get() );
      
      InOrder digestOrdering = inOrder( digest, externalApi );
      digestOrdering.verify( digest ).fetching( JenkinsFetcherDigest.USERS );
      digestOrdering.verify( externalApi ).getUsersList();
      digestOrdering.verify( digest ).parsing( JenkinsFetcherDigest.USERS );
      digestOrdering.verify( digest ).updated( JenkinsFetcherDigest.USERS );
   }//End Method
   
}//End Class
