/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.panel.description;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link SimpleJobPanelDescriptionImpl} provides a {@link GridPane} overlay for 
 * the description of the {@link JenkinsJob} in a simple single line arrangement.
 */
public class SimpleJobPanelDescriptionImpl extends JobPanelDescriptionBaseImpl {

   static final double BUILD_PROPERTY_PERCENTAGE = 50;
   static final double JOB_NAME_PERCENTAGE = 100;
   static final double COMPLETION_ESTIMATE_PERCENTAGE = 50;
   
   /**
    * Constructs a new {@link SimpleJobPanelDescriptionImpl}.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param theme the {@link BuildWallTheme}.
    * @param job the {@link JenkinsJob} being described.
    */
   public SimpleJobPanelDescriptionImpl( BuildWallConfiguration configuration, BuildWallTheme theme, JenkinsJob job ) {
      super( configuration, theme, job );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override protected void applyLayout() {
      propertiesPane().add( buildNumber(), 0, 0 );
      propertiesPane().add( jobName(), 1, 0 );
      propertiesPane().add( completionEstimate(), 2, 0 );
      propertiesPane().setAlignment( Pos.CENTER );
      setCenter( propertiesPane() );
   }//End Class
   
   /**
    * {@inheritDoc}
    */
   @Override protected void applyColumnConstraints() {
      ColumnConstraints buildNumberColumn = new ColumnConstraints();
      buildNumberColumn.setPercentWidth( BUILD_PROPERTY_PERCENTAGE );
      buildNumberColumn.setHalignment( HPos.LEFT );
      ColumnConstraints jobNameColumn = new ColumnConstraints();
      jobNameColumn.setPercentWidth( JOB_NAME_PERCENTAGE );
      jobNameColumn.setHalignment( HPos.CENTER );
      ColumnConstraints completionEstimateColumn = new ColumnConstraints();
      completionEstimateColumn.setPercentWidth( COMPLETION_ESTIMATE_PERCENTAGE );
      completionEstimateColumn.setHalignment( HPos.RIGHT );
      propertiesPane().getColumnConstraints().addAll( buildNumberColumn, jobNameColumn, completionEstimateColumn );
   }//End Method
   
}//End Class
