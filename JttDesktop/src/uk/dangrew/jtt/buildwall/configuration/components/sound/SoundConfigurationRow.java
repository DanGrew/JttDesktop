/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components.sound;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.buildwall.effects.sound.StringMediaConverter;
import uk.dangrew.jtt.friendly.controlsfx.FriendlyFileChooser;
import uk.dangrew.jtt.friendly.javafx.FriendlyMediaPlayer;

/**
 * The {@link SoundConfigurationRow} provides the controls for configuring a sound.
 */
class SoundConfigurationRow extends GridPane {

   static final int LABEL_WIDTH = 40;
   static final int CHOOSER_WIDTH = 40;
   static final int TEST_WIDTH = 10;
   static final int CLEAR_WIDTH = 10;
   
   static final String CLEAR_TEXT = "Clear";
   static final String APPLY_TEXT = "Apply";
   static final String TEST_TEXT = "Test";
   static final String FILE_CHOOSER_TITLE = "Choose Sound";
   static final String CHOOSE_SOUND_TEXT = "Choose sound...";
   static final String INVALID_SOUND_TEXT = "Invalid sound...";
   static final Color ERROR_COLOUR = Color.RED;

   private final StringMediaConverter converter;
   private final FriendlyFileChooser fileChooser;
   private final SoundConfigurationApplier configurationApplier;
   private String selectedFile;
   
   private final Label label;
   private final Button soundChooserButton;
   private final Button testButton;
   private final Button clearButton;
   
   /**
    * Constructs a new {@link SoundConfigurationRow}.
    * @param labelText the text for the label.
    * @param configurationApplier the {@link SoundConfigurationApplier} to call when a sound is selected.
    */
   public SoundConfigurationRow( String labelText, SoundConfigurationApplier configurationApplier ) {
      this( new JavaFxStyle(), new FriendlyFileChooser(), new StringMediaConverter(), labelText, configurationApplier );
   }//End Constructor
   
   /**
    * Constructs a new {@link SoundConfigurationRow}.
    * @param styling the {@link JavaFxStyle}.
    * @param fileChooser the {@link FriendlyFileChooser}.
    * @param converter the {@link StringMediaConverter}.
    * @param labelText the text for the label.
    * @param configurationApplier the {@link SoundConfigurationApplier} to call when a sound is selected.
    */
   SoundConfigurationRow( 
            JavaFxStyle styling, 
            FriendlyFileChooser fileChooser, 
            StringMediaConverter converter, 
            String labelText, 
            SoundConfigurationApplier configurationApplier 
   ) {
      styling.configureConstraintsForColumnPercentages( this, LABEL_WIDTH, CHOOSER_WIDTH, TEST_WIDTH, CLEAR_WIDTH );
      this.fileChooser = fileChooser;
      styling.configureFileChooser( fileChooser, FILE_CHOOSER_TITLE );
      this.converter = converter;
      this.configurationApplier = configurationApplier;
      
      this.label = styling.createBoldLabel( labelText );
      this.add( label, 0, 0 );
      
      this.soundChooserButton = new Button( CHOOSE_SOUND_TEXT );
      this.soundChooserButton.setMaxWidth( Double.MAX_VALUE );
      this.soundChooserButton.setOnAction( this::selectSoundFile );
      this.add( soundChooserButton, 1, 0 );
      
      this.testButton = new Button( TEST_TEXT );
      this.testButton.setMaxWidth( Double.MAX_VALUE );
      this.testButton.setOnAction( this::testSoundFile );
      this.add( testButton, 2, 0 );
      
      this.clearButton = new Button( CLEAR_TEXT );
      this.clearButton.setMaxWidth( Double.MAX_VALUE );
      this.clearButton.setOnAction( this::clearSelected );
      this.add( clearButton, 3, 0 );
   }//End Constructor
   
   /**
    * Method to handle the selection of a file.
    * @param event the {@link ActionEvent} - method reference.
    */
   private void selectSoundFile( ActionEvent event ) {
      File file = fileChooser.showOpenDialog( null );
      if ( file == null ) {
         return;
      }
      updateSelectedFile( file.getPath() );
   }//End Method
      
   /**
    * Method to update the selected file.
    * @param filename the file selected.
    */
   void updateSelectedFile( String filename ) {
      selectedFile = filename;
      if ( converter.isValidMedia( filename ) ) {
         configurationApplier.configure( selectedFile );
         showValidButton();
      } else {
         showInvalidSoundFile();
      }
   }//End Method
   
   /**
    * Method to handle the testing of a sound.
    * @param event the {@link ActionEvent} - method reference.
    */
   private void testSoundFile( ActionEvent event ) {
      if ( selectedFile == null ) {
         showInvalidSoundFile();
         return;
      }
      
      FriendlyMediaPlayer player = converter.convert( selectedFile );
      if ( player != null ) {
         showValidButton();
         player.friendly_play();
      } else {
         showInvalidSoundFile();
      }
   }//End Method
   
   /**
    * Method to handle the clearing of the current sound.
    * @param event the {@link ActionEvent} - method reference.
    */
   private void clearSelected( ActionEvent event ) {
      selectedFile = null;
      configurationApplier.configure( selectedFile );
      showValidButton();
   }//End Method
   
   /**
    * Method to indicate an invalid sound.
    */
   private void showInvalidSoundFile(){
      selectedFile = null;
      soundChooserButton.setText( INVALID_SOUND_TEXT );
      soundChooserButton.setBorder( new Border( new BorderStroke( ERROR_COLOUR, BorderStrokeStyle.SOLID, null, null ) ) );
   }//End Method
   
   /**
    * Method to indicate a valid sound and display the file text. 
    */
   private void showValidButton(){
      soundChooserButton.setText( stripToDisplayString() );
      soundChooserButton.setBorder( null );
   }//End Method
   
   /**
    * Method to strip the file name to a more readable form.
    * @return the stripped version.
    */
   private String stripToDisplayString() {
      if ( selectedFile == null ) {
         return CHOOSE_SOUND_TEXT;
      }
      String[] split = selectedFile.split( "/" );
      return split[ split.length - 1 ];
   }//End Method
   
   /**
    * Getter for the text on the {@link Label} of the {@link SoundConfigurationRow}.
    * @return the text.
    */
   public String getLabelText(){
      return label.getText();
   }//End Method
   
   /**
    * Getter for the currently selected file.
    * @return the {@link String} file name.
    */
   public String getSelectedFile(){
      return selectedFile;
   }//End Method
   
   /**
    * Method to determine whether the given {@link Class} is the same as that associated.
    * @param applierClass the {@link SoundConfigurationApplier} class.
    * @return true if identical.
    */
   public boolean isAssociatedWithType( Class< ? extends SoundConfigurationApplier > applierClass ) {
      return configurationApplier.getClass() == applierClass;
   }//End Method
   
   /**
    * Method to determine whether the given is associated.
    * @param configuration the {@link SoundConfiguration} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( SoundConfiguration configuration ) {
      return configurationApplier.isAssociatedWith( configuration );
   }//End Method
   
   Label label(){
      return label;
   }//End Method
   
   Button soundChooserButton(){
      return soundChooserButton;
   }//End Method
   
   Button testButton(){
      return testButton;
   }//End Method
   
   Button clearButton(){
      return clearButton;
   }//End Method

}//End Class
