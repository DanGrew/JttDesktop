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

import uk.dangrew.jtt.api.handling.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

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
         if ( job.buildStateProperty().get().equals( BuildState.Built ) ) {
            continue;
         }
         
         long currentTime = clock.millis();
         long timestamp = job.currentBuildTimestampProperty().get();
         if ( timestamp > currentTime ) {
            continue;
         }
         
         job.currentBuildTimeProperty().set( currentTime - timestamp );
      }
   }//End Method

}//End Class
