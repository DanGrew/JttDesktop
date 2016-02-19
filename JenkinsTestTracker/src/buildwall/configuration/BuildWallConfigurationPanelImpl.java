/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.combobox.FontFamilyPropertyBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
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
import javafx.spinner.IntegerPropertySpinner;
import javafx.spinner.PropertySpinner;

/**
 * The {@link BuildWallConfigurationPanelImpl} provides a {@link GridPane} for configuring
 * the {@link BuildWallConfiguration}.
 */
public class BuildWallConfigurationPanelImpl extends GridPane {
   
   static final int MAXIMUM_FONT_SIZE = 500;
   static final int MINIMUM_FONT_SIZE = 1;
   static final double LABEL_PERCENTAGE_WIDTH = 40;
   static final double CONTROLS_PERCENTAGE_WIDTH = 60;
   static final int MINIMUM_COLUMNS = 1;
   static final int MAXIMUM_COLUMNS = 1000;
   private BuildWallConfiguration configuration;
   
   private TitledPane dimensionsPane;
   private TitledPane fontPane;
   private TitledPane colourPane;
   
   private Label columnsLabel;
   private IntegerPropertySpinner columnsSpinner;
   
   private Label jobNameFontLabel;
   private Label buildNumberFontLabel;
   private Label completionEstimateFontLabel;
   
   private Label jobNameColourLabel;
   private Label buildNumberColourLabel;
   private Label completionEstimateColourLabel;
   
   private Label jobNameFontSizeLabel;
   private Label buildNumberFontSizeLabel;
   private Label completionEstimateFontSizeLabel;
   
   private FontFamilyPropertyBox jobNameFontBox;
   private FontFamilyPropertyBox buildNumberFontBox;
   private FontFamilyPropertyBox completionEstimateFontBox;
   
   private PropertySpinner< Integer, Font > jobNameFontSizeSpinner;
   private PropertySpinner< Integer, Font > buildNumberFontSizeSpinner;
   private PropertySpinner< Integer, Font > completionEstimateFontSizeSpinner;
   
   private ColorPicker jobNameColourPicker;
   private ColorPicker buildNumberColourPicker;
   private ColorPicker completionEstimateColourPicker;
   
   /**
    * Constructs a new {@link BuildWallConfigurationPanelImpl}.
    * @param configuration the {@link BuildWallConfiguration} to configure.
    */
   public BuildWallConfigurationPanelImpl( BuildWallConfiguration configuration ) {
      this.configuration = configuration;
      
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
      
      columnsSpinner = new IntegerPropertySpinner();  
      configureIntegerSpinner( columnsSpinner, configuration.numberOfColumns(), 1, 1000 );
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
      
      jobNameFontLabel = createBoldLabel( "Job Name Font" );
      fontContent.add( jobNameFontLabel, 0, 0 );
      jobNameFontBox = new FontFamilyPropertyBox( configuration.jobNameFont() );
      jobNameFontBox.setMaxWidth( Double.MAX_VALUE );
      fontContent.add( jobNameFontBox, 1, 0 );
      
      jobNameFontSizeLabel = createBoldLabel( "Job Name Size" );
      fontContent.add( jobNameFontSizeLabel, 0, 2 );
      jobNameFontSizeSpinner = new PropertySpinner<>();  
      configureFontSizeSpinner( jobNameFontSizeSpinner, configuration.jobNameFont() );
      fontContent.add( jobNameFontSizeSpinner, 1, 2 );

      buildNumberFontLabel = createBoldLabel( "Build Number Font" );
      fontContent.add( buildNumberFontLabel, 0, 4 );
      buildNumberFontBox = new FontFamilyPropertyBox( configuration.buildNumberFont() );
      buildNumberFontBox.setMaxWidth( Double.MAX_VALUE );
      fontContent.add( buildNumberFontBox, 1, 4 );
      
      buildNumberFontSizeLabel = createBoldLabel( "Build Number Size" );
      fontContent.add( buildNumberFontSizeLabel, 0, 6 );
      buildNumberFontSizeSpinner = new PropertySpinner<>();  
      configureFontSizeSpinner( buildNumberFontSizeSpinner, configuration.buildNumberFont() );
      fontContent.add( buildNumberFontSizeSpinner, 1, 6 );

      completionEstimateFontLabel = createBoldLabel( "Build Time Font" );
      fontContent.add( completionEstimateFontLabel, 0, 8 );
      completionEstimateFontBox = new FontFamilyPropertyBox( configuration.completionEstimateFont() );
      completionEstimateFontBox.setMaxWidth( Double.MAX_VALUE );
      fontContent.add( completionEstimateFontBox, 1, 8 );
      
      completionEstimateFontSizeLabel = createBoldLabel( "Build Time Size" );
      fontContent.add( completionEstimateFontSizeLabel, 0, 10 );
      completionEstimateFontSizeSpinner = new PropertySpinner<>();  
      configureFontSizeSpinner( completionEstimateFontSizeSpinner, configuration.completionEstimateFont() );
      fontContent.add( completionEstimateFontSizeSpinner, 1, 10 );
      
      configureColumnConstraints( fontContent );
      
      fontPane = new TitledPane( "Fonts", fontContent );
      add( fontPane, 0, 1 );
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
   
   /**
    * Method to configure an {@link IntegerPropertySpinner} for the given property and range.
    * @param spinner the {@link IntegerPropertySpinner} to configure.
    * @param property the {@link IntegerProperty} to bind to.
    * @param min the min range.
    * @param max the max range.
    */
   private void configureIntegerSpinner( IntegerPropertySpinner spinner, IntegerProperty property, int min, int max ){
      spinner.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory( min, max, property.get() ) );
      spinner.bindProperty( property );
      spinner.setMaxWidth( Double.MAX_VALUE );
   }//End Method
   
   /**
    * Method to configure a {@link Font} {@link PropertySpinner}.
    * @param spinner the {@link PropertySpinner} to configure.
    * @param property the {@link ObjectProperty} to bind to.
    */
   private void configureFontSizeSpinner( PropertySpinner< Integer, Font> spinner, ObjectProperty< Font > property ){
      spinner.setValueFactory( new SpinnerValueFactory.IntegerSpinnerValueFactory( MINIMUM_FONT_SIZE, MAXIMUM_FONT_SIZE, 13 ) );
      spinner.bindProperty( 
               property,
               size -> { return Font.font( property.get().getFamily(), size ); },
               font -> { return ( int )font.getSize(); }
      );
      spinner.setMaxWidth( Double.MAX_VALUE );
   }//End Method
   
   ComboBox< String > jobNameFontBox() {
      return jobNameFontBox;
   }//End Method

   ComboBox< String > buildNumberFontBox() {
      return buildNumberFontBox;
   }//End Method
   
   ComboBox< String > completionEstimateFontBox() {
      return completionEstimateFontBox;
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

   PropertySpinner< Integer, Font > jobNameFontSizeSpinner() {
      return jobNameFontSizeSpinner;
   }//End Method
   
   PropertySpinner< Integer, Font > buildNumberFontSizeSpinner() {
      return buildNumberFontSizeSpinner;
   }//End Method
   
   PropertySpinner< Integer, Font > completionEstimateFontSizeSpinner() {
      return completionEstimateFontSizeSpinner;
   }//End Method

   Label jobNameFontSizeLabel() {
      return jobNameFontSizeLabel;
   }//End Method
   
   Label buildNumberFontSizeLabel() {
      return buildNumberFontSizeLabel;
   }//End Method
   
   Label completionEstimateFontSizeLabel() {
      return completionEstimateFontSizeLabel;
   }//End Method

}//End Class
