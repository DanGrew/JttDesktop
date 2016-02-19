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

import org.junit.Before;
import org.junit.Test;

import graphics.JavaFxInitializer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;

/**
 * {@link IntegerPropertySpinner} test.
 */
public class IntegerPropertySpinnerTest {

   private IntegerPropertySpinner systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.runAndWait( () -> {
         systemUnderTest = new IntegerPropertySpinner();
         systemUnderTest.setValueFactory( new IntegerSpinnerValueFactory( 0, 100 ) );
      } );
   }//End Method
   
   @Test public void shouldBindWithSimpleFunction() {
      IntegerProperty property = new SimpleIntegerProperty( 0 );
      systemUnderTest.bindProperty( property );
      
      systemUnderTest.getValueFactory().setValue( 10 );
      assertThat( property.get(), is( 10 ) );
      
      property.set( 98 );
      assertThat( systemUnderTest.getValue(), is( 98 ) );
   }//End Method

}//End Class
