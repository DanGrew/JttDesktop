/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.metrics;

import javafx.collections.transformation.FilteredList;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.statistics.panel.StatisticView;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

/**
 * {@link TotalPassingTests} provides an object responsible for calculating the total number
 * of tests passing in the system.
 */
public class TotalPassingTests extends StatisticCalculatorBase {

   private int passing = 0;
   private int total = 0;
   
   /**
    * Constructs a new {@link TotalPassingTests}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    * @param statistic the {@link StatisticView} to update.
    */
   public TotalPassingTests( 
            StatisticsConfiguration configuration, 
            JenkinsDatabase database, 
            StatisticView statistic 
   ) {
      super( configuration, database, statistic );
      
      recalculateStatistic();
      
      database().jenkinsJobProperties().addTestFailureCountListener( ( s, o, u ) -> recalculateStatistic() );
      database().jenkinsJobProperties().addTestTotalCountListener( ( s, o, u ) -> recalculateStatistic() );
   }//End Constructor
   
   /**
    * Method to recalculate the statistic value.
    * @param relevantJobs the {@link FilteredList} of {@link JenkinsJob}s to use in the statistic.
    * @return the {@link String} representation of the statistic.
    */
   @Override protected String constructStatisticRepresentation( FilteredList< JenkinsJob > relevantJobs ){
      int recalculatedTotal = 0;
      int recalculatedCount = 0;
      
      for ( JenkinsJob j : relevantJobs ) {
         recalculatedCount += ( j.testTotalCount().get() - j.testFailureCount().get() );
         recalculatedTotal += j.testTotalCount().get();
      }
      
      if ( recalculatedCount == passing && recalculatedTotal == total ) {
         return null;
      }
      
      this.passing = recalculatedCount;
      this.total = recalculatedTotal;
      
      return recalculatedCount + "/" + recalculatedTotal;
   }//End Method

}//End Class
