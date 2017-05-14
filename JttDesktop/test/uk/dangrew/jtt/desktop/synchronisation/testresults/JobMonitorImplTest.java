/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.synchronisation.testresults;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.sun.javafx.collections.NonIterableChange.SimpleAddChange;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import uk.dangrew.jtt.connection.api.handling.JenkinsFetcher;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;

/**
 * {@link JobMonitorImpl} test.
 */
public class JobMonitorImplTest {

   private JenkinsFetcher fetcher;
   private JenkinsDatabase database;
   private JobMonitorImpl systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      fetcher = Mockito.mock( JenkinsFetcher.class );
      database = new TestJenkinsDatabaseImpl();
      systemUnderTest = new JobMonitorImpl( database, fetcher );
   }//End Method
   
   @Test public void shouldFetchTestResultsForNewJob() {
      Mockito.verifyNoMoreInteractions( fetcher );
      
      JenkinsJob job = new JenkinsJobImpl( "first job" );
      database.store( job );
      
      Mockito.verify( fetcher ).updateTestResults( job );
      Mockito.verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldFetchTestResultsForNewJobs() {
      shouldFetchTestResultsForNewJob();
      
      JenkinsJob job = new JenkinsJobImpl( "second job" );
      database.store( job );
      
      Mockito.verify( fetcher ).updateTestResults( job );
      Mockito.verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldFetchTestResultsForNewLastBuildNumber() {
      JenkinsJob storedJob = new JenkinsJobImpl( "already stored" );
      database.store( storedJob );
      Mockito.verify( fetcher, Mockito.times( 1 ) ).updateTestResults( storedJob );
      
      storedJob.setBuildNumber( 21 );
      Mockito.verify( fetcher, Mockito.times( 2 ) ).updateTestResults( storedJob );
      Mockito.verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldNotFetchTestResultsForNewLastBuildNumberIfRemovedFromDatabase() {
      JenkinsJob storedJob = new JenkinsJobImpl( "already stored" );
      database.store( storedJob );
      Mockito.verify( fetcher, Mockito.times( 1 ) ).updateTestResults( storedJob );
      
      database.removeJenkinsJob( storedJob.nameProperty().get() );
      Assert.assertFalse( database.hasJenkinsJob( storedJob.nameProperty().get() ) );
      
      storedJob.setBuildNumber( 21 );
      Mockito.verify( fetcher, Mockito.times( 1 ) ).updateTestResults( storedJob );
      Mockito.verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldHandleOnChangeAddedForJenkinsJobList(){
      JenkinsJob job1 = new JenkinsJobImpl( "job1" );
      JenkinsJob job2 = new JenkinsJobImpl( "job2" );
      JenkinsJob job3 = new JenkinsJobImpl( "job3" );
      Change< JenkinsJob > change = new SimpleAddChange< JenkinsJob >( 0, 3, FXCollections.observableArrayList( job1, job2, job3 ) );
      systemUnderTest.onChanged( change );
      Mockito.verify( fetcher ).updateTestResults( job1 );
      Mockito.verify( fetcher ).updateTestResults( job2 );
      Mockito.verify( fetcher ).updateTestResults( job3 );
      Mockito.verifyNoMoreInteractions( fetcher );
   }//End Method

}//End Class
