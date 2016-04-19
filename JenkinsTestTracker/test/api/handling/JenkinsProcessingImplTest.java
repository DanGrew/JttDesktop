/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.handling;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;

/**
 * {@link JenkinsProcessingImpl} test.
 */
public class JenkinsProcessingImplTest {

   private JenkinsDatabase database;
   private JenkinsJob firstJob;
   private JenkinsJob secondJob;
   private JenkinsJob thirdJob;
   
   @Mock private JenkinsFetcher fetcher;
   @Mock private JenkinsProcessingDigest digest;
   private JenkinsProcessingImpl systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      
      database = new JenkinsDatabaseImpl();
      firstJob = new JenkinsJobImpl( "first" );
      database.store( firstJob );
      secondJob = new JenkinsJobImpl( "second" );
      database.store( secondJob );
      thirdJob = new JenkinsJobImpl( "third" );
      database.store( thirdJob );
      
      systemUnderTest = new JenkinsProcessingImpl( database, fetcher, digest );
   }//End Method

   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullDatabase(){
      new JenkinsProcessingImpl( null, fetcher );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullFetcher(){
      new JenkinsProcessingImpl( database, null );
   }//End Method
   
   @Test public void updateBuildStateShouldCallThrough() {
      systemUnderTest.updateBuildState( firstJob );
      verify( fetcher ).updateBuildState( firstJob );
   }//End Method
   
   @Test public void updateJobDetailsShouldCallThrough() {
      systemUnderTest.updateJobDetails( firstJob );
      verify( fetcher ).updateJobDetails( firstJob );
   }//End Method
   
   @Test public void fetchJobsShouldCallThrough() {
      systemUnderTest.fetchJobs();
      verify( fetcher ).fetchJobs();
   }//End Method
   
   @Test public void fetchUsersShouldCallThrough() {
      systemUnderTest.fetchUsers();
      verify( fetcher ).fetchUsers();
   }//End Method
   
   @Test public void updateTestResultsShouldCallThrough() {
      systemUnderTest.updateTestResults( firstJob );
      verify( fetcher ).updateTestResults( firstJob );
   }//End Method
   
   @Test public void fetchJobsAndUpdateDetailsShouldFetchAndUpdateForEachJobInOrder(){
      systemUnderTest.fetchJobsAndUpdateDetails();
      
      InOrder jobUpdates = inOrder( fetcher, digest );
      jobUpdates.verify( fetcher ).fetchJobs();
      jobUpdates.verify( digest ).startUpdatingJobs( 3 );
      jobUpdates.verify( fetcher ).updateJobDetails( firstJob );
      jobUpdates.verify( digest ).updatedJob( firstJob );
      jobUpdates.verify( fetcher ).updateJobDetails( secondJob );
      jobUpdates.verify( digest ).updatedJob( secondJob );
      jobUpdates.verify( fetcher ).updateJobDetails( thirdJob );
      jobUpdates.verify( digest ).updatedJob( thirdJob );
      jobUpdates.verify( digest ).jobsUpdated();
   }//End Method
   
   @Test public void shouldNotFetchTestResultsForJobsNotUnstable(){
      doAnswer( invocation -> { 
         firstJob.lastBuildNumberProperty().set( 10 );
         firstJob.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
         return null;
      } ).when( fetcher ).updateJobDetails( firstJob );
      
      doAnswer( invocation -> { 
         secondJob.lastBuildNumberProperty().set( 10 );
         secondJob.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
         return null;
      } ).when( fetcher ).updateJobDetails( secondJob );
      
      doAnswer( invocation -> { 
         thirdJob.lastBuildNumberProperty().set( 10 );
         thirdJob.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
         return null;
      } ).when( fetcher ).updateJobDetails( thirdJob );
      
      systemUnderTest.fetchJobsAndUpdateDetails();
      
      verify( fetcher, never() ).updateTestResults( firstJob );
      verify( fetcher, never() ).updateTestResults( secondJob );
      verify( fetcher, never() ).updateTestResults( thirdJob );
   }
   
   @Test public void shouldOnlyFetchTestResultsForJobsUnstableAndBuildNumberChanged(){
      doAnswer( invocation -> { 
         firstJob.lastBuildNumberProperty().set( 10 );
         firstJob.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
         return null;
      } ).when( fetcher ).updateJobDetails( firstJob );
      
      doAnswer( invocation -> { 
         secondJob.lastBuildNumberProperty().set( 10 );
         secondJob.lastBuildStatusProperty().set( BuildResultStatus.UNSTABLE );
         return null;
      } ).when( fetcher ).updateJobDetails( secondJob );
      
      doAnswer( invocation -> { 
         thirdJob.lastBuildNumberProperty().set( 10 );
         thirdJob.lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
         return null;
      } ).when( fetcher ).updateJobDetails( thirdJob );
      
      systemUnderTest.fetchJobsAndUpdateDetails();
      
      verify( fetcher, never() ).updateTestResults( firstJob );
      verify( fetcher, times( 1 ) ).updateTestResults( secondJob );
      verify( fetcher, never() ).updateTestResults( thirdJob );
   }
   
//   @Test public void shouldFetchTestResultsForJobsSuccessWhenWasUnstableAndBuildNumberChanged(){
//      firstJob.lastBuildStatusProperty().set( BuildResultStatus.UNSTABLE );
//      
//      doAnswer( invocation -> { 
//         firstJob.lastBuildNumberProperty().set( 10 );
//         firstJob.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
//         return null;
//      } ).when( fetcher ).updateJobDetails( firstJob );
//      
//      doAnswer( invocation -> { 
//         secondJob.lastBuildNumberProperty().set( 10 );
//         secondJob.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
//         return null;
//      } ).when( fetcher ).updateJobDetails( secondJob );
//      
//      doAnswer( invocation -> { 
//         thirdJob.lastBuildNumberProperty().set( 10 );
//         thirdJob.lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
//         return null;
//      } ).when( fetcher ).updateJobDetails( thirdJob );
//      
//      systemUnderTest.fetchJobsAndUpdateDetails();
//      
//      verify( fetcher, times( 1 ) ).updateTestResults( firstJob );
//      verify( fetcher, never() ).updateTestResults( secondJob );
//      verify( fetcher, never() ).updateTestResults( thirdJob );
//   }
   
}//End Class
