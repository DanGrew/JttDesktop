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

import buildwall.configuration.BuildWallConfigurationImpl;
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
      JavaFxInitializer.launchInWindow( () -> { return new JobPanelImpl( new BuildWallConfigurationImpl(), job ); } );
      
      JobBuildSimulator.simulateBuilding( job, BuildResultStatus.FAILURE, 1001, 300000, 100 );
      JobBuildSimulator.simulateBuilding( job, BuildResultStatus.SUCCESS, 1002, 600000, 100 );
      JobBuildSimulator.simulateBuilding( job, BuildResultStatus.UNSTABLE, 1003, 20000, 100 );
      JobBuildSimulator.simulateBuilding( job, BuildResultStatus.ABORTED, 1004, 234768, 100 );

      Thread.sleep( 10000000 );
   }//End Method
   
   @Test public void shouldHaveProgressAndDescription() {
      Assert.assertTrue( systemUnderTest.getChildren().get( 0 ) instanceof JobProgressImpl );
      Assert.assertTrue( systemUnderTest.getChildren().get( 1 ) instanceof JobPanelDescriptionImpl );
   }//End Method
   
   @Test public void shouldProvideJob(){
      Assert.assertEquals( job, systemUnderTest.getJenkinsJob() );
   }//End Method

}//End Class
