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
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.statistics.metrics.LastBuildStarted;
import uk.dangrew.jtt.statistics.panel.StatisticPanel;

/**
 * The {@link LastBuildStartedStatistic} is a graphical {@link StatisticPanel} for the
 * {@link LastBuildStarted}.
 */
public class LastBuildStartedStatistic extends StatisticPanel {

   static final String INITIAL_VALUE = "Unknown";
   static final String DESCRIPTION_TEXT = "Last Build Started";
   
   private final LastBuildStarted statistic;
   
   /**
    * Constructs a new {@link LastBuildStartedStatistic}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    */
   public LastBuildStartedStatistic( StatisticsConfiguration configuration, JenkinsDatabase database ) {
      this( new JavaFxStyle(), configuration, database );
   }//End Constructor
   
   /**
    * Constructs a new {@link LastBuildStartedStatistic}.
    * @param styling the {@link JavaFxStyle}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    */
   LastBuildStartedStatistic( JavaFxStyle styling, StatisticsConfiguration configuration, JenkinsDatabase database ) {
      super( styling, configuration, database, DESCRIPTION_TEXT, INITIAL_VALUE );
      statistic = new LastBuildStarted( 
               configuration, 
               database, 
               this 
      );
   }//End Constructor
   
   LastBuildStarted statistic(){
      return statistic;
   }//End Method

}//End Class
