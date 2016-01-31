/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel;

import java.util.concurrent.TimeUnit;

import graphics.DecoupledPlatformImpl;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import model.jobs.JenkinsJob;

/**
 * The {@link JobPanelDescriptionImpl} provides a {@link BorderPane} overlay for 
 * the description of the {@link JenkinsJob}.
 */
public class JobPanelDescriptionImpl extends BorderPane {

   static final String UNKNOWN_BUILD_NUMBER = "?";
   static final String BUILD_NUMBER_PREFIX = "#";
   static final double DEFAULT_PROPERTY_OPACITY = 0.8;
   static final double PROPERTIES_INSET = 10;
   static final double BUILD_PROPERTY_PERCENTAGE = 20;
   static final double COMPLETION_ESTIMATE_PERCENTAGE = 80;
   
   private Label jobName;
   private Label buildNumber;
   private Label completionEstimate;
   private GridPane properties;

   /**
    * Constructs a new {@link JobPanelDescriptionImpl}.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param job the {@link JenkinsJob} being described.
    */
   public JobPanelDescriptionImpl( BuildWallConfiguration configuration, JenkinsJob job ) {
      jobName = new Label( job.nameProperty().get() );
      jobName.fontProperty().bind( configuration.jobNameFont() );
      jobName.textFillProperty().bind( configuration.jobNameColour() );
      job.nameProperty().addListener( ( source, old, updated ) -> jobName.setText( job.nameProperty().get() ) );
      setCenter( jobName );

      properties = new GridPane();
      buildNumber = new Label( formatBuildNumber( job.lastBuildNumberProperty().get() ) );
      buildNumber.fontProperty().bind( configuration.propertiesFont() );
      buildNumber.textFillProperty().bind( configuration.propertiesColour() );
      buildNumber.setOpacity( DEFAULT_PROPERTY_OPACITY );
      job.lastBuildNumberProperty().addListener( ( source, old, updated ) -> 
            DecoupledPlatformImpl.runLater( () -> {
               buildNumber.setText( formatBuildNumber( job.lastBuildNumberProperty().get() ) );
            } 
      ) );
      properties.add( buildNumber, 0, 0 );

      completionEstimate = new Label();
      updateCompletionEstimate( job );
      completionEstimate.fontProperty().bind( configuration.propertiesFont() );
      completionEstimate.textFillProperty().bind( configuration.propertiesColour() );
      completionEstimate.setOpacity( DEFAULT_PROPERTY_OPACITY );
      job.currentBuildTimeProperty().addListener( ( source, old, updated ) -> updateCompletionEstimate( job ) );
      job.expectedBuildTimeProperty().addListener( ( source, old, updated ) -> updateCompletionEstimate( job ) );
      properties.add( completionEstimate, 1, 0 );
      setBottom( properties );

      ColumnConstraints buildNumberColumn = new ColumnConstraints();
      buildNumberColumn.setPercentWidth( BUILD_PROPERTY_PERCENTAGE );
      buildNumberColumn.setHalignment( HPos.LEFT );
      ColumnConstraints completionEstimateColumn = new ColumnConstraints();
      completionEstimateColumn.setPercentWidth( COMPLETION_ESTIMATE_PERCENTAGE );
      completionEstimateColumn.setHalignment( HPos.RIGHT );
      properties.getColumnConstraints().addAll( buildNumberColumn, completionEstimateColumn );

      properties.setPadding( new Insets( PROPERTIES_INSET ) );
   }//End Class
   
   /**
    * Method to update the complete estimate for the given associated {@link JenkinsJob}.
    * @param job the {@link JenkinsJob}.
    */
   private void updateCompletionEstimate( JenkinsJob job ){
      DecoupledPlatformImpl.runLater( () -> {
         completionEstimate.setText( formatCompletionEstimateInMilliseconds( 
                  job.currentBuildTimeProperty().get(),
                  job.expectedBuildTimeProperty().get()
         ) );
      } );
   }//End Method

   /**
    * Getter for the build number {@link Label}.
    * @return the {@link Label}.
    */
   Label buildNumber() {
      return buildNumber;
   }//End Method

   /**
    * Getter for the completion estimate {@link Label}.
    * @return the {@link Label}.
    */
   Label completionEstimate() {
      return completionEstimate;
   }//End Method

   /**
    * Getter for the job name {@link Label}.
    * @return the {@link Label}.
    */
   Label jobName() {
      return jobName;
   }//End Method

   /**
    * Getter for the {@link GridPane} holding the properties.
    * @return the {@link GridPane}.
    */
   GridPane propertiesPane() {
      return properties;
   }//End Method
   
   /**
    * Utility method to format the given build number for the ui.
    * @param buildNumber the {@link Integer} build number.
    * @return the formatted number.
    */
   static String formatBuildNumber( Integer buildNumber ) {
      if ( buildNumber == null ) return BUILD_NUMBER_PREFIX + UNKNOWN_BUILD_NUMBER;
      return BUILD_NUMBER_PREFIX + buildNumber.intValue();
   }//End Method

   /**
    * Utility method to format the given progress and estimate for the ui.
    * @param progressSeconds the progress in seconds.
    * @param estimateSeconds the estimate in seconds.
    * @return the formatted completion estimate.
    */
   static String formatCompletionEstimateInSeconds( int progressSeconds, int estimateSeconds ) {
      return formatMillisecondsIntoMintuesAndSeconds( TimeUnit.SECONDS.toMillis( progressSeconds ) ) + 
               " | " + 
             formatMillisecondsIntoMintuesAndSeconds( TimeUnit.SECONDS.toMillis( estimateSeconds ) );
   }//End Method
   
   /**
    * Utility method to format the given progress and estimate for the ui.
    * @param progressMillis the progress in milliseconds.
    * @param estimateMillis the estimate in milliseconds.
    * @return the formatted completion estimate.
    */
   static String formatCompletionEstimateInMilliseconds( long progressMillis, long estimateMillis ) {
      return formatMillisecondsIntoMintuesAndSeconds( progressMillis ) + " | " + formatMillisecondsIntoMintuesAndSeconds( estimateMillis );
   }//End Method
   
   /**
    * Utility method to format the given progress for the ui.
    * @param milliseconds the progress in milliseconds.
    * @return the formatted time.
    */
   static String formatMillisecondsIntoMintuesAndSeconds( long milliseconds ) {
      long progressAsMinutes = TimeUnit.MILLISECONDS.toMinutes( milliseconds );
      long progressAsSeconds = TimeUnit.MILLISECONDS.toSeconds( milliseconds );
      long progressSecondsRemainder = progressAsSeconds - progressAsMinutes * 60;
      return progressAsMinutes + "m " + progressSecondsRemainder + "s";
   }//End Method

}//End Class
