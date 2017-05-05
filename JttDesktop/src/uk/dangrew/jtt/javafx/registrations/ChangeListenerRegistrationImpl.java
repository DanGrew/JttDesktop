/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.registrations;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;

/**
 * {@link ChangeListenerRegistrationImpl} provides a {@link RegistrationImpl} that can handle
 * the registration of a {@link ChangeListener}.
 * @param <PropertyTypeT> the {@link ChangeListener} property type.
 */
public class ChangeListenerRegistrationImpl< PropertyTypeT > extends RegistrationImpl {
   
   private final Property< PropertyTypeT > property;
   private final ChangeListener< PropertyTypeT > action;
   private boolean registered = false;
   
   /**
    * Constructs a new {@link ChangeListenerRegistrationImpl}.
    * @param property the {@link Property} associated.
    * @param action the {@link ChangeListener} associated with the property.
    */
   public ChangeListenerRegistrationImpl( 
            Property< PropertyTypeT > property, 
            ChangeListener< PropertyTypeT > action 
   ) {
      this.property = property;
      this.action = action;
   }//End Constructor
   
   /**
    * Constructs a new {@link ChangeListenerRegistrationImpl}.
    * @param property the {@link Property} associated.
    * @param initialValue the initial value for the property.
    * @param action the {@link ChangeListener} associated with the property.
    */
   public ChangeListenerRegistrationImpl( 
            Property< PropertyTypeT > property, 
            PropertyTypeT initialValue,
            ChangeListener< PropertyTypeT > action 
   ) {
      this( property, action );
      property.setValue( initialValue );
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
