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
 * The {@link BuildTimeStateChangeIdentifier} is responsible for assessing the state changes made
 * following the updating of a {@link JenkinsJob} details.
 */
public class BuildTimeStateChangeIdentifier implements StateChangeIdentifier {
   
   static final Long BUILD_TIME_WHEN_BUILDING = 0L;
   
   private final JobBuiltEvent builtEvents;
   
   private JenkinsJob job;
   private Long buildTime;
   private BuildResultStatus result;

   /**
    * Constructs a new {@link BuildTimeStateChangeIdentifier}.
    */
   public BuildTimeStateChangeIdentifier() {
      builtEvents = new JobBuiltEvent();
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public void recordState( JenkinsJob job ) {
      this.job = job;
      this.result = job.getBuildStatus();
      this.buildTime = job.totalBuildTimeProperty().get();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void identifyStateChanges() {
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
      builtEvents.notify( new Event< JobBuiltResult >( new JobBuiltResult( job, result, job.getBuildStatus() ) ) );
   }//End Method

}//End Class
