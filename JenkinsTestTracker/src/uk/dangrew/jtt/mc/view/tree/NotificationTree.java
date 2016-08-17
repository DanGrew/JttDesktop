/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.tree;

import java.util.ArrayList;
import java.util.function.Function;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import uk.dangrew.jtt.javafx.tree.utility.ColumnHeaderHider;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;

/**
 * The {@link ConfigurationTree} provides a {@link TreeView} for {@link ConfigurationItem}s.
 */
public class NotificationTree extends TreeTableView< NotificationTreeItem > {
   
   private final NotificationTreeLayoutManager layoutManager;
   private final NotificationTreeController controller;
   
   /**
    * Method to construct a new {@link NotificationTree}.
    */
   public NotificationTree() {
      this( new ColumnHeaderHider() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ConfigurationTree}.
    * @param hider the {@link ColumnHeaderHider} in support.
    */
   NotificationTree( ColumnHeaderHider hider ) {
      this.layoutManager = new NotificationTreeLayoutManager( this );
      this.controller = new NotificationTreeController( layoutManager );
      
      constructRoot();
      configureTree();
      
      insertColumn( item -> item.contentProperty() );
      
      hider.hideColumnHeaders( this );
      layoutManager.reconstructTree( new ArrayList<>() );
   }//End Constructor
   
   /**
    * Method to construct the root for the tree.
    */
   private void constructRoot(){
      TreeItem< NotificationTreeItem > root = new TreeItem<>();
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
   private void insertColumn( Function< NotificationTreeItem, ObjectProperty< Node > > nodeRetriever ){
      TreeTableColumn< NotificationTreeItem, Node > iconColumn = new TreeTableColumn<>();
      iconColumn.setCellValueFactory( object -> {
         if ( object.getValue().getValue() == null ) {
            return null;
         }
         return nodeRetriever.apply( object.getValue().getValue() );
      } );
      getColumns().add( iconColumn );
   }//End Method
   
   NotificationTreeController controller(){
      return controller;
   }//End Method
   
   NotificationTreeLayoutManager layoutManager(){
      return layoutManager;
   }//End Method
   
}//End Class
