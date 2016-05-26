/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.spinner;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.javafx.spinner.DefensiveIntegerSpinnerValueFactory;
import uk.dangrew.jtt.javafx.spinner.DefensiveIntegerStringConverter;

/**
 * {@link DefensiveIntegerSpinnerValueFactory} test.
 */
public class DefensiveIntegerSpinnerValueFactoryTest {
   
   private DefensiveIntegerSpinnerValueFactory systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new DefensiveIntegerSpinnerValueFactory( 0, 100 );
   }//End Method

   @Test public void shouldUseDefensiveIntegerStringConverter() {
      assertThat( systemUnderTest.getConverter(), instanceOf( DefensiveIntegerStringConverter.class ) );
   }//End Method
   
   @Test public void shouldUseValuePropertyForDefault() {
      assertThat( systemUnderTest.getConverter().fromString( "anything" ), is( 0 ) );
      systemUnderTest.setValue( 100 );
      assertThat( systemUnderTest.getConverter().fromString( "anything" ), is( 100 ) );
   }//End Method

}//End Class
