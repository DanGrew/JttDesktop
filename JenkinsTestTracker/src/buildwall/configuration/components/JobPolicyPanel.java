/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallJobPolicy;
import buildwall.configuration.style.BuildWallConfigurationStyle;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.MapChangeListener.Change;
import javafx.combobox.SimplePropertyBox;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import model.jobs.JenkinsJob;
import utility.comparator.Comparators;

/**
 * The {@link JobPolicyPanel} provides a {@link GridPane} for the {@link JenkinsJob}s currently
 * in the {@link BuildWallConfiguration} and configuration for the {@link BuildWallJobPolicy}.
 */
public class JobPolicyPanel extends GridPane {
   
   static final double INSETS = 10;
   private BuildWallConfigurationStyle styling;
   private BuildWallConfiguration configuration;
   
   private Map< JenkinsJob, Label > labels;
   private Map< JenkinsJob, SimplePropertyBox< BuildWallJobPolicy > > boxes;
   private Map< JenkinsJob, ObjectProperty< BuildWallJobPolicy > > properties;
   
   /**
    * Constructs a new {@link JobPolicyPanel}.
    * @param configuration the {@link BuildWallConfiguration}.
    */
   public JobPolicyPanel( BuildWallConfiguration configuration ) {
      this.configuration = configuration;
      this.styling = new BuildWallConfigurationStyle();
      
      labels = new HashMap<>();
      boxes = new HashMap<>();
      properties = new HashMap<>();
      
      constructLayout();
      
      configuration.jobPolicies().addListener( ( Change< ? extends JenkinsJob, ? extends BuildWallJobPolicy > change ) -> {
         if ( !labels.containsKey( change.getKey() ) ) {
            constructLayout();
         } else {
            boxes.get( change.getKey() ).getSelectionModel().select( change.getValueAdded() );
         }
      } );
   }//End Constructor
   
   /**
    * Method to construct the layout for the {@link GridPane}.
    */
   void constructLayout(){
      getChildren().clear();
      labels.clear();
      boxes.clear();
      properties.clear();
      
      int rowIndex = 0;
      List< JenkinsJob > orderedJobs = new ArrayList<>( configuration.jobPolicies().keySet() );
      orderedJobs.sort( Comparators.stringExtractionComparater( job -> job.nameProperty().get() ) );
      
      for ( JenkinsJob job : orderedJobs ) {
         Label label = styling.createBoldLabel( job.nameProperty().get() );
         add( label, 0, rowIndex );
         labels.put( job, label );
         
         ObjectProperty< BuildWallJobPolicy > property = new SimpleObjectProperty<>( configuration.jobPolicies().get( job ) );
         property.addListener( ( source, old, updated ) -> configuration.jobPolicies().put( job, updated ) );
         properties.put( job, property );
         
         SimplePropertyBox< BuildWallJobPolicy > box = new SimplePropertyBox<>();
         box.getItems().addAll( BuildWallJobPolicy.values() );
         box.bindProperty( property );
         
         box.setMaxWidth( Double.MAX_VALUE );
         add( box, 1, rowIndex );
         boxes.put( job, box );
         
         rowIndex++;
      }
      
      setPadding( new Insets( INSETS ) );
   }//End Method

   Label jobLabel( JenkinsJob job ) {
      return labels.get( job );
   }//End Method

   SimplePropertyBox< BuildWallJobPolicy > jobBox( JenkinsJob job ) {
      return boxes.get( job );
   }//End Method

   ObjectProperty< BuildWallJobPolicy > jobProperty( JenkinsJob job ) {
      return properties.get( job );
   }//End Method

}//End Class
