/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.handling;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import api.sources.ExternalApi;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;
import utility.TestCommon;

/**
 * {@link JenkinsFetcherImpl} test.
 */
public class JenkinsFetcherImplTest {

   private JenkinsJob jenkinsJob;
   private JenkinsDatabase database;
   private ExternalApi externalApi;
   private JenkinsFetcher systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      externalApi = Mockito.mock( ExternalApi.class );
      database = new JenkinsDatabaseImpl();
      jenkinsJob = new JenkinsJobImpl( "JenkinsJob" );
      database.store( jenkinsJob );
      systemUnderTest = new JenkinsFetcherImpl( database, externalApi );
   }//End Method

   @Test public void shouldUpdateJobBuilding() {
      jenkinsJob.buildStateProperty().set( BuildState.Built );
      Assert.assertEquals( BuildState.Built, jenkinsJob.buildStateProperty().get() );
      
      String response = TestCommon.readFileIntoString( getClass(), "building-state.json" );
      Mockito.when( externalApi.getLastBuildBuildingState( jenkinsJob ) ).thenReturn( response );
      systemUnderTest.updateBuildState( jenkinsJob );
      Assert.assertEquals( BuildState.Building, jenkinsJob.buildStateProperty().get() );
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
      
      Mockito.verify( externalApi, Mockito.times( 0 ) ).getLastBuildJobDetails( Mockito.any() );
   }//End Method
   
   @Test public void shouldNotUpdateJobDetailsWhenNullJob() {
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      systemUnderTest.updateJobDetails( null );
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      
      Mockito.verify( externalApi, Mockito.times( 0 ) ).getLastBuildJobDetails( Mockito.any() );
      Mockito.verify( externalApi, Mockito.times( 0 ) ).getLastBuildBuildingState( Mockito.any() );
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
   }//End Method
   
   @Test public void shouldNotFetchTestResultsForNullJob(){
      systemUnderTest.updateTestResults( null );
      Mockito.verifyZeroInteractions( externalApi );
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
   
   @Test public void shouldFetchJobsAndUpdateDetailsOnAllNotBuilding(){
      JenkinsFetcher spy = Mockito.spy( systemUnderTest );
      
      JenkinsJob buildingJob1 = new JenkinsJobImpl( "buildingJob1" );
      database.store( buildingJob1 );
      buildingJob1.buildStateProperty().set( BuildState.Building );
      JenkinsJob builtJob1 = new JenkinsJobImpl( "builtJob1" );
      database.store( builtJob1 );
      JenkinsJob buildingJob2 = new JenkinsJobImpl( "buildingJob2" );
      database.store( buildingJob2 );
      buildingJob2.buildStateProperty().set( BuildState.Building );
      JenkinsJob builtJob2 = new JenkinsJobImpl( "builtJob2" );
      database.store( builtJob2 );
      
      spy.fetchJobsAndUpdateDetails();
      Mockito.verify( spy ).fetchJobs();
      Mockito.verify( spy ).updateBuildState( buildingJob1 );
      Mockito.verify( spy ).updateBuildState( builtJob1 );
      Mockito.verify( spy ).updateBuildState( buildingJob2 );
      Mockito.verify( spy ).updateBuildState( builtJob2 );
      Mockito.verify( spy ).updateJobDetails( builtJob1 );
      Mockito.verify( spy ).updateJobDetails( builtJob2 );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectNullDatabaseInConstructor(){
      systemUnderTest = new JenkinsFetcherImpl( null, externalApi );
   }//End Method

   @Test( expected = IllegalArgumentException.class ) public void shouldRejectNullApiInConstructor(){
      systemUnderTest = new JenkinsFetcherImpl( database, null );
   }//End Method
   
}//End Class
