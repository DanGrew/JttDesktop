/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.metrics;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.nodes.JenkinsNodeImpl;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.statistics.panel.StatisticView;
import uk.dangrew.jtt.statistics.panel.StatisticViewImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;

public class NodesInUseTest {

   private JenkinsJob job1;
   private JenkinsJob job2;
   private JenkinsJob job3;
   private JenkinsJob job4;
   private JenkinsJob job5;
   private JenkinsNode node1;
   private JenkinsNode node2;
   private JenkinsNode node3;
   
   private StatisticsConfiguration configuration;
   private JenkinsDatabase database;
   private StatisticView statistic;
   private NodesInUse systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      
      database = new JenkinsDatabaseImpl();
      database.store( job1 = new JenkinsJobImpl( "Job1" ) );
      database.store( job2 = new JenkinsJobImpl( "Job2" ) );
      database.store( job3 = new JenkinsJobImpl( "Job3" ) );
      database.store( job4 = new JenkinsJobImpl( "Job4" ) );
      database.store( job5 = new JenkinsJobImpl( "Job5" ) );
      database.store( node1 = new JenkinsNodeImpl( "Node1" ) );
      database.store( node2 = new JenkinsNodeImpl( "Node2" ) );
      database.store( node3 = new JenkinsNodeImpl( "Node3" ) );
      
      statistic = new StatisticViewImpl();
      configuration = new StatisticsConfiguration();
      systemUnderTest = new NodesInUse( configuration, database, statistic );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullConfiguration(){
      new NodesInUse( null, database, statistic );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullDatabase(){
      new NodesInUse( configuration, null, statistic );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullStatisticView(){
      new NodesInUse( configuration, database, null );
   }//End Method
   
   @Test public void shouldNotSetDefaultValueWhenConstructed(){
      assertThat( statistic.getStatisticValue(), is( nullValue() ) );
   }//End Method

   @Test public void shouldUpdateValueWhenBuiltOnChanges() {
      job2.builtOnProperty().set( node1 );
      job2.buildStateProperty().set( BuildState.Building );
      assertThat( statistic.getStatisticValue(), is( "1" ) );
      job2.builtOnProperty().set( null );
      assertThat( statistic.getStatisticValue(), is( "0" ) );
      job2.builtOnProperty().set( node2 );
      assertThat( statistic.getStatisticValue(), is( "1" ) );
   }//End Method
   
   @Test public void shouldUpdateValueWhenMultipleJobsChange() {
      job1.builtOnProperty().set( node1 );
      job1.buildStateProperty().set( BuildState.Building );
      job2.builtOnProperty().set( node2 );
      job2.buildStateProperty().set( BuildState.Building );
      assertThat( statistic.getStatisticValue(), is( "2" ) );
      job2.buildStateProperty().set( BuildState.Built );
      assertThat( statistic.getStatisticValue(), is( "1" ) );
      job1.buildStateProperty().set( BuildState.Built );
      assertThat( statistic.getStatisticValue(), is( "0" ) );
   }//End Method
   
   @Test public void shouldUpdateNotCountSameNodeMultipleTimes() {
      job1.builtOnProperty().set( node1 );
      job1.buildStateProperty().set( BuildState.Building );
      job2.builtOnProperty().set( node1 );
      job2.buildStateProperty().set( BuildState.Building );
      assertThat( statistic.getStatisticValue(), is( "1" ) );
      job2.buildStateProperty().set( BuildState.Built );
      assertThat( statistic.getStatisticValue(), is( "1" ) );
      job1.buildStateProperty().set( BuildState.Built );
      assertThat( statistic.getStatisticValue(), is( "0" ) );
   }//End Method
   
   @Test public void shouldUpdateValueWhenBuildStateChanges() {
      job2.builtOnProperty().set( node1 );
      job2.buildStateProperty().set( BuildState.Building );
      assertThat( statistic.getStatisticValue(), is( "1" ) );
      job2.buildStateProperty().set( BuildState.Built );
      assertThat( statistic.getStatisticValue(), is( "0" ) );
   }//End Method
   
   @Test public void shouldNotIncludeExcludedJobs(){
      job2.builtOnProperty().set( node1 );
      job2.buildStateProperty().set( BuildState.Building );
      assertThat( statistic.getStatisticValue(), is( "1" ) );
      
      configuration.excludedJobs().add( job2 );
      assertThat( statistic.getStatisticValue(), is( "0" ) );
      configuration.excludedJobs().clear();
      assertThat( statistic.getStatisticValue(), is( "1" ) );
   }//End Method
   
   @Test public void shouldUseConfiguration(){
      assertThat( systemUnderTest.uses( configuration ), is( true ) );
      assertThat( systemUnderTest.uses( new StatisticsConfiguration() ), is( false ) );
   }//End Method
   
   @Test public void shouldUseDatabase(){
      assertThat( systemUnderTest.uses( database ), is( true ) );
      assertThat( systemUnderTest.uses( new JenkinsDatabaseImpl() ), is( false ) );
   }//End Method
   
}//End Class
