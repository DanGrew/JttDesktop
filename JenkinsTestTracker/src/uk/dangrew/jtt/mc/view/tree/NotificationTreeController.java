/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.tree;

import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;

/**
 * The {@link NotificationTreeController} is responsible for redirecting instructions to the {@link NotificationTree}
 * in a controlled manner.
 */
class NotificationTreeController {
   
   private final NotificationTreeLayoutManager layoutManager;

   /**
    * Constructs a new {@link NotificationTreeController}.
    * @param layoutManager the {@link NotificationTreeLayoutManager} for organising the tree.
    */
   NotificationTreeController( NotificationTreeLayoutManager layoutManager ) {
      this.layoutManager = layoutManager;
   }//End Constructor

   /**
    * Method to add a {@link NotificationTreeItem} to the {@link NotificationTree}.
    * @param item the {@link NotificationTreeItem} to add.
    */
   void addItem( NotificationTreeItem item ) {
      layoutManager.add( item );
   }//End Method

   /**
    * Method to remove a {@link NotificationTreeItem} from the {@link NotificationTree}.
    * @param item the {@link NotificationTreeItem} to remove.
    */
   void removeItem( NotificationTreeItem item ) {
      layoutManager.remove( item );
   }//End Method

   NotificationTreeLayoutManager layoutManager(){
      return layoutManager;
   }//End Method

}//End Class
