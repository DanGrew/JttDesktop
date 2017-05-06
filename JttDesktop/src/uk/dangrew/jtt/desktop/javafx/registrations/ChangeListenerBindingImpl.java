/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.registrations;

import javafx.beans.property.Property;

/**
 * The {@link ChangeListenerBindingImpl} provides a {@link RegistrationImpl} that uses two {@link ChangeListenerRegistrationImpl}s,
 * one for each direction keeping two properties in sync.
 * @param <PropertyTypeT> the property value type.
 */
public class ChangeListenerBindingImpl< PropertyTypeT > extends RegistrationImpl {
   
   private final Property< PropertyTypeT > propertyA;
   private final Property< PropertyTypeT > propertyB;
   private RegistrationImpl propertyARegistration;
   private RegistrationImpl propertyBRegistration;

   /**
    * Constructs a new {@link ChangeListenerBindingImpl}.
    * @param propertyA the {@link Property} providing initial value.
    * @param propertyB the {@link Property} to bind with.
    */
   public ChangeListenerBindingImpl( Property< PropertyTypeT > propertyA, Property< PropertyTypeT > propertyB ) {
      this.propertyA = propertyA;
      this.propertyB = propertyB;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override protected void register() {
      if ( propertyARegistration != null ) {
         throw new IllegalStateException( "Already bound." );
      }
      
      propertyBRegistration = new ChangeListenerRegistrationImpl<>( 
               propertyB, 
               propertyA.getValue(),
               ( source, old, updated ) -> propertyA.setValue( updated ) 
      );
      propertyBRegistration.register();
      
      propertyARegistration = new ChangeListenerRegistrationImpl<>( 
               propertyA,
               propertyB.getValue(),
               ( source, old, updated ) -> propertyB.setValue( updated )  
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
