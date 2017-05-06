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

import javafx.scene.control.SelectionMode;
import uk.dangrew.jtt.friendly.javafx.FriendlyMenuOpener;
import uk.dangrew.jtt.javafx.tree.structure.Tree;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

/**
 * The {@link NotificationTree} provides a {@link Tree} for {@link NotificationTreeItem}s.
 */
public class NotificationTree extends Tree< 
         NotificationTreeItem,
         NotificationTreeItem, 
         NotificationTreeLayoutManager, 
         NotificationTreeController 
> {
   
   private final NotificationTreeContextMenu menu;
   
   /**
    * Method to construct a new {@link NotificationTree}.
    */
   public NotificationTree( JenkinsDatabase database ) {
      super();
      initialise();
      
      getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
      menu = new NotificationTreeContextMenu( this, database );
      new FriendlyMenuOpener( this, menu );
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
   
   NotificationTreeContextMenu menu(){
      return menu;
   }//End Method
}//End Class
