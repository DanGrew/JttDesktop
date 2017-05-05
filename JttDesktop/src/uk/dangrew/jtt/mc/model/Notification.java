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
import uk.dangrew.jtt.mc.view.tree.NotificationTreeController;

/**
 * The {@link Notification} provides an interface for every type of {@link Notification}
 * to be displayed in the {@link uk.dangrew.jtt.mc.view.tree.NotificationTree}.
 */
public interface Notification {
   
   /**
    * Method to get a description of the {@link Notification}.
    * @return the {@link String} description.
    */
   public String getDescription();
   
   /**
    * Method to construct a new {@link NotificationTreeItem} from this {@link Notification}.
    * @param controller the {@link NotificationTreeController} for providing instructions.
    * @return the {@link NotificationTreeItem}.
    */
   public NotificationTreeItem constructTreeItem( NotificationTreeController controller );

   /**
    * Method to show a desktop notification for the {@link Notification}.
    */
   public void showDesktopNotification();
   
}//End Interface
