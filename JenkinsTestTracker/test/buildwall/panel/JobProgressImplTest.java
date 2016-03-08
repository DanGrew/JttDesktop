/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel;

import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import graphics.JavaFxInitializer;
import javafx.scene.control.ProgressBar;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import styling.BuildWallStyles;
import styling.SystemStyles;
import styling.SystemStyling;
import utility.TestCommon;

/**
 * {@link JobProgressImpl} test.
 */
public class JobProgressImplTest {

   private static SystemStyles styles;
   private JenkinsJob job;
   private JobProgressImpl systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      styles = Mockito.mock( SystemStyles.class );
      SystemStyling.set( styles );
      
      job = new JenkinsJobImpl( "MyTestJob" );
      job.currentBuildTimeProperty().set( 1000 );
      job.expectedBuildTimeProperty().set( 3000 );
      job.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      systemUnderTest = new JobProgressImpl( job );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> { return new JobProgressImpl( job ); } );
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void shouldProvideProgressInCenter() {
      Assert.assertEquals( systemUnderTest.progressBar(), systemUnderTest.getCenter() );
   }//End Method
   
   @Test public void shouldHaveInitialProgressAccordingToJob() {
      assertProgressMatchesJob();
   }//End Method
   
   @Test public void shouldUpdateProgressAccordingToJob() {
      assertProgressMatchesJob();
      job.currentBuildTimeProperty().set( 2000 );
      assertProgressMatchesJob();
      job.expectedBuildTimeProperty().set( 4000 );
      assertProgressMatchesJob();
   }//End Method
   
   /**
    * Method to assert that the progress in the {@link ProgressBar} matches the {@link JenkinsJob}.
    */
   private void assertProgressMatchesJob(){
      Assert.assertEquals( 
               JobProgressImpl.calculateProgress( job ), 
               systemUnderTest.progressBar().getProgress(),
               TestCommon.precision()
      );
   }//End Method
   
   @Test public void shouldHaveInitialStyleAccordingToJobState() {
      Mockito.verify( styles ).applyStyle( BuildWallStyles.ProgressBarSuccess, systemUnderTest.progressBar() );
   }//End Method
   
   @Test public void shouldUpdateStyleWhenJobStateUpdates() {
      Mockito.verify( styles ).applyStyle( BuildWallStyles.ProgressBarSuccess, systemUnderTest.progressBar() );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.ABORTED );
      Mockito.verify( styles ).applyStyle( BuildWallStyles.ProgressBarAborted, systemUnderTest.progressBar() );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.FAILURE );
      Mockito.verify( styles ).applyStyle( BuildWallStyles.ProgressBarFailed, systemUnderTest.progressBar() );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.NOT_BUILT );
      Mockito.verify( styles ).applyStyle( BuildWallStyles.ProgressBarNotBuilt, systemUnderTest.progressBar() );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      Mockito.verify( styles, Mockito.times( 2 ) ).applyStyle( BuildWallStyles.ProgressBarSuccess, systemUnderTest.progressBar() );
      
      job.lastBuildStatusProperty().set( BuildResultStatus.UNKNOWN );
      Mockito.verify( styles ).applyStyle( BuildWallStyles.ProgressBarUnknown, systemUnderTest.progressBar() );

      job.lastBuildStatusProperty().set( BuildResultStatus.UNSTABLE );
      Mockito.verify( styles ).applyStyle( BuildWallStyles.ProgressBarUnstable, systemUnderTest.progressBar() );
   }//End Method
   
   @Test public void shouldBindWDimensionsOfProgressToContainer() {
      Assert.assertEquals( systemUnderTest.getWidth(), systemUnderTest.progressBar().getWidth(), TestCommon.precision() );
      Assert.assertEquals( systemUnderTest.getHeight(), systemUnderTest.progressBar().getHeight(), TestCommon.precision() );
   }//End Method
   
   @Test public void shouldCalculateProgress(){
      job.currentBuildTimeProperty().set( 1000 );
      job.expectedBuildTimeProperty().set( 2000 );
      Assert.assertEquals( 0.5, JobProgressImpl.calculateProgress( job ), TestCommon.precision() );
      
      job.currentBuildTimeProperty().set( 3000 );
      job.expectedBuildTimeProperty().set( 2000 );
      Assert.assertEquals( 1.5, JobProgressImpl.calculateProgress( job ), TestCommon.precision() );
   }//End Method
   
   @Test public void detachShouldNotUpdateProgressAccordingToJob() {
      systemUnderTest.detachFromSystem();
      assertProgressMatchesJob();
      
      job.currentBuildTimeProperty().set( 2000 );
      Assert.assertNotEquals( 
               JobProgressImpl.calculateProgress( job ), 
               systemUnderTest.progressBar().getProgress(),
               TestCommon.precision()
      );
      job.expectedBuildTimeProperty().set( 4000 );
      Assert.assertNotEquals( 
               JobProgressImpl.calculateProgress( job ), 
               systemUnderTest.progressBar().getProgress(),
               TestCommon.precision()
      );
   }//End Method
   
   @Test public void detachShouldNotUpdateStyleWhenJobStateUpdates() {
      Mockito.verify( styles ).applyStyle( BuildWallStyles.ProgressBarSuccess, systemUnderTest.progressBar() );
      systemUnderTest.detachFromSystem();
      
      job.lastBuildStatusProperty().set( BuildResultStatus.ABORTED );
      verifyNoMoreInteractions( styles );
   }//End Method
   
}//End Class
