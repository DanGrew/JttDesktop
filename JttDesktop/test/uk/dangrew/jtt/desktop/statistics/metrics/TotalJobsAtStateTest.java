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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.statistics.panel.StatisticView;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;

@RunWith( JUnitParamsRunner.class )
public class TotalJobsAtStateTest {

   private JenkinsJob job1;
   private JenkinsJob job2;
   private JenkinsJob job3;
   private JenkinsJob job4;
   private JenkinsJob job5;
   
   private StatisticsConfiguration configuration;
   private JenkinsDatabase database;
   @Mock private StatisticView statistic;
   private TotalJobsAtState systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      
      database = new TestJenkinsDatabaseImpl();
      database.store( job1 = new JenkinsJobImpl( "Job1" ) );
      database.store( job2 = new JenkinsJobImpl( "Job2" ) );
      database.store( job3 = new JenkinsJobImpl( "Job3" ) );
      database.store( job4 = new JenkinsJobImpl( "Job4" ) );
      database.store( job5 = new JenkinsJobImpl( "Job5" ) );
      
      configuration = new StatisticsConfiguration();
      systemUnderTest = new TotalJobsAtState( configuration, database, statistic, BuildResultStatus.SUCCESS );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullConfiguration(){
      new TotalJobsAtState( null, database, statistic, BuildResultStatus.SUCCESS );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullDatabase(){
      new TotalJobsAtState( configuration, null, statistic, BuildResultStatus.SUCCESS );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullStatisticView(){
      new TotalJobsAtState( configuration, database, null, BuildResultStatus.SUCCESS );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullStatus(){
      new TotalJobsAtState( configuration, database, statistic, null );
   }//End Method
   
   @Test public void shouldSetDefaultValueWhenConstructed(){
      verify( statistic ).setStatisticValue( "0/5" );
      
      new TotalJobsAtState( configuration, database, statistic, BuildResultStatus.SUCCESS );
      verify( statistic, times( 2 ) ).setStatisticValue( "0/5" );
   }//End Method

   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldIdentifyAllJobsAtState( BuildResultStatus status ) {
      statistic = mock( StatisticView.class );
      systemUnderTest = new TotalJobsAtState( configuration, database, statistic, status );
      
      if ( status == JenkinsJob.DEFAULT_LAST_BUILD_STATUS ) {
         verify( statistic ).setStatisticValue( "5/5" );
      } else {
         job2.setBuildStatus( status );
         verify( statistic ).setStatisticValue( "1/5" );
         job3.setBuildStatus( status );
         verify( statistic ).setStatisticValue( "2/5" );
         job5.setBuildStatus( status );
         verify( statistic ).setStatisticValue( "3/5" );
      }
   }//End Method
   
   @Test public void shouldUpdateValueAccountingForChangeInAlreadyCountedJobs() {
      job2.setBuildStatus( BuildResultStatus.SUCCESS );
      verify( statistic ).setStatisticValue( "1/5" );
      job2.setBuildStatus( BuildResultStatus.FAILURE );
      verify( statistic, times( 2 ) ).setStatisticValue( "0/5" );
      job2.setBuildStatus( BuildResultStatus.SUCCESS );
      verify( statistic, times( 2 ) ).setStatisticValue( "1/5" );
   }//End Method
   
   @Test public void shouldUpdateValueWhenJobAddedWithMatchingStatus() {
      JenkinsJob job = new JenkinsJobImpl( "New Job" );
      job.setBuildStatus( BuildResultStatus.SUCCESS );
      
      database.store( job );
      verify( statistic ).setStatisticValue( "1/6" );
   }//End Method
   
   @Test public void shouldNotUpdateValueWhenJobAddedWithNotMatchingStatus() {
      JenkinsJob job = new JenkinsJobImpl( "New Job" );
      job.setBuildStatus( BuildResultStatus.FAILURE );
      
      database.store( job );
      verify( statistic ).setStatisticValue( "0/5" );
   }//End Method
   
   @Test public void shouldUpdateValueWhenJobRemovedWithMatchingStatus() {
      job1.setBuildStatus( BuildResultStatus.SUCCESS );
      verify( statistic ).setStatisticValue( "1/5" );
      
      database.removeJenkinsJob( job1 );
      verify( statistic ).setStatisticValue( "0/4" );
   }//End Method
   
   @Test public void shouldUpdateValueWhenJobRemovedWithNotMatchingStatus() {
      job1.setBuildStatus( BuildResultStatus.FAILURE );
      verify( statistic ).setStatisticValue( "0/5" );
      job2.setBuildStatus( BuildResultStatus.SUCCESS );
      verify( statistic ).setStatisticValue( "1/5" );
      
      database.removeJenkinsJob( job1 );
      verify( statistic ).setStatisticValue( "1/4" );
   }//End Method
   
   @Test public void shouldNotIncludeExcludedJobs(){
      verify( statistic ).setStatisticValue( "0/5" );
      job2.setBuildStatus( BuildResultStatus.SUCCESS );
      verify( statistic ).setStatisticValue( "1/5" );
      configuration.excludedJobs().add( job2 );
      verify( statistic ).setStatisticValue( "0/4" );
      configuration.excludedJobs().clear();
      verify( statistic, times( 2 ) ).setStatisticValue( "1/5" );
   }//End Method
   
   @Test public void shouldUseConfiguration(){
      assertThat( systemUnderTest.uses( configuration ), is( true ) );
      assertThat( systemUnderTest.uses( new StatisticsConfiguration() ), is( false ) );
   }//End Method
   
   @Test public void shouldUseDatabase(){
      assertThat( systemUnderTest.uses( database ), is( true ) );
      assertThat( systemUnderTest.uses( new TestJenkinsDatabaseImpl() ), is( false ) );
   }//End Method
   
   @Test public void shouldBeMonitoring(){
      assertThat( systemUnderTest.isMonitoring( BuildResultStatus.SUCCESS ), is( true ) );
      assertThat( systemUnderTest.isMonitoring( BuildResultStatus.FAILURE ), is( false ) );
   }//End Method
   
}//End Class
