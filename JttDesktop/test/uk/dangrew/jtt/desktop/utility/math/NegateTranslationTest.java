/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.utility.math;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class NegateTranslationTest {

   private NegateTranslation systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new NegateTranslation();
   }//End Method

   @Test public void shouldNegateValue() {
      assertThat( systemUnderTest.apply( 74.238 ), is( -74.238 ) );
   }//End Method

}//End Class
