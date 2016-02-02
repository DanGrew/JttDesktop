/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import java.util.function.Supplier;

import javafx.beans.property.ObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
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
   
   static final String QUICK_FOX = "\"The quick brown fox jumps over the lazy dog\"";
   static final double LABEL_PERCENTAGE_WIDTH = 40;
   static final double CONTROLS_PERCENTAGE_WIDTH = 60;
   private BuildWallConfiguration configuration;
   private Supplier< Font > fontSupplier;
   
   private TitledPane fontPane;
   private TitledPane colourPane;
   
   private Label jobNameFontLabel;
   private Label jobNameColourLabel;
   private Label buildNumberFontLabel;
   private Label buildNumberColourLabel;
   private Label completionEstimateFontLabel;
   private Label completionEstimateColourLabel;
   
   private Button jobNameFontButton;
   private Label jobNameQuickFoxLabel;
   private Button buildNumberFontButton;
   private Label buildNumberQuickFoxLabel;
   private Button completionEstimateFontButton;
   private Label completionEstimateQuickFoxLabel;
   
   private ColorPicker jobNameColourPicker;
   private ColorPicker buildNumberColourPicker;
   private ColorPicker completionEstimateColourPicker;
   
   /**
    * Constructs a new {@link BuildWallConfigurationPanelImpl}.
    * @param configuration the {@link BuildWallConfiguration} to configure.
    * @param fontSupplier the method of inputting a different {@link Font}.
    */
   public BuildWallConfigurationPanelImpl( BuildWallConfiguration configuration, Supplier< Font > fontSupplier ) {
      this.configuration = configuration;
      this.fontSupplier = fontSupplier;
      
      constructFontItemPane();
      constructColourItemPane();
      
      ColumnConstraints width = new ColumnConstraints();
      width.setPercentWidth( 100 );
      width.setHgrow( Priority.ALWAYS );
      getColumnConstraints().addAll( width );
   }//End Constructor
   
   /**
    * Method to construct a {@link TitledPane} for the {@link Font}s.
    */
   private void constructFontItemPane(){
      GridPane fontContent = new GridPane();
      
      jobNameFontLabel = createBoldLabel( "Job Name" );
      fontContent.add( jobNameFontLabel, 0, 0 );
      jobNameFontButton = new Button( configuration.jobNameFont().get().getName() );
      jobNameQuickFoxLabel = new Label( QUICK_FOX );
      configureFontButtonAndExample( 
               jobNameFontButton, 
               jobNameQuickFoxLabel, 
               configuration.jobNameFont() 
      );
      fontContent.add( jobNameFontButton, 1, 0 );
      fontContent.add( jobNameQuickFoxLabel, 0, 1 );

      buildNumberFontLabel = createBoldLabel( "Build Number" );
      fontContent.add( buildNumberFontLabel, 0, 2 );
      buildNumberFontButton = new Button( configuration.buildNumberFont().get().getName() );
      buildNumberQuickFoxLabel = new Label( QUICK_FOX );
      configureFontButtonAndExample( 
               buildNumberFontButton, 
               buildNumberQuickFoxLabel, 
               configuration.buildNumberFont() 
      );
      fontContent.add( buildNumberFontButton, 1, 2 );
      fontContent.add( buildNumberQuickFoxLabel, 0, 3 );

      completionEstimateFontLabel = createBoldLabel( "Build Time" );
      fontContent.add( completionEstimateFontLabel, 0, 4 );
      completionEstimateFontButton = new Button( configuration.completionEstimateFont().get().getName() );
      completionEstimateQuickFoxLabel = new Label( QUICK_FOX );
      configureFontButtonAndExample( 
               completionEstimateFontButton, 
               completionEstimateQuickFoxLabel, 
               configuration.completionEstimateFont() 
      );
      fontContent.add( completionEstimateFontButton, 1, 2 );
      fontContent.add( completionEstimateQuickFoxLabel, 0, 3 );
      
      configureColumnConstraints( fontContent );
      
      fontPane = new TitledPane( "Fonts", fontContent );
      add( fontPane, 0, 0 );
   }//End Method
   
   /**
    * Method to configure a {@link Font} configuring {@link Button}.
    * @param button the {@link Button} to configure.
    * @param quickFox the {@link Label} to update.
    * @param configProperty the property associated.
    */
   private void configureFontButtonAndExample( Button button, Label quickFox, ObjectProperty< Font > configProperty ){
      button.setMaxWidth( Double.MAX_VALUE );
      button.setOnAction( event -> configProperty.set( fontSupplier.get() ) );
      configProperty.addListener( ( source, old, updated ) -> updateFont( button, quickFox, updated ) );
      
      quickFox.setFont( configProperty.get() );
      quickFox.setPadding( new Insets( 10, 0, 10, 0 ) );
      GridPane.setColumnSpan( quickFox, 2 );
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
      add( colourPane, 0, 1 );
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
   private void updateFont( Button button, Label quickFox, Font newFont ) {
      if ( newFont == null ) return;
      
      button.setText( newFont.getName() );
      quickFox.setFont( newFont );
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

   Label jobNameQuickFoxLabel() {
      return jobNameQuickFoxLabel;
   }//End Method

   Button buildNumberFontButton() {
      return buildNumberFontButton;
   }//End Method
   
   Label buildNumberQuickFoxLabel() {
      return buildNumberQuickFoxLabel;
   }//End Method

   Button completionEstimateFontButton() {
      return completionEstimateFontButton;
   }//End Method

   Label completionEstimateQuickFoxLabel() {
      return completionEstimateQuickFoxLabel;
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

}//End Class
