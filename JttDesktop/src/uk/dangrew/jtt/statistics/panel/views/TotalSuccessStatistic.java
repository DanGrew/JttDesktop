/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.panel.views;

import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.statistics.metrics.TotalJobsAtState;
import uk.dangrew.jtt.statistics.panel.StatisticPanel;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link TotalSuccessStatistic} is a graphical {@link StatisticPanel} for the
 * {@link TotalJobsAtState} with {@link BuildResultStatus#SUCCESS}.
 */
public class TotalSuccessStatistic extends StatisticPanel {

   static final String INITIAL_VALUE = "Unknown";
   static final String DESCRIPTION_TEXT = "Passing Jobs";
   
   private final TotalJobsAtState statistic;
   
   /**
    * Constructs a new {@link TotalSuccessStatistic}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    */
   public TotalSuccessStatistic( StatisticsConfiguration configuration, JenkinsDatabase database ) {
      this( new JavaFxStyle(), configuration, database );
   }//End Constructor
   
   /**
    * Constructs a new {@link TotalSuccessStatistic}.
    * @param styling the {@link JavaFxStyle}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    */
   TotalSuccessStatistic( JavaFxStyle styling, StatisticsConfiguration configuration, JenkinsDatabase database ) {
      super( styling, configuration, database, DESCRIPTION_TEXT, INITIAL_VALUE );
      statistic = new TotalJobsAtState( 
               configuration, 
               database, 
               this, 
               BuildResultStatus.SUCCESS 
      );
   }//End Constructor
   
   TotalJobsAtState statistic(){
      return statistic;
   }//End Method

}//End Class
