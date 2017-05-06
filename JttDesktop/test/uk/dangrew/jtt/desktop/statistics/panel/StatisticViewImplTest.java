/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.panel;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.desktop.statistics.panel.StatisticView;

public class StatisticViewImplTest {

   private StatisticView systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new StatisticViewImpl();
   }//End Method
   
   @Test public void shouldHoldValueAndRetrieve() {
      systemUnderTest.setStatisticValue( "anything" );
      assertThat( systemUnderTest.getStatisticValue(), is( "anything" ) );
      
      systemUnderTest.setStatisticValue( "something" );
      assertThat( systemUnderTest.getStatisticValue(), is( "something" ) );
      assertThat( systemUnderTest.getStatisticValue(), is( not( "anything" ) ) );
   }//End Method

}//End Class
