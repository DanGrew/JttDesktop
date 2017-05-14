/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.binding;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.function.Consumer;
import java.util.function.Function;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * {@link NodeBinder} test.
 */
public class NodeBinderTest {
   
   private enum TestItem {
      First, Second, Third, Fourth;
   }
   private ObjectProperty< TestItem > nodeItemProperty;
   private Consumer< TestItem > nodeItemPropertySetter;
   private ObjectProperty< String > property;
   private Function< TestItem, String > boxToPropertyFunction;
   private Function< String, TestItem > propertyToBoxFunction;
   private NodeBinder< TestItem, String > systemUnderTest;
   
   @Rule public final ExpectedException exception = ExpectedException.none();
   
   @Before public void initialiseSystemUnderTest(){
      nodeItemProperty = new SimpleObjectProperty<>();
      nodeItemPropertySetter = item -> nodeItemProperty.set( item );
      property = new SimpleObjectProperty<>();
      boxToPropertyFunction = object -> { return object.name(); };
      propertyToBoxFunction = object -> { return object == null ? null : TestItem.valueOf( object ); };
      
      systemUnderTest = new NodeBinder<>( nodeItemProperty, nodeItemPropertySetter, property, boxToPropertyFunction, propertyToBoxFunction );
   }//End Method
   
   @Test public void shouldNotAcceptNullNodeItemPropertyInConstructor() {
      exception.expect( IllegalArgumentException.class );
      exception.expectMessage( NodeBinder.ILLEGAL_NODE_ITEM_PROPERTY );
      systemUnderTest = new NodeBinder<>( null, nodeItemPropertySetter, property, boxToPropertyFunction, propertyToBoxFunction );
   }//End Method
   
   @Test public void shouldNotAcceptNullNodeItemPropertySetterInConstructor() {
      exception.expect( IllegalArgumentException.class );
      exception.expectMessage( NodeBinder.ILLEGAL_NODE_ITEM_PROPERTY_SETTER );  
      systemUnderTest = new NodeBinder<>( nodeItemProperty, null, property, boxToPropertyFunction, propertyToBoxFunction );
   }//End Method
   
   @Test public void shouldNotAcceptNullPropertyInConstructor() {
      exception.expect( IllegalArgumentException.class );
      exception.expectMessage( NodeBinder.ILLEGAL_PROPERTY );
      systemUnderTest = new NodeBinder<>( nodeItemProperty, nodeItemPropertySetter, null, boxToPropertyFunction, propertyToBoxFunction );
   }//End Method
   
   @Test public void shouldNotAcceptNullItemToPropertyFunctionInConstructor() {
      exception.expect( IllegalArgumentException.class );
      exception.expectMessage( NodeBinder.ILLEGAL_ITEM_TO_PROPERTY_FUNCTION );
      systemUnderTest = new NodeBinder<>( nodeItemProperty, nodeItemPropertySetter, property, null, propertyToBoxFunction );
   }//End Method
   
   @Test public void shouldNotAcceptNullPropertyToItemFunctionInConstructor() {
      exception.expect( IllegalArgumentException.class );
      exception.expectMessage( NodeBinder.ILLEGAL_PROPERTY_TO_ITEM_FUNCTION );
      systemUnderTest = new NodeBinder<>( nodeItemProperty, nodeItemPropertySetter, property, boxToPropertyFunction, null );
   }//End Method
   
   @Test public void shouldInitiallySelectPropertyValue() {
      property.set( TestItem.Third.name() );
      systemUnderTest = new NodeBinder<>( nodeItemProperty, nodeItemPropertySetter, property, boxToPropertyFunction, propertyToBoxFunction );
      assertThat( nodeItemProperty.get(), is( TestItem.Third ) );
   }//End Method
   
   @Test public void shouldInitiallySelectNothing() {
      property.set( null );
      systemUnderTest = new NodeBinder<>( nodeItemProperty, nodeItemPropertySetter, property, boxToPropertyFunction, propertyToBoxFunction );
      assertThat( nodeItemProperty.get(), nullValue() );
   }//End Method
   
   @Test public void shouldChangeSelectionWhenPropertyChanges() {
      assertThat( property.get(), not( TestItem.First ) );
      property.set( TestItem.First.name() );
      assertThat( nodeItemProperty.get(), is( TestItem.First ) );
   }//End Method
   
   @Test public void shouldChangePropertyWhenSelectionChanges() {
      assertThat( nodeItemProperty.get(), not( TestItem.Second ) );
      nodeItemProperty.set( TestItem.Second );
      assertThat( property.get(), is( TestItem.Second.name() ) );
   }//End Method

   @Test public void shouldBeBoundTo(){
      assertThat( systemUnderTest.isBoundTo( property ), is( true ) );
      assertThat( systemUnderTest.isBoundTo( new SimpleObjectProperty<>() ), is( false ) );
   }//End Method
   
}//End Class
