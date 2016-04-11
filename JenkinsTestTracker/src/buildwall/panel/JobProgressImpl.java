/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel;

import javafx.registrations.ChangeListenerRegistrationImpl;
import javafx.registrations.RegistrationManager;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import model.jobs.JenkinsJob;
import styling.BuildWallStyles;
import styling.SystemStyling;

/**
 * The {@link JobProgressImpl} provides a {@link BorderPane} with a specially formatted
 * {@link ProgressBar} for showing build progress of a {@link JenkinsJob}.
 */
public class JobProgressImpl extends BorderPane {

   private ProgressBar progress;
   private RegistrationManager registrations;
   
   /**
    * Constructs a new {@link JobProgressImpl}.
    * @param job the {@link JenkinsJob}.
    */
   public JobProgressImpl( JenkinsJob job ) {
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
               job.lastBuildStatusProperty(), ( source, old, updated ) -> updateStyle( job ) 
      ) );
      updateStyle( job );
   }//End Class
   
   /**
    * Method to update the style of the {@link ProgressBar} using loaded styles, based on the {@link JenkinsJob}
    * build state of the last build.
    * @param job the {@link JenkinsJob} to update for.
    */
   private void updateStyle( JenkinsJob job ) {
      switch ( job.lastBuildStatusProperty().get() ) {
         case ABORTED:
            SystemStyling.applyStyle( BuildWallStyles.ProgressBarAborted, progress );
            break;
         case FAILURE:
            SystemStyling.applyStyle( BuildWallStyles.ProgressBarFailed, progress );
            break;
         case NOT_BUILT:
            SystemStyling.applyStyle( BuildWallStyles.ProgressBarNotBuilt, progress );
            break;
         case SUCCESS:
            SystemStyling.applyStyle( BuildWallStyles.ProgressBarSuccess, progress );
            break;
         case UNKNOWN:
            SystemStyling.applyStyle( BuildWallStyles.ProgressBarUnknown, progress );
            break;
         case UNSTABLE:
            SystemStyling.applyStyle( BuildWallStyles.ProgressBarUnstable, progress );
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
