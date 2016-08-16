/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.jobs;

import java.util.function.Function;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import uk.dangrew.jtt.javafx.tree.ColumnHeaderHider;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link JobProgressTree} provides a {@link TreeView} for {@link JobProgressTreeItem}s.
 */
public class JobProgressTree extends TreeTableView< JobProgressTreeItem > {
   
   private final BuildResultStatusLayout layoutManager;
   private final JobProgressTreeController controller;
   
   /**
    * Constructs a new {@link JobProgressTree}.
    */
   public JobProgressTree( JenkinsDatabase database ) {
      this( database, new ColumnHeaderHider() );
   }//End Constructor
   
   /**
    * Constructs a new {@link JobProgressTree}.
    * @param database the {@link JenkinsDatabase} providing {@link uk.dangrew.jtt.model.jobs.JenkinsJob}s.
    * @param hider the {@link ColumnHeaderHider} in support.
    */
   JobProgressTree( JenkinsDatabase database, ColumnHeaderHider hider ) {
      constructRoot();
      configureTree();
      
      insertColumn( item -> item.contentProperty() );
      this.layoutManager = new BuildResultStatusLayout( this );
      layoutManager.reconstructTree( database.jenkinsJobs() );
      this.controller = new JobProgressTreeController( layoutManager, database );
      
      hider.hideColumnHeaders( this );
   }//End Constructor
   
   /**
    * Method to construct the root for the tree.
    */
   private void constructRoot(){
      TreeItem< JobProgressTreeItem > root = new TreeItem<>();
      root.setExpanded( true );
      setRoot( root );
      setShowRoot( false );
   }//End Method
   
   /**
    * Method to configure the tree.
    */
   private void configureTree(){
      getSelectionModel().setSelectionMode( SelectionMode.SINGLE );
      setColumnResizePolicy( TreeTableView.CONSTRAINED_RESIZE_POLICY );
   }//End Method
   
   /**
    * Method to insert a single column into the tree.
    * @param nodeRetriever the {@link Function} for supplying the {@link Node} to draw.
    * @param width the preferred width of the column.
    */
   private void insertColumn( Function< JobProgressTreeItem, ObjectProperty< Node > > nodeRetriever ){
      TreeTableColumn< JobProgressTreeItem, Node > iconColumn = new TreeTableColumn<>();
      iconColumn.setCellValueFactory( object -> {
         if ( object.getValue().getValue() == null ) {
            return null;
         }
         return nodeRetriever.apply( object.getValue().getValue() );
      } );
      getColumns().add( iconColumn );
   }//End Method
   
   JobProgressTreeController controller(){
      return controller;
   }//End Method
   
   BuildResultStatusLayout layoutManager(){
      return layoutManager;
   }//End Method
   
}//End Class
