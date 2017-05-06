/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.combobox;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.javafx.combobox.SimplePropertyBox;

/**
 * {@link SimplePropertyBox} test.
 */
public class SimplePropertyBoxTest {

   private ObjectProperty< String > property;
   private SimplePropertyBox< String > systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.runAndWait( () -> {
         property = new SimpleObjectProperty<>();
         systemUnderTest = new SimplePropertyBox<>();
         systemUnderTest.bindProperty( property );
      } );
   }//End Method
   
   @Test public void shouldSimplyPushSameValueThroughFunctions() {
      final String value = "anything";
      
      systemUnderTest.setValue( value );
      assertThat( property.get(), is( value ) );
      
      final String anotherValue = "something-else";
      property.set( anotherValue );
      assertThat( systemUnderTest.getValue(), is( anotherValue ) );
   }//End Method

}//End Class
