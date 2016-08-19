/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.tree;

import uk.dangrew.jtt.javafx.tree.structure.TreeController;
import uk.dangrew.jtt.javafx.tree.structure.TreeLayout;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;

/**
 * The {@link NotificationTreeController} is responsible for redirecting instructions to the {@link NotificationTree}
 * in a controlled manner.
 */
public class NotificationTreeController extends TreeController< NotificationTreeItem, NotificationTreeItem >{
   
   /**
    * Constructs a new {@link NotificationTreeController}.
    * @param layoutManager the {@link NotificationTreeLayoutManager} for organising the tree.
    */
   NotificationTreeController( NotificationTreeLayoutManager layoutManager ) {
      super( layoutManager );
      
      new NotificationEvent().register( event -> add( event.getValue().constructTreeItem( this ) ) );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override protected TreeLayout< NotificationTreeItem, NotificationTreeItem > getLayoutManager() {
      return super.getLayoutManager();
   }//End Method
}//End Class
