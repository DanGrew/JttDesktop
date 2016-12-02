/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components.sound;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.event.ActionEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import uk.dangrew.jtt.buildwall.configuration.components.sound.applier.BrsChangeListApplier;
import uk.dangrew.jtt.buildwall.configuration.components.sound.applier.PassPassApplier;
import uk.dangrew.jtt.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.buildwall.effects.sound.StringMediaConverter;
import uk.dangrew.jtt.friendly.controlsfx.FriendlyFileChooser;
import uk.dangrew.jtt.friendly.javafx.FriendlyMediaPlayer;
import uk.dangrew.jtt.graphics.JavaFxInitializer;

public class SoundConfigurationRowTest {

   private static final String LABEL_TEXT = "This is the label text";
   private static final String FILE_NAME = "Just/a/file/name";
   private static final String SHORTENED_FILE_NAME = "name";
   
   @Mock private File file;
   
   @Spy private JavaFxStyle styling;
   @Mock private FriendlyFileChooser fileChooser;
   @Mock private StringMediaConverter converter;
   @Mock private SoundConfigurationApplier configurationApplier;
   
   private SoundConfigurationRow systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      JavaFxInitializer.startPlatform();
      
      when( fileChooser.showOpenDialog( null ) ).thenReturn( file );
      when( file.getPath() ).thenReturn( FILE_NAME );
      
      when( converter.isValidMedia( FILE_NAME ) ).thenReturn( true );
      
      systemUnderTest = new SoundConfigurationRow( styling, fileChooser, converter, LABEL_TEXT, configurationApplier );
   }//End Method

   @Test public void shouldSeparateIntoColumns(){
      verify( styling ).configureConstraintsForColumnPercentages( 
               systemUnderTest, 
               SoundConfigurationRow.LABEL_WIDTH,
               SoundConfigurationRow.CHOOSER_WIDTH,
               SoundConfigurationRow.TEST_WIDTH,
               SoundConfigurationRow.CLEAR_WIDTH
      );
   }//End Method
   
   @Test public void shouldContainComponents() {
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.label() ), is( true ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.soundChooserButton() ), is( true ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.testButton() ), is( true ) );
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.clearButton() ), is( true ) );
   }//End Method
   
   @Test public void shouldConfigureButtons(){
      assertThat( systemUnderTest.label().getText(), is( LABEL_TEXT ) );
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SoundConfigurationRow.CHOOSE_SOUND_TEXT ) );
      assertThat( systemUnderTest.testButton().getText(), is( SoundConfigurationRow.TEST_TEXT ) );
      assertThat( systemUnderTest.clearButton().getText(), is( SoundConfigurationRow.CLEAR_TEXT ) );
      
      assertThat( systemUnderTest.soundChooserButton().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.testButton().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.clearButton().getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void shouldDisplayTextGiven() {
      assertThat( systemUnderTest.label().getText(), is( LABEL_TEXT ) );
   }//End Method
   
   @Test public void shouldConfigureFileChooser(){
      verify( styling ).configureFileChooser( fileChooser, SoundConfigurationRow.FILE_CHOOSER_TITLE );
   }//End Method
   
   @Test public void shouldChooseSoundAndSetText() {
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SoundConfigurationRow.CHOOSE_SOUND_TEXT ) );
      systemUnderTest.soundChooserButton().getOnAction().handle( new ActionEvent() );
      verify( fileChooser ).showOpenDialog( null );
      
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SHORTENED_FILE_NAME ) );
      assertThatNoErrorIsShownWithFile( FILE_NAME );
   }//End Method
   
   @Test public void shouldChooseSoundButNotSetTextWhenNotValidSound() {
      when( converter.isValidMedia( FILE_NAME ) ).thenReturn( false );
      
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SoundConfigurationRow.CHOOSE_SOUND_TEXT ) );
      systemUnderTest.soundChooserButton().getOnAction().handle( new ActionEvent() );
      verify( fileChooser ).showOpenDialog( null );
      
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SoundConfigurationRow. INVALID_SOUND_TEXT) );
      assertThatErrorIsShown();
   }//End Method
   
   @Test public void shouldAllowChooserToBeCancelled() {
      when( fileChooser.showOpenDialog( null ) ).thenReturn( null );
      
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SoundConfigurationRow.CHOOSE_SOUND_TEXT ) );
      systemUnderTest.soundChooserButton().getOnAction().handle( new ActionEvent() );
      verify( fileChooser ).showOpenDialog( null );
      
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SoundConfigurationRow.CHOOSE_SOUND_TEXT ) );
   }//End Method
   
   @Test public void shouldTestSoundByRaisingEvent() {
      FriendlyMediaPlayer player = mock( FriendlyMediaPlayer.class );
      when( converter.convert( FILE_NAME ) ).thenReturn( player );
      
      systemUnderTest.soundChooserButton().getOnAction().handle( new ActionEvent() );
      systemUnderTest.testButton().getOnAction().handle( new ActionEvent() );
      verify( player ).friendly_play();
      
      assertThatNoErrorIsShownWithFile( FILE_NAME );
   }//End Method
   
   @Test public void shouldNotTestSoundWhenNoFileSelected() {
      FriendlyMediaPlayer player = mock( FriendlyMediaPlayer.class );
      when( converter.convert( FILE_NAME ) ).thenReturn( player );
      
      systemUnderTest.testButton().getOnAction().handle( new ActionEvent() );
      verify( player, never() ).friendly_play();
      
      assertThatErrorIsShown();
   }//End Method
   
   private void assertThatNoErrorIsShownWithFile( String file ){
      assertThat( systemUnderTest.soundChooserButton().getText(), is( not( SoundConfigurationRow.INVALID_SOUND_TEXT ) ) );
      assertThat( systemUnderTest.soundChooserButton().getBorder(), is( nullValue() ) );
      assertThat( systemUnderTest.getSelectedFile(), is( file ) );
   }//End Method
   
   private void assertThatErrorIsShown(){
      assertThat( systemUnderTest.getSelectedFile(), is( nullValue() ) );
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SoundConfigurationRow.INVALID_SOUND_TEXT ) );
      Border border = systemUnderTest.soundChooserButton().getBorder();
      final BorderStroke borderStroke = border.getStrokes().get( 0 );
      assertThat( borderStroke.getBottomStroke(), is( SoundConfigurationRow.ERROR_COLOUR ) );
      assertThat( borderStroke.getBottomStyle(), is( BorderStrokeStyle.SOLID ) );
      assertThat( borderStroke.getTopStroke(), is( SoundConfigurationRow.ERROR_COLOUR ) );
      assertThat( borderStroke.getTopStyle(), is( BorderStrokeStyle.SOLID ) );
      assertThat( borderStroke.getRightStroke(), is( SoundConfigurationRow.ERROR_COLOUR ) );
      assertThat( borderStroke.getRightStyle(), is( BorderStrokeStyle.SOLID ) );
      assertThat( borderStroke.getLeftStroke(), is( SoundConfigurationRow.ERROR_COLOUR ) );
      assertThat( borderStroke.getLeftStyle(), is( BorderStrokeStyle.SOLID ) );
   }//End Method
   
   @Test public void shouldNotPlaySoundIfNotValidSound() {
      when( converter.convert( FILE_NAME ) ).thenReturn( null );
      systemUnderTest.testButton().getOnAction().handle( new ActionEvent() );
      assertThatErrorIsShown();
   }//End Method
   
   @Test public void shouldNotApplyIfSoundIsNotValid() {
      when( converter.isValidMedia( Mockito.anyString() ) ).thenReturn( false );
      systemUnderTest.soundChooserButton().getOnAction().handle( new ActionEvent() );
      verify( configurationApplier, never() ).configure( FILE_NAME );
   }//End Method
   
   @Test public void shouldNotApplySoundWhenNoSoundSelected() {
      when( file.getPath() ).thenReturn( null );
      when( converter.isValidMedia( Mockito.anyString() ) ).thenReturn( true );
      
      systemUnderTest.soundChooserButton().getOnAction().handle( new ActionEvent() );
      verify( configurationApplier, never() ).configure( FILE_NAME );
   }//End Method
   
   @Test public void shouldClearChosen() {
      systemUnderTest.soundChooserButton().getOnAction().handle( new ActionEvent() );
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SHORTENED_FILE_NAME ) );
      systemUnderTest.clearButton().getOnAction().handle( new ActionEvent() );
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SoundConfigurationRow.CHOOSE_SOUND_TEXT ) );
      assertThatNoErrorIsShownWithFile( null );
      verify( configurationApplier ).configure( null );
   }//End Method
   
   @Test public void shouldNotClearIfNothingChosen() {
      systemUnderTest.clearButton().getOnAction().handle( new ActionEvent() );
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SoundConfigurationRow.CHOOSE_SOUND_TEXT ) );
   }//End Method
   
   @Test public void shouldDisplayFileNameOnButton(){
      when( file.getPath() ).thenReturn( FILE_NAME );
      when( converter.isValidMedia( FILE_NAME ) ).thenReturn( true );
      
      systemUnderTest.soundChooserButton().getOnAction().handle( new ActionEvent() );
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SHORTENED_FILE_NAME ) );
      assertThatNoErrorIsShownWithFile( FILE_NAME );
   }//End Method
   
   @Test public void shouldApplyAfterSet(){
      when( file.getPath() ).thenReturn( FILE_NAME );
      when( converter.isValidMedia( FILE_NAME ) ).thenReturn( true );
      
      systemUnderTest.soundChooserButton().getOnAction().handle( new ActionEvent() );
      verify( configurationApplier ).configure( FILE_NAME );
      assertThatNoErrorIsShownWithFile( FILE_NAME );
   }//End Method
   
   @Test public void shouldTestAfterSet(){
      FriendlyMediaPlayer player = mock( FriendlyMediaPlayer.class );
      when( converter.convert( FILE_NAME ) ).thenReturn( player );
      
      systemUnderTest.soundChooserButton().getOnAction().handle( new ActionEvent() );
      systemUnderTest.testButton().getOnAction().handle( new ActionEvent() );
      verify( player ).friendly_play();
   }//End Method
   
   @Test public void shouldUpdateSelectedFile(){
      systemUnderTest.updateSelectedFile( FILE_NAME );
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SHORTENED_FILE_NAME ) );
      assertThatNoErrorIsShownWithFile( FILE_NAME );
   }//End Method
   
   @Test public void shouldUpdateSelectedFileIfInvalid(){
      when( converter.isValidMedia( FILE_NAME ) ).thenReturn( false );
      systemUnderTest.updateSelectedFile( FILE_NAME );
      assertThat( systemUnderTest.soundChooserButton().getText(), is( SoundConfigurationRow.INVALID_SOUND_TEXT ) );
      assertThatErrorIsShown();
   }//End Method
   
   @Test public void shouldProvideLabelText(){
      assertThat( systemUnderTest.getLabelText(), is( LABEL_TEXT ) );
   }//End Method
   
   @Test public void shouldBeAssociated(){
      when( configurationApplier.isAssociatedWith( Mockito.any() ) ).thenReturn( true );
      assertThat( systemUnderTest.isAssociatedWith( new SoundConfiguration() ), is( true ) );
      when( configurationApplier.isAssociatedWith( Mockito.any() ) ).thenReturn( false );
      assertThat( systemUnderTest.isAssociatedWith( new SoundConfiguration() ), is( false ) );
      
      systemUnderTest = new SoundConfigurationRow( LABEL_TEXT, new PassPassApplier( new SoundConfiguration() ) );
      assertThat( systemUnderTest.isAssociatedWithType( PassPassApplier.class ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWithType( BrsChangeListApplier.class ), is( false ) );
   }//End Method
   
}//End Class
