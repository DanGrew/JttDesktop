/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.javafx.progressbar.DynamicProgressBarProperties;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.styling.BuildWallStyles;
import uk.dangrew.jtt.utility.TestCommon;

/**
 * {@link JobProgressImpl} test.
 */
public class JobProgressImplTest {

   @Mock private DynamicProgressBarProperties dynamicProperties;
   private JenkinsJob job;
   private JobProgressImpl systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      job = new JenkinsJobImpl( "MyTestJob" );
      job.currentBuildTimeProperty().set( 1000 );
      job.expectedBuildTimeProperty().set( 3000 );
      job.setLastBuildStatus( BuildResultStatus.SUCCESS );
      systemUnderTest = new JobProgressImpl( dynamicProperties, job );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> systemUnderTest );
      systemUnderTest.updateStyle( job );
      
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
      verify( dynamicProperties ).applyStandardColourFor( BuildWallStyles.ProgressBarSuccess, systemUnderTest.progressBar() );
   }//End Method
   
   @Test public void shouldUpdateStyleWhenJobStateUpdates() {
      verify( dynamicProperties ).applyStandardColourFor( BuildWallStyles.ProgressBarSuccess,systemUnderTest.progressBar() );
      
      job.setLastBuildStatus( BuildResultStatus.ABORTED );
      verify( dynamicProperties ).applyStandardColourFor( BuildWallStyles.ProgressBarAborted, systemUnderTest.progressBar() );
      
      job.setLastBuildStatus( BuildResultStatus.FAILURE );
      verify( dynamicProperties ).applyStandardColourFor( BuildWallStyles.ProgressBarFailed, systemUnderTest.progressBar() );
      
      job.setLastBuildStatus( BuildResultStatus.NOT_BUILT );
      verify( dynamicProperties ).applyStandardColourFor( BuildWallStyles.ProgressBarNotBuilt, systemUnderTest.progressBar() );
      
      job.setLastBuildStatus( BuildResultStatus.SUCCESS );
      verify( dynamicProperties, Mockito.times( 2 ) ).applyStandardColourFor( BuildWallStyles.ProgressBarSuccess, systemUnderTest.progressBar() );
      
      job.setLastBuildStatus( BuildResultStatus.UNKNOWN );
      verify( dynamicProperties ).applyStandardColourFor( BuildWallStyles.ProgressBarUnknown, systemUnderTest.progressBar() );

      job.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      verify( dynamicProperties ).applyStandardColourFor( BuildWallStyles.ProgressBarUnstable, systemUnderTest.progressBar() );
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
      verify( dynamicProperties ).applyStandardColourFor( BuildWallStyles.ProgressBarSuccess, systemUnderTest.progressBar() );
      systemUnderTest.detachFromSystem();
      
      job.setLastBuildStatus( BuildResultStatus.ABORTED );
      verifyNoMoreInteractions( dynamicProperties );
   }//End Method
   
   @Test public void shouldShowAsDetached(){
      assertThat( systemUnderTest.isDetached(), is( false ) );
      systemUnderTest.detachFromSystem();
      assertThat( systemUnderTest.isDetached(), is( true ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWith(){
      assertThat( systemUnderTest.isAssociatedWith( job ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new JenkinsJobImpl( "anything" ) ), is( false ) );
   }//End Method
   
}//End Class
