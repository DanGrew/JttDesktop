/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.jobs;

import uk.dangrew.jtt.desktop.javafx.tree.structure.TreeController;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.utility.observable.FunctionListChangeListenerImpl;

/**
 * The {@link JobProgressTreeController} is responsible for controlling the information in the 
 * {@link JobProgressTree}.
 */
public class JobProgressTreeController extends TreeController< 
   JobProgressTreeItem, 
   JenkinsJob, 
   BuildResultStatusLayout 
>{

   /**
    * Constructs a new {@link JobProgressTreeController}.
    * @param layout the {@link BuildResultStatusLayout} for positioning items in the tree.
    * @param database the {@link JenkinsDatabase} for monitoring {@link JenkinsJob}s.
    */
   public JobProgressTreeController( BuildResultStatusLayout layout, JenkinsDatabase database ) {
      super( layout );
      
      database.jenkinsJobs().addListener( new FunctionListChangeListenerImpl<>( 
                this::add,
                this::remove
      )  );
      database.jenkinsJobProperties().addBuildResultStatusListener( ( job, old, updated ) -> update( job ) );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override protected BuildResultStatusLayout getLayoutManager() {
      return super.getLayoutManager();
   }//End Method

}//End Class
