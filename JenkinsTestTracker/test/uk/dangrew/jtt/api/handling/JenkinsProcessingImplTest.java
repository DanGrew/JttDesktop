/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.util.Pair;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.jtt.utility.TestCommon;
import uk.dangrew.jtt.utility.mockito.DoAnswerNullReturn;

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
   
   /**
    * Method to update the properties of a {@link JenkinsJob} when the {@link JenkinsFetcher} is requested to update details.
    * @param job the {@link JenkinsJob} to update.
    * @param buildNumber the build number updated to.
    * @param status the {@link BuildResultStatus} updated to.
    */
   private void updatePropertiesWhenJobDetailsUpdated( JenkinsJob job, int buildNumber, BuildResultStatus status ) {
      doAnswer( invocation -> { 
         job.lastBuildProperty().set( new Pair<>( buildNumber, status ) );
         return null;
      } ).when( fetcher ).updateJobDetails( job );
   }//End Method
   
   @Test public void shouldNotFetchTestResultsForJobsNotUnstable(){
      updatePropertiesWhenJobDetailsUpdated( firstJob, 10, BuildResultStatus.SUCCESS );
      updatePropertiesWhenJobDetailsUpdated( secondJob, 10, BuildResultStatus.SUCCESS );
      updatePropertiesWhenJobDetailsUpdated( thirdJob, 10, BuildResultStatus.SUCCESS );
      
      systemUnderTest.fetchJobsAndUpdateDetails();
      
      verify( fetcher, never() ).updateTestResults( firstJob );
      verify( fetcher, never() ).updateTestResults( secondJob );
      verify( fetcher, never() ).updateTestResults( thirdJob );
   }//End Method
   
   @Test public void shouldOnlyFetchTestResultsForJobsUnstableAndBuildNumberChanged(){
      updatePropertiesWhenJobDetailsUpdated( firstJob, 10, BuildResultStatus.SUCCESS );
      updatePropertiesWhenJobDetailsUpdated( secondJob, 10, BuildResultStatus.UNSTABLE );
      updatePropertiesWhenJobDetailsUpdated( thirdJob, 10, BuildResultStatus.FAILURE );
      
      systemUnderTest.fetchJobsAndUpdateDetails();
      
      verify( fetcher, never() ).updateTestResults( firstJob );
      verify( fetcher, times( 1 ) ).updateTestResults( secondJob );
      verify( fetcher, never() ).updateTestResults( thirdJob );
   }//End Method
   
   @Test public void shouldFetchTestResultsForJobsSucessFromUnstableAndBuildNumberChanged(){
      secondJob.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      
      updatePropertiesWhenJobDetailsUpdated( firstJob, 10, BuildResultStatus.SUCCESS );
      updatePropertiesWhenJobDetailsUpdated( secondJob, 10, BuildResultStatus.SUCCESS );
      updatePropertiesWhenJobDetailsUpdated( thirdJob, 10, BuildResultStatus.SUCCESS );
      
      systemUnderTest.fetchJobsAndUpdateDetails();
      
      verify( fetcher, never() ).updateTestResults( firstJob );
      verify( fetcher, times( 1 ) ).updateTestResults( secondJob );
      verify( fetcher, never() ).updateTestResults( thirdJob );
   }//End Method
   
   @Test public void shouldFetchTestResultsOnceForJobsUnstableToUnstableAndBuildNumberChanged(){
      secondJob.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      
      updatePropertiesWhenJobDetailsUpdated( firstJob, 10, BuildResultStatus.SUCCESS );
      updatePropertiesWhenJobDetailsUpdated( secondJob, 10, BuildResultStatus.UNSTABLE );
      updatePropertiesWhenJobDetailsUpdated( thirdJob, 10, BuildResultStatus.SUCCESS );
      
      systemUnderTest.fetchJobsAndUpdateDetails();
      
      verify( fetcher, never() ).updateTestResults( firstJob );
      verify( fetcher, times( 1 ) ).updateTestResults( secondJob );
      verify( fetcher, never() ).updateTestResults( thirdJob );
   }//End Method
   
   @Test public void shouldFetchTestResultsForJobsSuccessWhenWasUnstableAndBuildNumberChanged(){
      firstJob.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      
      doAnswer( invocation -> { 
         firstJob.lastBuildProperty().set( new Pair<>( 10, BuildResultStatus.SUCCESS ) );
         return null;
      } ).when( fetcher ).updateJobDetails( firstJob );
      
      doAnswer( invocation -> { 
         secondJob.lastBuildProperty().set( new Pair<>( 10, BuildResultStatus.SUCCESS ) );
         return null;
      } ).when( fetcher ).updateJobDetails( secondJob );
      
      doAnswer( invocation -> { 
         thirdJob.lastBuildProperty().set( new Pair<>( 10, BuildResultStatus.FAILURE ) );
         return null;
      } ).when( fetcher ).updateJobDetails( thirdJob );
      
      systemUnderTest.fetchJobsAndUpdateDetails();
      
      verify( fetcher, times( 1 ) ).updateTestResults( firstJob );
      verify( fetcher, never() ).updateTestResults( secondJob );
      verify( fetcher, never() ).updateTestResults( thirdJob );
   }//End Method
   
   @Test public void shouldNotConcurrentModificationWithJobChanges(){
      TestCommon.assertConcurrencyIsNotAnIssue( 
               count -> systemUnderTest.fetchJobsAndUpdateDetails(), 
               count -> database.store( new JenkinsJobImpl( "" + count ) ), 
               10 
      );
   }//End Method
   
   @Test public void shouldProcessOnlyThosePresentAtTheStartOfTheProcessing(){
      CountDownLatch latch = new CountDownLatch( 1 );
      doAnswer( invocation -> new DoAnswerNullReturn( () -> {
         try {
            latch.await();
         } catch ( Exception e ) {
            e.printStackTrace();
         }
      } ) ).when( fetcher ).updateJobDetails( firstJob );
      
      systemUnderTest.fetchJobsAndUpdateDetails();
      
      JenkinsJob extra = new JenkinsJobImpl( "Extra" );
      database.store( extra );
      latch.countDown();
      
      verify( fetcher, never() ).updateJobDetails( extra );
   }//End Method
   
}//End Class
