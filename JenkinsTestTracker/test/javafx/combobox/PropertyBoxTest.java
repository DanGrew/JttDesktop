/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package javafx.combobox;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import graphics.JavaFxInitializer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * {@link PropertyBox} test.
 */
public class PropertyBoxTest {
   
   private enum TestItem {
      First, Second, Third, Fourth;
   }
   private ObjectProperty< String > property;
   private Function< TestItem, String > boxToPropertyFunction;
   private Function< String, TestItem > propertyToBoxFunction;
   private PropertyBox< TestItem, String > systemUnderTest;
   
   @Rule public final ExpectedException exception = ExpectedException.none();
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.runAndWait( () -> {
         systemUnderTest = new PropertyBox<>();
      } );
      systemUnderTest.getItems().addAll( TestItem.values() );
      
      property = new SimpleObjectProperty<>();
      boxToPropertyFunction = object -> { return object.name(); };
      propertyToBoxFunction = object -> { return object == null ? null : TestItem.valueOf( object ); };
   }//End Method
   
   @Test public void shouldNotAcceptNullParametersInConstructor() {
      exception.expect( IllegalArgumentException.class );
      exception.expectMessage( PropertyBox.ILLEGAL_PROPERTY );
      
      systemUnderTest.bindProperty( 
               null, 
               boxToPropertyFunction,
               propertyToBoxFunction
      );
      
      exception.expect( IllegalArgumentException.class );
      exception.expectMessage( PropertyBox.ILLEGAL_BOX_TO_PROPERTY_FUNCTION );  
      
      systemUnderTest.bindProperty( 
               property, 
               null,
               propertyToBoxFunction
      );
      
      exception.expect( IllegalArgumentException.class );
      exception.expectMessage( PropertyBox.ILLEGAL_PROPERTY_TO_BOX_FUNCTION );  
      
      systemUnderTest.bindProperty( 
               property, 
               boxToPropertyFunction,
               null
      );
   }//End Method
   
   @Test public void shouldInitiallySelectPropertyValue() {
      property.set( TestItem.Third.name() );
      
      systemUnderTest.bindProperty( 
               property, 
               boxToPropertyFunction, 
               propertyToBoxFunction
      );
      
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( TestItem.Third ) );
   }//End Method
   
   @Test public void shouldInitiallySelectNothing() {
      property.set( null );
      
      systemUnderTest.bindProperty( 
               property, 
               boxToPropertyFunction, 
               propertyToBoxFunction
      );
      
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), nullValue() );
   }//End Method
   
   @Test public void shouldChangeSelectionWhenPropertyChanges() {
      property.set( TestItem.Third.name() );
      
      systemUnderTest.bindProperty( 
               property, 
               boxToPropertyFunction, 
               propertyToBoxFunction
      );
      
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( TestItem.Third ) );
      
      property.set( TestItem.First.name() );
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( TestItem.First ) );
   }//End Method
   
   @Test public void shouldChangePropertyWhenSelectionChanges() {
      property.set( TestItem.Third.name() );
      
      systemUnderTest.bindProperty( 
               property, 
               boxToPropertyFunction, 
               propertyToBoxFunction
      );
      
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( TestItem.Third ) );
      
      systemUnderTest.getSelectionModel().select( TestItem.Second );
      assertThat( property.get(), is( TestItem.Second.name() ) );
   }//End Method
   
   @Test public void shouldResetPropertyIfSelectedRemovedFromBox() {
      property.set( TestItem.Third.name() );
      
      systemUnderTest.bindProperty( 
               property, 
               boxToPropertyFunction, 
               propertyToBoxFunction
      );
      
      assertThat( systemUnderTest.getSelectionModel().getSelectedItem(), is( TestItem.Third ) );
      systemUnderTest.getItems().remove( TestItem.Third );
      assertThat( property.get(), is( TestItem.Second.name() ) );
   }//End Method
   
   @Test public void shouldNotAllowRebindingOfProperty() {
      systemUnderTest.bindProperty( property, boxToPropertyFunction, propertyToBoxFunction );
      exception.expect( IllegalStateException.class );
      exception.expectMessage( PropertyBox.ILLEGAL_BINDING );
      systemUnderTest.bindProperty( property, boxToPropertyFunction, propertyToBoxFunction );
   }//End Method

}//End Class
