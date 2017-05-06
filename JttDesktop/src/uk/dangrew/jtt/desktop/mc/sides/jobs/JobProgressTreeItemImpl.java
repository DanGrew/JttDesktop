/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.jobs;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JobProgressTreeItem} defines the content of a {@link javafx.scene.control.TreeItem}
 * in the {@link JobProgressTree}.
 */
public class JobProgressTreeItemImpl implements JobProgressTreeItem {
   
   private final JenkinsJob job;
   private final ObjectProperty< Node > content;
   
   /**
    * Constructs a new {@link JobProgressTreeItem}.
    * @param job the {@link JenkinsJob} associated in the tree.
    */
   public JobProgressTreeItemImpl( JenkinsJob job ) {
      this.job = job;
      this.content = new SimpleObjectProperty<>( new JobProgressTreeNode( job ) );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > contentProperty(){
      return content;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public JenkinsJob getJenkinsJob(){
      return job;
   }//End Method
   
}//End Class
