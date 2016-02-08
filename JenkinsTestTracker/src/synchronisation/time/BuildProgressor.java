/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package synchronisation.time;

import java.time.Clock;
import java.util.Timer;

import model.jobs.JenkinsJob;
import storage.database.JenkinsDatabase;
import synchronisation.model.TimeKeeper;

public class BuildProgressor extends TimeKeeper {

   /**
    * Constructs a new {@link BuildProgressor}.
    * @param timer the {@link Timer} for scheduling.
    * @param database the {@link JenkinsDatabase} for accessing {@link JenkinsJob}s.
    * @param interval the interval of the update.
    */
   public BuildProgressor( Timer timer, JenkinsDatabase database, Long interval ) {
      super( timer, new BuildProgressCalculator( database, Clock.systemUTC() ), interval );
   }//End Constructor
   
}//End Class
