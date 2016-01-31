/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel;

import api.sources.ExternalApi;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;

/**
 * The {@link JobBuildSimulator} is a utility class that changes a particular {@link JenkinsJob} 
 * to simulate change in state from an {@link ExternalApi}.
 */
public class JobBuildSimulator {
   
   /**
    * Method to simulate the building of the associated {@link JenkinsJob} on the current {@link Thread}.
    * @param job the {@link JenkinsJob} to simulate for.
    * @param status the {@link BuildResultStatus} to simulate for.
    * @param number the build number.
    * @param expected the expected length of the build.
    * @param interval the time between each progress update.
    * @throws InterruptedException if the {@link Thread} is interrupted.
    */
   public static void simulateBuilding( JenkinsJob job, BuildResultStatus status, int number, long expected, int interval ) throws InterruptedException{
      job.lastBuildStatusProperty().set( status );
      job.lastBuildNumberProperty().set( number );
      job.currentBuildTimeProperty().set( 0 );
      job.expectedBuildTimeProperty().set( expected );
      for ( int i = 0; i < 101; i++ ) {
         Thread.sleep( interval );
         job.currentBuildTimeProperty().set( ( expected / 100 ) * i );
      }
      Thread.sleep( 3000 );
   }//End Method
   
   /**
    * Method to simulate the building of the associated {@link JenkinsJob} on another {@link Thread}.
    * @param job the {@link JenkinsJob} to simulate for.
    * @param status the {@link BuildResultStatus} to simulate for.
    * @param number the build number.
    * @param expected the expected length of the build.
    * @param interval the time between each progress update.
    * @throws InterruptedException if the {@link Thread} is interrupted.
    */
   public static void simulateConcurrentBuilding( JenkinsJob job, BuildResultStatus status, int number, long expected, int interval ) throws InterruptedException{
      new Thread( () -> {
         try {
            simulateBuilding( job, status, number, expected, interval );
         } catch ( Exception e ) {
            e.printStackTrace();
         }
      } ).start();
   }//End Method
}//End Class
