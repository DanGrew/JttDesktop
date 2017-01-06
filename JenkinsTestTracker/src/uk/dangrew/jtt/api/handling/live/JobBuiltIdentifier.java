/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling.live;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JobBuiltIdentifier} is a {@link StateChangeIdentifier} that is responsible
 * for detecting when a {@link JenkinsJob} has built, and providing a {@link JobBuiltEvent}. 
 */
public class JobBuiltIdentifier implements StateChangeIdentifier {

   private final Map< JenkinsJob, BuildState > buildStateMap;
   private final Map< JenkinsJob, BuildResultStatus > buildResultStatusMap;
   private final Map< JenkinsJob, Integer > buildNumberMap;
   
   private final JobBuiltEvent builtEvents;
   
   /**
    * Constructs a new {@link JobBuiltIdentifier}.
    */
   public JobBuiltIdentifier() {
      this.buildStateMap = new LinkedHashMap<>();
      this.buildResultStatusMap = new HashMap<>();
      this.buildNumberMap = new HashMap<>();
      
      this.builtEvents = new JobBuiltEvent();
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public void recordState( JenkinsJob job ) {
      if ( buildStateMap.containsKey( job ) ) {
         return;
      }
      updateJobState( job );
   }//End Method
   
   /**
    * Method to update the record of the {@link JenkinsJob}.
    * @param job the {@link JenkinsJob} to record the state of.
    */
   private void updateJobState( JenkinsJob job ) {
      buildStateMap.put( job, job.buildStateProperty().get() );
      buildResultStatusMap.put( job, job.getBuildStatus() );
      buildNumberMap.put( job, job.getBuildNumber() );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void identifyStateChanges() {
      for ( Entry< JenkinsJob, BuildState > entry : buildStateMap.entrySet() ) {
         BuildState previous = entry.getValue();
         BuildState current = entry.getKey().buildStateProperty().get();
         
         if ( previous == current ) {
            Integer previousNumber = buildNumberMap.get( entry.getKey() );
            Integer currentNumber = entry.getKey().getBuildNumber();
            
            if ( !previousNumber.equals( currentNumber ) ) {
               notifyJobBuilt( entry.getKey() );
            }
         } else if ( current == BuildState.Built ) {
            notifyJobBuilt( entry.getKey() );
         }
         
         updateJobState( entry.getKey() );
      }
   }//End Method
   
   /**
    * Method to notify the {@link JobBuiltResult}.
    * @param job the {@link JenkinsJob} to notify for.
    */
   private void notifyJobBuilt( JenkinsJob job ){
      builtEvents.notify( new Event< JobBuiltResult >( new JobBuiltResult( 
               job, 
               buildResultStatusMap.get( job ), 
               job.getBuildStatus() ) 
      ) );
   }//End Method
   
}//End Class
