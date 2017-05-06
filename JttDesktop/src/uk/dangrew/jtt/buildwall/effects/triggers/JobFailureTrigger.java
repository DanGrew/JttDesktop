/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.triggers;

import javafx.util.Pair;
import uk.dangrew.jtt.buildwall.effects.flasher.control.ImageFlasherControls;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

/**
 * The {@link JobFailureTrigger} is responsible for identifying new failing builds and triggering
 * the {@link ImageFlasherControls#flashingSwitch()}. 
 */
public class JobFailureTrigger {
   
   private final ImageFlasherControls imageFlasherControls;

   /**
    * Constructs a new {@link JobFailureTrigger}.
    * @param database the {@link JenkinsDatabase} to monitor for failures.
    * @param imageFlasherControls the {@link ImageFlasherControls} to control.
    */
   public JobFailureTrigger( JenkinsDatabase database, ImageFlasherControls imageFlasherControls ) {
      this.imageFlasherControls = imageFlasherControls;
      
      database.jenkinsJobProperties().addBuildResultStatusListener( this::handleJobStatusChange );
   }//End Constructor
   
   /**
    * Method to handle the change of {@link JenkinsJob} {@link BuildResultStatus}.
    * @param job the source of event.
    * @param old the old status.
    * @param updated the new status.
    */
   private void handleJobStatusChange( JenkinsJob job, Pair< Integer, BuildResultStatus > old, Pair< Integer, BuildResultStatus > updated ) {
      switch ( updated.getValue() ) {
         case ABORTED:
         case FAILURE:
         case UNSTABLE:
            imageFlasherControls.flashingSwitch().set( true );
            break;
         case NOT_BUILT:
         case SUCCESS:
         case UNKNOWN:
         default:
            break;
      }
   }//End Method

}//End Class
