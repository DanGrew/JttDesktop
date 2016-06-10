/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core.testupdating;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import uk.dangrew.jtt.api.handling.BuildState;
import uk.dangrew.jtt.api.sources.ExternalApi;
import uk.dangrew.jtt.core.JenkinsTestTrackerCoreImpl;
import uk.dangrew.jtt.core.JttSystemCoreImpl;
import uk.dangrew.jtt.core.JttTestCoreImpl;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.tests.TestResultStatus;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.synchronisation.model.TimeKeeper;
import uk.dangrew.jtt.utility.TestCommon;
import uk.dangrew.jtt.view.table.TestTableView;

/**
 * {@link JenkinsTestTrackerCoreImpl} test.
 */
@Ignore
public class JttCoreTestUpdatingTest {

   @Ignore
   @Test public void manualInspection() throws InterruptedException {
      final ExternalApi api = Mockito.mock( ExternalApi.class );
      
      new Thread( () -> {
         
         String response = TestCommon.readFileIntoString( getClass(), "job.json" );
         Mockito.when( api.getJobsList() ).thenReturn( response );
         
         response = TestCommon.readFileIntoString( getClass(), "building-state.json" );
         Mockito.when( api.getLastBuildBuildingState( Mockito.any() ) ).thenReturn( response );
         
         convenienceWait( 1000 );
         
         response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
         Mockito.when( api.getLastBuildBuildingState( Mockito.any() ) ).thenReturn( response );
         
         convenienceWait( 1000 );
         
         response = TestCommon.readFileIntoString( getClass(), "all-passing-tests.json" );
         Mockito.when( api.getLatestTestResultsWrapped( Mockito.any() ) ).thenReturn( response );
         response = TestCommon.readFileIntoString( getClass(), "job-details-22.json" );
         Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
         
         convenienceWait( 5000 );
         
         response = TestCommon.readFileIntoString( getClass(), "some-failures-tests.json" );
         Mockito.when( api.getLatestTestResultsWrapped( Mockito.any() ) ).thenReturn( response );
         response = TestCommon.readFileIntoString( getClass(), "job-details-23.json" );
         Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
         
         convenienceWait( 5000 );
         
         response = TestCommon.readFileIntoString( getClass(), "all-passing-tests.json" );
         Mockito.when( api.getLatestTestResultsWrapped( Mockito.any() ) ).thenReturn( response );
         response = TestCommon.readFileIntoString( getClass(), "job-details-24.json" );
         Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
         
         convenienceWait( 5000 );
         
         response = TestCommon.readFileIntoString( getClass(), "large-failures-tests.json" );
         Mockito.when( api.getLatestTestResultsWrapped( Mockito.any() ) ).thenReturn( response );
         response = TestCommon.readFileIntoString( getClass(), "job-details-25.json" );
         Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
         
         convenienceWait( 5000 );
         
      } ).start();
      
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      JenkinsTestTrackerCoreImpl core = new JttSystemCoreImpl( api );
      JavaFxInitializer.launchInWindow( () -> { return new TestTableView( core.getJenkinsDatabase() ); } );
      Thread.sleep( 100000 );
   }//End Method
   
   /**
    * Method to prove a convenience method for waiting for a number of milliseconds.
    * @param millis the number of milliseconds to wait.
    */
   private void convenienceWait( long millis ){
      try {
         Thread.sleep( millis );
      } catch ( Exception e ) {
         e.printStackTrace();
      }
   }//End Method
   
   @Test public void shouldHaveDatabase(){
      Assert.assertNotNull( new JttTestCoreImpl( Mockito.mock( ExternalApi.class ) ).getJenkinsDatabase() );
   }//End Method
   
   @Test public void shouldHaveTimeKeeper(){
      Assert.assertNotNull( new JttTestCoreImpl( Mockito.mock( ExternalApi.class ) ).getJobUpdater() );
   }//End Method
   
   @Test public void shouldPullInChangesFromExternalApiAndUpdateDatabase(){
      final ExternalApi api = Mockito.mock( ExternalApi.class );
      JenkinsTestTrackerCoreImpl core = new JttTestCoreImpl( api );
      
      JenkinsDatabase database = core.getJenkinsDatabase();
      TimeKeeper timeKeeper = core.getJobUpdater();
      
      Assert.assertTrue( database.hasNoJenkinsJobs() );
      Assert.assertTrue( database.hasNoTestClasses() );
      
      //Pull in the only JenkinsJob...
      String response = TestCommon.readFileIntoString( getClass(), "job.json" );
      Mockito.when( api.getJobsList() ).thenReturn( response );
      timeKeeper.poll();
      
      //...and prove that nothing else changes, and the job is initially built...
      Assert.assertFalse( database.hasNoJenkinsJobs() );
      Assert.assertTrue( database.hasNoTestClasses() );
      Assert.assertEquals( 1, database.jenkinsJobs().size() );
      Assert.assertEquals( BuildState.Built, database.jenkinsJobs().get( 0 ).buildStateProperty().get() );
      
      //...then simulate the building state...
      response = TestCommon.readFileIntoString( getClass(), "building-state.json" );
      Mockito.when( api.getLastBuildBuildingState( Mockito.any() ) ).thenReturn( response );
      timeKeeper.poll();
      
      //...and prove the job starts building...
      Assert.assertFalse( database.hasNoJenkinsJobs() );
      Assert.assertTrue( database.hasNoTestClasses() );
      Assert.assertEquals( 1, database.jenkinsJobs().size() );
      Assert.assertEquals( BuildState.Building, database.jenkinsJobs().get( 0 ).buildStateProperty().get() );
      
      //...finish the build and provide test results...
      response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
      Mockito.when( api.getLastBuildBuildingState( Mockito.any() ) ).thenReturn( response );
      response = TestCommon.readFileIntoString( getClass(), "all-passing-tests.json" );
      Mockito.when( api.getLatestTestResultsWrapped( Mockito.any() ) ).thenReturn( response );
      response = TestCommon.readFileIntoString( getClass(), "job-details-22.json" );
      Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
      timeKeeper.poll();
      
      //...prove the build is complete and test results are parsed...
      Assert.assertFalse( database.hasNoJenkinsJobs() );
      Assert.assertFalse( database.hasNoTestClasses() );
      Assert.assertEquals( 1, database.jenkinsJobs().size() );
      Assert.assertEquals( 36, database.testClasses().size() );
      Assert.assertEquals( BuildState.Built, database.jenkinsJobs().get( 0 ).buildStateProperty().get() );
      Assert.assertEquals( TestResultStatus.PASSED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
      
      //...then provide a new build with some failures...
      response = TestCommon.readFileIntoString( getClass(), "some-failures-tests.json" );
      Mockito.when( api.getLatestTestResultsWrapped( Mockito.any() ) ).thenReturn( response );
      response = TestCommon.readFileIntoString( getClass(), "job-details-23.json" );
      Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
      timeKeeper.poll();
      
      //...and prove the failures are picked up...
      Assert.assertFalse( database.hasNoJenkinsJobs() );
      Assert.assertFalse( database.hasNoTestClasses() );
      Assert.assertEquals( 1, database.jenkinsJobs().size() );
      Assert.assertEquals( 36, database.testClasses().size() );
      Assert.assertEquals( TestResultStatus.FAILED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
      
      //...then pass all the tests in a further build...
      response = TestCommon.readFileIntoString( getClass(), "all-passing-tests.json" );
      Mockito.when( api.getLatestTestResultsWrapped( Mockito.any() ) ).thenReturn( response );
      response = TestCommon.readFileIntoString( getClass(), "job-details-24.json" );
      Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
      timeKeeper.poll();
      
      //...and prove that they are picked up...
      Assert.assertFalse( database.hasNoJenkinsJobs() );
      Assert.assertFalse( database.hasNoTestClasses() );
      Assert.assertEquals( 1, database.jenkinsJobs().size() );
      Assert.assertEquals( 36, database.testClasses().size() );
      Assert.assertEquals( TestResultStatus.PASSED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
      
      //...finally fail a large number of tests...
      response = TestCommon.readFileIntoString( getClass(), "large-failures-tests.json" );
      Mockito.when( api.getLatestTestResultsWrapped( Mockito.any() ) ).thenReturn( response );
      response = TestCommon.readFileIntoString( getClass(), "job-details-25.json" );
      Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
      timeKeeper.poll();
      
      //...and prove that they are picked up.
      Assert.assertFalse( database.hasNoJenkinsJobs() );
      Assert.assertFalse( database.hasNoTestClasses() );
      Assert.assertEquals( 1, database.jenkinsJobs().size() );
      Assert.assertEquals( 36, database.testClasses().size() );
      Assert.assertEquals( TestResultStatus.FAILED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
   }//End Method
   
   @Test public void shouldBringInMultipleJobsAndKeepThemUpdated(){
      final ExternalApi api = Mockito.mock( ExternalApi.class );
      JenkinsTestTrackerCoreImpl core = new JttTestCoreImpl( api );
      JenkinsDatabase database = core.getJenkinsDatabase();
      TimeKeeper timeKeeper = core.getJobUpdater();
      
      Assert.assertTrue( database.hasNoJenkinsJobs() );
      Assert.assertTrue( database.hasNoTestClasses() );
      
      //Pull in the only JenkinsJob...
      String response = TestCommon.readFileIntoString( getClass(), "multiple-jobs.json" );
      Mockito.when( api.getJobsList() ).thenReturn( response );
      timeKeeper.poll();
      
      //...and prove that nothing else changes, and the job is initially built...
      Assert.assertFalse( database.hasNoJenkinsJobs() );
      Assert.assertTrue( database.hasNoTestClasses() );
      Assert.assertEquals( 2, database.jenkinsJobs().size() );
      Assert.assertEquals( BuildState.Built, database.jenkinsJobs().get( 0 ).buildStateProperty().get() );
      
      final JenkinsJob first = database.jenkinsJobs().get( 0 );
      final JenkinsJob second = database.jenkinsJobs().get( 1 );
      
      //...then simulate the building state...
      response = TestCommon.readFileIntoString( getClass(), "building-state.json" );
      Mockito.when( api.getLastBuildBuildingState( first ) ).thenReturn( response );
      timeKeeper.poll();
      
      //...and prove the job starts building...
      Assert.assertFalse( database.hasNoJenkinsJobs() );
      Assert.assertTrue( database.hasNoTestClasses() );
      Assert.assertEquals( 2, database.jenkinsJobs().size() );
      Assert.assertEquals( BuildState.Building, first.buildStateProperty().get() );
      
      //...finish the build and provide test results...
      response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
      Mockito.when( api.getLastBuildBuildingState( first ) ).thenReturn( response );
      response = TestCommon.readFileIntoString( getClass(), "all-passing-tests.json" );
      Mockito.when( api.getLatestTestResultsWrapped( first ) ).thenReturn( response );
      response = TestCommon.readFileIntoString( getClass(), "job-details-22.json" );
      Mockito.when( api.getLastBuildJobDetails( first ) ).thenReturn( response );
      timeKeeper.poll();
      
      //...prove the build is complete and test results are parsed...
      Assert.assertFalse( database.hasNoJenkinsJobs() );
      Assert.assertFalse( database.hasNoTestClasses() );
      Assert.assertEquals( 2, database.jenkinsJobs().size() );
      Assert.assertEquals( 36, database.testClasses().size() );
      Assert.assertEquals( BuildState.Built, database.jenkinsJobs().get( 0 ).buildStateProperty().get() );
      Assert.assertEquals( TestResultStatus.PASSED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
      
      //...then provide a new build with some failures...
      response = TestCommon.readFileIntoString( getClass(), "some-failures-tests.json" );
      Mockito.when( api.getLatestTestResultsWrapped( second ) ).thenReturn( response );
      response = TestCommon.readFileIntoString( getClass(), "job-details-23.json" );
      Mockito.when( api.getLastBuildJobDetails( second ) ).thenReturn( response );
      timeKeeper.poll();
      
      //...and prove the failures are picked up...
      Assert.assertFalse( database.hasNoJenkinsJobs() );
      Assert.assertFalse( database.hasNoTestClasses() );
      Assert.assertEquals( 2, database.jenkinsJobs().size() );
      Assert.assertEquals( 36, database.testClasses().size() );
      Assert.assertEquals( TestResultStatus.FAILED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
      
      //...then pass all the tests in a further build...
      response = TestCommon.readFileIntoString( getClass(), "all-passing-tests.json" );
      Mockito.when( api.getLatestTestResultsWrapped( first ) ).thenReturn( response );
      response = TestCommon.readFileIntoString( getClass(), "job-details-24.json" );
      Mockito.when( api.getLastBuildJobDetails( first ) ).thenReturn( response );
      timeKeeper.poll();
      
      //...and prove that they are picked up...
      Assert.assertFalse( database.hasNoJenkinsJobs() );
      Assert.assertFalse( database.hasNoTestClasses() );
      Assert.assertEquals( 2, database.jenkinsJobs().size() );
      Assert.assertEquals( 36, database.testClasses().size() );
      Assert.assertEquals( TestResultStatus.PASSED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
      
      //...finally fail a large number of tests...
      response = TestCommon.readFileIntoString( getClass(), "large-failures-tests.json" );
      Mockito.when( api.getLatestTestResultsWrapped( second ) ).thenReturn( response );
      response = TestCommon.readFileIntoString( getClass(), "job-details-25.json" );
      Mockito.when( api.getLastBuildJobDetails( second ) ).thenReturn( response );
      timeKeeper.poll();
      
      //...and prove that they are picked up.
      Assert.assertFalse( database.hasNoJenkinsJobs() );
      Assert.assertFalse( database.hasNoTestClasses() );
      Assert.assertEquals( 2, database.jenkinsJobs().size() );
      Assert.assertEquals( 36, database.testClasses().size() );
      Assert.assertEquals( TestResultStatus.FAILED, database.testClasses().get( 0 ).testCasesList().get( 1 ).statusProperty().get() );
   }//End Method
   
}//End Class