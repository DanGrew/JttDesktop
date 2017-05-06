/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.flasher.configuration;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser.ExtensionFilter;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyleTest;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.ImageFlasherImplTest;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.ImageFlasherProperties;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.ImageFlasherPropertiesImpl;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.configuration.ImageFlasherConfigurationPanel;
import uk.dangrew.jtt.desktop.friendly.controlsfx.FriendlyFileChooser;
import uk.dangrew.jtt.desktop.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.model.utility.TestCommon;

/**
 * {@link ImageFlasherConfigurationPanel} test.
 */
public class ImageFlasherConfigurationPanelTest {
   
   private static final String TEST_FLASHER_TITLE = "Test Flasher";
   
   private final File ALERT_IMAGE_FILE = new File( ImageFlasherImplTest.class.getResource( "alert-image.png" ).getFile() );
   @Spy private JavaFxStyle styling;
   @Mock private FriendlyFileChooser fileChooser; 
   private ObservableList< ExtensionFilter > extensionFilters;
   private ImageFlasherProperties properties;
   private ImageFlasherConfigurationPanel systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      extensionFilters = FXCollections.observableArrayList();
      when( fileChooser.getExtensionFilters() ).thenReturn( extensionFilters );
      
      properties = new ImageFlasherPropertiesImpl();
      systemUnderTest = new ImageFlasherConfigurationPanel( styling, TEST_FLASHER_TITLE, properties, fileChooser );
   }//End Method

   @Ignore
   @Test public void manualInspection() throws InterruptedException{
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      
      JavaFxInitializer.launchInWindow( () -> {
         systemUnderTest = new ImageFlasherConfigurationPanel( TEST_FLASHER_TITLE, properties );
         return systemUnderTest;
      } );
      
      Thread.sleep( 2000000 );
   }//End Method
   
   @Test public void shouldUseInsets(){
      assertThat( systemUnderTest.getInsets().getBottom(), is( ImageFlasherConfigurationPanel.INSETS ) );
      assertThat( systemUnderTest.getInsets().getTop(), is( ImageFlasherConfigurationPanel.INSETS ) );
      assertThat( systemUnderTest.getInsets().getRight(), is( ImageFlasherConfigurationPanel.INSETS ) );
      assertThat( systemUnderTest.getInsets().getLeft(), is( ImageFlasherConfigurationPanel.INSETS ) );
   }//End Method
   
   @Test public void shouldShowImageChooseBoldLabel() {
      Label imageLabel = systemUnderTest.imageLabel();
      
      TestCommon.assertThatFontIsBold( imageLabel.getFont() );
      assertThat( imageLabel.getText(), is( ImageFlasherConfigurationPanel.IMAGE_LABEL_TEXT ) );
   }//End Method
   
   @Test public void imageButtonShouldBeMaxWidthWithMissingImageText() {
      Button imageButton = systemUnderTest.imageButton();
      
      assertThat( imageButton.getText(), is( ImageFlasherConfigurationPanel.SELECT_IMAGE_TEXT ) );
      assertThat( imageButton.getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void fileChooserShouldBeConfiguredForImageSelection() {
      verify( styling ).configureFileChooser( fileChooser, ImageFlasherConfigurationPanel.IMAGE_CHOOSER_TITLE );
      assertThat( extensionFilters, hasSize( 1 ) );
      assertThat( extensionFilters.get( 0 ), is( ImageFlasherConfigurationPanel.IMAGE_FILTER ) );
   }//End Method
   
   @Test public void imageButtonShouldNotDisplayImageViewToHoldImageInitially(){
      assertThat( systemUnderTest.imageButton().getGraphic(), is( nullValue() ) );
   }//End Method
   
   @Test public void chosenImageShouldAppearOnButtonWithFixedSize() {
      when( fileChooser.showOpenDialog( null ) ).thenReturn( ALERT_IMAGE_FILE );
      
      systemUnderTest.imageButton().getOnAction().handle( new ActionEvent() );

      ImageView imageView = systemUnderTest.imageButtonGraphic();
      assertThat( imageView.getImage(), is( notNullValue() ) );
      assertThat( imageView.getFitWidth(), is( ImageFlasherConfigurationPanel.IMAGE_BUTTON_GRAPHIC_WIDTH ) );
      assertThat( imageView.getFitHeight(), is( ImageFlasherConfigurationPanel.IMAGE_BUTTON_GRAPHIC_HEIGHT ) );
   }//End Method
   
   @Test public void choosingImageShouldUpdateConfigurationAndHideChooseText() {
      when( fileChooser.showOpenDialog( null ) ).thenReturn( ALERT_IMAGE_FILE );
      
      systemUnderTest.imageButton().getOnAction().handle( new ActionEvent() );
      
      assertThat( properties.imageProperty().get(), is( notNullValue() ) );
      assertThat( properties.imageProperty().get(), is( systemUnderTest.imageButtonGraphic().getImage() ) );
      assertThat( systemUnderTest.imageButton().getText(), is( nullValue() ) );
   }//End Method
   
   @Test public void cancellingImageWhenAlreadySelectedPreservesSelection() {
      when( fileChooser.showOpenDialog( null ) ).thenReturn( ALERT_IMAGE_FILE );
      
      systemUnderTest.imageButton().getOnAction().handle( new ActionEvent() );
      assertThat( systemUnderTest.imageButtonGraphic().getImage(), is( notNullValue() ) );
      
      when( fileChooser.showOpenDialog( null ) ).thenReturn( null );
      systemUnderTest.imageButton().getOnAction().handle( new ActionEvent() );
      assertThat( systemUnderTest.imageButtonGraphic().getImage(), is( notNullValue() ) );
   }//End Method
   
   @Test public void shouldProvideBoldLabelForNumberOfFlashes(){
      TestCommon.assertThatFontIsBold( systemUnderTest.numberOfFlashesLabel().getFont() );
      assertThat( systemUnderTest.numberOfFlashesLabel().getText(), is( ImageFlasherConfigurationPanel.NUMBER_OF_FLASHES_LABEL ) );
   }//End Method
   
   @Test public void shouldUpdateNumberOfFlashesWhenConfigurationChanges(){
      final int original = 10;
      systemUnderTest.numberOfFlashesSpinner().getValueFactory().setValue( original );
      assertThat( systemUnderTest.numberOfFlashesSpinner().getValue(), is( original ) );
      
      final int newValue = 20;
      properties.numberOfFlashesProperty().set( newValue );
      assertThat( systemUnderTest.numberOfFlashesSpinner().getValue(), is( newValue ) );
   }//End Method
   
   @Test public void shouldUpdateConfigurationWhenNumberOfFlashesChanges(){
      final int original = 20;
      properties.numberOfFlashesProperty().set( original );
      assertThat( properties.numberOfFlashesProperty().get(), is( original ) );
      
      final int newValue = 10;
      systemUnderTest.numberOfFlashesSpinner().getValueFactory().setValue( newValue );
      assertThat( properties.numberOfFlashesProperty().get(), is( newValue ) );
   }//End Method
   
   @Test public void numberOfFlashesSpinnerShouldBeConfigured(){
      systemUnderTest.numberOfFlashesSpinner().getValueFactory().setValue( 1001 );
      assertThat( systemUnderTest.numberOfFlashesSpinner().getValue(), is( 1000 ) );
      
      systemUnderTest.numberOfFlashesSpinner().getValueFactory().setValue( 0 );
      assertThat( systemUnderTest.numberOfFlashesSpinner().getValue(), is( 1 ) );
      
      systemUnderTest.numberOfFlashesSpinner().increment();
      assertThat( systemUnderTest.numberOfFlashesSpinner().getValue(), is( 2 ) );
   }//End Method
   
   @Test public void shouldProvideBoldLabelForFlashOn(){
      TestCommon.assertThatFontIsBold( systemUnderTest.flashOnLabel().getFont() );
      assertThat( systemUnderTest.flashOnLabel().getText(), is( ImageFlasherConfigurationPanel.FLASH_ON_LABEL ) );
   }//End Method
   
   @Test public void shouldUpdateFlashOnWhenConfigurationChanges(){
      final int original = 10;
      systemUnderTest.flashOnSpinner().getValueFactory().setValue( original );
      assertThat( systemUnderTest.flashOnSpinner().getValue(), is( original ) );
      
      final int newValue = 20;
      properties.flashOnProperty().set( newValue );
      assertThat( systemUnderTest.flashOnSpinner().getValue(), is( newValue ) );
   }//End Method
   
   @Test public void shouldUpdateConfigurationWhenFlashOnChanges(){
      final int original = 20;
      properties.flashOnProperty().set( original );
      assertThat( properties.flashOnProperty().get(), is( original ) );
      
      final int newValue = 10;
      systemUnderTest.flashOnSpinner().getValueFactory().setValue( newValue );
      assertThat( properties.flashOnProperty().get(), is( newValue ) );
   }//End Method
   
   @Test public void flashOnSpinnerShouldBeConfigured(){
      systemUnderTest.flashOnSpinner().getValueFactory().setValue( 1000000 );
      assertThat( systemUnderTest.flashOnSpinner().getValue(), is( 1000000 ) );
      
      systemUnderTest.flashOnSpinner().getValueFactory().setValue( 0 );
      assertThat( systemUnderTest.flashOnSpinner().getValue(), is( 1 ) );
      
      systemUnderTest.flashOnSpinner().increment();
      assertThat( systemUnderTest.flashOnSpinner().getValue(), is( 1001 ) );
   }//End Method
   
   @Test public void shouldProvideBoldLabelForFlashOff(){
      TestCommon.assertThatFontIsBold( systemUnderTest.flashOffLabel().getFont() );
      assertThat( systemUnderTest.flashOffLabel().getText(), is( ImageFlasherConfigurationPanel.FLASH_OFF_LABEL ) );
   }//End Method
   
   @Test public void shouldUpdateFlashOffWhenConfigurationChanges(){
      final int original = 10;
      systemUnderTest.flashOffSpinner().getValueFactory().setValue( original );
      assertThat( systemUnderTest.flashOffSpinner().getValue(), is( original ) );
      
      final int newValue = 20;
      properties.flashOffProperty().set( newValue );
      assertThat( systemUnderTest.flashOffSpinner().getValue(), is( newValue ) );
   }//End Method
   
   @Test public void shouldUpdateConfigurationWhenFlashOffChanges(){
      final int original = 20;
      properties.flashOffProperty().set( original );
      assertThat( properties.flashOffProperty().get(), is( original ) );
      
      final int newValue = 10;
      systemUnderTest.flashOffSpinner().getValueFactory().setValue( newValue );
      assertThat( properties.flashOffProperty().get(), is( newValue ) );
   }//End Method
   
   @Test public void flashOffSpinnerShouldBeConfigured(){
      systemUnderTest.flashOffSpinner().getValueFactory().setValue( 1000000 );
      assertThat( systemUnderTest.flashOffSpinner().getValue(), is( 1000000 ) );
      
      systemUnderTest.flashOffSpinner().getValueFactory().setValue( 0 );
      assertThat( systemUnderTest.flashOffSpinner().getValue(), is( 1 ) );
      
      systemUnderTest.flashOffSpinner().increment();
      assertThat( systemUnderTest.flashOffSpinner().getValue(), is( 501 ) );
   }//End Method
   
   @Test public void shouldProvideBoldLabelForTransparency(){
      TestCommon.assertThatFontIsBold( systemUnderTest.transparencyLabel().getFont() );
      assertThat( systemUnderTest.transparencyLabel().getText(), is( ImageFlasherConfigurationPanel.TRANSPARENCY_LABEL ) );
   }//End Method
   
   @Test public void shouldUpdateTransparencyWhenConfigurationChanges(){
      final double original = 0.2;
      systemUnderTest.transparencySpinner().getValueFactory().setValue( original );
      assertThat( systemUnderTest.transparencySpinner().getValue(), is( original ) );
      
      final double newValue = 0.7;
      properties.transparencyProperty().set( newValue );
      assertThat( systemUnderTest.transparencySpinner().getValue(), is( newValue ) );
   }//End Method
   
   @Test public void shouldUpdateConfigurationWhenTransparencyChanges(){
      final double original = 0.7;
      properties.transparencyProperty().set( original );
      assertThat( properties.transparencyProperty().get(), is( original ) );
      
      final double newValue = 0.2;
      systemUnderTest.transparencySpinner().getValueFactory().setValue( newValue );
      assertThat( properties.transparencyProperty().get(), is( newValue ) );
   }//End Method
   
   @Test public void transparencySpinnerShouldBeConfigured(){
      systemUnderTest.transparencySpinner().getValueFactory().setValue( 2.0 );
      assertThat( systemUnderTest.transparencySpinner().getValue(), is( 1.0 ) );
      
      systemUnderTest.transparencySpinner().getValueFactory().setValue( -1.0 );
      assertThat( systemUnderTest.transparencySpinner().getValue(), is( 0.0 ) );
      
      systemUnderTest.transparencySpinner().increment();
      assertThat( systemUnderTest.transparencySpinner().getValue(), is( 0.05 ) );
   }//End Method
   
   @Test public void shouldProvideConfiguredTestButton(){
      Button testButton = systemUnderTest.testFlashButton();
      assertThat( testButton.getText(), is( ImageFlasherConfigurationPanel.TEST_FLASH_LABEL ) );
      assertThat( testButton.getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void testButtonShouldTurnOnFlashSwitch(){
      assertThat( properties.flashingSwitch().get(), is( false ) );
      systemUnderTest.testFlashButton().getOnAction().handle( new ActionEvent() );
      assertThat( properties.flashingSwitch().get(), is( true ) );
   }//End Method
   
   @Test public void testButtonShouldBeDisableWhileFlashSwitchIsOn(){
      assertThat( systemUnderTest.testFlashButton().isDisabled(), is( false ) );
      properties.flashingSwitch().set( true );
      assertThat( systemUnderTest.testFlashButton().isDisabled(), is( true ) );
      properties.flashingSwitch().set( false );
      assertThat( systemUnderTest.testFlashButton().isDisabled(), is( false ) );
   }//End Method
   
   @Test public void shouldProvideConfiguredStopTestButton(){
      Button testButton = systemUnderTest.stopTestFlashButton();
      assertThat( testButton.getText(), is( ImageFlasherConfigurationPanel.STOP_FLASH_LABEL ) );
      assertThat( testButton.getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void stopTestButtonShouldTurnOffFlashSwitch(){
      properties.flashingSwitch().set( true );
      assertThat( properties.flashingSwitch().get(), is( true ) );
      systemUnderTest.stopTestFlashButton().getOnAction().handle( new ActionEvent() );
      assertThat( properties.flashingSwitch().get(), is( false ) );
   }//End Method
   
   @Test public void stopTestButtonShouldBeDisableWhileFlashSwitchIsOff(){
      assertThat( systemUnderTest.stopTestFlashButton().isDisabled(), is( true ) );
      properties.flashingSwitch().set( true );
      assertThat( systemUnderTest.stopTestFlashButton().isDisabled(), is( false ) );
      properties.flashingSwitch().set( false );
      assertThat( systemUnderTest.stopTestFlashButton().isDisabled(), is( true ) );
   }//End Method
   
   @Test public void shouldCreateTitleWithExpectedProperties(){
      Label titleLabel = systemUnderTest.titleLabel();
      assertThat( titleLabel.getFont().getSize(), closeTo( JavaFxStyleTest.TITLE_FONT_SIZE, TestCommon.precision() ) );
      assertThat( GridPane.getColumnIndex( titleLabel ), is( 0 ) );
      assertThat( GridPane.getRowIndex( titleLabel ), is( 0 ) );
      assertThat( GridPane.getColumnSpan( titleLabel ), is( 2 ) );
      assertThat( GridPane.getRowSpan( titleLabel ), is( 1 ) );
      assertThat( GridPane.getHalignment( titleLabel ), is( HPos.CENTER ) );
      assertThat( GridPane.getValignment( titleLabel ), is( VPos.CENTER ) );
   }//End Method

}//End Class
