/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.spinner;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.kode.javafx.platform.JavaFxThreading;
import uk.dangrew.kode.javafx.spinner.IntegerPropertySpinner;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * {@link IntegerPropertySpinner} test.
 */
public class IntegerPropertySpinnerTest {

   private IntegerPropertySpinner systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
       JavaFxThreading.runAndWait( () -> {
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
