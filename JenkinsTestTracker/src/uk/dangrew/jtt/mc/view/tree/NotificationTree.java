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

import uk.dangrew.jtt.javafx.tree.structure.Tree;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;

/**
 * The {@link NotificationTree} provides a {@link Tree} for {@link NotificationTreeItem}s.
 */
public class NotificationTree extends Tree< 
         NotificationTreeItem, NotificationTreeItem, NotificationTreeController, NotificationTreeLayoutManager 
> {
   
   /**
    * Method to construct a new {@link NotificationTree}.
    */
   public NotificationTree() {
      super();
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
   @Override protected NotificationTreeLayoutManager constructLayout() {
      return new NotificationTreeLayoutManager( this );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected NotificationTreeController constructController() {
      return new NotificationTreeController( getLayoutManager() );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected void performInitialLayout() {
      getLayoutManager().reconstructTree( new ArrayList<>() );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected NotificationTreeController getController() {
      return super.getController();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected NotificationTreeLayoutManager getLayoutManager() {
      return super.getLayoutManager();
   }//End Method
}//End Class
