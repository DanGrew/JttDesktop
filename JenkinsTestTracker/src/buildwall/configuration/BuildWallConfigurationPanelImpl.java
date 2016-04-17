/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import buildwall.configuration.components.JobPolicyPanel;
import buildwall.configuration.style.BuildWallConfigurationStyle;
import buildwall.panel.type.JobPanelDescriptionProvider;
import buildwall.panel.type.JobPanelDescriptionProviders;
import javafx.beans.property.ObjectProperty;
import javafx.combobox.FontFamilyPropertyBox;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.spinner.DefensiveIntegerSpinnerValueFactory;
import javafx.spinner.IntegerPropertySpinner;
import javafx.spinner.PropertySpinner;

/**
 * The {@link BuildWallConfigurationPanelImpl} provides a {@link GridPane} for configuring
 * the {@link BuildWallConfiguration}.
 */
public class BuildWallConfigurationPanelImpl extends GridPane {
   
   static final int MAXIMUM_FONT_SIZE = 500;
   static final int MINIMUM_FONT_SIZE = 1;
   static final int MINIMUM_COLUMNS = 1;
   static final int MAXIMUM_COLUMNS = 1000;
   
   private BuildWallConfigurationStyle styling;
   private BuildWallConfiguration configuration;
   
   private Label titleLabel;
   
   private TitledPane dimensionsPane;
   private TitledPane jobPoliciesPane;
   private TitledPane fontPane;
   private TitledPane colourPane;
   
   private Label columnsLabel;
   private IntegerPropertySpinner columnsSpinner;
   
   private Label descriptionTypeLabel;
   private RadioButton simpleDescriptionButton;
   private RadioButton defaultDescriptionButton;
   private RadioButton detailedDescriptionButton;
   
   private Label jobNameFontLabel;
   private Label buildNumberFontLabel;
   private Label completionEstimateFontLabel;
   private Label culpritsFontLabel;
   
   private Label jobNameColourLabel;
   private Label buildNumberColourLabel;
   private Label completionEstimateColourLabel;
   private Label culpritsColourLabel;
   
   private Label jobNameFontSizeLabel;
   private Label buildNumberFontSizeLabel;
   private Label completionEstimateFontSizeLabel;
   private Label culpritsFontSizeLabel;
   
   private FontFamilyPropertyBox jobNameFontBox;
   private FontFamilyPropertyBox buildNumberFontBox;
   private FontFamilyPropertyBox completionEstimateFontBox;
   private FontFamilyPropertyBox culpritsFontBox;
   
   private PropertySpinner< Integer, Font > jobNameFontSizeSpinner;
   private PropertySpinner< Integer, Font > buildNumberFontSizeSpinner;
   private PropertySpinner< Integer, Font > completionEstimateFontSizeSpinner;
   private PropertySpinner< Integer, Font > culpritsFontSizeSpinner;
   
   private ColorPicker jobNameColourPicker;
   private ColorPicker buildNumberColourPicker;
   private ColorPicker completionEstimateColourPicker;
   private ColorPicker culpritsColourPicker;
   
   /**
    * Constructs a new {@link BuildWallConfigurationPanelImpl}.
    * @param configuration the {@link BuildWallConfiguration} to configure.
    * @param title the title of the panel.
    */
   public BuildWallConfigurationPanelImpl( String title, BuildWallConfiguration configuration ) {
      this.configuration = configuration;
      this.styling = new BuildWallConfigurationStyle();
      
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
      GridPane dimensionsContent = new GridPane();
      
      columnsLabel = styling.createBoldLabel( "Columns" );
      dimensionsContent.add( columnsLabel, 0, 0 );
      
      columnsSpinner = new IntegerPropertySpinner();  
      styling.configureIntegerSpinner( columnsSpinner, configuration.numberOfColumns(), 1, 1000, 1 );
      dimensionsContent.add( columnsSpinner, 1, 0 );
      
      final ToggleGroup descriptionToggles = new ToggleGroup();
      
      descriptionTypeLabel = styling.createBoldLabel( "Description Type" );
      dimensionsContent.add( descriptionTypeLabel, 0, 1 );
      
      simpleDescriptionButton = configureRadioButton( "Simple Description", JobPanelDescriptionProviders.Simple, descriptionToggles );
      dimensionsContent.add( simpleDescriptionButton, 1, 1 );
      
      defaultDescriptionButton = configureRadioButton( "Default Description", JobPanelDescriptionProviders.Default, descriptionToggles );
      dimensionsContent.add( defaultDescriptionButton, 1, 2 );
      
      detailedDescriptionButton = configureRadioButton( "Detailed Description", JobPanelDescriptionProviders.Detailed, descriptionToggles );
      dimensionsContent.add( detailedDescriptionButton, 1, 3 );
      
      updateDescriptionTypeButton( configuration.jobPanelDescriptionProvider().get() );
      configuration.jobPanelDescriptionProvider().addListener( 
               ( source, old, updated ) -> updateDescriptionTypeButton( updated ) 
      );

      styling.configureColumnConstraints( dimensionsContent );
      
      dimensionsPane = new TitledPane( "Dimensions", dimensionsContent );
      dimensionsPane.setExpanded( true );
      add( dimensionsPane, 0, 1 );
   }//End Method
   
   /**
    * Method to construct the {@link JobPolicyPanel} and add it to this {@link GridPane}.
    */
   private void constructJobPoliciesPane(){
      GridPane policiesContent = new JobPolicyPanel( configuration );
      styling.configureColumnConstraints( policiesContent );
      
      jobPoliciesPane = new TitledPane( "Job Policies", policiesContent );
      jobPoliciesPane.setExpanded( false );
      add( jobPoliciesPane, 0, 2 );
   }//End Method
   
   /**
    * Method to construct a {@link TitledPane} for the {@link Font}s.
    */
   private void constructFontItemPane(){
      GridPane fontContent = new GridPane();
      
      jobNameFontLabel = styling.createBoldLabel( "Job Name Font" );
      fontContent.add( jobNameFontLabel, 0, 0 );
      jobNameFontBox = new FontFamilyPropertyBox( configuration.jobNameFont() );
      jobNameFontBox.setMaxWidth( Double.MAX_VALUE );
      fontContent.add( jobNameFontBox, 1, 0 );
      
      jobNameFontSizeLabel = styling.createBoldLabel( "Job Name Size" );
      fontContent.add( jobNameFontSizeLabel, 0, 2 );
      jobNameFontSizeSpinner = new PropertySpinner<>();  
      configureFontSizeSpinner( jobNameFontSizeSpinner, configuration.jobNameFont() );
      fontContent.add( jobNameFontSizeSpinner, 1, 2 );

      buildNumberFontLabel = styling.createBoldLabel( "Build Number Font" );
      fontContent.add( buildNumberFontLabel, 0, 4 );
      buildNumberFontBox = new FontFamilyPropertyBox( configuration.buildNumberFont() );
      buildNumberFontBox.setMaxWidth( Double.MAX_VALUE );
      fontContent.add( buildNumberFontBox, 1, 4 );
      
      buildNumberFontSizeLabel = styling.createBoldLabel( "Build Number Size" );
      fontContent.add( buildNumberFontSizeLabel, 0, 6 );
      buildNumberFontSizeSpinner = new PropertySpinner<>();  
      configureFontSizeSpinner( buildNumberFontSizeSpinner, configuration.buildNumberFont() );
      fontContent.add( buildNumberFontSizeSpinner, 1, 6 );

      completionEstimateFontLabel = styling.createBoldLabel( "Build Time Font" );
      fontContent.add( completionEstimateFontLabel, 0, 8 );
      completionEstimateFontBox = new FontFamilyPropertyBox( configuration.completionEstimateFont() );
      completionEstimateFontBox.setMaxWidth( Double.MAX_VALUE );
      fontContent.add( completionEstimateFontBox, 1, 8 );
      
      completionEstimateFontSizeLabel = styling.createBoldLabel( "Build Time Size" );
      fontContent.add( completionEstimateFontSizeLabel, 0, 10 );
      completionEstimateFontSizeSpinner = new PropertySpinner<>();  
      configureFontSizeSpinner( completionEstimateFontSizeSpinner, configuration.completionEstimateFont() );
      fontContent.add( completionEstimateFontSizeSpinner, 1, 10 );
      
      culpritsFontLabel = styling.createBoldLabel( "Culprits Font" );
      fontContent.add( culpritsFontLabel, 0, 12 );
      culpritsFontBox = new FontFamilyPropertyBox( configuration.culpritsFont() );
      culpritsFontBox.setMaxWidth( Double.MAX_VALUE );
      fontContent.add( culpritsFontBox, 1, 12 );
      
      culpritsFontSizeLabel = styling.createBoldLabel( "Culprits Size" );
      fontContent.add( culpritsFontSizeLabel, 0, 14 );
      culpritsFontSizeSpinner = new PropertySpinner<>();  
      configureFontSizeSpinner( culpritsFontSizeSpinner, configuration.culpritsFont() );
      fontContent.add( culpritsFontSizeSpinner, 1, 14 );
      
      styling.configureColumnConstraints( fontContent );
      
      fontPane = new TitledPane( "Fonts", fontContent );
      fontPane.setExpanded( true );
      add( fontPane, 0, 3 );
   }//End Method
   
   /**
    * Method to construct the {@link TitledPane} for configuring {@link Color}s.
    */
   private void constructColourItemPane() {
      GridPane content = new GridPane();
      
      jobNameColourLabel = styling.createBoldLabel( "Job Name" );
      content.add( jobNameColourLabel, 0, 0 );
      
      jobNameColourPicker = new ColorPicker();
      configureColorPicker( jobNameColourPicker, configuration.jobNameColour() );
      content.add( jobNameColourPicker, 1, 0 );
      
      buildNumberColourLabel = styling.createBoldLabel( "Build Number" );
      content.add( buildNumberColourLabel, 0, 1 );
      
      buildNumberColourPicker = new ColorPicker();
      configureColorPicker( buildNumberColourPicker, configuration.buildNumberColour() );
      content.add( buildNumberColourPicker, 1, 1 );
      
      completionEstimateColourLabel = styling.createBoldLabel( "Build Time" );
      content.add( completionEstimateColourLabel, 0, 2 );
      
      completionEstimateColourPicker = new ColorPicker();
      configureColorPicker( completionEstimateColourPicker, configuration.completionEstimateColour() );
      content.add( completionEstimateColourPicker, 1, 2 );
      
      culpritsColourLabel = styling.createBoldLabel( "Culprits" );
      content.add( culpritsColourLabel, 0, 3 );
      
      culpritsColourPicker = new ColorPicker();
      configureColorPicker( culpritsColourPicker, configuration.culpritsColour() );
      content.add( culpritsColourPicker, 1, 3 );
      
      styling.configureColumnConstraints( content );
      
      colourPane = new TitledPane( "Colours", content );
      colourPane.setExpanded( true );
      add( colourPane, 0, 4 );
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
    * Method to configure a {@link Font} {@link PropertySpinner}.
    * @param spinner the {@link PropertySpinner} to configure.
    * @param property the {@link ObjectProperty} to bind to.
    */
   private void configureFontSizeSpinner( PropertySpinner< Integer, Font> spinner, ObjectProperty< Font > property ){
      spinner.setValueFactory( new DefensiveIntegerSpinnerValueFactory( MINIMUM_FONT_SIZE, MAXIMUM_FONT_SIZE ) );
      spinner.bindProperty( 
               property,
               size -> Font.font( property.get().getFamily(), size ),
               font -> ( int )font.getSize()
      );
      spinner.setMaxWidth( Double.MAX_VALUE );
      spinner.setEditable( true );
   }//End Method
   
   /**
    * Method to configure the {@link RadioButton}s for this panel.
    * @param name the name on the button.
    * @param provider the {@link JobPanelDescriptionProviders} the button is for.
    * @param descriptionToggles the {@link ToggleGroup} for the {@link RadioButton}s.
    * @return the {@link RadioButton} constructed.
    */
   private RadioButton configureRadioButton( String name, JobPanelDescriptionProviders provider, ToggleGroup descriptionToggles ) {
      RadioButton descriptionButton = new RadioButton( name );
      descriptionButton.setToggleGroup( descriptionToggles );
      descriptionButton.setOnAction( 
               event -> configuration.jobPanelDescriptionProvider().set( provider ) 
      );
      return descriptionButton;
   }//End Method
   
   /**
    * Method to update the {@link RadioButton}s for the {@link JobPanelDescriptionProviders}.
    * @param provider the {@link JobPanelDescriptionProvider} set in the {@link BuildWallConfiguration}.
    */
   private void updateDescriptionTypeButton( JobPanelDescriptionProvider provider ){
      simpleDescriptionButton.setSelected( JobPanelDescriptionProviders.Simple.equals( provider )  );
      defaultDescriptionButton.setSelected( JobPanelDescriptionProviders.Default.equals( provider )  );
      detailedDescriptionButton.setSelected( JobPanelDescriptionProviders.Detailed.equals( provider )  );
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
   
   ComboBox< String > culpritsFontBox() {
      return culpritsFontBox;
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
   
   ColorPicker culpritsColourPicker() {
      return culpritsColourPicker;
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
   
   Labeled culpritsFontLabel() {
      return culpritsFontLabel;
   }//End Method

   Labeled culpritsColourLabel() {
      return culpritsColourLabel;
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
   
   PropertySpinner< Integer, Font > culpritsFontSizeSpinner() {
      return culpritsFontSizeSpinner;
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
   
   Label culpritsFontSizeLabel() {
      return culpritsFontSizeLabel;
   }//End Method

   TitledPane jobPoliciesPane() {
      return jobPoliciesPane;
   }//End Method

   RadioButton simpleDescriptionButton() {
      return simpleDescriptionButton;
   }//End Method

   RadioButton defaultDescriptionButton() {
      return defaultDescriptionButton;
   }//End Method
   
   RadioButton detailedDescriptionButton() {
      return detailedDescriptionButton;
   }//End Method

   Label desriptionTypeLabel() {
      return descriptionTypeLabel;
   }//End Method

   Label titleLabel() {
      return titleLabel;
   }//End Method

}//End Class
