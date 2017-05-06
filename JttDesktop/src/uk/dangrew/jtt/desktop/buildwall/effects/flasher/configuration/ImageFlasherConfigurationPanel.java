/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.flasher.configuration;

import java.io.File;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser.ExtensionFilter;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.ImageFlasherProperties;
import uk.dangrew.jtt.desktop.friendly.controlsfx.FriendlyFileChooser;
import uk.dangrew.jtt.desktop.javafx.spinner.DoublePropertySpinner;
import uk.dangrew.jtt.desktop.javafx.spinner.IntegerPropertySpinner;

/**
 * The {@link ImageFlasherConfigurationPanel} provides a graphical configuration panel
 * for the {@link ImageFlasherConfiguration}.
 */
public class ImageFlasherConfigurationPanel extends GridPane {

   static final String NUMBER_OF_FLASHES_LABEL = "Number of flashes";
   static final String FLASH_ON_LABEL = "Flash On (millis)";
   static final String FLASH_OFF_LABEL = "Flash Off (millis)";
   static final String TRANSPARENCY_LABEL = "Transparency";
   static final String STOP_FLASH_LABEL = "Stop Flash";
   static final String TEST_FLASH_LABEL = "Test Flash";
   
   static final double IMAGE_BUTTON_GRAPHIC_HEIGHT = 50.0;
   static final double IMAGE_BUTTON_GRAPHIC_WIDTH = 100.0;
   static final String IMAGE_CHOOSER_TITLE = "Select Image";
   
   static final ExtensionFilter IMAGE_FILTER = new ExtensionFilter( "images only", "*.png", "*.jpg" );
   
   static final String IMAGE_LABEL_TEXT = "Choose Image";
   static final String SELECT_IMAGE_TEXT = "<click here>";
   static final double INSETS = 10;
   
   private final JavaFxStyle styling;
   private final ImageFlasherProperties properties;
   
   private Label titleLabel;
   
   private Label imageLabel;
   private Button imageChooserButton;
   private ImageView imageButtonGraphic;
   private FriendlyFileChooser imageChooser;
   
   private Label numberOfFlashesLabel;
   private IntegerPropertySpinner numberOfFlashesSpinner;
   
   private Label flashOnLabel;
   private IntegerPropertySpinner flashOnSpinner;
   
   private Label flashOffLabel;
   private IntegerPropertySpinner flashOffSpinner;
   
   private Label transparencyLabel;
   private DoublePropertySpinner transparencySpinner;
   
   private Button testFlashButton;
   private Button stopFlashButton;
   
   /**
    * Constructs a new {@link ImageFlasherConfigurationPanel}.
    * @param title the title of the configuration pane.
    * @param configuration the {@link ImageFlasherProperties} to configure.
    */
   public ImageFlasherConfigurationPanel( String title, ImageFlasherProperties properties ) {
      this( new JavaFxStyle(), title, properties, new FriendlyFileChooser() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ImageFlasherConfigurationPanel}.
    * @param styling the {@link JavaFxStyle}.
    * @param title the title for the configuration pane.
    * @param configuration the {@link ImageFlasherProperties} to configure.
    * @param fileChooser the {@link FriendlyFileChooser} to use.
    */
   ImageFlasherConfigurationPanel( JavaFxStyle styling, String title, ImageFlasherProperties properties, FriendlyFileChooser fileChooser ) {
      this.properties = properties;
      this.imageChooser = fileChooser;
      this.styling = styling;
      
      provideTitle( title );
      provideImageConfiguration();
      provideNumberOfFlashes();
      provideFlashOn();
      provideFlashOff();
      provideTransparency();
      provideTestMechanism();
      
      styling.configureColumnConstraints( this );
      setPadding( new Insets( INSETS ) );
   }//End Constructor
   
   /**
    * Method to construct the title for the panel.
    * @param title the title to use.
    */
   private void provideTitle( String title ) {
      titleLabel = styling.constructTitle( title );
      add( titleLabel, 0, 0 );
   }//End Method

   /**
    * Method to provide configuration components for the image property.
    */
   private void provideImageConfiguration() {
      imageLabel = styling.createBoldLabel( IMAGE_LABEL_TEXT );
      add( imageLabel, 0, 1 );
      
      styling.configureFileChooser( imageChooser, IMAGE_CHOOSER_TITLE );
      imageChooser.getExtensionFilters().add( IMAGE_FILTER );
      
      imageButtonGraphic = new ImageView();
      imageButtonGraphic.setFitWidth( IMAGE_BUTTON_GRAPHIC_WIDTH );
      imageButtonGraphic.setFitHeight( IMAGE_BUTTON_GRAPHIC_HEIGHT );
      
      imageChooserButton = new Button( SELECT_IMAGE_TEXT );
      imageChooserButton.setMaxWidth( Double.MAX_VALUE );
      imageChooserButton.setOnAction( event -> handleImageSelection() );
      add( imageChooserButton, 1, 1 );
   }//End Method
   
   /**
    * Method to provide configuration components for the number of flashes property.
    */
   private void provideNumberOfFlashes() {
      numberOfFlashesLabel = styling.createBoldLabel( NUMBER_OF_FLASHES_LABEL );
      add( numberOfFlashesLabel, 0, 2 );
      
      numberOfFlashesSpinner = new IntegerPropertySpinner();  
      styling.configureIntegerSpinner( numberOfFlashesSpinner, properties.numberOfFlashesProperty(), 1, 1000, 1 );
      add( numberOfFlashesSpinner, 1, 2 );
   }//End Method
   
   /**
    * Method to provide configuration components for the flash on property.
    */
   private void provideFlashOn() {
      flashOnLabel = styling.createBoldLabel( FLASH_ON_LABEL );
      add( flashOnLabel, 0, 3 );
      
      flashOnSpinner = new IntegerPropertySpinner();  
      styling.configureIntegerSpinner( flashOnSpinner, properties.flashOnProperty(), 1, Integer.MAX_VALUE, 1000 );
      add( flashOnSpinner, 1, 3 );
   }//End Method

   /**
    * Method to provide configuration components for the flash off property.
    */
   private void provideFlashOff() {
      flashOffLabel = styling.createBoldLabel( FLASH_OFF_LABEL );
      add( flashOffLabel, 0, 4 );
      
      flashOffSpinner = new IntegerPropertySpinner();  
      styling.configureIntegerSpinner( flashOffSpinner, properties.flashOffProperty(), 1, Integer.MAX_VALUE, 500 );
      add( flashOffSpinner, 1, 4 );
   }//End Method
   
   /**
    * Method to provide configuration components for the transparency property.
    */
   private void provideTransparency() {
      transparencyLabel = styling.createBoldLabel( TRANSPARENCY_LABEL );
      add( transparencyLabel, 0, 5 );
      
      transparencySpinner = new DoublePropertySpinner();  
      styling.configureDoubleSpinner( transparencySpinner, properties.transparencyProperty(), 0.0, 1.0, 0.05 );
      add( transparencySpinner, 1, 5 );
   }//End Method
   
   /**
    * Method to handle the selection of an {@link Image} by the user.
    */
   private void handleImageSelection(){
      File file = imageChooser.showOpenDialog( null );
      if ( file == null ) return;
      
      Image image = new Image( file.toURI().toString() );
      imageChooserButton.setGraphic( imageButtonGraphic );
      imageButtonGraphic.setImage( image );
      imageChooserButton.setText( null );
      properties.imageProperty().set( image );
   }//End Method
   
   /**
    * Method to provide the components for testing the image flashing.
    */
   private void provideTestMechanism(){
      testFlashButton = new Button( TEST_FLASH_LABEL );
      testFlashButton.setMaxWidth( Double.MAX_VALUE );
      testFlashButton.setOnAction( event -> properties.flashingSwitch().set( true ) );
      add( testFlashButton, 0, 6 );
      
      stopFlashButton = new Button( STOP_FLASH_LABEL );
      stopFlashButton.setMaxWidth( Double.MAX_VALUE );
      stopFlashButton.setOnAction( event -> properties.flashingSwitch().set( false ) );
      add( stopFlashButton, 1, 6 );
      
      properties.flashingSwitch().addListener( this::updateTestButtons );
      updateTestButtons( properties.flashingSwitch(), false, false );
   }//End Method
   
   /**
    * Method to update the test button when the flashing switch changes.
    * @param source the property observed.
    * @param old the old value.
    * @param updated the updated value.
    */
   private void updateTestButtons( ObservableValue< ? extends Boolean > source, Boolean old, Boolean updated ) {
      if ( updated ) {
         testFlashButton.setDisable( true );
         stopFlashButton.setDisable( false );
      } else {
         testFlashButton.setDisable( false );
         stopFlashButton.setDisable( true );
      }
   }//End Method
   
   Label imageLabel() {
      return imageLabel;
   }//End Method

   Button imageButton() {
      return imageChooserButton;
   }//End Method
   
   ImageView imageButtonGraphic(){
      return imageButtonGraphic;
   }//End Method

   Label numberOfFlashesLabel() {
      return numberOfFlashesLabel;
   }//End Method
   
   IntegerPropertySpinner numberOfFlashesSpinner() {
      return numberOfFlashesSpinner;
   }//End Method

   Label flashOnLabel() {
      return flashOnLabel;
   }//End Method

   IntegerPropertySpinner flashOnSpinner() {
      return flashOnSpinner;
   }//End Method

   Label flashOffLabel() {
      return flashOffLabel;
   }//End Method

   IntegerPropertySpinner flashOffSpinner() {
      return flashOffSpinner;
   }//End Method
   
   Label transparencyLabel() {
      return transparencyLabel;
   }//End Method

   DoublePropertySpinner transparencySpinner() {
      return transparencySpinner;
   }//End Method

   Button testFlashButton() {
      return testFlashButton;
   }//End Method

   Button stopTestFlashButton() {
      return stopFlashButton;
   }//End Method

   Label titleLabel() {
      return titleLabel;
   }//End Method
}//End Class
