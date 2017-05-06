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
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

/**
 * {@link TotalJobsAtState} provides an object responsible for calculating the total number
 * of {@link JenkinsJob}s currently passing in the system.
 */
public class TotalJobsAtState extends StatisticCalculatorBase {

   private final BuildResultStatus monitoredState;
   
   private int count;
   private int total;
   
   /**
    * Constructs a new {@link TotalJobsAtState}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    * @param statistic the {@link StatisticView} to update.
    * @param status the {@link BuildResultStatus} being counted.
    */
   public TotalJobsAtState( 
            StatisticsConfiguration configuration, 
            JenkinsDatabase database, 
            StatisticView statistic, 
            BuildResultStatus status 
   ) {
      super( configuration, database, statistic );
      if ( status == null ) {
         throw new IllegalArgumentException( "Must supply non null parameters." );
      }
      
      this.monitoredState = status;
      recalculateStatistic();
      
      database().jenkinsJobProperties().addBuildResultStatusListener( ( s, o, u ) -> recalculateStatistic() );
   }//End Constructor
   
   /**
    * Method to recalculate the statistic value.
    * @param relevantJobs the {@link FilteredList} of {@link JenkinsJob}s to use in the statistic.
    * @return the {@link String} representation of the statistic.
    */
   @Override protected String constructStatisticRepresentation( FilteredList< JenkinsJob > relevantJobs ){
      final int recalculatedTotal = relevantJobs.size();
      final int recalculatedCount = relevantJobs
               .filtered( j -> j.getBuildStatus() == monitoredState ).size();
      
      if ( this.total == recalculatedTotal && this.count == recalculatedCount ) {
         return null;
      }
      
      this.total = recalculatedTotal;
      this.count = recalculatedCount;
      return recalculatedCount + "/" + recalculatedTotal;
   }//End Method

   /**
    * Method to determine whether this is monitoring the given {@link BuildResultStatus}.
    * @param status the {@link BuildResultStatus} in question.
    * @return true if identical.
    */
   public boolean isMonitoring( BuildResultStatus status ) {
      return monitoredState == status;
   }//End Method

}//End Class
