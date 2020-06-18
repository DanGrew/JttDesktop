/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.spinner;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import uk.dangrew.kode.javafx.spinner.DefensiveDoubleSpinnerValueFactory;
import uk.dangrew.kode.javafx.spinner.DefensiveDoubleStringConverter;

/**
 * {@link DefensiveDoubleSpinnerValueFactory} test.
 */
public class DefensiveDoubleSpinnerValueFactoryTest {
   
   private DefensiveDoubleSpinnerValueFactory systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new DefensiveDoubleSpinnerValueFactory( 0, 100 );
   }//End Method

   @Test public void shouldUseDefensiveDoubleStringConverter() {
      assertThat( systemUnderTest.getConverter(), instanceOf( DefensiveDoubleStringConverter.class ) );
   }//End Method
   
   @Test public void shouldUseValuePropertyForDefault() {
      assertThat( systemUnderTest.getConverter().fromString( "anything" ), is( 0.0 ) );
      systemUnderTest.setValue( 99.1 );
      assertThat( systemUnderTest.getConverter().fromString( "anything" ), is( 99.1 ) );
   }//End Method
   
   @Test public void shouldRespectMinimum(){
      systemUnderTest.setValue( -100.0 );
      assertThat( systemUnderTest.getValue(), is( 0.0 ) );
   }//End Method
   
   @Test public void shouldRespectMaximum(){
      systemUnderTest.setValue( 1000.0 );
      assertThat( systemUnderTest.getValue(), is( 100.0 ) );
   }//End Method
   
}//End Class
