/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.spinner;

import java.util.function.Function;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Spinner;
import uk.dangrew.jtt.desktop.javafx.binding.NodeBinder;

/**
 * The {@link PropertySpinner} is responsible for binding a property to a {@link Spinner}
 * keeping values in sync.
 * @param <SpinnerTypeT> the {@link Spinner} type.
 * @param <PropertyTypeT> the property type.
 */
public class PropertySpinner< SpinnerTypeT, PropertyTypeT > extends Spinner< SpinnerTypeT > {

   static final String ILLEGAL_BINDING = "Property already bound.";

   private NodeBinder< SpinnerTypeT, PropertyTypeT > binder;

   /**
    * Method to bind the given {@link ObjectProperty} to the {@link PropertyBox}, updating the property
    * when the selection changes, and updating the selection when the property changes.
    * @param property the {@link ObjectProperty} to bind with.
    * @param boxToPropertyFunction the {@link Function} to convert from the {@link ComboBox} item
    * to the {@link ObjectProperty} value.
    * @param propertyToBoxFunction the {@link Function} to convert from the {@link ObjectProperty} value
    * to the {@link ComboBox} item.
    */
   public void bindProperty(
            ObjectProperty< PropertyTypeT > property, 
            Function< SpinnerTypeT, PropertyTypeT > boxToPropertyFunction,
            Function< PropertyTypeT, SpinnerTypeT > propertyToBoxFunction
   ) {
      if ( binder != null ) {
         throw new IllegalStateException( ILLEGAL_BINDING );
      }
      binder = new NodeBinder<>( 
               getValueFactory().valueProperty(),
               item -> { if ( item != null ) getValueFactory().setValue( item ); },
               property,
               boxToPropertyFunction, 
               propertyToBoxFunction 
      );
   }//End Method
   
}//End Class
