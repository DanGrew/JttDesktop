/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.tree;

import uk.dangrew.jtt.mc.model.Notification;
import uk.dangrew.jtt.model.event.structure.AbstractEventManagerTest;
import uk.dangrew.jtt.model.event.structure.EventManager;

/**
 * {@link NotificationEvent} test.
 */
public class NotificationEventTest extends AbstractEventManagerTest< Notification > {

   /**
    * {@inheritDoc}
    */
   @Override protected EventManager< Notification > constructSut() {
      return new NotificationEvent();
   }//End Method

}//End Class
