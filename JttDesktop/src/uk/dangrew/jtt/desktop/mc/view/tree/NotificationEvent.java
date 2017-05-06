/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.view.tree;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.locks.ReentrantLock;

import uk.dangrew.jtt.desktop.mc.model.Notification;
import uk.dangrew.jtt.model.event.structure.EventManager;
import uk.dangrew.jtt.model.event.structure.EventSubscription;

/**
 * {@link NotificationEvent} provides the manager for {@link Notification}s to be fired and
 * received.
 */
public class NotificationEvent extends EventManager< Notification >{

   private static final Collection< EventSubscription< Notification > > subscriptions = 
            new LinkedHashSet<>();
   private static final ReentrantLock lock = new ReentrantLock();
   
   /**
    * Constructs a new {@link NotificationEvent}.
    */
   public NotificationEvent() {
      super( subscriptions, lock );
   }//End Constructor
   
}//End Class
