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

import api.handling.BuildState;
import model.jobs.JenkinsJob;
import storage.database.JenkinsDatabase;

/**
 * The {@link BuildProgressCalculator} is responsible for updating the build progress
 * of the {@link JenkinsJob}s in the {@link JenkinsDatabase}.
 */
public class BuildProgressCalculator implements Runnable {

   private JenkinsDatabase database;
   private Clock clock;
   
   /**
    * Constructs a new {@link BuildProgressCalculator}.
    * @param database the {@link JenkinsDatabase} containing the {@link JenkinsJob}s.
    * @param clock the {@link Clock} for getting the current time.
    */
   public BuildProgressCalculator( JenkinsDatabase database, Clock clock ) {
      this.database = database;
      this.clock = clock;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void run() {
      for ( JenkinsJob job : database.jenkinsJobs() ) {
         if ( job.buildStateProperty().get().equals( BuildState.Built ) ) continue;
         
         long currentTime = clock.millis();
         long timestamp = job.lastBuildTimestampProperty().get();
         if ( timestamp > currentTime ) continue;
         
         job.currentBuildTimeProperty().set( currentTime - timestamp );
      }
   }//End Method

}//End Class
