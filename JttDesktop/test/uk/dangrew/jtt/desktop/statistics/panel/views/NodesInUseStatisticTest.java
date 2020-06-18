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
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.nodes.JenkinsNodeImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

public class NodesInUseStatisticTest extends StatisticPanelTest {

   private StatisticsConfiguration configuration;
   private JenkinsDatabase database;
   private NodesInUseStatistic systemUnderTest;

   /**
    * {@inheritDoc}
    */
   @Override protected StatisticPanel constructSut( JavaFxStyle styling, StatisticsConfiguration configuration, JenkinsDatabase database ) {
      this.database = database;
      this.configuration = configuration;
      this.systemUnderTest = new NodesInUseStatistic( configuration, database );
      return systemUnderTest;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected String getDescriptionText() {
      return NodesInUseStatistic.DESCRIPTION_TEXT;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected String getInitialValue() {
      return NodesInUseStatistic.INITIAL_VALUE;
   }//End Method

   @Test public void shouldConfigureStatistic() {
      assertThat( systemUnderTest.statistic().uses( configuration ), is( true ) );
      assertThat( systemUnderTest.statistic().uses( database ), is( true ) );
   }//End Method
   
   @Test public void shouldPopulatePanelWhenStatChanges(){
      JenkinsJob job = new JenkinsJobImpl( "Anything" );
      job.buildStateProperty().set( BuildState.Building );
      job.builtOnProperty().set( new JenkinsNodeImpl( "node" ) );
      database.store( job );
      
      assertThat( systemUnderTest.getStatisticValue(), is( "1" ) );
   }//End Method

}//End Class
