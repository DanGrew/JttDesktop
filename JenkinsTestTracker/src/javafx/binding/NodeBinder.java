/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package javafx.binding;

import java.util.function.Consumer;
import java.util.function.Function;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.combobox.PropertyBox;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;

/**
 * The {@link NodeBinder} is responsible for binding a {@link Node}s value with
 * an external property, keeping both in sync.
 * @param <NodeItemTypeT> the type in the {@link Node}.
 * @param <PropertyTypeT> the proeprty type.
 */
public class NodeBinder< NodeItemTypeT, PropertyTypeT > {
   
   static final String ILLEGAL_NODE_ITEM_PROPERTY = "Null node item property provided.";
   static final String ILLEGAL_NODE_ITEM_PROPERTY_SETTER = "Null property setter provided.";
   static final String ILLEGAL_PROPERTY = "Null property provided.";
   static final String ILLEGAL_ITEM_TO_PROPERTY_FUNCTION = "Null item to property function provided.";
   static final String ILLEGAL_PROPERTY_TO_ITEM_FUNCTION = "Null property to item function provided.";
   
   private ObservableValue< NodeItemTypeT > nodeItemProperty;
   private Consumer< NodeItemTypeT > nodeItemPropertySetter;
   private ObjectProperty< PropertyTypeT > property;
   private Function< NodeItemTypeT, PropertyTypeT > boxToPropertyFunction;
   private Function< PropertyTypeT, NodeItemTypeT > propertyToBoxFunction;

   /**
    * Method to bind the given {@link ObjectProperty} to the {@link PropertyBox}, updating the property
    * when the selection changes, and updating the selection when the property changes.
    * @param nodeItemProperty the {@link ObservableValue} for the value associated with the {@link Node}.
    * @param nodeItemPropertySetter the {@link Consumer} to set the value on the {@link Node}.
    * @param property the {@link ObjectProperty} to bind with.
    * @param boxToPropertyFunction the {@link Function} to convert from the {@link ComboBox} item
    * to the {@link ObjectProperty} value.
    * @param propertyToBoxFunction the {@link Function} to convert from the {@link ObjectProperty} value
    * to the {@link ComboBox} item.
    */
   public NodeBinder(
            ObservableValue< NodeItemTypeT > nodeItemProperty,
            Consumer< NodeItemTypeT > nodeItemPropertySetter,
            ObjectProperty< PropertyTypeT > property, 
            Function< NodeItemTypeT, PropertyTypeT > boxToPropertyFunction,
            Function< PropertyTypeT, NodeItemTypeT > propertyToBoxFunction
   ) {
      if ( nodeItemProperty == null ) throw new IllegalArgumentException( ILLEGAL_NODE_ITEM_PROPERTY );
      if ( nodeItemPropertySetter == null ) throw new IllegalArgumentException( ILLEGAL_NODE_ITEM_PROPERTY_SETTER );
      if ( property == null ) throw new IllegalArgumentException( ILLEGAL_PROPERTY );
      if ( boxToPropertyFunction == null ) throw new IllegalArgumentException( ILLEGAL_ITEM_TO_PROPERTY_FUNCTION );
      if ( propertyToBoxFunction == null ) throw new IllegalArgumentException( ILLEGAL_PROPERTY_TO_ITEM_FUNCTION );
      
      this.nodeItemProperty = nodeItemProperty;
      this.nodeItemPropertySetter = nodeItemPropertySetter;
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
      nodeItemProperty.addListener( ( source, old, updated ) -> {
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
      NodeItemTypeT boxItem = this.propertyToBoxFunction.apply( this.property.get() );
      nodeItemPropertySetter.accept( boxItem );
   }//End Method

}//End Class
