/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.view.tree;

import uk.dangrew.jtt.desktop.mc.model.Notification;
import uk.dangrew.kode.event.structure.AbstractEventManagerTest;
import uk.dangrew.kode.event.structure.EventManager;

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
