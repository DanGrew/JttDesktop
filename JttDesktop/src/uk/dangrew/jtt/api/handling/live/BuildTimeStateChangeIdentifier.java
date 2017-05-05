/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling.live;

import java.util.HashSet;
import java.util.Set;

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
   
   private final Set< JenkinsJob > earlierNotifications;

   /**
    * Constructs a new {@link BuildTimeStateChangeIdentifier}.
    */
   public BuildTimeStateChangeIdentifier() {
      this.builtEvents = new JobBuiltEvent();
      this.earlierNotifications = new HashSet<>();
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

      if ( resultStatusHasChangedAndBeenImmediatelyNotified() ) {
         return;
      }
      
      if ( job.totalBuildTimeProperty().get().equals( BUILD_TIME_WHEN_BUILDING ) ) {
         return;
      }
      
      //whenever this value changes, doesn't matter what value
      if ( !buildTime.equals( job.totalBuildTimeProperty().get() ) ) {
         if ( jobResultChangedAndWasImmediatelyNotified() ) {
            return;
         }
         notifyJobBuilt();
         return;
      }
   }//End Method
   
   /**
    * Method to determine and handle the notification of a {@link BuildResultStatus} change. This is a particularly
    * tricky issue as sometimes jenkins appears to change the status before the build has completed. This mechanism 
    * aims to immediately respond to all state changes to make sure the correct state is notified. Then the remainder 
    * of the checks will pick up the completion of the build if the state hasn't changed.
    * @return true if the {@link BuildResultStatus} has changed and has been notified requiring no further action.
    */
   private boolean resultStatusHasChangedAndBeenImmediatelyNotified(){
      if ( !result.equals( job.getBuildStatus() ) ) {
         earlierNotifications.add( job );
         notifyJobBuilt();
         
         //this is used for efficiency, it is safe to return false but further checks should
         //not be carried out
         return true;
      }
      
      return false;
   }//End Method
   
   /**
    * Method to check whether the build completion has already been notified following a {@link BuildResultStatus} change.
    * See {@link #resultStatusHasChangedAndBeenImmediatelyNotified()}.
    * @return true if the completion has already been notified and requires no further action.
    */
   private boolean jobResultChangedAndWasImmediatelyNotified(){
      if ( earlierNotifications.contains( job ) ) {
         earlierNotifications.remove( job );
         return true;
      }
      
      return false;
   }//End Method
   
   /**
    * Method to notify the {@link JobBuiltResult}.
    */
   private void notifyJobBuilt(){
      builtEvents.notify( new Event< JobBuiltResult >( new JobBuiltResult( job, result, job.getBuildStatus() ) ) );
   }//End Method

}//End Class
