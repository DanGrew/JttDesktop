/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import java.util.Optional;
import java.util.function.Function;

import org.controlsfx.dialog.FontSelectorDialog;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import graphics.JavaFxInitializer;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.ColumnConstraints;
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
   private Function< Font, Font > fontSupplier;
   private BuildWallConfiguration configuration;
   private BuildWallConfigurationPanelImpl systemUnderTest;
   
   @SuppressWarnings("unchecked") //Simply mocking genericized objects.
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      fontSupplier = Mockito.mock( Function.class );
      configuration = new BuildWallConfigurationImpl();
      systemUnderTest = new BuildWallConfigurationPanelImpl( configuration, fontSupplier );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> { 
         return new BuildWallConfigurationPanelImpl(
               configuration, 
               initial -> { 
                  Optional< Font > result = new FontSelectorDialog( initial ).showAndWait();
                  if ( result.isPresent() ) return result.get();
                  else return null;
               } 
         ); 
      } );
      
      Thread.sleep( 10000000 );
   }//End Method
   
   @Test public void shouldContainNecessaryElements(){
      TitledPane dimensionsPane = systemUnderTest.dimensionsPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( dimensionsPane ) );
      
      GridPane dimensionsContent = ( GridPane )dimensionsPane.getContent();
      Assert.assertTrue( dimensionsContent.getChildren().contains( systemUnderTest.columnsSpinner() ) );
      
      TitledPane fontPane = systemUnderTest.fontPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( fontPane ) );
      
      GridPane fontContent = ( GridPane )fontPane.getContent();
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.jobNameFontButton() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.buildNumberFontButton() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.completionEstimateFontButton() ) );
      
      TitledPane colourPane = systemUnderTest.colourPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( colourPane ) );
      
      GridPane colourContent = ( GridPane )colourPane.getContent();
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.jobNameColourPicker() ) );
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.buildNumberColourPicker() ) );
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.completionEstimateColourPicker() ) );
   }//End Method
   
   @Test public void shouldUseInitialConfiguration(){
      Assert.assertEquals( configuration.numberOfColumns().get(), systemUnderTest.columnsSpinner().getValue().intValue() );
      
      Assert.assertEquals( configuration.jobNameFont().get().getName(), systemUnderTest.jobNameFontButton().getText() );
      Assert.assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobNameColourPicker().valueProperty().get() );
      
      Assert.assertEquals( configuration.buildNumberFont().get().getName(), systemUnderTest.buildNumberFontButton().getText() );
      Assert.assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumberColourPicker().valueProperty().get() );
      
      Assert.assertEquals( configuration.completionEstimateFont().get().getName(), systemUnderTest.completionEstimateFontButton().getText() );
      Assert.assertEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimateColourPicker().valueProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateColumnsSpinnerWhenConfigurationChanges(){
      Assert.assertEquals( configuration.numberOfColumns().get(), systemUnderTest.columnsSpinner().getValue().intValue() );
      configuration.numberOfColumns().set( 100 );
      Assert.assertEquals( 100, systemUnderTest.columnsSpinner().getValue().intValue() );
   }//End Method
   
   @Test public void shouldUpdateConfigurationWhenColumnsSpinnerChanges(){
      Assert.assertEquals( configuration.numberOfColumns().get(), systemUnderTest.columnsSpinner().getValue().intValue() );
      systemUnderTest.columnsSpinner().getValueFactory().setValue( 100 );
      Assert.assertEquals( 100, configuration.numberOfColumns().get() );
   }//End Method
   
   @Test public void columnsSpinnerShouldBeBound(){
      IntegerSpinnerValueFactory factory = ( IntegerSpinnerValueFactory )systemUnderTest.columnsSpinner().getValueFactory();
      Assert.assertEquals( 
               BuildWallConfigurationPanelImpl.MINIMUM_COLUMNS, 
               factory.getMin() 
      );
      Assert.assertEquals( 
               BuildWallConfigurationPanelImpl.MAXIMUM_COLUMNS, 
               factory.getMax() 
      );
   }//End Method
   
   @Test public void shouldProvideJobNameFontNameOnChooserButton() {
      configuration.jobNameFont().set( TEST_FONT_A );
      Assert.assertEquals( configuration.jobNameFont().get().getName(), systemUnderTest.jobNameFontButton().getText() );
      Assert.assertEquals( TEST_FONT_A, configuration.jobNameFont().get() );
      
      Mockito.when( fontSupplier.apply( TEST_FONT_A ) ).thenReturn( TEST_FONT_B );
      systemUnderTest.jobNameFontButton().fire();
      Assert.assertEquals( TEST_FONT_B.getName(), systemUnderTest.jobNameFontButton().getText() );
   }//End Method
   
   @Test public void shouldProvideBuildNumberFontNameOnChooserButton() {
      configuration.buildNumberFont().set( TEST_FONT_A );
      Assert.assertEquals( configuration.buildNumberFont().get().getName(), systemUnderTest.buildNumberFontButton().getText() );
      Assert.assertEquals( TEST_FONT_A, configuration.buildNumberFont().get() );
      
      Mockito.when( fontSupplier.apply( TEST_FONT_A ) ).thenReturn( TEST_FONT_B );
      systemUnderTest.buildNumberFontButton().fire();
      Assert.assertEquals( TEST_FONT_B.getName(), systemUnderTest.buildNumberFontButton().getText() );
   }//End Method
   
   @Test public void shouldProvideCompletionEstimateFontNameOnChooserButton() {
      configuration.completionEstimateFont().set( TEST_FONT_A );
      Assert.assertEquals( configuration.completionEstimateFont().get().getName(), systemUnderTest.completionEstimateFontButton().getText() );
      Assert.assertEquals( TEST_FONT_A, configuration.completionEstimateFont().get() );
      
      Mockito.when( fontSupplier.apply( TEST_FONT_A ) ).thenReturn( TEST_FONT_B );
      systemUnderTest.completionEstimateFontButton().fire();
      Assert.assertEquals( TEST_FONT_B.getName(), systemUnderTest.completionEstimateFontButton().getText() );
   }//End Method
   
   @Test public void shouldUpdateJobNameFontFromConfiguration(){
      configuration.jobNameFont().set( TEST_FONT_A );
      Assert.assertEquals( configuration.jobNameFont().get().getName(), systemUnderTest.jobNameFontButton().getText() );
      
      configuration.jobNameFont().set( TEST_FONT_B );
      
      Assert.assertEquals( configuration.jobNameFont().get().getName(), systemUnderTest.jobNameFontButton().getText() );

      configuration.jobNameFont().set( TEST_FONT_C );
      
      Assert.assertEquals( configuration.jobNameFont().get().getName(), systemUnderTest.jobNameFontButton().getText() );
   }//End Method
   
   @Test public void shouldSetJobNameFontInConfiguration(){
      configuration.jobNameFont().set( TEST_FONT_A );
      Assert.assertEquals( TEST_FONT_A, configuration.jobNameFont().get() );
      
      Mockito.when( fontSupplier.apply( TEST_FONT_A ) ).thenReturn( TEST_FONT_B );
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
      
      configuration.buildNumberFont().set( TEST_FONT_B );
      
      Assert.assertEquals( configuration.buildNumberFont().get().getName(), systemUnderTest.buildNumberFontButton().getText() );

      configuration.buildNumberFont().set( TEST_FONT_C );
      
      Assert.assertEquals( configuration.buildNumberFont().get().getName(), systemUnderTest.buildNumberFontButton().getText() );
   }//End Method
   
   @Test public void shouldSetBuildNumberFontInConfiguration(){
      configuration.buildNumberFont().set( TEST_FONT_A );
      Assert.assertEquals( TEST_FONT_A, configuration.buildNumberFont().get() );
      
      Mockito.when( fontSupplier.apply( TEST_FONT_A ) ).thenReturn( TEST_FONT_B );
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
      
      configuration.completionEstimateFont().set( TEST_FONT_B );
      Assert.assertEquals( configuration.completionEstimateFont().get().getName(), systemUnderTest.completionEstimateFontButton().getText() );

      configuration.completionEstimateFont().set( TEST_FONT_C );
      Assert.assertEquals( configuration.completionEstimateFont().get().getName(), systemUnderTest.completionEstimateFontButton().getText() );
   }//End Method
   
   @Test public void shouldSetCompletionEstimateFontInConfiguration(){
      configuration.completionEstimateFont().set( TEST_FONT_A );
      Assert.assertEquals( TEST_FONT_A, configuration.completionEstimateFont().get() );
      
      Mockito.when( fontSupplier.apply( TEST_FONT_A ) ).thenReturn( TEST_FONT_B );
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
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.columnsSpinnerLabel().getFont().getStyle() ) );
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
      assertColumnConstraints( ( GridPane )systemUnderTest.dimensionsPane().getContent() );
      assertColumnConstraints( ( GridPane )systemUnderTest.fontPane().getContent() );
      assertColumnConstraints( ( GridPane )systemUnderTest.colourPane().getContent() );
   }//End Method
   
   /**
    * Method to assert that the {@link ColumnConstraints} are present in the given {@link GridPane}.
    * @param grid the {@link GridPane} to check.
    */
   private void assertColumnConstraints( GridPane grid ){
      Assert.assertEquals( 
               BuildWallConfigurationPanelImpl.CONTROLS_PERCENTAGE_WIDTH, 
               grid.getColumnConstraints().get( 1 ).getPercentWidth(),
               TestCommon.precision()
      );
      Assert.assertEquals( Priority.ALWAYS, grid.getColumnConstraints().get( 1 ).getHgrow() );
      
      Assert.assertTrue( 
               grid.getColumnConstraints().get( 0 ).getPercentWidth() +
               grid.getColumnConstraints().get( 1 ).getPercentWidth() 
               >= 100
      );
   }//End Method

}//End Class
