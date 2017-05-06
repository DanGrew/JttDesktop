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
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.utility.observable.FunctionListChangeListenerImpl;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.statistics.panel.StatisticView;

/**
 * {@link StatisticCalculatorBase} provides the base for an object responsible for calculating a 
 * statistic of the system.
 */
public abstract class StatisticCalculatorBase {

   private final StatisticsConfiguration configuration;
   private final JenkinsDatabase database;
   private final StatisticView statistic;
   
   /**
    * Constructs a new {@link StatisticCalculatorBase}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    * @param statistic the {@link StatisticView} to update.
    */
   public StatisticCalculatorBase( 
            StatisticsConfiguration configuration, 
            JenkinsDatabase database, 
            StatisticView statistic 
   ) {
      if ( configuration == null || database == null || statistic == null  ) {
         throw new IllegalArgumentException( "Must supply non null parameters." );
      }
      
      this.configuration = configuration;
      this.database = database;
      this.statistic = statistic;
      
      FunctionListChangeListenerImpl< JenkinsJob > listener = new FunctionListChangeListenerImpl<>( 
               c -> recalculateStatistic(), c -> recalculateStatistic() 
      );
      this.database.jenkinsJobs().addListener( listener );
      this.configuration.excludedJobs().addListener( listener );
   }//End Constructor
   
   /**
    * Method to recalculate the statistic value.
    */
   protected void recalculateStatistic(){
      FilteredList< JenkinsJob > relevantJobs = database.jenkinsJobs()
               .filtered( j -> !configuration.excludedJobs().contains( j ) ); 
      String value = constructStatisticRepresentation( relevantJobs );
      
      if ( value == null || value.equals( statistic.getStatisticValue() ) ) {
         return;
      }
      statistic.setStatisticValue( value );
   }//End Method
   
   /**
    * Method to construct the representation, calculating the values appropriately.
    * @param applicableJobs the {@link JenkinsJob}s to count in the statistic.
    * @return the representation.
    */
   protected abstract String constructStatisticRepresentation( FilteredList< JenkinsJob > applicableJobs );

   /**
    * Access to the {@link JenkinsDatabase}.
    * @return the {@link JenkinsDatabase}.
    */
   protected JenkinsDatabase database(){
      return database;
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

}//End Class
