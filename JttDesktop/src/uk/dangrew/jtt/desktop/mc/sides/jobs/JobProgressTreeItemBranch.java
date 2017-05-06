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
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JobProgressTreeItemBranch} provides a {@link JobProgressTreeItem} for the branch specifically
 * and not the items in it.
 */
public class JobProgressTreeItemBranch implements JobProgressTreeItem {

   private final String name;
   private final ObjectProperty< Node > content;
   
   /**
    * Constructs a new {@link JobProgressTreeItemBranch}.
    * @param branchName the name of the branch.
    */
   public JobProgressTreeItemBranch( String branchName ) {
      this( branchName, new JavaFxStyle() );
   }//End Constructor

   /**
    * Constructs a new {@link JobProgressTreeItemBranch}.
    * @param branchName the name of the branch.
    * @param styling the {@link JavaFxStyle} to apply.
    */
   JobProgressTreeItemBranch( String branchName, JavaFxStyle styling ) {
      this.name = branchName;
      this.content = new SimpleObjectProperty<>( styling.createBoldLabel( branchName ) );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Node > contentProperty() {
      return content;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public JenkinsJob getJenkinsJob() {
      return null;
   }//End Method

   /**
    * Getter for the name of the branch.
    * @return the {@link String} name.
    */
   public String getName() {
      return name;
   }//End Method

}//End Class
