/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.synchronisation.time;

import java.time.Clock;
import java.util.Timer;

import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.synchronisation.model.TimeKeeper;

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
   
   /**
    * Constructs a new {@link BuildProgressor} that will be manually driven.
    * @param database the {@link JenkinsDatabase} for accessing {@link JenkinsJob}s.
    * @param clock the {@link Clock} providing the system time.
    */
   public BuildProgressor( JenkinsDatabase database, Clock clock ) {
      super( new BuildProgressCalculator( database, clock ) );
   }//End Constructor
   
}//End Class
