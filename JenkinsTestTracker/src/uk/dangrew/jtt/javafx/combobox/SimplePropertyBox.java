/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.combobox;

import java.util.function.Function;

import javafx.beans.property.ObjectProperty;

/**
 * Simple version of {@link PropertyBox} that simple pushes through a single property type
 * that does not need to be converted.
 * @param <PropertyTypeT> the property and box type.
 */
public class SimplePropertyBox< PropertyTypeT > extends PropertyBox< PropertyTypeT, PropertyTypeT >{

   private final Function< PropertyTypeT, PropertyTypeT > RETURN_PARAMETER_FUNCTION = parameter -> {
      return parameter;
   };
   
   /**
    * Method to bind with simple push through functions.
    * @see SimplePropertyBox#bindProperty(ObjectProperty, Function, Function)
    * @param property the {@link ObjectProperty} to bind to.
    */
   public void bindProperty( ObjectProperty< PropertyTypeT > property ) {
      super.bindProperty( property, RETURN_PARAMETER_FUNCTION, RETURN_PARAMETER_FUNCTION );
   }//End Method
   
}//End Class
