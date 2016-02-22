/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package javafx.combobox;

import java.util.function.Function;

import javafx.beans.property.ObjectProperty;
import javafx.binding.NodeBinder;
import javafx.scene.control.ComboBox;

/**
 * The {@link PropertyBox} provides a {@link ComboBox} that binds to an {@link ObjectProperty}
 * converting to an from the property value.
 * @param <BoxItemTypeT> the type in the {@link ComboBox}.
 * @param <PropertyTypeT> the type of the {@link ObjectProperty}.
 */
public class PropertyBox< BoxItemTypeT, PropertyTypeT > extends ComboBox< BoxItemTypeT > {

   static final String ILLEGAL_BINDING = "Property already bound.";
   
   private NodeBinder< BoxItemTypeT, PropertyTypeT > binder;

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
            Function< BoxItemTypeT, PropertyTypeT > boxToPropertyFunction,
            Function< PropertyTypeT, BoxItemTypeT > propertyToBoxFunction
   ) {
      if ( binder != null ) throw new IllegalStateException( ILLEGAL_BINDING );
      binder = new NodeBinder<>( 
               getSelectionModel().selectedItemProperty(),
               item -> getSelectionModel().select( item ),
               property,
               boxToPropertyFunction, 
               propertyToBoxFunction 
      );
   }//End Method

}//End Class
