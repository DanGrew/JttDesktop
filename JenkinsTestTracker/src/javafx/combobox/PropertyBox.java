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
import javafx.scene.control.ComboBox;

/**
 * The {@link PropertyBox} provides a {@link ComboBox} that binds to an {@link ObjectProperty}
 * converting to an from the property value.
 * @param <BoxItemTypeT> the type in the {@link ComboBox}.
 * @param <PropertyTypeT> the type of the {@link ObjectProperty}.
 */
public class PropertyBox< BoxItemTypeT, PropertyTypeT > extends ComboBox< BoxItemTypeT > {

   static final String ILLEGAL_PROPERTY = "Null property provided.";
   static final String ILLEGAL_BOX_TO_PROPERTY_FUNCTION = "Null box to property function provided.";
   static final String ILLEGAL_PROPERTY_TO_BOX_FUNCTION = "Null property to box function provided.";
   static final String ILLEGAL_BINDING = "Propety already bound.";
   
   private ObjectProperty< PropertyTypeT > property;
   private Function< BoxItemTypeT, PropertyTypeT > boxToPropertyFunction;
   private Function< PropertyTypeT, BoxItemTypeT > propertyToBoxFunction;

   /**
    * Method to bind the given {@link ObjectProperty} to the {@link PropertyBox}, updating the property
    * when the selection changes, and updating the selection when the property changes.
    * @param property the {@link ObjectProperty} to bind with.
    * @param boxToPropertyFunction the {@link Function} to convert from the {@link ComboBox} item
    * to the {@link ObjectProperty} value.
    * @param propertyToBoxFunction the {@link Function} to convert from the {@link ObjectProperty} value
    * to the {@link ComboBox} item.
    */
   protected void bindProperty(
            ObjectProperty< PropertyTypeT > property, 
            Function< BoxItemTypeT, PropertyTypeT > boxToPropertyFunction,
            Function< PropertyTypeT, BoxItemTypeT > propertyToBoxFunction
   ) {
      if ( property == null ) throw new IllegalArgumentException( ILLEGAL_PROPERTY );
      if ( boxToPropertyFunction == null ) throw new IllegalArgumentException( ILLEGAL_BOX_TO_PROPERTY_FUNCTION );
      if ( propertyToBoxFunction == null ) throw new IllegalArgumentException( ILLEGAL_PROPERTY_TO_BOX_FUNCTION );
      
      if ( this.property != null ) throw new IllegalStateException( ILLEGAL_BINDING );
      
      this.property = property;
      this.boxToPropertyFunction = boxToPropertyFunction;
      this.propertyToBoxFunction = propertyToBoxFunction;
      
      updateSelection();
      
      bind();
   }//End Method
   
   /**
    * Method to bind the property to the {@link ComboBox} attaching listeners appropriately.
    */
   private void bind(){
      getSelectionModel().selectedItemProperty().addListener( ( source, old, updated ) -> {
         PropertyTypeT newValue = boxToPropertyFunction.apply( updated );
         property.set( newValue );
      } );
      
      property.addListener( ( source, old, updated ) -> {
         updateSelection();
      } );
   }//End Method
   
   /**
    * Method to update the selection based on the current value of the {@link ObjectProperty}.
    */
   private void updateSelection(){
      BoxItemTypeT boxItem = this.propertyToBoxFunction.apply( this.property.get() );
      getSelectionModel().select( boxItem );
   }//End Method
}//End Class
