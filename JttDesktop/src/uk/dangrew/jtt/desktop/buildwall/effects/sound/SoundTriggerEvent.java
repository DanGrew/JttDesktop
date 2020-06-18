/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.sound;

import uk.dangrew.jtt.connection.api.handling.live.BuildResultStatusChange;
import uk.dangrew.kode.event.structure.EventManager;
import uk.dangrew.kode.event.structure.EventSubscription;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@link SoundTriggerEvent} provides the manager for {@link BuildResultStatusChange}s to be fired and
 * received.
 */
public class SoundTriggerEvent extends EventManager< BuildResultStatusChange > {

   private static final Collection<EventSubscription< BuildResultStatusChange >> subscriptions =
            new LinkedHashSet<>();
   private static final ReentrantLock lock = new ReentrantLock();
   
   /**
    * Constructs a new {@link SoundTriggerEvent}.
    */
   public SoundTriggerEvent() {
      super( subscriptions, lock );
   }//End Constructor
   
}//End Class
