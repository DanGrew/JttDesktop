/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel;

import javafx.util.Pair;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JobBuildSimulator} is a utility class that changes a particular {@link JenkinsJob} 
 * to simulate change in state from an {@link ExternalApi}.
 */
public class JobBuildSimulator {
   
   /**
    * Method to simulate the building of the associated {@link JenkinsJob} on the current {@link Thread}.
    * @param job the {@link JenkinsJob} to simulate for.
    * @param intialStatus the {@link BuildResultStatus} to simulate for.
    * @param resultingStatus the {@link BuildResultStatus} after when built.
    * @param number the build number.
    * @param expected the expected length of the build.
    * @param interval the time between each progress update.
    * @throws InterruptedException if the {@link Thread} is interrupted.
    */
   public static void simulateBuilding( 
            JenkinsJob job, 
            BuildResultStatus intialStatus,
            BuildResultStatus resultingStatus,
            int number, 
            long expected, 
            int interval 
   ) throws InterruptedException{
      job.buildStateProperty().set( BuildState.Building );
      job.lastBuildProperty().set( new Pair<>( number, intialStatus ) );
      job.currentBuildTimeProperty().set( 0 );
      job.expectedBuildTimeProperty().set( expected );
      
      long currentTime = 0;
      while ( currentTime < expected ) {
         Thread.sleep( interval );
         currentTime += interval;
         job.currentBuildTimeProperty().set( currentTime );
      }
      Thread.sleep( 1000 );
      job.lastBuildProperty().set( new Pair<>( number, resultingStatus ) );
      job.buildStateProperty().set( BuildState.Building );
   }//End Method
   
   /**
    * Method to simulate the building of the associated {@link JenkinsJob} on another {@link Thread}.
    * @param job the {@link JenkinsJob} to simulate for.
    * @param initialStatus the {@link BuildResultStatus} to simulate for.
    * @param resultingStatus the {@link BuildResultStatus} after when built.
    * @param number the build number.
    * @param expected the expected length of the build.
    * @param interval the time between each progress update.
    * @throws InterruptedException if the {@link Thread} is interrupted.
    */
   public static void simulateConcurrentBuilding( 
            JenkinsJob job, 
            BuildResultStatus initialStatus,
            BuildResultStatus resultingStatus,
            int number, 
            long expected, 
            int interval 
   ) throws InterruptedException{
      new Thread( () -> {
         try {
            simulateBuilding( job, initialStatus, resultingStatus, number, expected, interval );
         } catch ( Exception e ) {
            e.printStackTrace();
         }
      } ).start();
   }//End Method
}//End Class
