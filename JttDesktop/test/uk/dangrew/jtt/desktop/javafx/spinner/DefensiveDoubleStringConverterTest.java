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
import uk.dangrew.jtt.desktop.javafx.spinner.DefensiveDoubleStringConverter;

/**
 * {@link DefensiveDoubleStringConverter} test.
 */
public class DefensiveDoubleStringConverterTest {

   private ObjectProperty< Double > propertyBeingUpdated;
   private DefensiveDoubleStringConverter systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      propertyBeingUpdated = new SimpleObjectProperty<>();
      systemUnderTest = new DefensiveDoubleStringConverter( propertyBeingUpdated );
   }//End Method
   
   @Test public void shouldConvertValidValue() {
      assertThat( systemUnderTest.fromString( "100.56" ), is( 100.56 ) );
      assertThat( systemUnderTest.fromString( "123234.234" ), is( 123234.234 ) );
      assertThat( systemUnderTest.fromString( "0.0" ), is( 0.0 ) );
   }//End Method
   
   @Test public void shouldNotConvertInvalidValueAndUsePreviousValidValue() {
      propertyBeingUpdated.set( 200.54 );
      assertThat( systemUnderTest.fromString( "anything" ), is( 200.54 ) );
      propertyBeingUpdated.set( 13.0 );
      assertThat( systemUnderTest.fromString( "1/2" ), is( 13.0 ) );
   }//End Method
   
   @Test public void shouldNotConvertInvalidValueAndUseZeroForNoPreviousValidValue() {
      assertThat( systemUnderTest.fromString( "anything" ), is( 0.0 ) );
      assertThat( systemUnderTest.fromString( "-" ), is( 0.0 ) );
   }//End Method

}//End Class
