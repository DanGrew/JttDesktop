/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.synchronisation.time;

import java.util.Timer;

import uk.dangrew.jtt.api.handling.JenkinsProcessing;
import uk.dangrew.jtt.synchronisation.model.TimeKeeper;

/**
 * The {@link JobUpdater} provides a {@link TimeKeeper} for updating the job details
 * from the {@link JenkinsApiImpl}.
 */
public class JobUpdater extends TimeKeeper {
   
   static final long UPDATE_DELAY = TimeKeeper.TASK_DELAY;
   static final long INTERVAL = 5000L;
   
   private final JenkinsProcessing processing;
   
   /**
    * Constructs a new {@link JobUpdater}.
    * @param jenkinsProcessing the {@link JenkinsProcessing} to request job updates on.
    * @param timer the {@link Timer} to time events.
    * @param interval the interval between updates.
    */
   public JobUpdater( JenkinsProcessing jenkinsProcessing, Timer timer, Long interval ) {
      super( 
               () -> jenkinsProcessing.fetchJobsAndUpdateDetails(),
               timer, 
               interval
      );
      this.processing = jenkinsProcessing;
   }//End Constructor
   
   /**
    * Method to determine whether the given is associated with this.
    * @param processing the {@link JenkinsProcessing} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( JenkinsProcessing processing ) {
      return this.processing == processing;
   }//End Method
}//End Class
