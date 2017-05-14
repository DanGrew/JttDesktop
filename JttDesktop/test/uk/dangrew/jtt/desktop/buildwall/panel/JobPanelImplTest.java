/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.panel;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallThemeImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.description.DefaultJobPanelDescriptionImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.description.JobPanelDescriptionBaseImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.description.SimpleJobPanelDescriptionImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.desktop.styling.SystemStyling;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.sd.graphics.launch.TestApplication;

/**
 * {@link JobPanelImpl} test.
 */
public class JobPanelImplTest {

   private JenkinsJob job;
   private BuildWallTheme theme;
   private BuildWallConfiguration configuration;
   private JobPanelImpl systemUnderTest;
   
   @BeforeClass public static void initialiseStylings(){
      SystemStyling.initialise();
   }//End Method
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      job = new JenkinsJobImpl( "JenkinsTestTracker" );
      theme = new BuildWallThemeImpl( "Test" );
      TestApplication.startPlatform();
      configuration = new BuildWallConfigurationImpl();
      systemUnderTest = new JobPanelImpl( configuration, theme, job );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      job.expectedBuildTimeProperty().set( 1000000 );
      TestApplication.launch( () -> { return new JobPanelImpl( new BuildWallConfigurationImpl(), new BuildWallThemeImpl( "Test" ), job ); } );
      
      JobBuildSimulator.simulateBuilding( job, BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS, 1001, 300000, 100 );
      JobBuildSimulator.simulateBuilding( job, BuildResultStatus.SUCCESS, BuildResultStatus.SUCCESS, 1002, 600000, 100 );
      JobBuildSimulator.simulateBuilding( job, BuildResultStatus.UNSTABLE, BuildResultStatus.SUCCESS, 1003, 20000, 100 );
      JobBuildSimulator.simulateBuilding( job, BuildResultStatus.ABORTED, BuildResultStatus.SUCCESS, 1004, 234768, 100 );

      Thread.sleep( 10000000 );
   }//End Method
   
   @Test public void shouldHaveProgressAndDescription() {
      Assert.assertTrue( systemUnderTest.getChildren().get( 0 ) instanceof JobProgressImpl );
      assertThat( systemUnderTest.getChildren().get( 0 ), is( systemUnderTest.progress() ) );
      Assert.assertTrue( systemUnderTest.getChildren().get( 1 ) instanceof DefaultJobPanelDescriptionImpl );
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
   
   @Test public void shouldConstructDescriptionWhenProviderChanged(){
      JobPanelDescriptionBaseImpl initialDescription = systemUnderTest.description();
      assertThat( initialDescription.isDetached(), is( false ) );
      
      JobPanelDescriptionProviders provider = JobPanelDescriptionProviders.Simple;
      configuration.jobPanelDescriptionProvider().set( provider );
      
      JobPanelDescriptionBaseImpl updatedDescription = systemUnderTest.description();
      assertThat( updatedDescription, not( initialDescription ) );
      assertThat( updatedDescription, is( instanceOf( SimpleJobPanelDescriptionImpl.class ) ) );
      assertThat( initialDescription.isDetached(), is( true ) );
      assertThat( updatedDescription.isDetached(), is( false ) );
      assertThat( systemUnderTest.getChildren(), not( contains( initialDescription ) ) );
      assertThat( systemUnderTest.getChildren(), contains( systemUnderTest.progress(), updatedDescription ) );
   }//End Method
   
   @Test public void detachShouldUnregisterForProviderChanges(){
      JobPanelDescriptionBaseImpl initialDescription = systemUnderTest.description();
      assertThat( initialDescription.isDetached(), is( false ) );
      
      systemUnderTest.detachFromSystem();
      
      JobPanelDescriptionBaseImpl updatedDescription = systemUnderTest.description();
      assertThat( updatedDescription, is( instanceOf( DefaultJobPanelDescriptionImpl.class ) ) );
      
      JobPanelDescriptionProviders provider = JobPanelDescriptionProviders.Simple;
      configuration.jobPanelDescriptionProvider().set( provider );
      
      JobPanelDescriptionBaseImpl newestDescription = systemUnderTest.description();
      assertThat( newestDescription, is( instanceOf( DefaultJobPanelDescriptionImpl.class ) ) );
      assertThat( systemUnderTest.getChildren(), not( contains( systemUnderTest.progress(), updatedDescription ) ) );
   }//End Method
   
   @Test public void shouldAssociateProgressCorrectly(){
      assertThat( systemUnderTest.progress().isAssociatedWith( theme ), is( true ) );
      assertThat( systemUnderTest.progress().isAssociatedWith( job ), is( true ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWithThemeCorrectly(){
      assertThat( systemUnderTest.isAssociatedWith( theme ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( new BuildWallThemeImpl( "anything" ) ), is( false ) );
   }//End Method
   
}//End Class
