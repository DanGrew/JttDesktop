/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.configuration;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.sd.utility.synchronization.SynchronizedObservableList;

public class StatisticsConfigurationTest {

   private StatisticsConfiguration systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new StatisticsConfiguration();
   }//End Method

   @Test public void shouldProvideStatisticBackgroundColour() {
      assertThat( systemUnderTest.statisticBackgroundProperty(), is( not( nullValue() ) ) );
   }//End Method
   
   @Test public void shouldProvideStatisticTextColour() {
      assertThat( systemUnderTest.statisticTextProperty(), is( not( nullValue() ) ) );
   }//End Method
   
   @Test public void shouldProvideExcludedJobs(){
      assertThat( systemUnderTest.excludedJobs(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.excludedJobs(), is( instanceOf( SynchronizedObservableList.class ) ) );
   }//End Method

}//End Class
