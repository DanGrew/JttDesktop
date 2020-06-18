/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.environment.preferences;

import uk.dangrew.kode.event.structure.EventManager;
import uk.dangrew.kode.event.structure.EventSubscription;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.locks.ReentrantLock;

/**
 * {@link EventManager} for the event of opening the preferences window.
 */
public class PreferencesOpenEvent extends EventManager< PreferenceBehaviour > {

   private static final Collection<EventSubscription< PreferenceBehaviour >> subscriptions =
            new LinkedHashSet<>();
   private static final ReentrantLock lock = new ReentrantLock();
   
   /**
    * Constructs a new {@link PreferencesOpenEvent}.
    */
   public PreferencesOpenEvent() {
      super( subscriptions, lock );
   }//End Constructor

}//End Class
