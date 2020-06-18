/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.configuration;

import org.junit.Before;
import org.junit.Test;
import uk.dangrew.kode.synchronization.SynchronizedObservableList;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class StatisticsConfigurationTest {

   private StatisticsConfiguration systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new StatisticsConfiguration();
   }//End Method

   @Test public void shouldProvideStatisticBackgroundColour() {
      assertThat( systemUnderTest.statisticBackgroundColourProperty(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.statisticBackgroundColourProperty().get(), is( StatisticsConfiguration.DEFAULT_STATISTIC_BACKGROUND_COLOUR ) );
   }//End Method
   
   @Test public void shouldProvideStatisticTextColour() {
      assertThat( systemUnderTest.statisticTextColourProperty(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.statisticTextColourProperty().get(), is( StatisticsConfiguration.DEFAULT_STATISTIC_TEXT_COLOUR ) );
   }//End Method
   
   @Test public void shouldProvideStatisticValueTextFont() {
      assertThat( systemUnderTest.statisticValueTextFontProperty(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.statisticValueTextFontProperty().get(), is( StatisticsConfiguration.DEFAULT_STATISTIC_VALUE_TEXT_FONT ) );
   }//End Method
   
   @Test public void shouldProvideStatisticDescriptionTextFont() {
      assertThat( systemUnderTest.statisticDescriptionTextFontProperty(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.statisticDescriptionTextFontProperty().get(), is( StatisticsConfiguration.DEFAULT_STATISTIC_DESCRIPTION_TEXT_FONT ) );
   }//End Method
   
   @Test public void shouldProvideExcludedJobs(){
      assertThat( systemUnderTest.excludedJobs(), is( not( nullValue() ) ) );
      assertThat( systemUnderTest.excludedJobs(), is( instanceOf( SynchronizedObservableList.class ) ) );
   }//End Method

}//End Class
