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
import uk.dangrew.jtt.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.statistics.metrics.TotalPassingTests;
import uk.dangrew.jtt.statistics.panel.StatisticPanel;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link TotalPassingTestsStatistic} is a graphical {@link StatisticPanel} for the
 * {@link TotalPassingTests}.
 */
public class TotalPassingTestsStatistic extends StatisticPanel {

   static final String INITIAL_VALUE = "Unknown";
   static final String DESCRIPTION_TEXT = "Passing Tests";
   
   private final TotalPassingTests statistic;
   
   /**
    * Constructs a new {@link TotalPassingTestsStatistic}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    */
   public TotalPassingTestsStatistic( StatisticsConfiguration configuration, JenkinsDatabase database ) {
      this( new JavaFxStyle(), configuration, database );
   }//End Constructor
   
   /**
    * Constructs a new {@link TotalPassingTestsStatistic}.
    * @param styling the {@link JavaFxStyle}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    */
   TotalPassingTestsStatistic( JavaFxStyle styling, StatisticsConfiguration configuration, JenkinsDatabase database ) {
      super( styling, configuration, database, DESCRIPTION_TEXT, INITIAL_VALUE );
      statistic = new TotalPassingTests( 
               configuration, 
               database, 
               this 
      );
   }//End Constructor
   
   TotalPassingTests statistic(){
      return statistic;
   }//End Method

}//End Class
