/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.registrations;

import java.util.function.Function;

import javafx.beans.property.Property;

/**
 * The {@link ChangeListenerMismatchBindingImpl} is responsible for providing a {@link RegistrationImpl}
 * that binds like the {@link ChangeListenerBindingImpl} but allows mapping between different property values.
 * @param <PropertyTypeT> the first property value type.
 * @param <ConversionTypeT> the value type being converted to and from the property value type.
 */
public class ChangeListenerMismatchBindingImpl< PropertyTypeT, ConversionTypeT > extends RegistrationImpl {
   
   private final Property< PropertyTypeT > propertyA;
   private final Property< ConversionTypeT > propertyB;
   private final Function< ConversionTypeT, PropertyTypeT > toPropertyConverter;
   private final Function< PropertyTypeT, ConversionTypeT > fromPropertyConverter; 
   private RegistrationImpl propertyARegistration;
   private RegistrationImpl propertyBRegistration;

   /**
    * Constructs a new {@link ChangeListenerMismatchBindingImpl}.
    * @param propertyA the first property.
    * @param propertyB the second property.
    * @param toPropertyConverter the {@link Function} from first to second type.
    * @param fromPropertyConverter the {@link Function} from second to first type.
    */
   public ChangeListenerMismatchBindingImpl( 
            Property< PropertyTypeT > propertyA, 
            Property< ConversionTypeT > propertyB,
            Function< ConversionTypeT, PropertyTypeT > toPropertyConverter,
            Function< PropertyTypeT, ConversionTypeT > fromPropertyConverter
   ) {
      this.propertyA = propertyA;
      this.propertyB = propertyB;
      this.toPropertyConverter = toPropertyConverter;
      this.fromPropertyConverter = fromPropertyConverter;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override protected void register() {
      if ( propertyARegistration != null ) throw new IllegalStateException( "Already bound." );
      
      propertyBRegistration = new ChangeListenerRegistrationImpl< ConversionTypeT >( 
               propertyB, 
               fromPropertyConverter.apply( propertyA.getValue() ),
               ( source, old, updated ) -> propertyA.setValue( toPropertyConverter.apply( updated ) ) 
      );
      propertyBRegistration.register();
      
      propertyARegistration = new ChangeListenerRegistrationImpl< PropertyTypeT >( 
               propertyA,
               toPropertyConverter.apply( propertyB.getValue() ),
               ( source, old, updated ) -> propertyB.setValue( fromPropertyConverter.apply( updated ) )  
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
