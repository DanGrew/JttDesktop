/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

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
      
      JobBuildSimulator.simulateBuilding( job, BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS, 1001, 300000, 100 );
      JobBuildSimulator.simulateBuilding( job, BuildResultStatus.SUCCESS, BuildResultStatus.SUCCESS, 1002, 600000, 100 );
      JobBuildSimulator.simulateBuilding( job, BuildResultStatus.UNSTABLE, BuildResultStatus.SUCCESS, 1003, 20000, 100 );
      JobBuildSimulator.simulateBuilding( job, BuildResultStatus.ABORTED, BuildResultStatus.SUCCESS, 1004, 234768, 100 );

      Thread.sleep( 10000000 );
   }//End Method
   
   @Test public void shouldHaveProgressAndDescription() {
      Assert.assertTrue( systemUnderTest.getChildren().get( 0 ) instanceof JobProgressImpl );
      assertThat( systemUnderTest.getChildren().get( 0 ), is( systemUnderTest.progress() ) );
      Assert.assertTrue( systemUnderTest.getChildren().get( 1 ) instanceof JobPanelDescriptionImpl );
      assertThat( systemUnderTest.getChildren().get( 1 ), is( systemUnderTest.description() ) );
   }//End Method
   
   @Test public void shouldProvideJob(){
      Assert.assertEquals( job, systemUnderTest.getJenkinsJob() );
   }//End Method
   
   @Test public void detachShouldDetachProgressAndDescription(){
      assertThat( systemUnderTest.progress().isDetached(), is( false ) );
      assertThat( systemUnderTest.description().isDetached(), is( false ) );
      
      systemUnderTest.detachFromSystem();
      
      assertThat( systemUnderTest.getChildren(), empty() );
      assertThat( systemUnderTest.progress().isDetached(), is( true ) );
      assertThat( systemUnderTest.description().isDetached(), is( true ) );
   }//End Method
   
   @Test public void shouldShowAsDetached(){
      assertThat( systemUnderTest.isDetached(), is( false ) );
      systemUnderTest.detachFromSystem();
      assertThat( systemUnderTest.isDetached(), is( true ) );
   }//End Method

}//End Class
