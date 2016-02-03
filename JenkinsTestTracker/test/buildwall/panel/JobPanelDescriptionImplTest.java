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

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallConfigurationImpl;
import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.TestPlatformDecouplerImpl;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import utility.TestCommon;

/**
 * {@link JobPanelDescriptionImpl} test.
 */
public class JobPanelDescriptionImplTest {

   private JenkinsJob job;
   private BuildWallConfiguration configuration; 
   private JobPanelDescriptionImpl systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      job = new JenkinsJobImpl( "anything" );
      job.currentBuildTimeProperty().set( 1000 );
      job.expectedBuildTimeProperty().set( 10000 );
      configuration = new BuildWallConfigurationImpl();
      JavaFxInitializer.startPlatform();
      systemUnderTest = new JobPanelDescriptionImpl( configuration, job );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      JavaFxInitializer.threadedLaunch( () -> { return new JobPanelDescriptionImpl( configuration, job ); } );
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void shouldUseBuildNumberConfigurations() {
      Assert.assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumber().textFillProperty().get() );
      Assert.assertEquals( configuration.buildNumberFont().get(), systemUnderTest.buildNumber().fontProperty().get() );
   }//End Method
   
   @Test public void shouldUseCompletionEstimateConfigurations() {
      Assert.assertEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimate().textFillProperty().get() );
      Assert.assertEquals( configuration.completionEstimateFont().get(), systemUnderTest.completionEstimate().fontProperty().get() );
   }//End Method
   
   @Test public void shouldUseJobNameConfigurations() {
      Assert.assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobName().textFillProperty().get() );
      Assert.assertEquals( configuration.jobNameFont().get(), systemUnderTest.jobName().fontProperty().get() );
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
      Assert.assertEquals( JobPanelDescriptionImpl.DEFAULT_PROPERTY_OPACITY, systemUnderTest.buildNumber().getOpacity(), TestCommon.precision() );
      Assert.assertEquals( JobPanelDescriptionImpl.DEFAULT_PROPERTY_OPACITY, systemUnderTest.completionEstimate().getOpacity(), TestCommon.precision() );
   }//End Method
   
   @Test public void propertiesShouldBeInsetFromEdge(){
      Assert.assertEquals( JobPanelDescriptionImpl.PROPERTIES_INSET, systemUnderTest.propertiesPane().getInsets().getBottom(), TestCommon.precision() );
      Assert.assertEquals( JobPanelDescriptionImpl.PROPERTIES_INSET, systemUnderTest.propertiesPane().getInsets().getTop(), TestCommon.precision() );
      Assert.assertEquals( JobPanelDescriptionImpl.PROPERTIES_INSET, systemUnderTest.propertiesPane().getInsets().getRight(), TestCommon.precision() );
      Assert.assertEquals( JobPanelDescriptionImpl.PROPERTIES_INSET, systemUnderTest.propertiesPane().getInsets().getLeft(), TestCommon.precision() );
   }//End Method
   
   @Test public void propertiesShouldBeEvenlySplit(){
      GridPane properties = systemUnderTest.propertiesPane();
      ObservableList< ColumnConstraints > constraints = properties.getColumnConstraints();
      Assert.assertEquals( 2, constraints.size() );
      Assert.assertEquals( JobPanelDescriptionImpl.BUILD_PROPERTY_PERCENTAGE, constraints.get( 0 ).getPercentWidth(), TestCommon.precision() );
      Assert.assertEquals( JobPanelDescriptionImpl.COMPLETION_ESTIMATE_PERCENTAGE, constraints.get( 1 ).getPercentWidth(), TestCommon.precision() );
   }//End Method

   @Test public void jobNameShouldBeInCenter(){
      Assert.assertEquals( systemUnderTest.jobName(), systemUnderTest.getCenter() );
   }//End Method
   
   @Test public void buildNumberShouldBeBottomLeft(){
      Assert.assertEquals( 0, GridPane.getRowIndex( systemUnderTest.buildNumber() ).intValue() );
      Assert.assertEquals( 0, GridPane.getColumnIndex( systemUnderTest.buildNumber() ).intValue() );
      
      ObservableList< ColumnConstraints > constraints = systemUnderTest.propertiesPane().getColumnConstraints();
      Assert.assertEquals( HPos.LEFT, constraints.get( 0 ).getHalignment() );
   }//End Method
   
   @Test public void completionEstimateShouldBeBottomRight(){
      Assert.assertEquals( 0, GridPane.getRowIndex( systemUnderTest.completionEstimate() ).intValue() );
      Assert.assertEquals( 1, GridPane.getColumnIndex( systemUnderTest.completionEstimate() ).intValue() );
      
      ObservableList< ColumnConstraints > constraints = systemUnderTest.propertiesPane().getColumnConstraints();
      Assert.assertEquals( HPos.RIGHT, constraints.get( 1 ).getHalignment() );
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
               JobPanelDescriptionImpl.formatBuildNumber( job.lastBuildNumberProperty().get() ), 
               systemUnderTest.buildNumber().getText() 
      );
      
      final Integer value = 799;
      job.lastBuildNumberProperty().set( value );
      Assert.assertEquals( 
               JobPanelDescriptionImpl.formatBuildNumber( job.lastBuildNumberProperty().get() ), 
               systemUnderTest.buildNumber().getText() 
      );
      Assert.assertEquals( 
               JobPanelDescriptionImpl.formatBuildNumber( value ), 
               systemUnderTest.buildNumber().getText() 
      );
   }//End Method

   @Test public void shouldUseJobBuildTimeAndKeepUpdated(){
      Assert.assertEquals( 
               JobPanelDescriptionImpl.formatCompletionEstimateInMilliseconds( 
                        job.currentBuildTimeProperty().get(),
                        job.expectedBuildTimeProperty().get()
               ), 
               systemUnderTest.completionEstimate().getText() 
      );
      
      final Integer progress = 180;
      job.currentBuildTimeProperty().set( progress );
      Assert.assertEquals( 
               JobPanelDescriptionImpl.formatCompletionEstimateInMilliseconds( 
                        job.currentBuildTimeProperty().get(),
                        job.expectedBuildTimeProperty().get()
               ), 
               systemUnderTest.completionEstimate().getText()  
      );
      Assert.assertEquals( 
               JobPanelDescriptionImpl.formatCompletionEstimateInMilliseconds( 
                        progress,
                        job.expectedBuildTimeProperty().get()
               ), 
               systemUnderTest.completionEstimate().getText()  
      );
      
      final Integer estimate = 180;
      job.expectedBuildTimeProperty().set( estimate );
      Assert.assertEquals( 
               JobPanelDescriptionImpl.formatCompletionEstimateInMilliseconds( 
                        job.currentBuildTimeProperty().get(),
                        job.expectedBuildTimeProperty().get()
               ), 
               systemUnderTest.completionEstimate().getText()  
      );
      Assert.assertEquals( 
               JobPanelDescriptionImpl.formatCompletionEstimateInMilliseconds( 
                        progress,
                        estimate
               ), 
               systemUnderTest.completionEstimate().getText()  
      );
   }//End Method
   
   @Test public void shouldFormatBuildNumber(){
      Assert.assertEquals( "#2001", JobPanelDescriptionImpl.formatBuildNumber( 2001 ) );
      Assert.assertEquals( "#?", JobPanelDescriptionImpl.formatBuildNumber( null ) );
   }//End Method
   
   @Test public void shouldFormatCompletionEstimateInSeconds(){
      Assert.assertEquals( "1m 30s | 2m 0s", JobPanelDescriptionImpl.formatCompletionEstimateInSeconds( 90, 120 ) );
      Assert.assertEquals( "5m 0s | 1m 0s", JobPanelDescriptionImpl.formatCompletionEstimateInSeconds( 300, 60 ) );
   }//End Method
   
   @Test public void shouldFormatCompletionEstimateInMilliseconds(){
      Assert.assertEquals( "1m 30s | 2m 0s", JobPanelDescriptionImpl.formatCompletionEstimateInMilliseconds( 90000, 120000 ) );
      Assert.assertEquals( "5m 0s | 1m 0s", JobPanelDescriptionImpl.formatCompletionEstimateInMilliseconds( 300000, 60000 ) );
   }//End Method
   
   @Test public void shouldFormatMillisecondsIntoMinutesAndSeconds(){
      Assert.assertEquals( "1m 30s", JobPanelDescriptionImpl.formatMillisecondsIntoMintuesAndSeconds( 90000 ) );
      Assert.assertEquals( "2m 0s", JobPanelDescriptionImpl.formatMillisecondsIntoMintuesAndSeconds( 120000 ) );
      Assert.assertEquals( "0m 0s", JobPanelDescriptionImpl.formatMillisecondsIntoMintuesAndSeconds( 0 ) );
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
}//End Class