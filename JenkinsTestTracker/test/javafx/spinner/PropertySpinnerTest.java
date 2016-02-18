/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package javafx.spinner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.function.Function;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import graphics.JavaFxInitializer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.SpinnerValueFactory;

/**
 * {@link PropertySpinner} test.
 */
public class PropertySpinnerTest {
   
   private ObjectProperty< String > property;
   private Function< Integer, String > boxToPropertyFunction;
   private Function< String, Integer > propertyToBoxFunction;
   private PropertySpinner< Integer, String > systemUnderTest;
   
   @Rule public final ExpectedException exception = ExpectedException.none();
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.runAndWait( () -> {
         systemUnderTest = new PropertySpinner<>();
         systemUnderTest.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory( 1, 1000, 1 ) );
      } );
      
      property = new SimpleObjectProperty<>();
      boxToPropertyFunction = object -> { return object.toString(); };
      propertyToBoxFunction = object -> { return object == null ? null : Integer.valueOf( object ); };
   }//End Method
   
   @Test public void shouldInitiallySelectPropertyValue() {
      property.set( "100" );
      
      systemUnderTest.bindProperty( 
               property, 
               boxToPropertyFunction, 
               propertyToBoxFunction
      );
      
      assertThat( systemUnderTest.getValue(), is( 100 ) );
   }//End Method
   
   @Test public void shouldInitiallySelectNothing() {
      property.set( null );
      
      systemUnderTest.bindProperty( 
               property, 
               boxToPropertyFunction, 
               propertyToBoxFunction
      );
      
      assertThat( systemUnderTest.getValue(), is( 1 ) );
   }//End Method
   
   @Test public void shouldChangeSelectionWhenPropertyChanges() {
      property.set( "100" );
      
      systemUnderTest.bindProperty( 
               property, 
               boxToPropertyFunction, 
               propertyToBoxFunction
      );
      
      assertThat( systemUnderTest.getValue(), is( 100 ) );
      
      property.set( "789" );
      assertThat( systemUnderTest.getValue(), is( 789 ) );
   }//End Method
   
   @Test public void shouldChangePropertyWhenSelectionChanges() {
      property.set( "100" );
      
      systemUnderTest.bindProperty( 
               property, 
               boxToPropertyFunction, 
               propertyToBoxFunction
      );
      
      assertThat( systemUnderTest.getValue(), is( 100 ) );
      
      systemUnderTest.getValueFactory().setValue( 34 );
      assertThat( property.get(), is( "34" ) );
   }//End Method
   
   @Test public void shouldNotAllowRebindingOfProperty() {
      systemUnderTest.bindProperty( property, boxToPropertyFunction, propertyToBoxFunction );
      exception.expect( IllegalStateException.class );
      exception.expectMessage( PropertySpinner.ILLEGAL_BINDING );
      systemUnderTest.bindProperty( property, boxToPropertyFunction, propertyToBoxFunction );
   }//End Method

}//End Class
