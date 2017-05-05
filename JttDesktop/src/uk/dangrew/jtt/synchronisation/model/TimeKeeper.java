/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.synchronisation.model;

import java.util.Timer;
import java.util.TimerTask;

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
    * @param runnable the {@link Runnable} to run.
    * @param timer the {@link Timer} to schedule calls to the {@link Runnable}.
    * @param interval the interval between calls to the {@link Runnable}.
    */
   protected TimeKeeper( Runnable runnable, Timer timer, Long interval ) {
      if ( runnable == null ) {
         throw new IllegalArgumentException( "Null runnable provided." );
      }
      
      this.timer = timer;
      this.runnable = runnable;
      this.interval = interval;
      
      if ( timer != null ) {
         if ( interval == null ) {
            throw new IllegalArgumentException( "Timer provided: must provide interval." );
         }
         schedule();
      }
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

   /**
    * Method to determine whether the {@link TimeKeeper} has a {@link Timer}.
    * @return true if automatically running, false if requires manual interaction.
    */
   public boolean hasTimer() {
      return timer != null;
   }//End Method
   
   /**
    * Getter for the interval associated with the running of the task.
    * @return the interval, can be null.
    */
   public Long getInterval(){
      return interval;
   }//End Method
   
}//End Class
