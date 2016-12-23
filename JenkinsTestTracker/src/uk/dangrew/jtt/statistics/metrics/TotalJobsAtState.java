/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.metrics;

import javafx.collections.transformation.FilteredList;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.statistics.panel.StatisticView;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.utility.observable.FunctionListChangeListenerImpl;

/**
 * {@link TotalJobsAtState} provides an object responsible for calculating the total number
 * of {@link JenkinsJob}s currently passing in the system.
 */
public class TotalJobsAtState {

   private final StatisticsConfiguration configuration;
   private final JenkinsDatabase database;
   private final StatisticView statistic;
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
      if ( configuration == null || database == null || statistic == null || status == null ) {
         throw new IllegalArgumentException( "Must supply non null parameters." );
      }
      
      this.configuration = configuration;
      this.database = database;
      this.statistic = statistic;
      this.monitoredState = status;
      
      this.database.jenkinsJobProperties().addBuildResultStatusListener( ( s, o, u ) -> recalculateStatistic() );
      FunctionListChangeListenerImpl< JenkinsJob > listener = new FunctionListChangeListenerImpl<>( 
               c -> recalculateStatistic(), c -> recalculateStatistic() 
      );
      this.database.jenkinsJobs().addListener( listener );
      this.configuration.excludedJobs().addListener( listener );
      
      recalculateStatistic();
   }//End Constructor
   
   /**
    * Method to recalculate the statistic value.
    */
   private void recalculateStatistic(){
      FilteredList< JenkinsJob > relevantJobs = database.jenkinsJobs()
               .filtered( j -> !configuration.excludedJobs().contains( j ) ); 
      final int recalculatedTotal = relevantJobs.size();
      final int recalculatedCount = relevantJobs
               .filtered( j -> j.getLastBuildStatus() == monitoredState ).size();
      
      if ( this.total == recalculatedTotal && this.count == recalculatedCount ) {
         return;
      }
      
      this.total = recalculatedTotal;
      this.count = recalculatedCount;
      statistic.setStatisticValue( recalculatedCount + "/" + recalculatedTotal );
   }//End Method

   /**
    * Method to determine whether this is associated with the given.
    * @param configuration the {@link StatisticsConfiguration} in question.
    * @return true if identical.
    */
   public boolean uses( StatisticsConfiguration configuration ) {
      return this.configuration == configuration;
   }//End Method

   /**
    * Method to determine whether this is associated with the given.
    * @param database the {@link JenkinsDatabase} in question.
    * @return true if identical.
    */
   public boolean uses( JenkinsDatabase database ) {
      return this.database == database;
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
