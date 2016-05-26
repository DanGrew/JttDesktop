/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.MapChangeListener.Change;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.BuildWallJobPolicy;
import uk.dangrew.jtt.buildwall.configuration.style.BuildWallConfigurationStyle;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.javafx.combobox.SimplePropertyBox;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.utility.comparator.Comparators;

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
   
   private SimplePropertyBox< BuildWallJobPolicy > setAllBox;
   private Button setAllButton;
   
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
      DecoupledPlatformImpl.runLater( () -> {
         clearAndResetComponents();
         
         provideSetAllControl();
         providePolicyConfigurationPerJob();
         
         setPadding( new Insets( INSETS ) );
      } );
   }//End Method

   /**
    * Method to clear and reset the components of the panel.
    */
   private void clearAndResetComponents() {
      getChildren().clear();
      labels.clear();
      boxes.clear();
      properties.clear();
   }//End Method
   
   /**
    * Method to provide the control to set {@link BuildWallJobPolicy} for all {@link JenkinsJob}s at once.
    */
   private void provideSetAllControl() {
      setAllBox = new SimplePropertyBox<>();
      setAllBox.getItems().addAll( BuildWallJobPolicy.values() );
      setAllBox.setMaxWidth( Double.MAX_VALUE );
      setAllBox.getSelectionModel().select( BuildWallJobPolicy.NeverShow );
      add( setAllBox, 0, 0 );
      
      setAllButton = new Button( "Set All" );
      Font setAllButtonFont = setAllButton.getFont();
      setAllButton.setFont( Font.font( setAllButtonFont.getFamily(), FontWeight.BOLD, FontPosture.REGULAR, setAllButtonFont.getSize() ) );
      setAllButton.setMaxWidth( Double.MAX_VALUE );
      add( setAllButton, 1, 0 );
      
      setAllButton.setOnAction( event -> setAllBoxesToSelectedPolicy() );
   }//End Method
   
   /**
    * Method to set all {@link SimplePropertyBox}es to the currently selected {@link BuildWallJobPolicy}.
    */
   private void setAllBoxesToSelectedPolicy() {
      BuildWallJobPolicy selected = setAllBox.getSelectionModel().getSelectedItem();
      boxes.entrySet().forEach( entry -> entry.getValue().getSelectionModel().select( selected ) );
   }//End Method

   /**
    * Method to configure the panel for all {@link JenkinsJob}s.
    */
   private void providePolicyConfigurationPerJob() {
      int rowIndex = 1;
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

   SimplePropertyBox< BuildWallJobPolicy > setAllPropertyBox() {
      return setAllBox;
   }//End Method

   Button setAllButton() {
      return setAllButton;
   }//End Method

}//End Class
