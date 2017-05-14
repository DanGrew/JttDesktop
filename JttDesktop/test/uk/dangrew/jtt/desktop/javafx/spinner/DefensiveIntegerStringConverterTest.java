/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.spinner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * {@link DefensiveIntegerStringConverter} test.
 */
public class DefensiveIntegerStringConverterTest {

   private ObjectProperty< Integer > propertyBeingUpdated;
   private DefensiveIntegerStringConverter systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      propertyBeingUpdated = new SimpleObjectProperty<>();
      systemUnderTest = new DefensiveIntegerStringConverter( propertyBeingUpdated );
   }//End Method
   
   @Test public void shouldConvertValidValue() {
      assertThat( systemUnderTest.fromString( "100" ), is( 100 ) );
      assertThat( systemUnderTest.fromString( "123234" ), is( 123234 ) );
      assertThat( systemUnderTest.fromString( "0" ), is( 0 ) );
   }//End Method
   
   @Test public void shouldNotConvertInvalidValueAndUsePreviousValidValue() {
      propertyBeingUpdated.set( 200 );
      assertThat( systemUnderTest.fromString( "anything" ), is( 200 ) );
      propertyBeingUpdated.set( 13 );
      assertThat( systemUnderTest.fromString( "0.456" ), is( 13 ) );
   }//End Method
   
   @Test public void shouldNotConvertInvalidValueAndUseZeroForNoPreviousValidValue() {
      assertThat( systemUnderTest.fromString( "anything" ), is( 0 ) );
      assertThat( systemUnderTest.fromString( "100.23" ), is( 0 ) );
   }//End Method

}//End Class
