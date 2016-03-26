/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel.description;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.concurrent.TimeUnit;

import buildwall.configuration.BuildWallConfiguration;
import graphics.DecoupledPlatformImpl;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.registrations.ChangeListenerBindingImpl;
import javafx.registrations.ChangeListenerRegistrationImpl;
import javafx.registrations.PaintColorChangeListenerBindingImpl;
import javafx.registrations.RegistrationImpl;
import javafx.registrations.RegistrationManager;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.jobs.JenkinsJob;

/**
 * The {@link SimpleJobPanelDescriptionImpl} provides a {@link GridPane} overlay for 
 * the description of the {@link JenkinsJob} in a simple single line arrangement.
 */
public class SimpleJobPanelDescriptionImpl extends GridPane {

   static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern( "hh:mm" ).appendLiteral( "-" ).appendPattern( "dd/MM" ).toFormatter();
   static final String UNKNOWN_BUILD_NUMBER = "?";
   static final String BUILD_NUMBER_PREFIX = "#";
   static final double DEFAULT_PROPERTY_OPACITY = 0.8;
   static final double PROPERTIES_INSET = 10;
   static final double BUILD_PROPERTY_PERCENTAGE = 66;
   static final double JOB_NAME_PERCENTAGE = 66;
   static final double COMPLETION_ESTIMATE_PERCENTAGE = 66;
   
   private JenkinsJob job;
   private BuildWallConfiguration configuration;
   private Label jobName;
   private Label buildNumber;
   private Label completionEstimate;
   
   private RegistrationManager registrations;

   /**
    * Constructs a new {@link SimpleJobPanelDescriptionImpl}.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param job the {@link JenkinsJob} being described.
    */
   public SimpleJobPanelDescriptionImpl( BuildWallConfiguration configuration, JenkinsJob job ) {
      this.configuration = configuration;
      this.job = job;
      this.registrations = new RegistrationManager();
      
      buildNumber = new Label( formatBuildNumberAndTimestamp( job.lastBuildNumberProperty().get(), job.lastBuildTimestampProperty().get() ) );
      buildNumber.setOpacity( DEFAULT_PROPERTY_OPACITY );
      add( buildNumber, 0, 0 );
      
      jobName = new Label( job.nameProperty().get() );
      jobName.setAlignment( Pos.CENTER );
      updateJobNameFont();
      updateJobNameColour();
      add( jobName, 1, 0 );

      completionEstimate = new Label();
      updateCompletionEstimate( job );
      completionEstimate.setOpacity( DEFAULT_PROPERTY_OPACITY );
      add( completionEstimate, 2, 0 );
      setPadding( new Insets( PROPERTIES_INSET ) );

      applyRegistrations();
      applyColumnConstraints();
   }//End Class
   
   /**
    * Method to apply the {@link RegistrationImpl}s needed to keep the panel up to date.
    */
   private void applyRegistrations(){
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               configuration.jobNameFont(), 
               ( source, old, updated ) -> updateJobNameFont() 
      ) );
      
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               configuration.jobNameColour(), 
               ( source, old, updated ) -> updateJobNameColour() 
      ) );
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               job.nameProperty(), 
               ( source, old, updated ) -> jobName.setText( job.nameProperty().get() ) 
      ) );
      
      registrations.apply( new PaintColorChangeListenerBindingImpl( 
               configuration.buildNumberColour(), buildNumber.textFillProperty() ) 
      );
      registrations.apply( new ChangeListenerBindingImpl<>( 
               configuration.buildNumberFont(), buildNumber.fontProperty() ) 
      );
      
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               job.lastBuildNumberProperty(), 
               ( source, old, updated ) -> { 
                  DecoupledPlatformImpl.runLater( () -> {
                     buildNumber.setText( formatBuildNumberAndTimestamp( job.lastBuildNumberProperty().get(), job.lastBuildTimestampProperty().get() ) );
                  } );
               }
      ) );
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               job.lastBuildTimestampProperty(), 
               ( source, old, updated ) -> { 
                  DecoupledPlatformImpl.runLater( () -> {
                     buildNumber.setText( formatBuildNumberAndTimestamp( job.lastBuildNumberProperty().get(), job.lastBuildTimestampProperty().get() ) );
                  } );
               }
      ) );
      
      registrations.apply( new PaintColorChangeListenerBindingImpl( 
               configuration.completionEstimateColour(), completionEstimate.textFillProperty() ) 
      );
      registrations.apply( new ChangeListenerBindingImpl<>( 
               configuration.completionEstimateFont(), completionEstimate.fontProperty() ) 
      );
      
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               job.currentBuildTimeProperty(), 
               ( source, old, updated ) -> updateCompletionEstimate( job ) 
      ) );
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               job.expectedBuildTimeProperty(), 
               ( source, old, updated ) -> updateCompletionEstimate( job ) 
      ) );
   }//End Method

   /**
    * Method to apply the {@link ColumnConstraints}.
    */
   private void applyColumnConstraints() {
      ColumnConstraints buildNumberColumn = new ColumnConstraints();
      buildNumberColumn.setPercentWidth( BUILD_PROPERTY_PERCENTAGE );
      buildNumberColumn.setHalignment( HPos.LEFT );
      ColumnConstraints jobNameColumn = new ColumnConstraints();
      jobNameColumn.setPercentWidth( JOB_NAME_PERCENTAGE );
      jobNameColumn.setHalignment( HPos.CENTER );
      ColumnConstraints completionEstimateColumn = new ColumnConstraints();
      completionEstimateColumn.setPercentWidth( COMPLETION_ESTIMATE_PERCENTAGE );
      completionEstimateColumn.setHalignment( HPos.RIGHT );
      getColumnConstraints().addAll( buildNumberColumn, jobNameColumn, completionEstimateColumn );
   }//End Method
   
   /**
    * Method to update the job name {@link Font} in line with the {@link BuildWallConfiguration}.
    */
   private void updateJobNameFont(){
      jobName.fontProperty().set( configuration.jobNameFont().get() );
   }//End Method
   
   /**
    * Method to update the job name {@link Color} in line with the {@link BuildWallConfiguration}.
    */
   private void updateJobNameColour(){
      jobName.textFillProperty().set( configuration.jobNameColour().get() );
   }//End Method
   
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
    * Method to detach this object and its {@link RegistrationImpl}s from the system.
    */
   public void detachFromSystem() {
      registrations.shutdown();
   }//End Method
   
   /**
    * Method to determine whether the {@link SimpleJobPanelDescriptionImpl} is detached and
    * not registered with anything in the system.
    * @return true if no registrations held.
    */
   public boolean isDetached() {
      return registrations.isEmpty();
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
    * Utility method to format the given build number for the ui.
    * @param buildNumber the {@link Integer} build number.
    * @return the formatted number.
    */
   static String formatBuildNumber( Integer buildNumber ) {
      if ( buildNumber == null ) return BUILD_NUMBER_PREFIX + UNKNOWN_BUILD_NUMBER;
      
      return BUILD_NUMBER_PREFIX + buildNumber.intValue();
   }//End Method
   
   /**
    * Utility method to format the build number and timestamp into the format to display.
    * @param buildNumber the {@link Integer} build number.
    * @param timestamp the {@link Long} timestamp.
    * @return the {@link String} formatted.
    */
   static String formatBuildNumberAndTimestamp( Integer buildNumber, Long timestamp ) {
      return formatBuildNumber( buildNumber ) +
               " | " +
             formatTimestamp( timestamp );
   }//End Method

   /**
    * Method to format the timetamp.
    * @param timestamp the timestamp.
    * @return the displayable timestamp representation.
    */
   static String formatTimestamp( Long timestamp ) {
      if ( timestamp == null ) return "?-?";

      return new Timestamp( timestamp ).toLocalDateTime().format( DATE_TIME_FORMATTER );
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
