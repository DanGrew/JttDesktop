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

import org.controlsfx.dialog.FontSelectorDialog;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import graphics.JavaFxInitializer;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import utility.TestCommon;

/**
 * {@link BuildWallConfigurationPanelImpl} test.
 */
public class BuildWallConfigurationPaneImplTest {

   private static final Font TEST_FONT_A = new Font( "anything", 14 );
   private static final Font TEST_FONT_B = new Font( "anything", 20 );
   private static final Font TEST_FONT_C = new Font( "anything", 8 );
   private Supplier< Font > fontSupplier;
   private BuildWallConfiguration configuration;
   private BuildWallConfigurationPanelImpl systemUnderTest;
   
   @SuppressWarnings("unchecked") //Simply mocking genericized objects.
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      fontSupplier = Mockito.mock( Supplier.class );
      configuration = new BuildWallConfigurationImpl();
      systemUnderTest = new BuildWallConfigurationPanelImpl( configuration, fontSupplier );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      JavaFxInitializer.threadedLaunch( () -> { 
         return new BuildWallConfigurationPanelImpl(
               configuration, 
               () -> { return new FontSelectorDialog( configuration.jobNameFont().get() ).showAndWait().get(); } 
         ); 
      } );
      
      Thread.sleep( 10000000 );
   }//End Method
   
   @Test public void shouldContainNecessaryElements(){
      TitledPane fontPane = systemUnderTest.fontPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( fontPane ) );
      
      GridPane fontContent = ( GridPane )fontPane.getContent();
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.jobNameFontButton() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.buildNumberFontButton() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.completionEstimateFontButton() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.jobNameQuickFoxLabel() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.buildNumberQuickFoxLabel() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.completionEstimateQuickFoxLabel() ) );
      
      TitledPane colourPane = systemUnderTest.colourPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( colourPane ) );
      
      GridPane colourContent = ( GridPane )colourPane.getContent();
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.jobNameColourPicker() ) );
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.buildNumberColourPicker() ) );
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.completionEstimateColourPicker() ) );
   }//End Method
   
   @Test public void shouldUseInitialConfiguration(){
      Assert.assertEquals( configuration.jobNameFont().get().getName(), systemUnderTest.jobNameFontButton().getText() );
      Assert.assertEquals( configuration.jobNameFont().get(), systemUnderTest.jobNameQuickFoxLabel().getFont() );
      Assert.assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobNameColourPicker().valueProperty().get() );
      
      Assert.assertEquals( configuration.buildNumberFont().get().getName(), systemUnderTest.buildNumberFontButton().getText() );
      Assert.assertEquals( configuration.buildNumberFont().get(), systemUnderTest.buildNumberQuickFoxLabel().getFont() );
      Assert.assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumberColourPicker().valueProperty().get() );
      
      Assert.assertEquals( configuration.completionEstimateFont().get().getName(), systemUnderTest.completionEstimateFontButton().getText() );
      Assert.assertEquals( configuration.completionEstimateFont().get(), systemUnderTest.completionEstimateQuickFoxLabel().getFont() );
      Assert.assertEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimateColourPicker().valueProperty().get() );
   }//End Method
   
   @Test public void shouldProvideJobNameFontNameOnChooserButton() {
      configuration.jobNameFont().set( TEST_FONT_A );
      Assert.assertEquals( configuration.jobNameFont().get().getName(), systemUnderTest.jobNameFontButton().getText() );
      Assert.assertEquals( TEST_FONT_A, configuration.jobNameFont().get() );
      
      Mockito.when( fontSupplier.get() ).thenReturn( TEST_FONT_B );
      systemUnderTest.jobNameFontButton().fire();
      Assert.assertEquals( TEST_FONT_B.getName(), systemUnderTest.jobNameFontButton().getText() );
   }//End Method
   
   @Test public void shouldProvideJobNameQuickBrownFoxWithChosenFont() {
      configuration.jobNameFont().set( TEST_FONT_A );
      Assert.assertEquals( configuration.jobNameFont().get(), systemUnderTest.jobNameQuickFoxLabel().getFont() );
      Assert.assertEquals( TEST_FONT_A, configuration.jobNameFont().get() );
      
      Mockito.when( fontSupplier.get() ).thenReturn( TEST_FONT_B );
      systemUnderTest.jobNameFontButton().fire();
      Assert.assertEquals( TEST_FONT_B, systemUnderTest.jobNameQuickFoxLabel().getFont() );
   }//End Method
   
   @Test public void shouldProvideBuildNumberFontNameOnChooserButton() {
      configuration.buildNumberFont().set( TEST_FONT_A );
      Assert.assertEquals( configuration.buildNumberFont().get().getName(), systemUnderTest.buildNumberFontButton().getText() );
      Assert.assertEquals( TEST_FONT_A, configuration.buildNumberFont().get() );
      
      Mockito.when( fontSupplier.get() ).thenReturn( TEST_FONT_B );
      systemUnderTest.buildNumberFontButton().fire();
      Assert.assertEquals( TEST_FONT_B.getName(), systemUnderTest.buildNumberFontButton().getText() );
   }//End Method
   
   @Test public void shouldProvideBuildNumberQuickBrownFoxWithChosenFont() {
      configuration.buildNumberFont().set( TEST_FONT_A );
      Assert.assertEquals( configuration.buildNumberFont().get(), systemUnderTest.buildNumberQuickFoxLabel().getFont() );
      Assert.assertEquals( TEST_FONT_A, configuration.buildNumberFont().get() );
      
      Mockito.when( fontSupplier.get() ).thenReturn( TEST_FONT_B );
      systemUnderTest.buildNumberFontButton().fire();
      Assert.assertEquals( TEST_FONT_B, systemUnderTest.buildNumberQuickFoxLabel().getFont() );
   }//End Method
   
   @Test public void shouldProvideCompletionEstimateFontNameOnChooserButton() {
      configuration.completionEstimateFont().set( TEST_FONT_A );
      Assert.assertEquals( configuration.completionEstimateFont().get().getName(), systemUnderTest.completionEstimateFontButton().getText() );
      Assert.assertEquals( TEST_FONT_A, configuration.completionEstimateFont().get() );
      
      Mockito.when( fontSupplier.get() ).thenReturn( TEST_FONT_B );
      systemUnderTest.completionEstimateFontButton().fire();
      Assert.assertEquals( TEST_FONT_B.getName(), systemUnderTest.completionEstimateFontButton().getText() );
   }//End Method
   
   @Test public void shouldProvideCompletionEstimateQuickBrownFoxWithChosenFont() {
      configuration.completionEstimateFont().set( TEST_FONT_A );
      Assert.assertEquals( configuration.completionEstimateFont().get(), systemUnderTest.completionEstimateQuickFoxLabel().getFont() );
      Assert.assertEquals( TEST_FONT_A, configuration.completionEstimateFont().get() );
      
      Mockito.when( fontSupplier.get() ).thenReturn( TEST_FONT_B );
      systemUnderTest.completionEstimateFontButton().fire();
      Assert.assertEquals( TEST_FONT_B, systemUnderTest.completionEstimateQuickFoxLabel().getFont() );
   }//End Method
   
   @Test public void shouldUpdateJobNameFontFromConfiguration(){
      configuration.jobNameFont().set( TEST_FONT_A );
      Assert.assertEquals( configuration.jobNameFont().get().getName(), systemUnderTest.jobNameFontButton().getText() );
      Assert.assertEquals( configuration.jobNameFont().get(), systemUnderTest.jobNameQuickFoxLabel().getFont() );
      
      configuration.jobNameFont().set( TEST_FONT_B );
      
      Assert.assertEquals( configuration.jobNameFont().get().getName(), systemUnderTest.jobNameFontButton().getText() );
      Assert.assertEquals( configuration.jobNameFont().get(), systemUnderTest.jobNameQuickFoxLabel().getFont() );

      configuration.jobNameFont().set( TEST_FONT_C );
      
      Assert.assertEquals( configuration.jobNameFont().get().getName(), systemUnderTest.jobNameFontButton().getText() );
      Assert.assertEquals( configuration.jobNameFont().get(), systemUnderTest.jobNameQuickFoxLabel().getFont() );
   }//End Method
   
   @Test public void shouldSetJobNameFontInConfiguration(){
      configuration.jobNameFont().set( TEST_FONT_A );
      Assert.assertEquals( TEST_FONT_A, configuration.jobNameFont().get() );
      
      Mockito.when( fontSupplier.get() ).thenReturn( TEST_FONT_B );
      systemUnderTest.jobNameFontButton().fire();
      Assert.assertEquals( TEST_FONT_B, configuration.jobNameFont().get() );
   }//End Method
   
   @Test public void shouldUpdateJobNameColourFromConfiguration(){
      configuration.jobNameColour().set( Color.BISQUE );
      Assert.assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobNameColourPicker().valueProperty().get() );
      
      configuration.jobNameColour().set( Color.DARKORCHID );
      Assert.assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobNameColourPicker().valueProperty().get() );
      
      configuration.jobNameColour().set( Color.MOCCASIN );
      Assert.assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobNameColourPicker().valueProperty().get() );
   }//End Method
   
   @Test public void shouldSetJobNameColourInConfiguration(){
      Assert.assertNotEquals( Color.AQUA, configuration.jobNameColour().get() );
      systemUnderTest.jobNameColourPicker().valueProperty().set( Color.AQUA );
      Assert.assertEquals( Color.AQUA, configuration.jobNameColour().get() );
   }//End Method
   
   @Test public void shouldUpdateBuildNumberFontFromConfiguration(){
      configuration.buildNumberFont().set( TEST_FONT_A );
      Assert.assertEquals( configuration.buildNumberFont().get().getName(), systemUnderTest.buildNumberFontButton().getText() );
      Assert.assertEquals( configuration.buildNumberFont().get(), systemUnderTest.buildNumberQuickFoxLabel().getFont() );
      
      configuration.buildNumberFont().set( TEST_FONT_B );
      
      Assert.assertEquals( configuration.buildNumberFont().get().getName(), systemUnderTest.buildNumberFontButton().getText() );
      Assert.assertEquals( configuration.buildNumberFont().get(), systemUnderTest.buildNumberQuickFoxLabel().getFont() );

      configuration.buildNumberFont().set( TEST_FONT_C );
      
      Assert.assertEquals( configuration.buildNumberFont().get().getName(), systemUnderTest.buildNumberFontButton().getText() );
      Assert.assertEquals( configuration.buildNumberFont().get(), systemUnderTest.buildNumberQuickFoxLabel().getFont() );
   }//End Method
   
   @Test public void shouldSetBuildNumberFontInConfiguration(){
      configuration.buildNumberFont().set( TEST_FONT_A );
      Assert.assertEquals( TEST_FONT_A, configuration.buildNumberFont().get() );
      
      Mockito.when( fontSupplier.get() ).thenReturn( TEST_FONT_B );
      systemUnderTest.buildNumberFontButton().fire();
      Assert.assertEquals( TEST_FONT_B, configuration.buildNumberFont().get() );
   }//End Method
   
   @Test public void shouldUpdateBuildNumberColourFromConfiguration(){
      configuration.buildNumberColour().set( Color.BISQUE );
      Assert.assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumberColourPicker().valueProperty().get() );
      
      configuration.buildNumberColour().set( Color.DARKORCHID );
      Assert.assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumberColourPicker().valueProperty().get() );
      
      configuration.buildNumberColour().set( Color.MOCCASIN );
      Assert.assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumberColourPicker().valueProperty().get() );
   }//End Method
   
   @Test public void shouldSetBuildNumberColourInConfiguration(){
      Assert.assertNotEquals( Color.AQUA, configuration.buildNumberColour().get() );
      systemUnderTest.buildNumberColourPicker().valueProperty().set( Color.AQUA );
      Assert.assertEquals( Color.AQUA, configuration.buildNumberColour().get() );
   }//End Method
   
   @Test public void shouldUpdateCompletionEstimateFontFromConfiguration(){
      configuration.completionEstimateFont().set( TEST_FONT_A );
      Assert.assertEquals( configuration.completionEstimateFont().get().getName(), systemUnderTest.completionEstimateFontButton().getText() );
      Assert.assertEquals( configuration.completionEstimateFont().get(), systemUnderTest.completionEstimateQuickFoxLabel().getFont() );
      
      configuration.completionEstimateFont().set( TEST_FONT_B );
      
      Assert.assertEquals( configuration.completionEstimateFont().get().getName(), systemUnderTest.completionEstimateFontButton().getText() );
      Assert.assertEquals( configuration.completionEstimateFont().get(), systemUnderTest.completionEstimateQuickFoxLabel().getFont() );

      configuration.completionEstimateFont().set( TEST_FONT_C );
      
      Assert.assertEquals( configuration.completionEstimateFont().get().getName(), systemUnderTest.completionEstimateFontButton().getText() );
      Assert.assertEquals( configuration.completionEstimateFont().get(), systemUnderTest.completionEstimateQuickFoxLabel().getFont() );
   }//End Method
   
   @Test public void shouldSetCompletionEstimateFontInConfiguration(){
      configuration.completionEstimateFont().set( TEST_FONT_A );
      Assert.assertEquals( TEST_FONT_A, configuration.completionEstimateFont().get() );
      
      Mockito.when( fontSupplier.get() ).thenReturn( TEST_FONT_B );
      systemUnderTest.completionEstimateFontButton().fire();
      Assert.assertEquals( TEST_FONT_B, configuration.completionEstimateFont().get() );
   }//End Method
   
   @Test public void shouldUpdateCompletionEstimateColourFromConfiguration(){
      configuration.completionEstimateColour().set( Color.BISQUE );
      Assert.assertEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimateColourPicker().valueProperty().get() );
      
      configuration.completionEstimateColour().set( Color.DARKORCHID );
      Assert.assertEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimateColourPicker().valueProperty().get() );
      
      configuration.completionEstimateColour().set( Color.MOCCASIN );
      Assert.assertEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimateColourPicker().valueProperty().get() );
   }//End Method
   
   @Test public void shouldSetCompletionEstimateColourInConfiguration(){
      Assert.assertNotEquals( Color.AQUA, configuration.completionEstimateColour().get() );
      systemUnderTest.completionEstimateColourPicker().valueProperty().set( Color.AQUA );
      Assert.assertEquals( Color.AQUA, configuration.completionEstimateColour().get() );
   }//End Method
   
   @Test public void shouldUseBoldLabels(){
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.jobNameFontLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.jobNameColourLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.buildNumberFontLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.buildNumberColourLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.completionEstimateFontLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.completionEstimateColourLabel().getFont().getStyle() ) );
   }//End Method
   
   @Test public void eachPaneShouldCoverEntireWidth() {
      Assert.assertEquals( 1, systemUnderTest.getColumnConstraints().size() );
      Assert.assertEquals( 100, systemUnderTest.getColumnConstraints().get( 0 ).getPercentWidth(), TestCommon.precision() );
      Assert.assertEquals( Priority.ALWAYS, systemUnderTest.getColumnConstraints().get( 0 ).getHgrow() );
   }//End Method
   
   @Test public void eachPaneShouldShareWidthAmongstColumns() {
      GridPane fontGrid = ( GridPane )systemUnderTest.fontPane().getContent();
      Assert.assertEquals( 
               BuildWallConfigurationPanelImpl.LABEL_PERCENTAGE_WIDTH, 
               fontGrid.getColumnConstraints().get( 0 ).getPercentWidth(),
               TestCommon.precision()
      );
      Assert.assertEquals( Priority.ALWAYS, fontGrid.getColumnConstraints().get( 0 ).getHgrow() );
      
      GridPane colourGrid = ( GridPane )systemUnderTest.colourPane().getContent();
      Assert.assertEquals( 
               BuildWallConfigurationPanelImpl.CONTROLS_PERCENTAGE_WIDTH, 
               colourGrid.getColumnConstraints().get( 1 ).getPercentWidth(),
               TestCommon.precision()
      );
      Assert.assertEquals( Priority.ALWAYS, colourGrid.getColumnConstraints().get( 1 ).getHgrow() );
      
      Assert.assertTrue( 
               colourGrid.getColumnConstraints().get( 0 ).getPercentWidth() +
               colourGrid.getColumnConstraints().get( 1 ).getPercentWidth() 
               >= 100
      );
   }//End Method

}//End Class
