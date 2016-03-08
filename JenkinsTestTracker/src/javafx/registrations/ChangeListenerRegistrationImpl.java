/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package javafx.registrations;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;

/**
 * {@link ChangeListenerRegistrationImpl} provides a {@link RegistrationImpl} that can handle
 * the registration of a {@link ChangeListener}.
 * @param <PropertyTypeT> the {@link ChangeListener} property type.
 */
public class ChangeListenerRegistrationImpl< PropertyTypeT > extends RegistrationImpl {
   
   private final ObjectProperty< PropertyTypeT > property;
   private final ChangeListener< PropertyTypeT > action;
   private boolean registered = false;
   
   /**
    * Constructs a new {@link ChangeListenerRegistrationImpl}.
    * @param property the {@link ObjectProperty} associated.
    * @param action the {@link ChangeListener} associated with the property.
    */
   public ChangeListenerRegistrationImpl( 
            ObjectProperty< PropertyTypeT > property, 
            ChangeListener< PropertyTypeT > action 
   ) {
      this.property = property;
      this.action = action;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override protected void register() {
      if ( registered ) throw new IllegalStateException( "Registered multiple times which is not supported." );
      
      registered = true;
      property.addListener( action );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected void unregister() {
      if ( !registered ) throw new IllegalStateException( "Unregistering something that was never registered." );
      
      property.removeListener( action );
   }//End Method

}//End Class
