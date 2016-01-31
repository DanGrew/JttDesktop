/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.layout;

import java.util.HashMap;
import java.util.Map;

import buildwall.panel.BuildWallConfiguration;
import buildwall.panel.JobPanelImpl;
import graphics.DecoupledPlatformImpl;
import javafx.collections.ListChangeListener.Change;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import model.jobs.JenkinsJob;
import storage.database.JenkinsDatabase;

/**
 * The {@link GridWallImpl} provides an implementation of the {@link BuildWall} providing
 * {@link JenkinsJob}s in a {@link GridPane}.
 */
public class GridWallImpl extends GridPane implements BuildWall {

   private JenkinsDatabase database;
   private BuildWallConfiguration configuration;
   private Map< JenkinsJob, JobPanelImpl > jobPanels;
   
   /**
    * Constructs a new {@link GridWallImpl}.
    * @param configuration the {@link BuildWallConfiguration} for customising the {@link BuildWall}.
    * @param database the {@link JenkinsDatabase} for the {@link JenkinsJob}s.
    */
   public GridWallImpl( BuildWallConfiguration configuration, JenkinsDatabase database ) {
      jobPanels = new HashMap<>();
      this.database = database;
      this.configuration = configuration;
      
      constructLayout();
      database.jenkinsJobs().addListener( ( Change< ? extends JenkinsJob > change ) -> constructLayout() );
      configuration.numberOfColumns().addListener( ( source, old, updated ) -> constructLayout() );
   }//End Constructor

   /**
    * Method to construct the layout for the current {@link JenkinsJob}s and {@link BuildWallConfiguration}.
    */
   void constructLayout() {
      getChildren().forEach( node -> GridPane.clearConstraints( node ) );
      DecoupledPlatformImpl.runLater( () -> {
         getChildren().clear();
         getRowConstraints().clear();
         getColumnConstraints().clear();
      } );
      jobPanels.clear();
      
      int numberOfColumns = configuration.numberOfColumns().get();
      if ( numberOfColumns <= 0 ) numberOfColumns = 1;
      
      int columnCount = 0;
      int rowCount = 0;
      for ( JenkinsJob job : database.jenkinsJobs() ) {
         if ( columnCount == numberOfColumns ) {
            columnCount = 0;
            rowCount++;
         }
         
         JobPanelImpl panel = new JobPanelImpl( configuration, job );
         jobPanels.put( job, panel );
         
         final int columnToAddTo = columnCount;
         final int rowToAddTo = rowCount;
         DecoupledPlatformImpl.runLater( () -> add( panel, columnToAddTo, rowToAddTo ) );
         
         columnCount++;
      }
      
      expandLastPanelIfNeeded( database.jenkinsJobs().size(), numberOfColumns, columnCount, rowCount );
      fixColumnWidths( numberOfColumns );
      fixRowHeights( rowCount + 1 ); //accounting for indices. 
   }//End Method

   /**
    * Method to expand the last {@link JobPanelImpl} if the number {@link JenkinsJob}s doesn't evenly fit
    * on the {@link GridWallImpl}.
    * @param numberOfJobs the number of {@link JenkinsJob} in the {@link JenkinsDatabase}.
    * @param numberOfColumns the number of columns to use.
    * @param columnCount the last column index.
    * @param rowCount the last row index.
    */
   private void expandLastPanelIfNeeded( int numberOfJobs, int numberOfColumns, int columnCount, int rowCount ) {
      int columnsToSpan = numberOfColumns - ( numberOfJobs % numberOfColumns );
      if ( columnsToSpan == numberOfColumns ) return;
      
      DecoupledPlatformImpl.runLater( () -> { 
            Node child = getChildren().get( getChildren().size() - 1 );
            GridPane.setConstraints( 
                  child, 
                  columnCount - 1, 
                  rowCount,
                  columnsToSpan + 1, //plus the original slot
                  1
            );
      } );
   }//End Method

   /**
    * Method to fix the heights of the rows so that they are evenly distributed across the {@link GridPane}.
    * @param rowCount the number of rows.
    */
   private void fixRowHeights( int rowCount ) {
      double rowPercentageHeight = calculateProportion( rowCount ); 
      for ( int i = 0; i < rowCount; i++ ) {
         RowConstraints row = new RowConstraints();
         row.setPercentHeight( rowPercentageHeight );
         getRowConstraints().add( row );
      }
   }//End Method
   
   /**
    * Method to fix the widths of the columns so that they are evenly distributed across the {@link GridPane}.
    * @param numberOfColumns the number of columns.
    */
   private void fixColumnWidths( int numberOfColumns ){
      double columnPercentageWidth = calculateProportion( numberOfColumns ); 
      for ( int i = 0; i < numberOfColumns; i++ ) {
         ColumnConstraints column = new ColumnConstraints();
         column.setPercentWidth( columnPercentageWidth );
         getColumnConstraints().add( column );
      }
   }//End Method
   
   /**
    * Method to calculate the proportion of each item in the {@link GridPane}.
    * @param numberOfItems the number of items to display across a length.
    * @return the proportion to use.
    */
   static double calculateProportion( double numberOfItems ) {
      if ( numberOfItems == 0 ) return 200;
      /* 100 is conceptually correct, however when dealing with fractions
       * it leaves a small percentage not covered. If we double to 200 we
       * keep proportions correct and the values adding up to > 100% cause the
       * entire screen to be filled. */
      return 200 / numberOfItems;
   }//End Method

   /**
    * Getter for the {@link JobPanelImpl} associated with the given {@link JenkinsJob}.
    * @param job the {@link JenkinsJob} to get for.
    * @return the {@link JobPanelImpl} associated.
    */
   JobPanelImpl getPanelFor( JenkinsJob job ) {
      return jobPanels.get( job );
   }//End Method

}//End Class
