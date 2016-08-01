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
import uk.dangrew.jtt.javafx.tree.ColumnHeaderHider;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;

/**
 * The {@link ConfigurationTree} provides a {@link TreeView} for {@link ConfigurationItem}s.
 */
public class NotificationTree extends TreeTableView< NotificationTreeItem > {
   
   static final double CONTROL_COLUMN_WIDTH = 40;
   static final double CONTENT_COLUMN_WIDTH = 400;
   static final double TYPE_COLUMN_WIDTH = 100;
   static final double ICON_COLUMN_WIDTH = 100;
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
      
      insertColumn( item -> item.getNotificationIcon(), ICON_COLUMN_WIDTH );
      insertColumn( item -> item.getNotificationType(), TYPE_COLUMN_WIDTH );
      insertColumn( item -> item.getContent(), CONTENT_COLUMN_WIDTH );
      insertColumn( item -> item.getActionButton(), CONTROL_COLUMN_WIDTH );
      insertColumn( item -> item.getCancelButton(), CONTROL_COLUMN_WIDTH );
      
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
   }//End Method
   
   /**
    * Method to insert a single column into the tree.
    * @param nodeRetriever the {@link Function} for supplying the {@link Node} to draw.
    * @param width the preferred width of the column.
    */
   private void insertColumn( Function< NotificationTreeItem, ObjectProperty< Node > > nodeRetriever, double width ){
      TreeTableColumn< NotificationTreeItem, Node > iconColumn = new TreeTableColumn<>();
      iconColumn.setCellValueFactory( object -> {
         if ( object.getValue().getValue() == null ) {
            return null;
         }
         return nodeRetriever.apply( object.getValue().getValue() );
      } );
      iconColumn.setPrefWidth( width );
      getColumns().add( iconColumn );
   }//End Method
   
   NotificationTreeController controller(){
      return controller;
   }//End Method
   
   NotificationTreeLayoutManager layoutManager(){
      return layoutManager;
   }//End Method
   
}//End Class
