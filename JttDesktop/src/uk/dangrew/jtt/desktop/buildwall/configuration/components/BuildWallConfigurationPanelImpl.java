/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;

/**
 * The {@link BuildWallConfigurationPanelImpl} provides a {@link GridPane} for configuring
 * the {@link BuildWallConfiguration}.
 */
public class BuildWallConfigurationPanelImpl extends GridPane {
   
   private JavaFxStyle styling;
   private BuildWallConfiguration configuration;
   
   private Label titleLabel;
   
   private TitledPane dimensionsPane;
   private TitledPane jobPoliciesPane;
   private TitledPane fontPane;
   private TitledPane colourPane;
   
   /**
    * Constructs a new {@link BuildWallConfigurationPanelImpl}.
    * @param configuration the {@link BuildWallConfiguration} to configure.
    * @param title the title of the panel.
    */
   public BuildWallConfigurationPanelImpl( String title, BuildWallConfiguration configuration ) {
      this.configuration = configuration;
      this.styling = new JavaFxStyle();
      
      constructTitle( title );
      constructDimensions();
      constructJobPoliciesPane();
      constructFontItemPane();
      constructColourItemPane();
      
      ColumnConstraints width = new ColumnConstraints();
      width.setPercentWidth( 100 );
      width.setHgrow( Priority.ALWAYS );
      getColumnConstraints().addAll( width );
   }//End Constructor

   /**
    * Method to determine whether the {@link BuildWallConfiguration} associated with this
    * {@link BuildWallConfigurationPanelImpl} is exactly that given.
    * @param configuration the {@link BuildWallConfiguration} in question.
    * @return true if exactly equal.
    */
   public boolean usesConfiguration( BuildWallConfiguration configuration ) {
      return this.configuration == configuration;
   }//End Method
   
   /**
    * Method to construct the title for the panel.
    * @param title the title to use.
    */
   private void constructTitle( String title ) {
      titleLabel = styling.constructTitle( title );
      add( titleLabel, 0, 0 );
   }//End Method
   
   /**
    * Method to construct the dimensions configurables.
    */
   private void constructDimensions(){ 
      DimensionsPanel dimensionsPanel = new DimensionsPanel( configuration );
      
      dimensionsPane = new TitledPane( "Dimensions", dimensionsPanel );
      dimensionsPane.setExpanded( true );
      add( dimensionsPane, 0, 1 );
   }//End Method
   
   /**
    * Method to construct the {@link JobPolicyPanel} and add it to this {@link GridPane}.
    */
   private void constructJobPoliciesPane(){
      GridPane policiesContent = new JobPolicyPanel( configuration );
      
      jobPoliciesPane = new TitledPane( "Job Policies", policiesContent );
      jobPoliciesPane.setExpanded( false );
      add( jobPoliciesPane, 0, 2 );
   }//End Method
   
   /**
    * Method to construct a {@link TitledPane} for the {@link Font}s.
    */
   private void constructFontItemPane(){
      FontsPanel fontContent = new FontsPanel( configuration );
      
      fontPane = new TitledPane( "Fonts", fontContent );
      fontPane.setExpanded( true );
      add( fontPane, 0, 3 );
   }//End Method
   
   /**
    * Method to construct the {@link TitledPane} for configuring {@link Color}s.
    */
   private void constructColourItemPane() {
      ColoursPanel coloursPanel = new ColoursPanel( configuration );
      
      colourPane = new TitledPane( "Colours", coloursPanel );
      colourPane.setExpanded( true );
      add( colourPane, 0, 4 );
   }//End Method
   
   TitledPane fontPane() {
      return fontPane;
   }//End Method

   TitledPane colourPane() {
      return colourPane;
   }//End Method

   TitledPane dimensionsPane() {
      return dimensionsPane;
   }//End Method

   TitledPane jobPoliciesPane() {
      return jobPoliciesPane;
   }//End Method

   Label titleLabel() {
      return titleLabel;
   }//End Method

}//End Class
