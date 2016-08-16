/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.jobs;

import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.utility.observable.FunctionListChangeListenerImpl;

/**
 * The {@link JobProgressTreeController} is responsible for controlling the information in the 
 * {@link JobProgressTree}.
 */
public class JobProgressTreeController {

   private final BuildResultStatusLayout layout;
   
   /**
    * Constructs a new {@link JobProgressTreeController}.
    * @param layout the {@link BuildResultStatusLayout} for positioning items in the tree.
    * @param database the {@link JenkinsDatabase} for monitoring {@link JenkinsJob}s.
    */
   public JobProgressTreeController( BuildResultStatusLayout layout, JenkinsDatabase database ) {
      this.layout = layout;
      
      database.jenkinsJobs().addListener( new FunctionListChangeListenerImpl<>( 
                this::addJob,
                this::removeJob
      )  );
      database.jenkinsJobProperties().addBuildResultStatusListener( ( job, old, updated ) -> updateJob( job ) );
   }//End Constructor
   
   /**
    * Method to add the given {@link JenkinsJob} to the {@link JobProgressTree}.
    * @param job the {@link JenkinsJob} to add.
    */
   void addJob( JenkinsJob job ) {
      layout.add( job );
   }//End MethodT
   
   /**
    * Method to remove the given {@link JenkinsJob} from the {@link JobProgressTree}.
    * @param job the {@link JenkinsJob} to remove.
    */
   void removeJob( JenkinsJob job ) {
      layout.remove( job );
   }//End Method
   
   /**
    * Method to update the given {@link JenkinsJob} in the {@link JobProgressTree}.
    * @param job the {@link JenkinsJob} to update.
    */
   void updateJob( JenkinsJob job ) {
      layout.update( job );
   }//End Method
   
   BuildResultStatusLayout layoutManager(){
      return layout;
   }//End Method

}//End Class
