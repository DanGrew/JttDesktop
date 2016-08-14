/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.item;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import uk.dangrew.jtt.mc.model.Notification;
import uk.dangrew.jtt.mc.view.tree.NotificationTreeController;

/**
 * Interface for providing the necessary elements to drive the {@link uk.dangrew.jtt.mc.view.tree.NotificationTree}.
 */
public interface NotificationTreeItem {

   /**
    * Provides the {@link ObjectProperty} for the {@link Node} to place in the {@link uk.dangrew.jtt.mc.view.tree.NotificationTree}.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Node > contentProperty();
   
   /**
    * Method to get the associated {@link Notification}.
    * @return the {@link Notification}.
    */
   public Notification getNotification();
   
   /**
    * Method to determine whether the {@link NotificationTreeItem} has the given {@link NotificationTreeController}.
    * @param controller the {@link NotificationTreeController} in question.
    * @return true if associated.
    */
   public boolean hasController( NotificationTreeController controller );
}//End Interface
