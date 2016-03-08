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

/**
 * The {@link ChangeListenerBindingImpl} provides a {@link RegistrationImpl} that uses two {@link ChangeListenerRegistrationImpl}s,
 * one for each direction keeping two properties in sync.
 * @param <PropertyTypeT> the property value type.
 */
public class ChangeListenerBindingImpl< PropertyTypeT > extends RegistrationImpl {
   
   private final ObjectProperty< PropertyTypeT > propertyA;
   private final ObjectProperty< PropertyTypeT > propertyB;
   private RegistrationImpl propertyARegistration;
   private RegistrationImpl propertyBRegistration;

   /**
    * Constructs a new {@link ChangeListenerBindingImpl}.
    * @param propertyA the {@link ObjectProperty} providing initial value.
    * @param propertyB the {@link ObjectProperty} to bind with.
    */
   public ChangeListenerBindingImpl( ObjectProperty< PropertyTypeT > propertyA, ObjectProperty< PropertyTypeT > propertyB ) {
      this.propertyA = propertyA;
      this.propertyB = propertyB;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override protected void register() {
      if ( propertyARegistration != null ) throw new IllegalStateException( "Already bound." );
      
      propertyBRegistration = new ChangeListenerRegistrationImpl<>( 
               propertyB, 
               propertyA.get(),
               ( source, old, updated ) -> propertyA.set( updated ) 
      );
      propertyBRegistration.register();
      
      propertyARegistration = new ChangeListenerRegistrationImpl<>( 
               propertyA,
               propertyB.get(),
               ( source, old, updated ) -> propertyB.set( updated )  
      );
      propertyARegistration.register();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected void unregister() {
      propertyARegistration.unregister();
      propertyBRegistration.unregister();
      
      propertyARegistration = null;
      propertyBRegistration = null;
   }//End Method

}//End Class
