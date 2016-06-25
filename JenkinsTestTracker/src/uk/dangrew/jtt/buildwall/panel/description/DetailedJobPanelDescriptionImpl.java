/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel.description;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link DetailedJobPanelDescriptionImpl} provides a {@link GridPane} overlay for 
 * the description of the {@link JenkinsJob} in a detailed arrangement with culprits and test results.
 */
public class DetailedJobPanelDescriptionImpl extends JobPanelDescriptionBaseImpl {

   static final double BUILD_PROPERTY_PERCENTAGE = 100;
   static final double COMPLETION_ESTIMATE_PERCENTAGE = 100;
   
   /**
    * Constructs a new {@link DetailedJobPanelDescriptionImpl}.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param job the {@link JenkinsJob} being described.
    */
   public DetailedJobPanelDescriptionImpl( BuildWallConfiguration configuration, JenkinsJob job ) {
      super( configuration, job );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override protected void applyLayout() {
      jobName().setAlignment( Pos.CENTER );
      jobName().setMaxWidth( Double.MAX_VALUE );
      setTop( jobName() );
      
      setCenter( new FailureDetail( getJenkinsJob(), getConfiguration() ) );
      
      propertiesPane().add( buildNumber(), 0, 0 );
      propertiesPane().add( completionEstimate(), 1, 0 );
      setBottom( propertiesPane() );
   }//End Class
   
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
