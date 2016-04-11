/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package synchronisation.time;

import java.util.Timer;

import api.handling.JenkinsFetcher;
import synchronisation.model.TimeKeeper;

/**
 * The {@link JobUpdater} provides a {@link TimeKeeper} for updating the job details
 * from the {@link JenkinsApiImpl}.
 */
public class JobUpdater extends TimeKeeper {
   
   static final long UPDATE_DELAY = TimeKeeper.TASK_DELAY;
   static final long INTERVAL = 5000L;
   
   /**
    * Constructs a new {@link JobUpdater}.
    * @param timer the {@link Timer} to time events.
    * @param fetcher the {@link JenkinsFetcher} to request job updates on.
    * @param interval the interval between updates.
    */
   public JobUpdater( Timer timer, JenkinsFetcher fetcher, Long interval ) {
      super( 
               timer, 
               () -> fetcher.fetchJobsAndUpdateDetails(),
               interval
      );
   }//End Constructor
   
   /**
    * Constructs a new {@link JobUpdater}.
    * @param fetcher the {@link JenkinsFetcher} to request job updates on.
    */
   public JobUpdater( JenkinsFetcher fetcher ) {
      super( () -> fetcher.fetchJobsAndUpdateDetails() );
   }//End Constructor

}//End Class
