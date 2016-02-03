/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import java.util.function.Function;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * The {@link BuildWallConfigurationPanelImpl} provides a {@link GridPane} for configuring
 * the {@link BuildWallConfiguration}.
 */
public class BuildWallConfigurationPanelImpl extends GridPane {
   
   static final double LABEL_PERCENTAGE_WIDTH = 40;
   static final double CONTROLS_PERCENTAGE_WIDTH = 60;
   static final int MINIMUM_COLUMNS = 1;
   static final int MAXIMUM_COLUMNS = 1000;
   private BuildWallConfiguration configuration;
   private Function< Font, Font > fontSupplier;
   
   private TitledPane dimensionsPane;
   private TitledPane fontPane;
   private TitledPane colourPane;
   
   private Label columnsLabel;
   private Label jobNameFontLabel;
   private Label jobNameColourLabel;
   private Label buildNumberFontLabel;
   private Label buildNumberColourLabel;
   private Label completionEstimateFontLabel;
   private Label completionEstimateColourLabel;

   private Spinner< Integer > columnsSpinner;
   
   private Button jobNameFontButton;
   private Button buildNumberFontButton;
   private Button completionEstimateFontButton;
   
   private ColorPicker jobNameColourPicker;
   private ColorPicker buildNumberColourPicker;
   private ColorPicker completionEstimateColourPicker;
   
   /**
    * Constructs a new {@link BuildWallConfigurationPanelImpl}.
    * @param configuration the {@link BuildWallConfiguration} to configure.
    * @param fontSupplier the method of inputting a different {@link Font}.
    */
   public BuildWallConfigurationPanelImpl( BuildWallConfiguration configuration, Function< Font, Font > fontSupplier ) {
      this.configuration = configuration;
      this.fontSupplier = fontSupplier;
      
      constructDimensions();
      constructFontItemPane();
      constructColourItemPane();
      
      ColumnConstraints width = new ColumnConstraints();
      width.setPercentWidth( 100 );
      width.setHgrow( Priority.ALWAYS );
      getColumnConstraints().addAll( width );
   }//End Constructor
   
   /**
    * Method to construct the dimensions configurables.
    */
   private void constructDimensions(){ 
      GridPane dimensionsContent = new GridPane();
      
      columnsLabel = createBoldLabel( "Columns" );
      dimensionsContent.add( columnsLabel, 0, 0 );
      
      columnsSpinner = new Spinner<>();
      columnsSpinner.setMaxWidth( Double.MAX_VALUE );
      columnsSpinner.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory( 1, 1000, configuration.numberOfColumns().get() ) );
      columnsSpinner.valueProperty().addListener( ( source, old, updated ) -> configuration.numberOfColumns().set( updated ) );
      configuration.numberOfColumns().addListener( ( source, old, updated ) -> columnsSpinner.getValueFactory().setValue( updated.intValue() ) );
      dimensionsContent.add( columnsSpinner, 1, 0 );

      configureColumnConstraints( dimensionsContent );
      
      dimensionsPane = new TitledPane( "Dimensions", dimensionsContent );
      add( dimensionsPane, 0, 0 );
   }//End Method
   
   /**
    * Method to construct a {@link TitledPane} for the {@link Font}s.
    */
   private void constructFontItemPane(){
      GridPane fontContent = new GridPane();
      
      jobNameFontLabel = createBoldLabel( "Job Name" );
      fontContent.add( jobNameFontLabel, 0, 0 );
      jobNameFontButton = new Button( configuration.jobNameFont().get().getName() );
      configureFontButtonAndExample( 
               jobNameFontButton, 
               configuration.jobNameFont() 
      );
      fontContent.add( jobNameFontButton, 1, 0 );

      buildNumberFontLabel = createBoldLabel( "Build Number" );
      fontContent.add( buildNumberFontLabel, 0, 2 );
      buildNumberFontButton = new Button( configuration.buildNumberFont().get().getName() );
      configureFontButtonAndExample( 
               buildNumberFontButton, 
               configuration.buildNumberFont() 
      );
      fontContent.add( buildNumberFontButton, 1, 2 );

      completionEstimateFontLabel = createBoldLabel( "Build Time" );
      fontContent.add( completionEstimateFontLabel, 0, 4 );
      completionEstimateFontButton = new Button( configuration.completionEstimateFont().get().getName() );
      configureFontButtonAndExample( 
               completionEstimateFontButton, 
               configuration.completionEstimateFont() 
      );
      fontContent.add( completionEstimateFontButton, 1, 4 );
      
      configureColumnConstraints( fontContent );
      
      fontPane = new TitledPane( "Fonts", fontContent );
      add( fontPane, 0, 1 );
   }//End Method
   
   /**
    * Method to configure a {@link Font} configuring {@link Button}.
    * @param button the {@link Button} to configure.
    * @param quickFox the {@link Label} to update.
    * @param configProperty the property associated.
    */
   private void configureFontButtonAndExample( Button button, ObjectProperty< Font > configProperty ){
      button.setMaxWidth( Double.MAX_VALUE );
      button.setOnAction( event -> configProperty.set( fontSupplier.apply( configProperty.get() ) ) );
      configProperty.addListener( ( source, old, updated ) -> updateFont( button, updated ) );
   }//End Method
   
   /**
    * Method to create a bold {@link Label}.
    * @param title the text in the {@link Label}.
    * @return the constructed {@link Label}.
    */
   private Label createBoldLabel( String title ) {
      Label label = new Label( title );
      Font existingFont = label.getFont();
      label.setFont( Font.font( existingFont.getFamily(), FontWeight.BOLD, FontPosture.REGULAR, existingFont.getSize() ) );
      return label;
   }//End Method
   
   /**
    * Method to construct the {@link TitledPane} for configuring {@link Color}s.
    */
   private void constructColourItemPane() {
      GridPane content = new GridPane();
      
      jobNameColourLabel = createBoldLabel( "Job Name" );
      content.add( jobNameColourLabel, 0, 0 );
      
      jobNameColourPicker = new ColorPicker();
      configureColorPicker( jobNameColourPicker, configuration.jobNameColour() );
      content.add( jobNameColourPicker, 1, 0 );
      
      buildNumberColourLabel = createBoldLabel( "Build Number" );
      content.add( buildNumberColourLabel, 0, 1 );
      
      buildNumberColourPicker = new ColorPicker();
      configureColorPicker( buildNumberColourPicker, configuration.buildNumberColour() );
      content.add( buildNumberColourPicker, 1, 1 );
      
      completionEstimateColourLabel = createBoldLabel( "Build Time" );
      content.add( completionEstimateColourLabel, 0, 2 );
      
      completionEstimateColourPicker = new ColorPicker();
      configureColorPicker( completionEstimateColourPicker, configuration.completionEstimateColour() );
      content.add( completionEstimateColourPicker, 1, 2 );
      
      configureColumnConstraints( content );
      
      colourPane = new TitledPane( "Colours", content );
      add( colourPane, 0, 2 );
   }//End Method
   
   /**
    * Method to configure a {@link ColorPicker} to synchronise with the given property.
    * @param colorPicker the {@link ColorPicker} for changing the {@link Color}.
    * @param colorProperty the property being configured.
    */
   private void configureColorPicker( ColorPicker colorPicker, ObjectProperty< Color > colorProperty ){
      colorPicker.setMaxWidth( Double.MAX_VALUE );
      colorPicker.valueProperty().set( colorProperty.get() );
      colorPicker.valueProperty().addListener( ( source, old, updated ) -> colorProperty.set( updated ) );
      colorProperty.addListener( ( source, old, updated ) -> colorPicker.valueProperty().set( updated ) );
   }//End Method
   
   /**
    * Method to update the {@link Font} property, updating the {@link Button} and text example.
    * @param button the {@link Button} to change the text on.
    * @param quickFox the {@link Label} with the example text to update.
    * @param newFont the new {@link Font}.
    */
   private void updateFont( Button button, Font newFont ) {
      if ( newFont == null ) return;
      
      button.setText( newFont.getName() );
   }//End Method
   
   /**
    * Method to configure the {@link ColumnConstraints} on the given {@link GridPane}.
    * @param grid the {@link GridPane} to apply constraints to.
    */
   private void configureColumnConstraints( GridPane grid ){
      ColumnConstraints labels = new ColumnConstraints();
      labels.setPercentWidth( LABEL_PERCENTAGE_WIDTH );
      labels.setHgrow( Priority.ALWAYS );
      ColumnConstraints controls = new ColumnConstraints();
      controls.setPercentWidth( CONTROLS_PERCENTAGE_WIDTH );
      controls.setHgrow( Priority.ALWAYS );
      grid.getColumnConstraints().addAll( labels, controls );  
   }//End Method
   
   Button jobNameFontButton() {
      return jobNameFontButton;
   }//End Method

   Button buildNumberFontButton() {
      return buildNumberFontButton;
   }//End Method
   
   Button completionEstimateFontButton() {
      return completionEstimateFontButton;
   }//End Method

   TitledPane fontPane() {
      return fontPane;
   }//End Method

   TitledPane colourPane() {
      return colourPane;
   }//End Method

   ColorPicker jobNameColourPicker() {
      return jobNameColourPicker;
   }//End Method

   ColorPicker buildNumberColourPicker() {
      return buildNumberColourPicker;
   }//End Method

   ColorPicker completionEstimateColourPicker() {
      return completionEstimateColourPicker;
   }//End Method

   Label jobNameFontLabel() {
      return jobNameFontLabel;
   }//End Method

   Labeled jobNameColourLabel() {
      return jobNameColourLabel;
   }//End Method

   Labeled buildNumberFontLabel() {
      return buildNumberFontLabel;
   }//End Method

   Labeled buildNumberColourLabel() {
      return buildNumberColourLabel;
   }//End Method

   Labeled completionEstimateFontLabel() {
      return completionEstimateFontLabel;
   }//End Method

   Labeled completionEstimateColourLabel() {
      return completionEstimateColourLabel;
   }//End Method

   TitledPane dimensionsPane() {
      return dimensionsPane;
   }//End Method

   Spinner< Integer > columnsSpinner() {
      return columnsSpinner;
   }//End Method

   Label columnsSpinnerLabel() {
      return columnsLabel;
   }//End Method

}//End Class