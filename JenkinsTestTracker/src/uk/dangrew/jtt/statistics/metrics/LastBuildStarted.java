/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.metrics;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import javafx.collections.transformation.FilteredList;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.statistics.panel.StatisticView;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.utility.time.TimestampFormatter;

/**
 * {@link LastBuildStarted} provides an object responsible for calculating the timestamp
 * of the last build started.
 */
public class LastBuildStarted extends StatisticCalculatorBase {

   static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern( "dd/MM/yy" )
            .appendLiteral( "-" )
            .appendPattern( "HH:mm:ss" )
            .toFormatter();

   static final String UNKNOWN = "Unknown";
   
   private final TimestampFormatter timestampformatter;
   
   /**
    * Constructs a new {@link LastBuildStarted}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    * @param statistic the {@link StatisticView} to update.
    */
   public LastBuildStarted( 
            StatisticsConfiguration configuration, 
            JenkinsDatabase database, 
            StatisticView statistic 
   ) {
      this( new TimestampFormatter(), configuration, database, statistic );
   }//End Constructor
      
   /**
    * Constructs a new {@link LastBuildStarted}.
    * @param timestampFormatter the {@link TimestampFormatter}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    * @param statistic the {@link StatisticView} to update.
    */
   LastBuildStarted( 
            TimestampFormatter timestampFormatter,
            StatisticsConfiguration configuration, 
            JenkinsDatabase database, 
            StatisticView statistic 
   ) {
      super( configuration, database, statistic );
      this.timestampformatter = timestampFormatter;
      
      recalculateStatistic();
      
      database().jenkinsJobProperties().addTimestampListener( ( s, o, u ) -> recalculateStatistic() );
   }//End Constructor
   
   /**
    * Method to recalculate the statistic value.
    * @param relevantJobs the {@link FilteredList} of {@link JenkinsJob}s to use in the statistic.
    * @return the {@link String} representation of the statistic.
    */
   @Override protected String constructStatisticRepresentation( FilteredList< JenkinsJob > relevantJobs ){
      Long recalculatedTimestamp = null;
      
      for ( JenkinsJob j : relevantJobs ) {
         Long timestamp = j.currentBuildTimestampProperty().get();
         if ( timestamp == null ) {
            continue;
         }
         if ( recalculatedTimestamp == null ) {
            recalculatedTimestamp = timestamp;
            continue;
         }
         if ( recalculatedTimestamp < timestamp ) {
            recalculatedTimestamp = j.currentBuildTimestampProperty().get();
         }
      }
      
      if ( recalculatedTimestamp == null ) {
         return UNKNOWN;
      }
      
      return timestampformatter.format( recalculatedTimestamp, DATE_TIME_FORMATTER );
   }//End Method

}//End Class
