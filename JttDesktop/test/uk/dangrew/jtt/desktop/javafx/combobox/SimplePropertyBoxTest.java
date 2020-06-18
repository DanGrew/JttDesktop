/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.combobox;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.Before;
import org.junit.Test;
import uk.dangrew.kode.javafx.platform.JavaFxThreading;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * {@link SimplePropertyBox} test.
 */
public class SimplePropertyBoxTest {

   private ObjectProperty< String > property;
   private SimplePropertyBox< String > systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      JavaFxThreading.runAndWait( () -> {
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
