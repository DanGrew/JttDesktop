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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.statistics.metrics.TotalPassingTests;
import uk.dangrew.jtt.desktop.statistics.panel.StatisticView;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;

public class TotalPassingTestsTest {

   private JenkinsJob job1;
   private JenkinsJob job2;
   private JenkinsJob job3;
   private JenkinsJob job4;
   private JenkinsJob job5;
   
   private StatisticsConfiguration configuration;
   private JenkinsDatabase database;
   @Mock private StatisticView statistic;
   private TotalPassingTests systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      
      database = new TestJenkinsDatabaseImpl();
      database.store( job1 = new JenkinsJobImpl( "Job1" ) );
      database.store( job2 = new JenkinsJobImpl( "Job2" ) );
      database.store( job3 = new JenkinsJobImpl( "Job3" ) );
      database.store( job4 = new JenkinsJobImpl( "Job4" ) );
      database.store( job5 = new JenkinsJobImpl( "Job5" ) );
      
      configuration = new StatisticsConfiguration();
      systemUnderTest = new TotalPassingTests( configuration, database, statistic );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullConfiguration(){
      new TotalPassingTests( null, database, statistic );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullDatabase(){
      new TotalPassingTests( configuration, null, statistic );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullStatisticView(){
      new TotalPassingTests( configuration, database, null );
   }//End Method
   
   @Test public void shouldNotSetDefaultValueWhenConstructed(){
      verifyZeroInteractions( statistic );
   }//End Method

   @Test public void shouldUpdateValueWhenFailureCountChanges() {
      job2.testFailureCount().set( 10 );
      verify( statistic ).setStatisticValue( "-10/0" );
      job2.testFailureCount().set( 12 );
      verify( statistic ).setStatisticValue( "-12/0" );
   }//End Method
   
   @Test public void shouldUpdateValueWhenTestCountChanges() {
      job2.testTotalCount().set( 10 );
      verify( statistic ).setStatisticValue( "10/10" );
      job2.testTotalCount().set( 12 );
      verify( statistic ).setStatisticValue( "12/12" );
   }//End Method
   
   @Test public void shouldUpdateValueInTotalWhenTestResultsChange() {
      job1.testFailureCount().set( 101 );
      job1.testTotalCount().set( 3875 );
      verify( statistic ).setStatisticValue( "3774/3875" );
      
      job2.testFailureCount().set( 11 );
      job2.testTotalCount().set( 12 );
      verify( statistic ).setStatisticValue( "3775/3887" );
      
      job2.testFailureCount().set( 2 );
      job2.testTotalCount().set( 13 );
      verify( statistic ).setStatisticValue( "3785/3888" );
   }//End Method
   
   @Test public void shouldUpdateValueInTotalWhenFailuresChange() {
      job1.testFailureCount().set( 101 );
      job1.testTotalCount().set( 3875 );
      verify( statistic ).setStatisticValue( "3774/3875" );
      
      job2.testFailureCount().set( 11 );
      verify( statistic ).setStatisticValue( "3763/3875" );
      
      job2.testFailureCount().set( 2 );
      verify( statistic ).setStatisticValue( "3772/3875" );
   }//End Method
   
   @Test public void shouldUpdateValueInTotalWhenTotalChange() {
      job1.testFailureCount().set( 101 );
      job1.testTotalCount().set( 3875 );
      verify( statistic ).setStatisticValue( "3774/3875" );
      
      job2.testTotalCount().set( 12 );
      verify( statistic ).setStatisticValue( "3786/3887" );
      
      job2.testTotalCount().set( 13 );
      verify( statistic ).setStatisticValue( "3787/3888" );
   }//End Method
   
   @Test public void shouldUpdateValueInTotalWhenTestCountChanges() {
      job2.testTotalCount().set( 10 );
      verify( statistic ).setStatisticValue( "10/10" );
      job2.testTotalCount().set( 12 );
      verify( statistic ).setStatisticValue( "12/12" );
   }//End Method
   
   @Test public void shouldNotIncludeExcludedJobs(){
      job2.testTotalCount().set( 100 );
      job2.testFailureCount().set( 12 );
      verify( statistic ).setStatisticValue( "88/100" );
      
      configuration.excludedJobs().add( job2 );
      verify( statistic ).setStatisticValue( "0/0" );
      configuration.excludedJobs().clear();
      verify( statistic, times( 2 ) ).setStatisticValue( "88/100" );
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
