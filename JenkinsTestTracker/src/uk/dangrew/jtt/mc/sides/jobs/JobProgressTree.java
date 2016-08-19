/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.jobs;

import uk.dangrew.jtt.javafx.tree.structure.Tree;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link JobProgressTree} provides a {@link TreeView} for {@link JobProgressTreeItem}s.
 */
public class JobProgressTree extends Tree< JobProgressTreeItem, JenkinsJob, JobProgressTreeController, BuildResultStatusLayout > {
   
   private final JenkinsDatabase database;
   
   /**
    * Constructs a new {@link JobProgressTree}.
    * @param database the {@link JenkinsDatabase} for monitoring {@link JenkinsJob}s.
    */
   public JobProgressTree( JenkinsDatabase database ) {
      super();
      this.database = database;
      initialise();
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override protected void insertColumns() {
      insertColumn( item -> item.contentProperty() );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected BuildResultStatusLayout constructLayout() {
      return new BuildResultStatusLayout( this );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected JobProgressTreeController constructController() {
      return new JobProgressTreeController( getLayoutManager(), database );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected void performInitialLayout() {
      getLayoutManager().reconstructTree( database.jenkinsJobs() );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected JobProgressTreeController getController() {
      return super.getController();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected BuildResultStatusLayout getLayoutManager() {
      return super.getLayoutManager();
   }//End Method
   
}//End Class
