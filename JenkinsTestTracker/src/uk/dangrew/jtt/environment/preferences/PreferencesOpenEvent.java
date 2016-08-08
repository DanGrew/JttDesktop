/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.preferences;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.locks.ReentrantLock;

import uk.dangrew.jtt.event.structure.EventManager;
import uk.dangrew.jtt.event.structure.EventSubscription;

/**
 * {@link EventManager} for the event of opening the preferences window.
 */
public class PreferencesOpenEvent extends EventManager< Void, WindowPolicy >{

   private static final Collection< EventSubscription< Void, WindowPolicy > > subscriptions = 
            new LinkedHashSet<>();
   private static final ReentrantLock lock = new ReentrantLock();
   
   /**
    * Constructs a new {@link PreferencesOpenEvent}.
    */
   public PreferencesOpenEvent() {
      super( subscriptions, lock );
   }//End Constructor

}//End Class
