/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.tree.structure;

import java.util.function.Function;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;

/**
 * The {@link Tree} provides a {@link TreeTableView} for that follows a {@link TreeController}
 * and {@link TreeLayout} pattern. In this pattern the controller is responsible for all actions
 * applied to the table, even if other objects actually perform the action, and the layout manager
 * determines how items are arranged in the {@link Tree}.
 * @param <TreeItemValueT> the {@link TreeItemValue} associated with {@link TreeItem}s.
 * @param <ObjectTypeT> the underlying object being represented in the tree, can be the same as TreeItemValueT.
 * @param <LayoutT> the {@link TreeLayout} type.
 * @param <ControllerT> the {@link TreeController} type.
 */
public abstract class Tree< 
      TreeItemValueT extends TreeItemValue, 
      ObjectTypeT,
      LayoutT extends TreeLayout< TreeItemValueT, ObjectTypeT >,
      ControllerT extends TreeController< TreeItemValueT, ObjectTypeT, LayoutT >
> extends TreeTableView< TreeItemValueT > 
{
   
   private LayoutT layoutManager;
   private ControllerT controller;
   
   /**
    * Constructs a new {@link Tree}.
    */
   protected Tree() {
      constructRoot();
      configureTree();
   }//End Constructor
   
   /**
    * Method to initialise the {@link Tree}. This should be called in the constructor of
    * the child once all variables required are initialised and ready for the {@link Tree}
    * to use.
    */
   protected void initialise(){
      insertColumns();
      layoutManager = constructLayout();
      performInitialLayout();
      controller = constructController();
   }//End Method
   
   /**
    * Method to insert all columns required in the table. This will be called only once on construction
    * and before the {@link TreeController} and {@link TreeLayout}s are available
    */
   protected abstract void insertColumns();
   
   /**
    * Method to construct the {@link TreeLayout} to use for this {@link Tree}. This will be called and
    * used before the {@link TreeController}.
    * @return the {@link TreeLayout} being used with this {@link Tree}.
    */
   protected abstract LayoutT constructLayout();
   
   /**
    * Method to construct the {@link TreeController} to use for this {@link Tree}. This will be called and
    * used after the {@link TreeLayout}.
    * @return the {@link TreeController} being used with this {@link Tree}.
    */
   protected abstract ControllerT constructController();
   
   /**
    * Method to perform the initial layout of items in the {@link Tree}. The {@link TreeController} and 
    * {@link TreeLayout} are available at this point.
    */
   protected abstract void performInitialLayout();
   
   /**
    * Method to construct the root for the tree.
    */
   private void constructRoot(){
      TreeItem< TreeItemValueT > root = new TreeItem<>();
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
   protected final void insertColumn( Function< TreeItemValueT, ObjectProperty< Node > > nodeRetriever ){
      TreeTableColumn< TreeItemValueT, Node > iconColumn = new TreeTableColumn<>();
      iconColumn.setCellValueFactory( object -> {
         if ( object.getValue().getValue() == null ) {
            return null;
         }
         return nodeRetriever.apply( object.getValue().getValue() );
      } );
      getColumns().add( iconColumn );
   }//End Method
   
   /**
    * Protected access to the {@link TreeLayout}.
    * @return the {@link TreeLayout}.
    */
   protected LayoutT getLayoutManager(){
      return layoutManager;
   }//End Method
   
   /**
    * Protected access to the {@link TreeController}.
    * @return the {@link TreeController}.
    */
   protected ControllerT getController(){
      return controller;
   }//End Method
   
}//End Class
