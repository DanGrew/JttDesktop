/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.metrics;

import java.util.HashSet;
import java.util.Set;

import javafx.collections.transformation.FilteredList;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.statistics.panel.StatisticView;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * {@link NodesInUse} provides an object responsible for calculating the number
 * of {@link JenkinsNode}s currently in use.
 */
public class NodesInUse extends StatisticCalculatorBase {

   private int count = 0;
   
   /**
    * Constructs a new {@link NodesInUse}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    * @param statistic the {@link StatisticView} to update.
    */
   public NodesInUse( 
            StatisticsConfiguration configuration, 
            JenkinsDatabase database, 
            StatisticView statistic 
   ) {
      super( configuration, database, statistic );
      
      recalculateStatistic();
      
      database().jenkinsJobProperties().addBuildStateListener( ( s, o, u ) -> recalculateStatistic() );
      database().jenkinsJobProperties().addBuiltOnListener( ( s, o, u ) -> recalculateStatistic() );
   }//End Constructor
   
   /**
    * Method to recalculate the statistic value.
    * @param relevantJobs the {@link FilteredList} of {@link JenkinsJob}s to use in the statistic.
    * @return the {@link String} representation of the statistic.
    */
   @Override protected String constructStatisticRepresentation( FilteredList< JenkinsJob > relevantJobs ){
      Set< JenkinsNode > nodesInUse = new HashSet<>();
      
      for ( JenkinsJob j : relevantJobs ) {
         if ( j.buildStateProperty().get() == BuildState.Built ) {
            continue;
         }
         JenkinsNode node = j.lastBuiltOnProperty().get();
         if ( node == null ) {
            continue;
         }
         nodesInUse.add( node );
      }
      
      int recalculatedCount = nodesInUse.size();
      if ( recalculatedCount == this.count ) {
         return null;
      }
      
      this.count = recalculatedCount;
      
      return Integer.toString( recalculatedCount );
   }//End Method

}//End Class
