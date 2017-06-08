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

public class IdentityTranslationTest {

   private IdentityTranslation systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new IdentityTranslation();
   }//End Method

   @Test public void shouldReturnValueAsIs() {
      assertThat( systemUnderTest.apply( 10.4872 ), is( 10.4872 ) );
   }//End Method

}//End Class
