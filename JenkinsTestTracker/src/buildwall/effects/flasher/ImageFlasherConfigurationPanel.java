/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.effects.flasher;

import java.io.File;

import buildwall.configuration.style.BuildWallConfigurationStyle;
import friendly.controlsfx.FriendlyFileChooser;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.spinner.DoublePropertySpinner;
import javafx.spinner.IntegerPropertySpinner;
import javafx.stage.FileChooser.ExtensionFilter;

/**
 * The {@link ImageFlasherConfigurationPanel} provides a graphical configuration panel
 * for the {@link ImageFlasherConfiguration}.
 */
public class ImageFlasherConfigurationPanel extends GridPane {
   
   static final String NUMBER_OF_FLASHES_LABEL = "Number of flashes";
   static final String FLASH_ON_LABEL = "Flash On (millis)";
   static final String FLASH_OFF_LABEL = "Flash Off (millis)";
   static final String TRANSPARENCY_LABEL = "Transparency";
   
   static final double IMAGE_BUTTON_GRAPHIC_HEIGHT = 50.0;
   static final double IMAGE_BUTTON_GRAPHIC_WIDTH = 100.0;
   static final String IMAGE_CHOOSER_TITLE = "Select Image";
   
   static final File USER_HOME_FILE = new File( System.getProperty( "user.home" ) );
   static final ExtensionFilter IMAGE_FILTER = new ExtensionFilter( "images only", "*.png", "*.jpg" );
   
   static final String IMAGE_LABEL_TEXT = "Choose Image";
   static final String SELECT_IMAGE_TEXT = "<click here>";
   static final double INSETS = 10;
   
   private final BuildWallConfigurationStyle styling;
   private final ImageFlasherConfiguration configuration;
   
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
   
//   private Button testEffectButton;
   
   /**
    * Constructs a new {@link ImageFlasherConfigurationPanel}.
    * @param configuration the {@link ImageFlasherConfiguration} to configure.
    */
   public ImageFlasherConfigurationPanel( ImageFlasherConfiguration configuration ) {
      this( configuration, new FriendlyFileChooser() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ImageFlasherConfigurationPanel}.
    * @param configuration the {@link ImageFlasherConfiguration} to configure.
    * @param fileChooser the {@link FriendlyFileChooser} to use.
    */
   ImageFlasherConfigurationPanel( ImageFlasherConfiguration configuration, FriendlyFileChooser fileChooser ) {
      this.configuration = configuration;
      this.imageChooser = fileChooser;
      this.styling = new BuildWallConfigurationStyle();
      
      provideImageConfiguration();
      provideNumberOfFlashes();
      provideFlashOn();
      provideFlashOff();
      provideTransparency();
      
//      testEffectButton = new Button( "Test Effect" );  
//      testEffectButton.setMaxWidth( Double.MAX_VALUE );
//      add( testEffectButton, 0, 5 );
      
      setPadding( new Insets( INSETS ) );
   }//End Constructor
   
   /**
    * Method to provide configuration components for the number of flashes property.
    */
   private void provideNumberOfFlashes() {
      numberOfFlashesLabel = styling.createBoldLabel( NUMBER_OF_FLASHES_LABEL );
      add( numberOfFlashesLabel, 0, 1 );
      
      numberOfFlashesSpinner = new IntegerPropertySpinner();  
      styling.configureIntegerSpinner( numberOfFlashesSpinner, configuration.numberOfFlashesProperty(), 1, 1000, 1 );
      add( numberOfFlashesSpinner, 1, 1 );
   }//End Method
   
   /**
    * Method to provide configuration components for the flash on property.
    */
   private void provideFlashOn() {
      flashOnLabel = styling.createBoldLabel( FLASH_ON_LABEL );
      add( flashOnLabel, 0, 2 );
      
      flashOnSpinner = new IntegerPropertySpinner();  
      styling.configureIntegerSpinner( flashOnSpinner, configuration.flashOnProperty(), 1, Integer.MAX_VALUE, 1000 );
      add( flashOnSpinner, 1, 2 );
   }//End Method

   /**
    * Method to provide configuration components for the flash off property.
    */
   private void provideFlashOff() {
      flashOffLabel = styling.createBoldLabel( FLASH_OFF_LABEL );
      add( flashOffLabel, 0, 3 );
      
      flashOffSpinner = new IntegerPropertySpinner();  
      styling.configureIntegerSpinner( flashOffSpinner, configuration.flashOffProperty(), 1, Integer.MAX_VALUE, 500 );
      add( flashOffSpinner, 1, 3 );
   }//End Method
   
   /**
    * Method to provide configuration components for the transparency property.
    */
   private void provideTransparency() {
      transparencyLabel = styling.createBoldLabel( TRANSPARENCY_LABEL );
      add( transparencyLabel, 0, 4 );
      
      transparencySpinner = new DoublePropertySpinner();  
      styling.configureDoubleSpinner( transparencySpinner, configuration.transparencyProperty(), 0.0, 1.0, 0.05 );
      add( transparencySpinner, 1, 4 );
   }//End Method

   /**
    * Method to provide configuration components for the image property.
    */
   private void provideImageConfiguration() {
      imageLabel = styling.createBoldLabel( IMAGE_LABEL_TEXT );
      add( imageLabel, 0, 0 );
      
      imageChooser.setTitle( IMAGE_CHOOSER_TITLE );
      imageChooser.setInitialDirectory( USER_HOME_FILE );
      imageChooser.getExtensionFilters().add( IMAGE_FILTER ); 
      
      imageButtonGraphic = new ImageView();
      imageButtonGraphic.setFitWidth( IMAGE_BUTTON_GRAPHIC_WIDTH );
      imageButtonGraphic.setFitHeight( IMAGE_BUTTON_GRAPHIC_HEIGHT );
      
      imageChooserButton = new Button( SELECT_IMAGE_TEXT );
      imageChooserButton.setGraphic( imageButtonGraphic );
      imageChooserButton.setMaxWidth( Double.MAX_VALUE );
      imageChooserButton.setOnAction( event -> handleImageSelection() );
      add( imageChooserButton, 1, 0 );
   }//End Method
   
   /**
    * Method to handle the selection of an {@link Image} by the user.
    */
   private void handleImageSelection(){
      File file = imageChooser.showOpenDialog( null );
      if ( file == null ) return;
      
      Image image = new Image( file.toURI().toString() );
      imageButtonGraphic.setImage( image );
      imageChooserButton.setText( null );
      configuration.imageProperty().set( image );
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
}//End Class
