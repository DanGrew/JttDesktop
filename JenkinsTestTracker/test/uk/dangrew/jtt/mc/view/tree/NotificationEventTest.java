/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.tree;

import uk.dangrew.jtt.event.structure.AbstractEventManagerTest;
import uk.dangrew.jtt.event.structure.EventManager;
import uk.dangrew.jtt.mc.model.Notification;

/**
 * {@link NotificationEvent} test.
 */
public class NotificationEventTest extends AbstractEventManagerTest< Void, Notification > {

   /**
    * {@inheritDoc}
    */
   @Override protected EventManager< Void, Notification > constructSut() {
      return new NotificationEvent();
   }//End Method

}//End Class
