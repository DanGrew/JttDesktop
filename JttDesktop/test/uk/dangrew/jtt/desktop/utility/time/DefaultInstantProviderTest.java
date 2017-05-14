/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.utility.time;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link DefaultInstantProvider} test.
 */
public class DefaultInstantProviderTest {
   
   private DefaultInstantProvider systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new DefaultInstantProvider();
   }//End Method

   @Test public void shouldProduceRelativelyLaterResults() {
      long first = systemUnderTest.get();
      long second = systemUnderTest.get();
      assertThat( second, is( greaterThanOrEqualTo( first ) ) );
   }//End Method

}//End Class
