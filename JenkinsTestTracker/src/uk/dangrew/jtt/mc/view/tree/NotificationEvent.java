/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.tree;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.locks.ReentrantLock;

import uk.dangrew.jtt.event.structure.EventManager;
import uk.dangrew.jtt.event.structure.EventSubscription;
import uk.dangrew.jtt.mc.model.Notification;

/**
 * {@link NotificationEvent} provides the manager for {@link Notification}s to be fired and
 * received.
 */
public class NotificationEvent extends EventManager< Void, Notification >{

   private static final Collection< EventSubscription< Void, Notification > > subscriptions = 
            new LinkedHashSet<>();
   private static final ReentrantLock lock = new ReentrantLock();
   
   /**
    * Constructs a new {@link NotificationEvent}.
    */
   public NotificationEvent() {
      super( subscriptions, lock );
   }//End Constructor
   
}//End Class
