/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

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
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallJobPolicy;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.javafx.combobox.SimplePropertyBox;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.utility.comparator.Comparators;

/**
 * The {@link JobPolicyPanel} provides a {@link GridPane} for the {@link JenkinsJob}s currently
 * in the {@link BuildWallConfiguration} and configuration for the {@link BuildWallJobPolicy}.
 */
public class JobPolicyPanel extends GridPane {
   
   static final double INSETS = 10;
   private JavaFxStyle styling;
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
      this( configuration, new JavaFxStyle() );
   }//End Constructor
   
   /**
    * Constructs a new {@link JobPolicyPanel}.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param styling the {@link JavaFxStyle} to apply.
    */
   JobPolicyPanel( BuildWallConfiguration configuration, JavaFxStyle styling ) {
      this.configuration = configuration;
      this.styling = styling;
      
      labels = new HashMap<>();
      boxes = new HashMap<>();
      properties = new HashMap<>();
      
      styling.configureColumnConstraints( this );
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
      
      //think there is a memory leak in the below, when they are cleared the lambdas are not removed
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

   /**
    * Method to determine whether the given {@link BuildWallConfiguration} is associated.
    * @param configuration the {@link BuildWallConfiguration} in question.
    * @return true if associated.
    */
   public boolean hasConfiguration( BuildWallConfiguration configuration ) {
      return this.configuration.equals( configuration );
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
