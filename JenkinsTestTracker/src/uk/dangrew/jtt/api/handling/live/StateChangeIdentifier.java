/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling.live;

import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link StateChangeIdentifier} is responsible for assessing the state changes made
 * following the updating of a {@link JenkinsJob} details.
 */
public class StateChangeIdentifier {
   
   static final Long BUILD_TIME_WHEN_BUILDING = 0L;
   
   private final JobBuiltEvent builtEvents;
   
   private JenkinsJob job;
   private Long buildTime;
   private BuildResultStatus result;

   /**
    * Constructs a new {@link StateChangeIdentifier}.
    */
   public StateChangeIdentifier() {
      builtEvents = new JobBuiltEvent();
   }//End Constructor
   
   /**
    * Method to record the state of the {@link JenkinsJob} before the update.
    * @param job the {@link JenkinsJob} in question.
    */
   public void recordState( JenkinsJob job ) {
      this.job = job;
      this.result = job.getLastBuildStatus();
      this.buildTime = job.totalBuildTimeProperty().get();
   }//End Method

   /**
    * Method to identify the state changes of the associated {@link JenkinsJob}.
    * {@link #recordState(JenkinsJob)} must have been run prior to this call.
    */
   public void identifyStateChanges() {
      if ( job == null ) {
         throw new IllegalStateException( "No state recorded." );
      }
      if ( job.totalBuildTimeProperty().get().equals( BUILD_TIME_WHEN_BUILDING ) ) {
         return;
      }
      if ( !buildTime.equals( job.totalBuildTimeProperty().get() ) ) {
         notifyJobBuilt();
         return;
      }
   }//End Method
   
   /**
    * Method to notify the {@link JobBuiltResult}.
    */
   private void notifyJobBuilt(){
      builtEvents.notify( new Event< JobBuiltResult >( new JobBuiltResult( job, result, job.getLastBuildStatus() ) ) );
   }//End Method

}//End Class
