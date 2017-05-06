/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.metrics;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.statistics.metrics.LastBuildStarted;
import uk.dangrew.jtt.desktop.statistics.panel.StatisticView;
import uk.dangrew.jtt.desktop.statistics.panel.StatisticViewImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;

public class LastBuildStartedTest {

   private JenkinsJob job1;
   private JenkinsJob job2;
   private JenkinsJob job3;
   
   private StatisticsConfiguration configuration;
   private JenkinsDatabase database;
   private StatisticView statistic;
   private LastBuildStarted systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      
      database = new TestJenkinsDatabaseImpl();
      database.store( job1 = new JenkinsJobImpl( "Job1" ) );
      database.store( job2 = new JenkinsJobImpl( "Job2" ) );
      database.store( job3 = new JenkinsJobImpl( "Job3" ) );
      
      job1.buildTimestampProperty().set( null );
      job2.buildTimestampProperty().set( null );
      job3.buildTimestampProperty().set( null );
      
      statistic = new StatisticViewImpl();
      configuration = new StatisticsConfiguration();
      systemUnderTest = new LastBuildStarted( configuration, database, statistic );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullConfiguration(){
      new LastBuildStarted( null, database, statistic );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullDatabase(){
      new LastBuildStarted( configuration, null, statistic );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullStatisticView(){
      new LastBuildStarted( configuration, database, null );
   }//End Method
   
   @Test public void shouldNotSetDefaultValueWhenConstructed(){
      assertThat( statistic.getStatisticValue(), is( LastBuildStarted.UNKNOWN ) );
   }//End Method

   @Test public void shouldUpdateValueTimestampChanges() {
      job2.buildTimestampProperty().set( 1000L );
      assertThat( statistic.getStatisticValue(), is( "01/01/70-01:00:01" ) );
      job2.buildTimestampProperty().set( null );
      assertThat( statistic.getStatisticValue(), is( LastBuildStarted.UNKNOWN ) );
      job2.buildTimestampProperty().set( 2000L );
      assertThat( statistic.getStatisticValue(), is( "01/01/70-01:00:02" ) );
   }//End Method
   
   @Test public void shouldPickTheLatestTimestamp() {
      job1.buildTimestampProperty().set( 36894576L );
      job2.buildTimestampProperty().set( 48567304L );
      job3.buildTimestampProperty().set( 98739456L );
      assertThat( statistic.getStatisticValue(), is( "02/01/70-04:25:39" ) );
      job3.buildTimestampProperty().set( 10000002L );
      job2.buildTimestampProperty().set( 10000001L );
      job1.buildTimestampProperty().set( 10000000L );
      assertThat( statistic.getStatisticValue(), is( "01/01/70-03:46:40" ) );
      job3.buildTimestampProperty().set( 10000002L );
      job2.buildTimestampProperty().set( 10000001L );
      job1.buildTimestampProperty().set( 10000002L );
      assertThat( statistic.getStatisticValue(), is( "01/01/70-03:46:40" ) );
   }//End Method
   
   @Test public void shouldNotIncludeExcludedJobs(){
      job2.buildTimestampProperty().set( 36894576L );
      assertThat( statistic.getStatisticValue(), is( "01/01/70-11:14:54" ) );
      
      configuration.excludedJobs().add( job2 );
      assertThat( statistic.getStatisticValue(), is( LastBuildStarted.UNKNOWN ) );
      configuration.excludedJobs().clear();
      assertThat( statistic.getStatisticValue(), is( "01/01/70-11:14:54" ) );
   }//End Method
   
   @Test public void shouldUseConfiguration(){
      assertThat( systemUnderTest.uses( configuration ), is( true ) );
      assertThat( systemUnderTest.uses( new StatisticsConfiguration() ), is( false ) );
   }//End Method
   
   @Test public void shouldUseDatabase(){
      assertThat( systemUnderTest.uses( database ), is( true ) );
      assertThat( systemUnderTest.uses( new TestJenkinsDatabaseImpl() ), is( false ) );
   }//End Method
   
}//End Class
