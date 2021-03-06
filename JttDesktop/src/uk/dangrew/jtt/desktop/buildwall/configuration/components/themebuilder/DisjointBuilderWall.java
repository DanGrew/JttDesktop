/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.themebuilder;

import java.util.EnumMap;
import java.util.Map;

import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.kode.javafx.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.desktop.buildwall.panel.JobPanelImpl;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

/**
 * The {@link DisjointBuilderWall} is a mini build wall that allows isolated configuration
 * of the {@link BuildWallTheme}.
 */
class DisjointBuilderWall extends GridPane {
   
   static final int COLUMN_COUNT = 3;
   static final double COLUMN_WIDTH = 33;
   static final double REDUCED_TEXT_SIZE = 12;
   static final long JOB_EXPECTED_BUILD_TIME = 100;
   static final long JOB_PROGRESS = 25;
   static final long JOB_TIMESTAMP = 0;
   
   private final JavaFxStyle styling;
   private final BuildWallTheme theme;
   private final BuildWallConfiguration configuration;
   
   private Map< BuildResultStatus, JobPanelImpl > panels;

   /**
    * Constructs a new {@link DisjointBuilderWall}.
    * @param theme the {@link BuildWallTheme} to configure.
    */
   public DisjointBuilderWall( BuildWallTheme theme ) {
      this( new JavaFxStyle(), new BuildWallConfigurationImpl(), theme );
   }//End Constructor
   
   /**
    * Constructs a new {@link DisjointBuilderWall}.
    * @param styling the {@link JavaFxStyle} for customizing the graphics.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param theme the {@link BuildWallTheme} to configure.
    */
   DisjointBuilderWall( JavaFxStyle styling, BuildWallConfiguration configuration, BuildWallTheme theme ) {
      this.styling = styling;
      this.theme = theme;
      this.configuration = configuration;
      this.configuration.buildNumberFont().set( Font.font( REDUCED_TEXT_SIZE ) );
      this.configuration.completionEstimateFont().set( Font.font( REDUCED_TEXT_SIZE ) );
      this.panels = new EnumMap<>( BuildResultStatus.class );
      
      this.createAndLayoutJobPanels();
      this.styling.configureConstraintsForColumnPercentages( this, COLUMN_WIDTH, COLUMN_WIDTH, COLUMN_WIDTH );
   }//End Constructor
   
   /**
    * Method to create the {@link JobPanelImpl}s and lay them out.
    */
   private void createAndLayoutJobPanels(){
      int column = 0;
      int row = 0;
      
      for ( BuildResultStatus status : BuildResultStatus.values() ) {
         JobPanelImpl panel = createAndAddJobPanel( status.displayName(), status );
         this.panels.put( status, panel );
         this.add( panel, column, row );
         
         column++;
         if ( column == COLUMN_COUNT ) {
            column = 0;
            row++;
         }
      }
   }//End Method
   
   /**
    * Method to create an individual {@link JobPanelImpl} and give it some state to provide a
    * good visualization of the {@link JenkinsJob}.
    * @param jobName the name of the associated {@link JenkinsJob}.
    * @param status the {@link BuildResultStatus}.
    * @return the {@link JobPanelImpl} created.
    */
   private JobPanelImpl createAndAddJobPanel( String jobName, BuildResultStatus status ) {
      JenkinsJob job = new JenkinsJobImpl( jobName );
      job.setBuildStatus( status );
      job.buildTimestampProperty().set( JOB_TIMESTAMP );
      job.currentBuildTimeProperty().set( JOB_PROGRESS );
      job.expectedBuildTimeProperty().set( JOB_EXPECTED_BUILD_TIME );
      return new JobPanelImpl( configuration, theme, job );
   }//End Method
   
   /**
    * Method to determine whether this is associated with the given {@link BuildWallTheme}.
    * @param theme the {@link BuildWallTheme} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( BuildWallTheme theme ) {
      return this.theme == theme;
   }//End Method
   
   JobPanelImpl panelFor( BuildResultStatus status ) {
      return panels.get( status );
   }//End Method
}//End Class
