/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel.description;

import buildwall.configuration.BuildWallConfiguration;
import javafx.geometry.HPos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import model.jobs.JenkinsJob;

/**
 * The {@link DefaultJobPanelDescriptionImpl} provides a {@link BorderPane} overlay for 
 * the description of the {@link JenkinsJob}.
 */
public class DefaultJobPanelDescriptionImpl extends JobPanelDescriptionBaseImpl {

   static final double BUILD_PROPERTY_PERCENTAGE = 50;
   static final double COMPLETION_ESTIMATE_PERCENTAGE = 50;
   
   /**
    * Constructs a new {@link DefaultJobPanelDescriptionImpl}.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param job the {@link JenkinsJob} being described.
    */
   public DefaultJobPanelDescriptionImpl( BuildWallConfiguration configuration, JenkinsJob job ) {
      super( configuration, job );
   }//End Class
   
   /**
    * {@inheritDoc}
    */
   @Override protected void applyLayout() {
      setCenter( jobName() );
      propertiesPane().add( buildNumber(), 0, 0 );
      propertiesPane().add( completionEstimate(), 1, 0 );
      setBottom( propertiesPane() );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected void applyColumnConstraints() {
      ColumnConstraints buildNumberColumn = new ColumnConstraints();
      buildNumberColumn.setPercentWidth( BUILD_PROPERTY_PERCENTAGE );
      buildNumberColumn.setHalignment( HPos.LEFT );
      ColumnConstraints completionEstimateColumn = new ColumnConstraints();
      completionEstimateColumn.setPercentWidth( COMPLETION_ESTIMATE_PERCENTAGE );
      completionEstimateColumn.setHalignment( HPos.RIGHT );
      propertiesPane().getColumnConstraints().addAll( buildNumberColumn, completionEstimateColumn );
   }//End Method
   
}//End Class
