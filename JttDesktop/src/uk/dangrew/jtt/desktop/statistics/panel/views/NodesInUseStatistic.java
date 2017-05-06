/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.panel.views;

import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.statistics.metrics.NodesInUse;
import uk.dangrew.jtt.desktop.statistics.panel.StatisticPanel;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

/**
 * The {@link NodesInUseStatistic} is a graphical {@link StatisticPanel} for the
 * {@link NodesInUse}.
 */
public class NodesInUseStatistic extends StatisticPanel {

   static final String INITIAL_VALUE = "0";
   static final String DESCRIPTION_TEXT = "Nodes In Use";
   
   private final NodesInUse statistic;
   
   /**
    * Constructs a new {@link NodesInUseStatistic}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    */
   public NodesInUseStatistic( StatisticsConfiguration configuration, JenkinsDatabase database ) {
      this( new JavaFxStyle(), configuration, database );
   }//End Constructor
   
   /**
    * Constructs a new {@link NodesInUseStatistic}.
    * @param styling the {@link JavaFxStyle}.
    * @param configuration the {@link StatisticsConfiguration}.
    * @param database the {@link JenkinsDatabase}.
    */
   NodesInUseStatistic( JavaFxStyle styling, StatisticsConfiguration configuration, JenkinsDatabase database ) {
      super( styling, configuration, database, DESCRIPTION_TEXT, INITIAL_VALUE );
      statistic = new NodesInUse( 
               configuration, 
               database, 
               this 
      );
   }//End Constructor
   
   NodesInUse statistic(){
      return statistic;
   }//End Method

}//End Class
