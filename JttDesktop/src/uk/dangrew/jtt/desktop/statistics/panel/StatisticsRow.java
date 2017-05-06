/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.panel;

import javafx.geometry.HPos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.statistics.panel.views.LastBuildStartedStatistic;
import uk.dangrew.jtt.desktop.statistics.panel.views.NodesInUseStatistic;
import uk.dangrew.jtt.desktop.statistics.panel.views.TotalPassingTestsStatistic;
import uk.dangrew.jtt.desktop.statistics.panel.views.TotalSuccessStatistic;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

/**
 * The {@link StatisticsRow} is a collecting panel for the {@link StatisticPanel}s to display on the 
 * build wall.
 */
public class StatisticsRow extends GridPane {
   
   private final JenkinsDatabase database;
   private final StatisticsConfiguration configuration;
   private final TotalSuccessStatistic totalSuccess;
   private final TotalPassingTestsStatistic totalPassingTests;
   private final NodesInUseStatistic nodesInUse;
   private final LastBuildStartedStatistic lastBuildStarted;

   /**
    * Constructs a new {@link StatisticsRow}.
    * @param database the {@link JenkinsDatabase}.
    * @param configuration the {@link StatisticsConfiguration}.
    */
   public StatisticsRow( JenkinsDatabase database, StatisticsConfiguration configuration ) {
      this( new JavaFxStyle(), database, configuration );
   }//End Constructor
   
   /**
    * Constructs a new {@link StatisticsRow}.
    * @param styling the {@link JavaFxStyle}.
    * @param database the {@link JenkinsDatabase}.
    * @param configuration the {@link StatisticsConfiguration}.
    */
   StatisticsRow( JavaFxStyle styling, JenkinsDatabase database, StatisticsConfiguration configuration ) {
      this.database = database;
      this.configuration = configuration;
      
      int columnPosition = 0;
      
      placeStatisticView( 
               this.totalSuccess = new TotalSuccessStatistic( configuration, database ),
               columnPosition++
      );
      placeStatisticView(
               this.totalPassingTests = new TotalPassingTestsStatistic( configuration, database ),
               columnPosition++
      );
      placeStatisticView(
               this.nodesInUse = new NodesInUseStatistic( configuration, database ),
               columnPosition++
      );
      placeStatisticView(
               this.lastBuildStarted = new LastBuildStartedStatistic( configuration, database ),
               columnPosition++
      );
      
      styling.configureConstraintsForEvenColumns( this, columnPosition );
      setBackground( new Background( new BackgroundFill( Color.BLACK, null, null ) ) );
   }//End Constructor
   
   /**
    * Convenience method to place a {@link StatisticPanel} in the {@link StatisticsRow}.
    * @param panel the {@link StatisticPanel} to add in.
    * @param columnPosition the position of the panel in the grid.
    */
   private void placeStatisticView( StatisticPanel panel, int columnPosition ){
      add( panel, columnPosition, 0 );
      GridPane.setHalignment( panel, HPos.CENTER );
   }//End Method
   
   /**
    * Method to determine whether the given is associated with this.
    * @param database the {@link JenkinsDatabase}.
    * @return true if identical.
    */
   public boolean isAssociatedWith( JenkinsDatabase database ) {
      return this.database == database;
   }//End Method
   
   /**
    * Method to determine whether the given is associated with this.
    * @param database the {@link StatisticsConfiguration}.
    * @return true if identical.
    */
   public boolean isAssociatedWith( StatisticsConfiguration statisticsConfiguration ) {
      return this.configuration == statisticsConfiguration;
   }//End Method
   
   TotalSuccessStatistic totalSuccessStatistic(){
      return totalSuccess;
   }//End Method
   
   TotalPassingTestsStatistic totalPassingTestsStatistic(){
      return totalPassingTests;
   }//End Method
   
   NodesInUseStatistic nodeInUseStatistic(){
      return nodesInUse;
   }//End Method
   
   LastBuildStartedStatistic lastBuildStartedStatistic(){
      return lastBuildStarted;
   }//End Method
   
}//End Class
