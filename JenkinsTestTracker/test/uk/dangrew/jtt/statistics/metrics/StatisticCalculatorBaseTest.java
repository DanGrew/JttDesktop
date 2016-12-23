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
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.collections.transformation.FilteredList;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.statistics.panel.StatisticView;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;

public class StatisticCalculatorBaseTest {

   private JenkinsJob job1;
   private JenkinsJob job2;
   
   private StatisticsConfiguration configuration;
   private JenkinsDatabase database;
   @Mock private StatisticView statistic;
   
   private List< JenkinsJob > givenJobs;
   private String representation;
   private StatisticCalculatorBase systemUnderTest;
   
   /**
    * Test implementation of {@link StatisticCalculatorBase}.
    */
   private class TestStatisticCalculator extends StatisticCalculatorBase {

      /**
       * Constructs a new {@link TestStatisticCalculator}.
       */
      public TestStatisticCalculator() {
         super( configuration, database, statistic );
      }//End Constructor

      /**
       * {@inheritDoc}
       */
      @Override protected String constructStatisticRepresentation( FilteredList< JenkinsJob > applicableJobs ) {
         givenJobs = applicableJobs;
         return representation;
      }//End Method
      
   }//End Class

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      
      database = new JenkinsDatabaseImpl();
      database.store( job1 = new JenkinsJobImpl( "Job1" ) );
      database.store( job2 = new JenkinsJobImpl( "Job2" ) );
      
      configuration = new StatisticsConfiguration();
      systemUnderTest = new TestStatisticCalculator();
   }//End Method
   
   @Test public void shouldUpdateRepresentationWhenJobAdded() {
      JenkinsJob job = new JenkinsJobImpl( "New Job" );
      database.store( job );
      assertThat( givenJobs, is( Arrays.asList( job1, job2, job ) ) );
   }//End Method
   
   @Test public void shouldUpdateRepresentationWhenJobRemoved() {
      database.removeJenkinsJob( job1 );
      assertThat( givenJobs, is( Arrays.asList( job2 ) ) );
   }//End Method
   
   @Test public void shouldNotIncludeExcludedJobs(){
      configuration.excludedJobs().add( job2 );
      assertThat( givenJobs, is( Arrays.asList( job1 ) ) );
      configuration.excludedJobs().clear();
      assertThat( givenJobs, is( Arrays.asList( job1, job2 ) ) );
   }//End Method
   
   @Test public void shouldUseConfiguration(){
      assertThat( systemUnderTest.uses( configuration ), is( true ) );
      assertThat( systemUnderTest.uses( new StatisticsConfiguration() ), is( false ) );
   }//End Method
   
   @Test public void shouldUseDatabase(){
      assertThat( systemUnderTest.uses( database ), is( true ) );
      assertThat( systemUnderTest.uses( new JenkinsDatabaseImpl() ), is( false ) );
   }//End Method
   
   @Test public void shouldPassRepresentationThroughToView(){
      representation = "anything";
      systemUnderTest.recalculateStatistic();
      verify( statistic ).setStatisticValue( representation );
   }//End Method
   
   @Test public void shouldIgnoreRepresentationIfNull(){
      representation = null;
      systemUnderTest.recalculateStatistic();
      verifyZeroInteractions( statistic );
   }//End Method
   
   @Test public void shouldIgnoreRepresentationIfSame(){
      representation = "anything";
      when( statistic.getStatisticValue() ).thenReturn( representation );
      systemUnderTest.recalculateStatistic();
      verify( statistic ).getStatisticValue();
      verifyNoMoreInteractions( statistic );
   }//End Method

}//End Class
