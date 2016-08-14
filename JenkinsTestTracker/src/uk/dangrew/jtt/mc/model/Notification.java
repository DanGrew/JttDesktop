/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.model;

import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;

/**
 * The {@link Notification} provides an interface for every type of {@link Notification}
 * to be displayed in the {@link uk.dangrew.jtt.mc.view.tree.NotificationTree}.
 */
public interface Notification {
   
   /**
    * Method to construct a new {@link NotificationTreeItem} from this {@link Notification}.
    * @return the {@link NotificationTreeItem}.
    */
   public NotificationTreeItem constructTreeItem();

}//End Interface
