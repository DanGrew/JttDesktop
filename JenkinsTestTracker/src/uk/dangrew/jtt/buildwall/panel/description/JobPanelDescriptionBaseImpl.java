/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel.description;

import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableMap;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.javafx.registrations.ChangeListenerBindingImpl;
import uk.dangrew.jtt.javafx.registrations.ChangeListenerRegistrationImpl;
import uk.dangrew.jtt.javafx.registrations.MapChangeListenerRegistrationImpl;
import uk.dangrew.jtt.javafx.registrations.RegisteredComponent;
import uk.dangrew.jtt.javafx.registrations.RegistrationManager;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.utility.observable.FunctionMapChangeListenerImpl;

/**
 * The {@link JobPanelDescriptionBaseImpl} is responsible for defining the structure and common
 * components for the description part of a {@link JobPanelImpl}.
 */
public abstract class JobPanelDescriptionBaseImpl extends BorderPane implements RegisteredComponent {

   static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern( "HH:mm" ).appendLiteral( "-" ).appendPattern( "dd/MM" ).toFormatter();
   static final String UNKNOWN_BUILD_NUMBER = "?";
   static final String BUILD_NUMBER_PREFIX = "#";
   static final double DEFAULT_PROPERTY_OPACITY = 0.8;
   static final double PROPERTIES_INSET = 5;
   
   private final JenkinsJob job;
   private final BuildWallTheme theme;
   private final BuildWallConfiguration configuration;
   private Label jobName;
   private Label buildNumber;
   private Label completionEstimate;
   private GridPane properties;
   
   private RegistrationManager registrations;

   /**
    * Constructs a new {@link DefaultJobPanelDescriptionImpl}.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param theme the {@link BuildWallTheme} providing configuration.
    * @param job the {@link JenkinsJob} being described.
    */
   protected JobPanelDescriptionBaseImpl( BuildWallConfiguration configuration, BuildWallTheme theme, JenkinsJob job ) {
      this.configuration = configuration;
      this.theme = theme;
      this.job = job;
      this.registrations = new RegistrationManager();
      
      jobName = new Label( job.nameProperty().get() );
      updateJobNameFont();
      
      properties = new GridPane();
      buildNumber = new Label();
      updateBuildNumberAndTimestamp();
      buildNumber.setOpacity( DEFAULT_PROPERTY_OPACITY );

      completionEstimate = new Label();
      updateCompletionEstimate();
      completionEstimate.setOpacity( DEFAULT_PROPERTY_OPACITY );
      setPadding( new Insets( PROPERTIES_INSET ) );

      applyRegistrations();
      applyLayout();
      applyColumnConstraints();
      
      updateJobNameColour();
      updateBuildNumberColour();
      updateCompletionEstimateColour();
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
      
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               configuration.buildNumberColour(),
               ( s, o, u ) -> updateBuildNumberColour()
      ) );
      registrations.apply( new ChangeListenerBindingImpl<>( 
               configuration.buildNumberFont(), buildNumber.fontProperty() ) 
      );
      
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               job.buildProperty(), 
               ( source, old, updated ) -> updateBuildNumberAndTimestamp()
      ) );
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               job.buildTimestampProperty(), 
               ( source, old, updated ) -> updateBuildNumberAndTimestamp()
      ) );
      
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               configuration.completionEstimateColour(),
               ( s, o, u ) -> updateCompletionEstimateColour()
      ) );
      registrations.apply( new ChangeListenerBindingImpl<>( 
               configuration.completionEstimateFont(), completionEstimate.fontProperty() ) 
      );
      
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               job.currentBuildTimeProperty(), 
               ( source, old, updated ) -> updateCompletionEstimate() 
      ) );
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               job.expectedBuildTimeProperty(), 
               ( source, old, updated ) -> updateCompletionEstimate() 
      ) );
      
      registrations.apply( new MapChangeListenerRegistrationImpl<>( theme.jobNameColoursMap(), 
               new FunctionMapChangeListenerImpl< BuildResultStatus, Color >( 
                     theme.jobNameColoursMap(), 
                     ( k, v ) -> updateJobNameColour(), 
                     ( k, v ) -> { /* do not respond to removal. */ }
               ) 
      ) );
      registrations.apply( new MapChangeListenerRegistrationImpl<>( theme.buildNumberColoursMap(), 
               new FunctionMapChangeListenerImpl< BuildResultStatus, Color >( 
                     theme.buildNumberColoursMap(), 
                     ( k, v ) -> updateBuildNumberColour(), 
                     ( k, v ) -> { /* do not respond to removal. */ }
               ) 
      ) );
      registrations.apply( new MapChangeListenerRegistrationImpl<>( theme.completionEstimateColoursMap(), 
               new FunctionMapChangeListenerImpl< BuildResultStatus, Color >( 
                     theme.completionEstimateColoursMap(), 
                     ( k, v ) -> updateCompletionEstimateColour(), 
                     ( k, v ) -> { /* do not respond to removal. */ }
               ) 
      ) );
   }//End Method

   /**
    * Method to take the constructed and initialised elements of the panel and arrange them
    * in the component.
    */
   protected abstract void applyLayout();
   
   /**
    * Method to apply the {@link ColumnConstraints}.
    */
   protected abstract void applyColumnConstraints();
   
   /**
    * Method to update the job name {@link Font} in line with the {@link BuildWallConfiguration}.
    */
   private void updateJobNameFont(){
      jobName.fontProperty().set( configuration.jobNameFont().get() );
   }//End Method
   
   /**
    * Method to update the job name {@link Label}s {@link Color}.
    */
   private void updateJobNameColour(){
      updateColour( theme.jobNameColoursMap(), configuration.jobNameColour(), jobName );
   }//End Method
   
   /**
    * Method to update the build number {@link Label}s {@link Color}.
    */
   private void updateBuildNumberColour(){
      updateColour( theme.buildNumberColoursMap(), configuration.buildNumberColour(), buildNumber );
   }//End Method
   
   /**
    * Method to update the completion estimate {@link Label}s {@link Color}.
    */
   private void updateCompletionEstimateColour(){
      updateColour( theme.completionEstimateColoursMap(), configuration.completionEstimateColour(), completionEstimate );
   }//End Method
   
   /**
    * Method to update the {@link Color} of a {@link Label}.
    * @param coloursMap the {@link ObservableMap} of {@link Color}s to use.
    * @param configurationProperty the {@link ObjectProperty} from the {@link BuildWallConfiguration}.
    * @param textLabel the {@link Label} to update.
    */
   private void updateColour( 
            ObservableMap< BuildResultStatus, Color > coloursMap, 
            ObjectProperty< Color > configurationProperty,
            Label textLabel
   ){
      Color themeColor = coloursMap.get( job.getBuildStatus() );
      if ( themeColor != null ) {
         textLabel.textFillProperty().set( themeColor );
      } else {
         textLabel.textFillProperty().set( configurationProperty.get() );
      }
   }//End Method
   
   /**
    * Method to update the complete estimate for the associated {@link JenkinsJob}.
    */
   private void updateCompletionEstimate(){
      DecoupledPlatformImpl.runLater( () -> {
         completionEstimate.setText( formatCompletionEstimateInMilliseconds( 
                  job.currentBuildTimeProperty().get(),
                  job.expectedBuildTimeProperty().get()
         ) );
      } );
   }//End Method
   
   /**
    * Method to update the build number and timestamp when information has changed.
    */
   private void updateBuildNumberAndTimestamp(){
      DecoupledPlatformImpl.runLater( () -> {
         buildNumber.setText( formatBuildNumberAndTimestamp( job.getBuildNumber(), job.buildTimestampProperty().get() ) );
      } );
   }//End Method
   
   /**
    * Method to detach this object and its {@link RegistrationImpl}s from the system.
    */
   @Override public void detachFromSystem() {
      registrations.shutdown();
   }//End Method
   
   /**
    * Method to determine whether the {@link DefaultJobPanelDescriptionImpl} is detached and
    * not registered with anything in the system.
    * @return true if no registrations held.
    */
   @Override public boolean isDetached() {
      return registrations.isEmpty();
   }//End Method
   
   /**
    * Getter for the associated {@link JenkinsJob}.
    * @return the {@link JenkinsJob}.
    */
   protected JenkinsJob getJenkinsJob(){
      return job;
   }//End Method
   
   /**
    * Getter for the associated {@link BuildWallConfiguration}.
    * @return the {@link BuildWallConfiguration}.
    */
   protected BuildWallConfiguration getConfiguration(){
      return configuration;
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
      if ( buildNumber == null ) {
         return BUILD_NUMBER_PREFIX + UNKNOWN_BUILD_NUMBER;
      }
      
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
      if ( timestamp == null ) {
         return "?-?";
      }

      return new Timestamp( timestamp ).toInstant()
               .atZone( ZoneId.of( "Europe/London" ) )
               .toLocalDateTime().format( DATE_TIME_FORMATTER );
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
