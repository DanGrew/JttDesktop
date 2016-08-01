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

/**
 * Interface for providing the necessary elements to drive the {@link uk.dangrew.jtt.mc.view.tree.NotificationTree}.
 */
public interface NotificationTreeItem {

   /**
    * {@link Node} {@link ObjectProperty} for the icon.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Node > getNotificationIcon();

   /**
    * {@link Node} {@link ObjectProperty} for the type.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Node > getNotificationType();

   /**
    * {@link Node} {@link ObjectProperty} for the content.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Node > getContent();

   /**
    * {@link Node} {@link ObjectProperty} for the action button.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Node > getActionButton();
   
   /**
    * {@link Node} {@link ObjectProperty} for the cancel button.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Node > getCancelButton();
}//End Interface
