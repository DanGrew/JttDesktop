/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.core.jobupdating;

/**
 * {@link JenkinsTestTrackerCoreImpl} test.
 */
public class JttCoreJobUpdatingTest {

//   @Ignore
//   @Test public void manualInspection() throws InterruptedException {
//      SystemStyling.initialise();
//      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
//      
//      final ExternalApi api = Mockito.mock( ExternalApi.class );
//      final JttTestCoreImpl core = new JttTestCoreImpl( api );
//      
//      new Thread( () -> {
//         
//         String response = TestCommon.readFileIntoString( getClass(), "jobs.json" );
//         Mockito.when( api.getJobsList() ).thenReturn( response );
//         
//         response = TestCommon.readFileIntoString( getClass(), "job-details-success.json" );
//         Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
//         
//         convenienceWait( 1000, core.getJobUpdater() );
//         
//         JenkinsJob second = core.getJenkinsDatabase().jenkinsJobs().get( 1 );
//         response = TestCommon.readFileIntoString( getClass(), "building-state-10.json" );
//         Mockito.when( api.getLastBuildBuildingState( second ) ).thenReturn( response );
//         
//         convenienceWait( 2000, core.getJobUpdater() );
//         
//         JenkinsJob fifth = core.getJenkinsDatabase().jenkinsJobs().get( 4 );
//         response = TestCommon.readFileIntoString( getClass(), "building-state-10.json" );
//         Mockito.when( api.getLastBuildBuildingState( fifth ) ).thenReturn( response );
//         
//         convenienceWait( 8000, core.getJobUpdater() );
//         
//         response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
//         Mockito.when( api.getLastBuildBuildingState( second ) ).thenReturn( response );
//         response = TestCommon.readFileIntoString( getClass(), "job-details-unstable.json" );
//         Mockito.when( api.getLastBuildJobDetails( second ) ).thenReturn( response );
//         
//         convenienceWait( 10000, core.getJobUpdater() );
//         
//         response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
//         Mockito.when( api.getLastBuildBuildingState( fifth ) ).thenReturn( response );
//         response = TestCommon.readFileIntoString( getClass(), "job-details-failure.json" );
//         Mockito.when( api.getLastBuildJobDetails( fifth ) ).thenReturn( response );
//         
//         convenienceWait( 5000, core.getJobUpdater() );
//         
//      } ).start();
//      
//      new BuildProgressor( core.getJenkinsDatabase(), new Timer(), 500l );
//      JavaFxInitializer.launchInWindow( () -> { return new BuildWallDisplayImpl( core.getJenkinsDatabase() ); } );
//      Thread.sleep( 100000 );
//   }//End Method
//   
//   /**
//    * Method to prove a convenience method for waiting for a number of milliseconds.
//    * @param millis the number of milliseconds to wait.
//    */
//   private void convenienceWait( long millis, TimeKeeper timeKeeper ){
//      try {
//         timeKeeper.poll();
//         Thread.sleep( millis );
//      } catch ( Exception e ) {
//         e.printStackTrace();
//      }
//   }//End Method
//   
//   @Test public void shouldPullInJobsAndUpdateBuiltStateAndTimes(){
//      final ExternalApi api = Mockito.mock( ExternalApi.class );
//      JttTestCoreImpl core = new JttTestCoreImpl( api );
//      core.initialiseTimeKeepers();
//      JenkinsDatabase database = core.getJenkinsDatabase();
//      TimeKeeper jobUpdater = core.getJobUpdater();
//      
//      Assert.assertTrue( database.hasNoJenkinsJobs() );
//      Assert.assertTrue( database.hasNoTestClasses() );
//
//      //initialise jobs to success
//      String response = TestCommon.readFileIntoString( getClass(), "jobs.json" );
//      Mockito.when( api.getJobsList() ).thenReturn( response );
//      response = TestCommon.readFileIntoString( getClass(), "job-details-success.json" );
//      Mockito.when( api.getLastBuildJobDetails( Mockito.any() ) ).thenReturn( response );
//      
//      jobUpdater.poll();
//      Assert.assertEquals( 7, database.jenkinsJobs().size() );
//      Assert.assertTrue( database.hasNoTestClasses() );
//
//      //start building the second job
//      JenkinsJob second = core.getJenkinsDatabase().jenkinsJobs().get( 1 );
//      response = TestCommon.readFileIntoString( getClass(), "building-state-10.json" );
//      Mockito.when( api.getLastBuildBuildingState( second ) ).thenReturn( response );
//      
//      jobUpdater.poll();
//      core.setSimulatedTime( 2000 );
//      Assert.assertEquals( 10000, second.expectedBuildTimeProperty().get() );
//      Assert.assertEquals( 1000, second.currentBuildTimeProperty().get() );
//      
//      //start building the fifth, from the same point for convenience
//      JenkinsJob fifth = core.getJenkinsDatabase().jenkinsJobs().get( 4 );
//      response = TestCommon.readFileIntoString( getClass(), "building-state-10.json" );
//      Mockito.when( api.getLastBuildBuildingState( fifth ) ).thenReturn( response );
//      
//      jobUpdater.poll();
//      core.setSimulatedTime( 8000 );
//      Assert.assertEquals( 10000, second.expectedBuildTimeProperty().get() );
//      Assert.assertEquals( 7000, second.currentBuildTimeProperty().get() );
//      Assert.assertEquals( 10000, fifth.expectedBuildTimeProperty().get() );
//      Assert.assertEquals( 7000, fifth.currentBuildTimeProperty().get() );
//      
//      //finish building the second leaving it at unstable
//      response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
//      Mockito.when( api.getLastBuildBuildingState( second ) ).thenReturn( response );
//      response = TestCommon.readFileIntoString( getClass(), "job-details-unstable.json" );
//      Mockito.when( api.getLastBuildJobDetails( second ) ).thenReturn( response );
//      
//      jobUpdater.poll();
//      core.setSimulatedTime( 12000 );
//      Assert.assertEquals( 10000, second.expectedBuildTimeProperty().get() );
//      Assert.assertEquals( 0, second.currentBuildTimeProperty().get() );
//      Assert.assertEquals( BuildState.Built, second.buildStateProperty().get() );
//      Assert.assertEquals( 10000, fifth.expectedBuildTimeProperty().get() );
//      Assert.assertEquals( 11000, fifth.currentBuildTimeProperty().get() );
//      Assert.assertEquals( BuildState.Building, fifth.buildStateProperty().get() );
//      
//      //finish building the fifth, leaving it at failure
//      response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
//      Mockito.when( api.getLastBuildBuildingState( fifth ) ).thenReturn( response );
//      response = TestCommon.readFileIntoString( getClass(), "job-details-failure.json" );
//      Mockito.when( api.getLastBuildJobDetails( fifth ) ).thenReturn( response );
//      
//      jobUpdater.poll();
//      Assert.assertEquals( 10000, second.expectedBuildTimeProperty().get() );
//      Assert.assertEquals( 0, second.currentBuildTimeProperty().get() );
//      Assert.assertEquals( BuildState.Built, second.buildStateProperty().get() );
//      Assert.assertEquals( 10000, fifth.expectedBuildTimeProperty().get() );
//      Assert.assertEquals( 0, fifth.currentBuildTimeProperty().get() );
//      Assert.assertEquals( BuildState.Built, fifth.buildStateProperty().get() );
//   }//End Method
   
}//End Class
