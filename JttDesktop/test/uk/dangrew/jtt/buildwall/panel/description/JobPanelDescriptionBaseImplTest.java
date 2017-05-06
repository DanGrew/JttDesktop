/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel.description;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallThemeImpl;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.utility.TestCommon;
import uk.dangrew.jtt.system.properties.DateAndTimes;

/**
 * {@link JobPanelDescriptionBaseImpl} test.
 */
public abstract class JobPanelDescriptionBaseImplTest {

   protected JenkinsJob job;
   protected BuildWallConfiguration configuration;
   protected BuildWallTheme theme;
   protected JobPanelDescriptionBaseImpl systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      DateAndTimes.initialise();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      job = new JenkinsJobImpl( "anything" );
      job.currentBuildTimeProperty().set( 1000 );
      job.expectedBuildTimeProperty().set( 10000 );
      job.setBuildNumber( 345 );
      configuration = new BuildWallConfigurationImpl();
      theme = new BuildWallThemeImpl( "Anything" );
      JavaFxInitializer.startPlatform();
      systemUnderTest = constructSut();
   }//End Method
   
   /**
    * Method to construct the particular {@link JobPanelDescriptionBaseImpl} being tested.
    * @return the {@link JobPanelDescriptionBaseImpl}.
    */
   protected abstract JobPanelDescriptionBaseImpl constructSut();
   
   @Test public void shouldUseJobNameColourFromThemeInitially(){
      systemUnderTest.detachFromSystem();
      theme.jobNameColoursMap().put( job.getBuildStatus(), Color.AZURE );
      systemUnderTest = constructSut();
      assertThat( systemUnderTest.jobName().getTextFill(), is( Color.AZURE ) );
   }//End Method
   
   @Test public void shouldUseBuildNumberColourFromThemeInitially(){
      systemUnderTest.detachFromSystem();
      theme.buildNumberColoursMap().put( job.getBuildStatus(), Color.AZURE );
      systemUnderTest = constructSut();
      assertThat( systemUnderTest.buildNumber().getTextFill(), is( Color.AZURE ) );
   }//End Method
   
   @Test public void shouldUseCompletionEstimateColourFromThemeInitially(){
      systemUnderTest.detachFromSystem();
      theme.completionEstimateColoursMap().put( job.getBuildStatus(), Color.AZURE );
      systemUnderTest = constructSut();
      assertThat( systemUnderTest.completionEstimate().getTextFill(), is( Color.AZURE ) );
   }//End Method
   
   @Test public void shouldUseBuildNumberConfigurations() {
      Assert.assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumber().textFillProperty().get() );
      Assert.assertEquals( configuration.buildNumberFont().get(), systemUnderTest.buildNumber().fontProperty().get() );
   }//End Method
   
   @Test public void shouldUseThemeForBuildNumberColourIfProvided(){
      theme.buildNumberColoursMap().put( JenkinsJob.DEFAULT_LAST_BUILD_STATUS, Color.YELLOW );
      assertThat( systemUnderTest.buildNumber().getTextFill(), is( Color.YELLOW ) );
   }//End Method
   
   @Test public void shouldUseCompletionEstimateConfigurations() {
      Assert.assertEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimate().textFillProperty().get() );
      Assert.assertEquals( configuration.completionEstimateFont().get(), systemUnderTest.completionEstimate().fontProperty().get() );
   }//End Method
   
   @Test public void shouldUseThemeForCompletionEstimateColourIfProvided(){
      theme.completionEstimateColoursMap().put( JenkinsJob.DEFAULT_LAST_BUILD_STATUS, Color.YELLOW );
      assertThat( systemUnderTest.completionEstimate().getTextFill(), is( Color.YELLOW ) );
   }//End Method
   
   @Test public void shouldUseJobNameConfigurations() {
      Assert.assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobName().textFillProperty().get() );
      Assert.assertEquals( configuration.jobNameFont().get(), systemUnderTest.jobName().fontProperty().get() );
   }//End Method
   
   @Test public void shouldUseThemeForJobNameColourIfProvided(){
      theme.jobNameColoursMap().put( JenkinsJob.DEFAULT_LAST_BUILD_STATUS, Color.YELLOW );
      assertThat( systemUnderTest.jobName().getTextFill(), is( Color.YELLOW ) );
   }//End Method
   
   @Test public void shouldUseStatusSpecificColourForJobName(){
      theme.jobNameColoursMap().put( BuildResultStatus.SUCCESS, Color.YELLOW );
      assertThat( systemUnderTest.jobName().getTextFill(), is( not( Color.YELLOW ) ) );
   }//End Method
   
   @Test public void shouldUseStatusSpecificColourForBuildNumber(){
      theme.buildNumberColoursMap().put( BuildResultStatus.SUCCESS, Color.YELLOW );
      assertThat( systemUnderTest.buildNumber().getTextFill(), is( not( Color.YELLOW ) ) );
   }//End Method
   
   @Test public void shouldUseStatusSpecificColourForCompletionEstimate(){
      theme.completionEstimateColoursMap().put( BuildResultStatus.SUCCESS, Color.YELLOW );
      assertThat( systemUnderTest.completionEstimate().getTextFill(), is( not( Color.YELLOW ) ) );
   }//End Method
   
   @Test public void shouldAvoidFeedBackFromPropertyToConfigurationToThemeForJobName(){
      systemUnderTest.jobName().setTextFill( Color.RED );
      assertThat( configuration.jobNameColour().get(), is( not( Color.RED ) ) );
   }//End Method
   
   @Test public void shouldAvoidFeedBackFromPropertyToConfigurationToThemeForBuildNumber(){
      systemUnderTest.buildNumber().setTextFill( Color.RED );
      assertThat( configuration.buildNumberColour().get(), is( not( Color.RED ) ) );
   }//End Method
   
   @Test public void shouldAvoidFeedBackFromPropertyToConfigurationToThemeForCompletionEstimate(){
      systemUnderTest.completionEstimate().setTextFill( Color.RED );
      assertThat( configuration.completionEstimateColour().get(), is( not( Color.RED ) ) );
   }//End Method
   
   @Test public void shouldUpdateBuildNumberConfigurations() {
      final Font testFont = new Font( 100 );
      
      configuration.buildNumberColour().set( Color.BLACK );
      Assert.assertEquals( Color.BLACK, systemUnderTest.buildNumber().textFillProperty().get() );
      
      configuration.buildNumberFont().set( testFont );
      Assert.assertEquals( testFont, systemUnderTest.buildNumber().fontProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateCompletionEstimateConfigurations() {
      final Font testFont = new Font( 100 );
      
      configuration.completionEstimateColour().set( Color.BLACK );
      Assert.assertEquals( Color.BLACK, systemUnderTest.completionEstimate().textFillProperty().get() );
      
      configuration.completionEstimateFont().set( testFont );
      Assert.assertEquals( testFont, systemUnderTest.completionEstimate().fontProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateJobNameConfigurations() {
      configuration.jobNameColour().set( Color.BLACK );
      Assert.assertEquals( Color.BLACK, systemUnderTest.jobName().textFillProperty().get() );
      
      final Font testFont = new Font( 100 );
      configuration.jobNameFont().set( testFont );
      Assert.assertEquals( testFont, systemUnderTest.jobName().fontProperty().get() );
   }//End Method
   
   @Test public void propertiesShouldBeSlightlyTransparent(){
      Assert.assertEquals( JobPanelDescriptionBaseImpl.DEFAULT_PROPERTY_OPACITY, systemUnderTest.buildNumber().getOpacity(), TestCommon.precision() );
      Assert.assertEquals( JobPanelDescriptionBaseImpl.DEFAULT_PROPERTY_OPACITY, systemUnderTest.completionEstimate().getOpacity(), TestCommon.precision() );
   }//End Method
   
   @Test public void propertiesShouldBeInsetFromEdge(){
      Assert.assertEquals( JobPanelDescriptionBaseImpl.PROPERTIES_INSET, systemUnderTest.getInsets().getBottom(), TestCommon.precision() );
      Assert.assertEquals( JobPanelDescriptionBaseImpl.PROPERTIES_INSET, systemUnderTest.getInsets().getTop(), TestCommon.precision() );
      Assert.assertEquals( JobPanelDescriptionBaseImpl.PROPERTIES_INSET, systemUnderTest.getInsets().getRight(), TestCommon.precision() );
      Assert.assertEquals( JobPanelDescriptionBaseImpl.PROPERTIES_INSET, systemUnderTest.getInsets().getLeft(), TestCommon.precision() );
   }//End Method
   
   @Test public void shouldUseJobNameAndKeepUpdated(){
      Assert.assertEquals( job.nameProperty().get(), systemUnderTest.jobName().getText() );
      
      final String value = "somethingElse";
      job.nameProperty().set( value );
      Assert.assertEquals( job.nameProperty().get(), systemUnderTest.jobName().getText() );
      Assert.assertEquals( value, systemUnderTest.jobName().getText() );
   }//End Method
   
   @Test public void shouldUseJobNumberAndKeepUpdated(){
      Assert.assertEquals( 
               JobPanelDescriptionBaseImpl.formatBuildNumberAndTimestamp( job.getBuildNumber(), job.buildTimestampProperty().get() ), 
               systemUnderTest.buildNumber().getText() 
      );
      
      final Integer value = 799;
      job.setBuildNumber( value );
      Assert.assertEquals( 
               JobPanelDescriptionBaseImpl.formatBuildNumberAndTimestamp( job.getBuildNumber(), job.buildTimestampProperty().get() ), 
               systemUnderTest.buildNumber().getText() 
      );
      Assert.assertEquals( 
               JobPanelDescriptionBaseImpl.formatBuildNumberAndTimestamp( value, job.buildTimestampProperty().get() ),
               systemUnderTest.buildNumber().getText() 
      );
      
      final long timestamp = 3756298;
      job.buildTimestampProperty().set( timestamp );
      Assert.assertEquals( 
               JobPanelDescriptionBaseImpl.formatBuildNumberAndTimestamp( job.getBuildNumber(), job.buildTimestampProperty().get() ), 
               systemUnderTest.buildNumber().getText() 
      );
      Assert.assertEquals( 
               JobPanelDescriptionBaseImpl.formatBuildNumberAndTimestamp( job.getBuildNumber(), timestamp ),
               systemUnderTest.buildNumber().getText() 
      );
   }//End Method

   @Test public void shouldUseJobBuildTimeAndKeepUpdated(){
      Assert.assertEquals( 
               JobPanelDescriptionBaseImpl.formatCompletionEstimateInMilliseconds( 
                        job.currentBuildTimeProperty().get(),
                        job.expectedBuildTimeProperty().get()
               ), 
               systemUnderTest.completionEstimate().getText() 
      );
      
      final Integer progress = 180;
      job.currentBuildTimeProperty().set( progress );
      Assert.assertEquals( 
               JobPanelDescriptionBaseImpl.formatCompletionEstimateInMilliseconds( 
                        job.currentBuildTimeProperty().get(),
                        job.expectedBuildTimeProperty().get()
               ), 
               systemUnderTest.completionEstimate().getText()  
      );
      Assert.assertEquals( 
               JobPanelDescriptionBaseImpl.formatCompletionEstimateInMilliseconds( 
                        progress,
                        job.expectedBuildTimeProperty().get()
               ), 
               systemUnderTest.completionEstimate().getText()  
      );
      
      final Integer estimate = 180;
      job.expectedBuildTimeProperty().set( estimate );
      Assert.assertEquals( 
               JobPanelDescriptionBaseImpl.formatCompletionEstimateInMilliseconds( 
                        job.currentBuildTimeProperty().get(),
                        job.expectedBuildTimeProperty().get()
               ), 
               systemUnderTest.completionEstimate().getText()  
      );
      Assert.assertEquals( 
               JobPanelDescriptionBaseImpl.formatCompletionEstimateInMilliseconds( 
                        progress,
                        estimate
               ), 
               systemUnderTest.completionEstimate().getText()  
      );
   }//End Method
   
   @Test public void shouldFormatBuildNumber(){
      Assert.assertEquals( "#2001", JobPanelDescriptionBaseImpl.formatBuildNumber( 2001 ) );
      Assert.assertEquals( "#?", JobPanelDescriptionBaseImpl.formatBuildNumber( null ) );
   }//End Method
   
   @Test public void shouldFormatBuildNumberAndTimestamp(){
      assertThat( 
               "#200 | 21:59-17/01", 
               is( JobPanelDescriptionBaseImpl.formatBuildNumberAndTimestamp( 200, 1457963846l ) ) 
      );
      assertThat( 
               "#? | ?-?", 
               is( JobPanelDescriptionBaseImpl.formatBuildNumberAndTimestamp( null, null ) ) 
      );
   }//End Method
   
   @Test public void shouldProvideCorrectDateTimeFormat(){
      assertThat( JobPanelDescriptionBaseImpl.formatTimestamp( 1457963846l ), is( "21:59-17/01" ) );
      assertThat( JobPanelDescriptionBaseImpl.formatTimestamp( null ), is( "?-?" ) );
      assertThat( JobPanelDescriptionBaseImpl.formatTimestamp( 1460445778247L ), is( "08:22-12/04" ) );
   }//End Method
   
   @Test public void shouldFormatCompletionEstimateInSeconds(){
      Assert.assertEquals( "1m 30s | 2m 0s", JobPanelDescriptionBaseImpl.formatCompletionEstimateInSeconds( 90, 120 ) );
      Assert.assertEquals( "5m 0s | 1m 0s", JobPanelDescriptionBaseImpl.formatCompletionEstimateInSeconds( 300, 60 ) );
   }//End Method
   
   @Test public void shouldFormatCompletionEstimateInMilliseconds(){
      Assert.assertEquals( "1m 30s | 2m 0s", JobPanelDescriptionBaseImpl.formatCompletionEstimateInMilliseconds( 90000, 120000 ) );
      Assert.assertEquals( "5m 0s | 1m 0s", JobPanelDescriptionBaseImpl.formatCompletionEstimateInMilliseconds( 300000, 60000 ) );
   }//End Method
   
   @Test public void shouldFormatMillisecondsIntoMinutesAndSeconds(){
      Assert.assertEquals( "1m 30s", JobPanelDescriptionBaseImpl.formatMillisecondsIntoMintuesAndSeconds( 90000 ) );
      Assert.assertEquals( "2m 0s", JobPanelDescriptionBaseImpl.formatMillisecondsIntoMintuesAndSeconds( 120000 ) );
      Assert.assertEquals( "0m 0s", JobPanelDescriptionBaseImpl.formatMillisecondsIntoMintuesAndSeconds( 0 ) );
   }//End Method
   
   @Test public void shouldConfigureJobNameFontAndUpdate(){
      Assert.assertEquals( configuration.jobNameFont().get(), systemUnderTest.jobName().getFont() );
      configuration.jobNameFont().set( new Font( 100 ) );
      Assert.assertEquals( configuration.jobNameFont().get(), systemUnderTest.jobName().getFont() );
   }//End Method
   
   @Test public void shouldConfigureJobNameColourAndUpdate(){
      Assert.assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobName().getTextFill() );
      configuration.jobNameColour().set( Color.ANTIQUEWHITE );
      Assert.assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobName().getTextFill() );
   }//End Method
   
   @Test public void shouldConfigureBuildNumberFontAndUpdate(){
      Assert.assertEquals( configuration.buildNumberFont().get(), systemUnderTest.buildNumber().getFont() );
      configuration.buildNumberFont().set( new Font( 100 ) );
      Assert.assertEquals( configuration.buildNumberFont().get(), systemUnderTest.buildNumber().getFont() );
   }//End Method
   
   @Test public void shouldConfigureBuildNumberColourAndUpdate(){
      Assert.assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumber().getTextFill() );
      configuration.buildNumberColour().set( Color.ANTIQUEWHITE );
      Assert.assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumber().getTextFill() );
   }//End Method
   
   @Test public void shouldConfigureBuildTimeFontAndUpdate(){
      Assert.assertEquals( configuration.completionEstimateFont().get(), systemUnderTest.completionEstimate().getFont() );
      configuration.completionEstimateFont().set( new Font( 100 ) );
      Assert.assertEquals( configuration.completionEstimateFont().get(), systemUnderTest.completionEstimate().getFont() );
   }//End Method
   
   @Test public void shouldConfigureBuildTimeColourAndUpdate(){
      Assert.assertEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimate().getTextFill() );
      configuration.completionEstimateColour().set( Color.ANTIQUEWHITE );
      Assert.assertEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimate().getTextFill() );
   }//End Method
   
   @Test public void detachmentShouldNotUpdateBuildNumberConfigurations() {
      systemUnderTest.detachFromSystem();
      
      configuration.buildNumberColour().set( Color.AQUA );
      assertThat( systemUnderTest.buildNumber().textFillProperty().get(), not( Color.AQUA ) );
      
      theme.buildNumberColoursMap().put( job.getBuildStatus(), Color.AQUA );
      assertThat( systemUnderTest.buildNumber().textFillProperty().get(), not( Color.AQUA ) );
      
      final Font testFont = new Font( 100 );
      configuration.buildNumberFont().set( testFont );
      assertThat( systemUnderTest.buildNumber().fontProperty().get(), not( testFont ) );
   }//End Method
   
   @Test public void detachmentShouldNotUpdateCompletionEstimateConfigurations() {
      systemUnderTest.detachFromSystem();
      
      configuration.completionEstimateColour().set( Color.AQUA );
      assertThat( systemUnderTest.completionEstimate().textFillProperty().get(), not( Color.AQUA ) );
      
      theme.completionEstimateColoursMap().put( job.getBuildStatus(), Color.AQUA );
      assertThat( systemUnderTest.completionEstimate().textFillProperty().get(), not( Color.AQUA ) );
      
      final Font testFont = new Font( 100 );
      configuration.completionEstimateFont().set( testFont );
      assertThat( systemUnderTest.completionEstimate().fontProperty().get(), not( testFont ) );
   }//End Method
   
   @Test public void detachmentShouldNotUpdateJobNameConfigurations() {
      systemUnderTest.detachFromSystem();
      
      configuration.jobNameColour().set( Color.ANTIQUEWHITE );
      assertThat( systemUnderTest.jobName().textFillProperty().get(), not( Color.ANTIQUEWHITE ) );
      
      theme.jobNameColoursMap().put( job.getBuildStatus(), Color.AQUA );
      assertThat( systemUnderTest.jobName().textFillProperty().get(), not( Color.AQUA ) );
      
      final Font testFont = new Font( 100 );
      configuration.jobNameFont().set( testFont );
      assertThat( systemUnderTest.jobName().fontProperty().get(), not( testFont ) );
   }//End Method
   
   @Test public void detachmentShouldNotUseJobNameAndKeepUpdated(){
      Assert.assertEquals( job.nameProperty().get(), systemUnderTest.jobName().getText() );
      
      systemUnderTest.detachFromSystem();
      
      final String value = "somethingElse";
      job.nameProperty().set( value );
      Assert.assertNotEquals( job.nameProperty().get(), systemUnderTest.jobName().getText() );
      Assert.assertNotEquals( value, systemUnderTest.jobName().getText() );
   }//End Method
   
   @Test public void detachmentShouldNotUseJobNumberAndKeepUpdated(){
      Assert.assertEquals( 
               JobPanelDescriptionBaseImpl.formatBuildNumberAndTimestamp( job.getBuildNumber(), job.buildTimestampProperty().get() ), 
               systemUnderTest.buildNumber().getText() 
      );
      
      systemUnderTest.detachFromSystem();
      
      final Integer value = 799;
      job.setBuildNumber( value );
      Assert.assertNotEquals( 
               JobPanelDescriptionBaseImpl.formatBuildNumberAndTimestamp( job.getBuildNumber(), job.buildTimestampProperty().get() ), 
               systemUnderTest.buildNumber().getText() 
      );
      Assert.assertNotEquals( 
               JobPanelDescriptionBaseImpl.formatBuildNumberAndTimestamp( value, job.buildTimestampProperty().get() ),
               systemUnderTest.buildNumber().getText() 
      );
      
      final Long timestamp = 394608347l;
      job.buildTimestampProperty().set( timestamp );
      Assert.assertNotEquals( 
               JobPanelDescriptionBaseImpl.formatBuildNumberAndTimestamp( job.getBuildNumber(), job.buildTimestampProperty().get() ), 
               systemUnderTest.buildNumber().getText() 
      );
      Assert.assertNotEquals( 
               JobPanelDescriptionBaseImpl.formatBuildNumberAndTimestamp( job.getBuildNumber(), timestamp ),
               systemUnderTest.buildNumber().getText() 
      );
   }//End Method

   @Test public void detachmentShouldNotUseJobBuildTimeAndKeepUpdated(){
      Assert.assertEquals( 
               JobPanelDescriptionBaseImpl.formatCompletionEstimateInMilliseconds( 
                        job.currentBuildTimeProperty().get(),
                        job.expectedBuildTimeProperty().get()
               ), 
               systemUnderTest.completionEstimate().getText() 
      );
      
      systemUnderTest.detachFromSystem();
      
      final Integer progress = 180;
      job.currentBuildTimeProperty().set( progress );
      Assert.assertNotEquals( 
               JobPanelDescriptionBaseImpl.formatCompletionEstimateInMilliseconds( 
                        job.currentBuildTimeProperty().get(),
                        job.expectedBuildTimeProperty().get()
               ), 
               systemUnderTest.completionEstimate().getText()  
      );
      Assert.assertNotEquals( 
               JobPanelDescriptionBaseImpl.formatCompletionEstimateInMilliseconds( 
                        progress,
                        job.expectedBuildTimeProperty().get()
               ), 
               systemUnderTest.completionEstimate().getText()  
      );
      
      final Integer estimate = 180;
      job.expectedBuildTimeProperty().set( estimate );
      Assert.assertNotEquals( 
               JobPanelDescriptionBaseImpl.formatCompletionEstimateInMilliseconds( 
                        job.currentBuildTimeProperty().get(),
                        job.expectedBuildTimeProperty().get()
               ), 
               systemUnderTest.completionEstimate().getText()  
      );
      Assert.assertNotEquals( 
               JobPanelDescriptionBaseImpl.formatCompletionEstimateInMilliseconds( 
                        progress,
                        estimate
               ), 
               systemUnderTest.completionEstimate().getText()  
      );
   }//End Method
   
   @Test public void detachmentShouldNotConfigureJobNameFontAndUpdate(){
      Assert.assertEquals( configuration.jobNameFont().get(), systemUnderTest.jobName().getFont() );
      systemUnderTest.detachFromSystem();
      configuration.jobNameFont().set( new Font( 100 ) );
      Assert.assertNotEquals( configuration.jobNameFont().get(), systemUnderTest.jobName().getFont() );
   }//End Method
   
   @Test public void detachmentShouldNotConfigureJobNameColourAndUpdate(){
      Assert.assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobName().getTextFill() );
      systemUnderTest.detachFromSystem();
      configuration.jobNameColour().set( Color.ANTIQUEWHITE );
      Assert.assertNotEquals( configuration.jobNameColour().get(), systemUnderTest.jobName().getTextFill() );
   }//End Method
   
   @Test public void detachmentShouldNotConfigureBuildNumberFontAndUpdate(){
      Assert.assertEquals( configuration.buildNumberFont().get(), systemUnderTest.buildNumber().getFont() );
      systemUnderTest.detachFromSystem();
      configuration.buildNumberFont().set( new Font( 100 ) );
      Assert.assertNotEquals( configuration.buildNumberFont().get(), systemUnderTest.buildNumber().getFont() );
   }//End Method
   
   @Test public void detachmentShouldNotConfigureBuildNumberColourAndUpdate(){
      Assert.assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumber().getTextFill() );
      systemUnderTest.detachFromSystem();
      configuration.buildNumberColour().set( Color.ANTIQUEWHITE );
      Assert.assertNotEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumber().getTextFill() );
   }//End Method
   
   @Test public void detachmentShouldNotConfigureBuildTimeFontAndUpdate(){
      Assert.assertEquals( configuration.completionEstimateFont().get(), systemUnderTest.completionEstimate().getFont() );
      systemUnderTest.detachFromSystem();
      configuration.completionEstimateFont().set( new Font( 100 ) );
      Assert.assertNotEquals( configuration.completionEstimateFont().get(), systemUnderTest.completionEstimate().getFont() );
   }//End Method
   
   @Test public void detachmentShouldNotConfigureBuildTimeColourAndUpdate(){
      Assert.assertEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimate().getTextFill() );
      systemUnderTest.detachFromSystem();
      configuration.completionEstimateColour().set( Color.ANTIQUEWHITE );
      Assert.assertNotEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimate().getTextFill() );
   }//End Method
   
   @Test public void shouldShowAsDetached(){
      assertThat( systemUnderTest.isDetached(), is( false ) );
      systemUnderTest.detachFromSystem();
      assertThat( systemUnderTest.isDetached(), is( true ) );
   }//End Method
   
   @Test public void shouldProvideAssociatedJob(){
      assertThat( systemUnderTest.getJenkinsJob(), is( job ) );
   }//End Method
   
   @Test public void shouldProvideAssociatedConfiguration(){
      assertThat( systemUnderTest.getConfiguration(), is( configuration ) );
   }//End Method
   
   @Test public void formatterShouldProvide24HourClock(){
      String formatted = JobPanelDescriptionBaseImpl.DATE_TIME_FORMATTER.format( LocalDateTime.of( 2016, 01, 01, 14, 20, 43 ) );
      assertThat( formatted, is( "14:20-01/01" ) );
   }//End Method
}//End Class
