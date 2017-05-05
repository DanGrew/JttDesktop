/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.panel.views;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.statistics.panel.StatisticPanel;
import uk.dangrew.jtt.statistics.panel.StatisticPanelTest;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

public class TotalSuccessStatisticTest extends StatisticPanelTest {

   private StatisticsConfiguration configuration;
   private JenkinsDatabase database;
   private TotalSuccessStatistic systemUnderTest;

   /**
    * {@inheritDoc}
    */
   @Override protected StatisticPanel constructSut( JavaFxStyle styling, StatisticsConfiguration configuration, JenkinsDatabase database ) {
      this.database = database;
      this.configuration = configuration;
      this.systemUnderTest = new TotalSuccessStatistic( configuration, database );
      return systemUnderTest;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected String getDescriptionText() {
      return TotalSuccessStatistic.DESCRIPTION_TEXT;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected String getInitialValue() {
      return TotalSuccessStatistic.INITIAL_VALUE;
   }//End Method

   @Test public void shouldConfigureStatistic() {
      assertThat( systemUnderTest.statistic().uses( configuration ), is( true ) );
      assertThat( systemUnderTest.statistic().uses( database ), is( true ) );
      assertThat( systemUnderTest.statistic().isMonitoring( BuildResultStatus.SUCCESS ), is( true ) );
   }//End Method
   
   @Test public void shouldPopulatePanelWhenStatChanges(){
      JenkinsJob job = new JenkinsJobImpl( "Anything" );
      job.setBuildStatus( BuildResultStatus.SUCCESS );
      database.store( job );
      
      assertThat( systemUnderTest.getStatisticValue(), is( "1/1" ) );
   }//End Method

}//End Class
