/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.registrations;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

/**
 * {@link MapChangeListenerRegistrationImpl} provides a {@link RegistrationImpl} that can handle
 * the registration of a {@link MapChangeListener}.
 * @param <KeyTypeT> the key type associated with the {@link MapChangeListener}.
 * @param <ValueTypeT> the value type associated with the {@link MapChangeListener}. 
 */
public class MapChangeListenerRegistrationImpl< KeyTypeT, ValueTypeT > extends RegistrationImpl {
   
   private final ObservableMap< KeyTypeT, ValueTypeT > map;
   private final MapChangeListener< KeyTypeT, ValueTypeT > listener;
   private boolean registered = false;
   
   /**
    * Constructs a new {@link MapChangeListenerRegistrationImpl}.
    * @param observableMap the {@link ObservableMap} associated.
    * @param action the {@link MapChangeListener} associated with the {@link ObservableMap}.
    */
   public MapChangeListenerRegistrationImpl( 
            ObservableMap< KeyTypeT, ValueTypeT > observableMap, 
            MapChangeListener< KeyTypeT, ValueTypeT > action 
   ) {
      this.map = observableMap;
      this.listener = action;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override protected void register() {
      if ( registered ) {
         throw new IllegalStateException( "Registered multiple times which is not supported." );
      }
      
      registered = true;
      map.addListener( listener );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected void unregister() {
      if ( !registered ) {
         throw new IllegalStateException( "Unregistering something that was never registered." );
      }
      
      map.removeListener( listener );
   }//End Method

}//End Class
