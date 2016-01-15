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
import java.util.TimerTask;

import api.handling.JenkinsFetcher;

/**
 * The {@link TimeKeeper} is responsible for polling for new jobs at regular
 * intervals.
 */
public class TimeKeeper {

   private JenkinsFetcher fetcher;
   private Timer timer;
   private long interval;
   
   /**
    * Constructs a new {@link TimeKeeper}.
    * @param fetcher the {@link JenkinsFetcher} to use to fetch data.
    * @param interval the interval of the polling.
    */
   public TimeKeeper( JenkinsFetcher fetcher, long interval ) {
      this.fetcher = fetcher;
      this.interval = interval;
      schedule();
   }//End Constructor
   
   /**
    * Method to schedule the repeating {@link TimerTask}.
    */
   private void schedule(){
      if ( timer != null ) timer.cancel();

      TimerTask task = new TimerTask() {
         @Override public void run() {
            fetcher.fetchJobsAndUpdateDetails();
         }
      };
      timer = new Timer();
      timer.schedule( task, 0, interval );
   }//End Method

   /**
    * Method to set the interval to poll at. This will cancel the current and 
    * start a fresh, causing an immediate poll.
    * @param newInterval the new interval to poll at.
    */
   public void setInterval( long newInterval ) {
      this.interval = newInterval;
      schedule();
   }//End Method

}//End Class
