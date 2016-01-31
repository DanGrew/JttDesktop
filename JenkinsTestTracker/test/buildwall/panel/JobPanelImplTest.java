/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.PlatformDecouplerImpl;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import styling.SystemStyling;

/**
 * {@link JobPanelImpl} test.
 */
public class JobPanelImplTest {

   private JenkinsJob job;
   private JobPanelImpl systemUnderTest;
   
   @BeforeClass public static void initialiseStylings(){
      SystemStyling.initialise();
   }//End Method
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      job = new JenkinsJobImpl( "JenkinsTestTracker" );
      JavaFxInitializer.startPlatform();
      systemUnderTest = new JobPanelImpl( new BuildWallConfigurationImpl(), job );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      job.expectedBuildTimeProperty().set( 1000000 );
      JavaFxInitializer.threadedLaunch( () -> { return new JobPanelImpl( new BuildWallConfigurationImpl(), job ); } );
      
      simulateBuilding( BuildResultStatus.FAILURE, 1001, 300000 );
      simulateBuilding( BuildResultStatus.SUCCESS, 1002, 600000 );
      simulateBuilding( BuildResultStatus.UNSTABLE, 1003, 20000 );
      simulateBuilding( BuildResultStatus.ABORTED, 1004, 234768 );

      Thread.sleep( 10000000 );
   }//End Method
   
   /**
    * Method to simulate the building of the associated {@link JenkinsJob}.
    * @param status the {@link BuildResultStatus} to simulate for.
    * @param number the build number.
    * @param expected the expected length of the build.
    */
   private void simulateBuilding( BuildResultStatus status, int number, long expected ) throws InterruptedException{
      job.lastBuildStatusProperty().set( status );
      job.lastBuildNumberProperty().set( number );
      job.currentBuildTimeProperty().set( 0 );
      job.expectedBuildTimeProperty().set( expected );
      for ( int i = 0; i < 101; i++ ) {
         Thread.sleep( 100 );
         job.currentBuildTimeProperty().set( ( expected / 100 ) * i );
      }
      Thread.sleep( 3000 );
   }//End Method
   
   @Test public void shouldHaveProgressAndDescription() {
      Assert.assertTrue( systemUnderTest.getChildren().get( 0 ) instanceof JobProgressImpl );
      Assert.assertTrue( systemUnderTest.getChildren().get( 1 ) instanceof JobPanelDescriptionImpl );
   }//End Method

}//End Class
