/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package synchronisation.model;

import java.util.Timer;
import java.util.TimerTask;

import api.handling.JenkinsFetcher;

/**
 * The {@link TimeKeeper} is responsible for polling for new jobs at regular
 * intervals.
 */
public class TimeKeeper {

   protected static final long TASK_DELAY = 0;
   private Timer timer;
   private Runnable runnable;
   private Long interval;
   
   /**
    * Constructs a new {@link TimeKeeper}.
    * @param timer the {@link Timer} to schedule calls to the {@link Runnable}.
    * @param runnable the {@link Runnable} to run.
    * @param interval the interval between calls to the {@link Runnable}.
    */
   protected TimeKeeper( Timer timer, Runnable runnable, Long interval ) {
      if ( timer == null ) throw new IllegalArgumentException( "Null Timer provided." );
      if ( runnable == null ) throw new IllegalArgumentException( "Null runnable provided." );
      if ( interval == null ) throw new IllegalArgumentException( "Null interval provided." );
      
      this.timer = timer;
      this.runnable = runnable;
      this.interval = interval;
      schedule();
   }//End Constructor
   
   /**
    * Method to schedule the repeating {@link TimerTask}.
    */
   private void schedule(){
      TimerTask task = new TimerTask() {
         @Override public void run() {
            poll();
         }
      };
      timer.schedule( task, TASK_DELAY, interval );
   }//End Method

   /**
    * Method to manually poll.
    */
   public void poll() {
      runnable.run();
   }//End Method

}//End Class
