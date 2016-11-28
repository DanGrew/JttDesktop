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

   private final JenkinsDatabase database;
   
   /**
    * Constructs a new {@link BuildProgressor}.
    * @param database the {@link JenkinsDatabase} for accessing {@link JenkinsJob}s.
    * @param timer the {@link Timer} for scheduling.
    * @param interval the interval of the update.
    */
   public BuildProgressor( JenkinsDatabase database, Timer timer, Long interval ) {
      this( database, timer, Clock.systemUTC(), interval );
   }//End Constructor
   
   /**
    * Constructs a new {@link BuildProgressor} that will be manually driven.
    * @param database the {@link JenkinsDatabase} for accessing {@link JenkinsJob}s.
    * @param timer the {@link Timer} for scheduling, can be null.
    * @param clock the {@link Clock} providing the system time.
    * @param interval the interval of the update, can be null.
    */
   public BuildProgressor( JenkinsDatabase database, Timer timer, Clock clock, Long interval ) {
      super( new BuildProgressCalculator( database, clock ), timer, interval );
      this.database = database;
   }//End Constructor
   
   /**
    * Method to determine whether the given is associated.
    * @param database the {@link JenkinsDatabase} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( JenkinsDatabase database ) {
      return this.database == database;
   }//End Method
   
}//End Class
