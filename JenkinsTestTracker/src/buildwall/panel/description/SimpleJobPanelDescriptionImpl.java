/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel.description;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.concurrent.TimeUnit;

import buildwall.configuration.BuildWallConfiguration;
import graphics.DecoupledPlatformImpl;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.registrations.ChangeListenerBindingImpl;
import javafx.registrations.ChangeListenerRegistrationImpl;
import javafx.registrations.PaintColorChangeListenerBindingImpl;
import javafx.registrations.RegistrationImpl;
import javafx.registrations.RegistrationManager;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.jobs.JenkinsJob;

/**
 * The {@link SimpleJobPanelDescriptionImpl} provides a {@link GridPane} overlay for 
 * the description of the {@link JenkinsJob} in a simple single line arrangement.
 */
public class SimpleJobPanelDescriptionImpl extends JobPanelDescriptionBaseImpl {

   static final double BUILD_PROPERTY_PERCENTAGE = 66;
   static final double JOB_NAME_PERCENTAGE = 66;
   static final double COMPLETION_ESTIMATE_PERCENTAGE = 66;
   
   /**
    * Constructs a new {@link SimpleJobPanelDescriptionImpl}.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param job the {@link JenkinsJob} being described.
    */
   public SimpleJobPanelDescriptionImpl( BuildWallConfiguration configuration, JenkinsJob job ) {
      super( configuration, job );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override protected void applyLayout() {
      propertiesPane().add( buildNumber(), 0, 0 );
      propertiesPane().add( jobName(), 1, 0 );
      propertiesPane().add( completionEstimate(), 2, 0 );
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
