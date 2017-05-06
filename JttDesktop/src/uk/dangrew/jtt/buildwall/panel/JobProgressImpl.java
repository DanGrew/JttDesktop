/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel;

import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.javafx.css.DynamicCssOnlyProperties;
import uk.dangrew.jtt.javafx.registrations.ChangeListenerRegistrationImpl;
import uk.dangrew.jtt.javafx.registrations.MapChangeListenerRegistrationImpl;
import uk.dangrew.jtt.javafx.registrations.RegistrationManager;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.utility.observable.FunctionMapChangeListenerImpl;
import uk.dangrew.jtt.styling.BuildWallStyles;

/**
 * The {@link JobProgressImpl} provides a {@link BorderPane} with a specially formatted
 * {@link ProgressBar} for showing build progress of a {@link JenkinsJob}.
 */
public class JobProgressImpl extends BorderPane {

   private final JenkinsJob job;
   private final DynamicCssOnlyProperties dynamicProperties;
   private final BuildWallTheme theme;
   private ProgressBar progress;
   private RegistrationManager registrations;
   
   /**
    * Constructs a new {@link JobProgressImpl}.
    * @param job the {@link JenkinsJob}.
    * @param theme the {@link BuildWallTheme} associated to control the {@link Color}s.
    */
   public JobProgressImpl( JenkinsJob job, BuildWallTheme theme ) {
      this( new DynamicCssOnlyProperties(), job, theme );
   }//End Constructor
   
   /**
    * Constructs a new {@link JobProgressImpl}.
    * @param dynamicProperties the {@link DynamicCssOnlyProperties} for changing the {@link javafx.scene.paint.Color}
    * of the {@link ProgressBar}.
    * @param job the associated {@link JenkinsJob}.
    * @param theme the {@link BuildWallTheme} associated to control the {@link Color}s.
    */
   JobProgressImpl( DynamicCssOnlyProperties dynamicProperties, JenkinsJob job, BuildWallTheme theme ) {
      this.job = job;
      this.dynamicProperties = dynamicProperties;
      this.theme = theme;
      this.registrations = new RegistrationManager();
      
      progress = new ProgressBar();
      setCenter( progress );
      
      progress.prefWidthProperty().bind( widthProperty() );
      progress.prefHeightProperty().bind( heightProperty() );
      
      progress.setProgress( calculateProgress( job ) );
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               job.currentBuildTimeProperty(), ( source, old, updated ) -> updateProgress( job ) 
      ) );
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               job.expectedBuildTimeProperty(), ( source, old, updated ) -> updateProgress( job ) 
      ) );
      registrations.apply( new ChangeListenerRegistrationImpl<>( 
               job.buildProperty(), ( source, old, updated ) -> updateStyle( job ) 
      ) );
      
      FunctionMapChangeListenerImpl< BuildResultStatus, Color > coloursListener = new FunctionMapChangeListenerImpl<>(
               theme.barColoursMap(), 
               ( k, v ) -> updateStyle( job ), 
               ( k, v ) -> updateStyle( job ) 
      );
      registrations.apply( new MapChangeListenerRegistrationImpl<>( 
               theme.barColoursMap(), 
               coloursListener
      ) );
      registrations.apply( new MapChangeListenerRegistrationImpl<>( 
               theme.trackColoursMap(), 
               coloursListener
      ) );
      updateStyle( job );
   }//End Constructor

   /**
    * Method to update the style of the {@link ProgressBar} using loaded styles, based on the {@link JenkinsJob}
    * build state of the last build.
    * @param job the {@link JenkinsJob} to update for.
    */
   void updateStyle( JenkinsJob job ) {
      BuildResultStatus status = job.buildProperty().get().getValue();
      
      Color trackColour = theme.trackColoursMap().get( status );
      Color barColour = theme.barColoursMap().get( status );
      if ( trackColour != null && barColour != null ) {
         dynamicProperties.applyCustomColours( barColour, trackColour, progress );
         return;
      }
      
      switch ( status ) {
         case ABORTED:
            dynamicProperties.applyStandardColourFor( BuildWallStyles.ProgressBarAborted, progress );
            break;
         case FAILURE:
            dynamicProperties.applyStandardColourFor( BuildWallStyles.ProgressBarFailed, progress );
            break;
         case NOT_BUILT:
            dynamicProperties.applyStandardColourFor( BuildWallStyles.ProgressBarNotBuilt, progress );
            break;
         case SUCCESS:
            dynamicProperties.applyStandardColourFor( BuildWallStyles.ProgressBarSuccess, progress );
            break;
         case UNKNOWN:
            dynamicProperties.applyStandardColourFor( BuildWallStyles.ProgressBarUnknown, progress );
            break;
         case UNSTABLE:
            dynamicProperties.applyStandardColourFor( BuildWallStyles.ProgressBarUnstable, progress );
            break;
         default:
            break;
      }
   }//End Method
   
   /**
    * Method to update the {@link ProgressBar} for the given {@link JenkinsJob}.
    * @param job the {@link JenkinsJob} providing the information for progress calculation.
    */
   private void updateProgress( JenkinsJob job ) {
      progress.setProgress( calculateProgress( job ) );
   }//End Method
   
   /**
    * Method to detach the {@link RegistrationImpl}s from the system as this object is 
    * no longer needed.
    */
   public void detachFromSystem(){
      registrations.shutdown();
   }//End Method
   
   /**
    * Method to determine whether the given {@link JenkinsJob} is associated with this {@link JobProgressImpl}.
    * @param job the {@link JenkinsJob} in question.
    * @return true if associated.
    */
   public boolean isAssociatedWith( JenkinsJob job ) {
      return this.job == job;
   }//End Method
   
   /**
    * Method to determine whether the given {@link BuildWallTheme} is associated with this {@link JobProgressImpl}.
    * @param theme the {@link BuildWallTheme} in question.
    * @return true if associated.
    */
   public boolean isAssociatedWith( BuildWallTheme theme ) {
      return this.theme == theme;
   }//End Method
   
   /**
    * Method to determine whether the {@link JobProgressImpl} is detached and
    * not registered with anything in the system.
    * @return true if no registrations held.
    */
   boolean isDetached() {
      return registrations.isEmpty();
   }//End Method
   
   /**
    * Getter for the {@link ProgressBar}.
    * @return the {@link ProgressBar}.
    */
   ProgressBar progressBar() {
      return progress;
   }//End Method
   
   /**
    * Utility method to calculate the progress of the build for {@link JenkinsJob}.
    * @param job the {@link JenkinsJob} to calculate for.
    * @return the double progress between 0 and 1.
    */
   static double calculateProgress( JenkinsJob job ){
      return ( double )job.currentBuildTimeProperty().get() / ( double )job.expectedBuildTimeProperty().get();
   }//End Method
   
}//End Class
