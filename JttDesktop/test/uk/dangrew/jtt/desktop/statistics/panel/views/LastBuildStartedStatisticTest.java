/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.panel.views;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import uk.dangrew.kode.javafx.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.statistics.panel.StatisticPanel;
import uk.dangrew.jtt.desktop.statistics.panel.StatisticPanelTest;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

public class LastBuildStartedStatisticTest extends StatisticPanelTest {

   private StatisticsConfiguration configuration;
   private JenkinsDatabase database;
   private LastBuildStartedStatistic systemUnderTest;

   /**
    * {@inheritDoc}
    */
   @Override protected StatisticPanel constructSut( JavaFxStyle styling, StatisticsConfiguration configuration, JenkinsDatabase database ) {
      this.database = database;
      this.configuration = configuration;
      this.systemUnderTest = new LastBuildStartedStatistic( configuration, database );
      return systemUnderTest;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected String getDescriptionText() {
      return LastBuildStartedStatistic.DESCRIPTION_TEXT;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected String getInitialValue() {
      return LastBuildStartedStatistic.INITIAL_VALUE;
   }//End Method

   @Test public void shouldConfigureStatistic() {
      assertThat( systemUnderTest.statistic().uses( configuration ), is( true ) );
      assertThat( systemUnderTest.statistic().uses( database ), is( true ) );
   }//End Method
   
   @Test public void shouldPopulatePanelWhenStatChanges(){
      JenkinsJob job = new JenkinsJobImpl( "Anything" );
      job.buildTimestampProperty().set( 1234567890L );
      database.store( job );
      
      assertThat( systemUnderTest.getStatisticValue(), is( "15/01/70-07:56:07" ) );
   }//End Method

}//End Class
