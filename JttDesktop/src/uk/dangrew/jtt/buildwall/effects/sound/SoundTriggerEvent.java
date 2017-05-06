/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.sound;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.locks.ReentrantLock;

import uk.dangrew.jtt.model.event.structure.EventManager;
import uk.dangrew.jtt.model.event.structure.EventSubscription;

/**
 * {@link SoundTriggerEvent} provides the manager for {@link BuildResultStatusChange}s to be fired and
 * received.
 */
public class SoundTriggerEvent extends EventManager< BuildResultStatusChange >{

   private static final Collection< EventSubscription< BuildResultStatusChange > > subscriptions = 
            new LinkedHashSet<>();
   private static final ReentrantLock lock = new ReentrantLock();
   
   /**
    * Constructs a new {@link SoundTriggerEvent}.
    */
   public SoundTriggerEvent() {
      super( subscriptions, lock );
   }//End Constructor
   
}//End Class
