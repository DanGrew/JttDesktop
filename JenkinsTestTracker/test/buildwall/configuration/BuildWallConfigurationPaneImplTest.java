/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

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

   private static final String TEST_FONT_FAMILY_A = Font.getFamilies().get( 3 );
   private static final String TEST_FONT_FAMILY_B = Font.getFamilies().get( 10 );
   private static final String TEST_FONT_FAMILY_C = Font.getFamilies().get( 5 );
   private BuildWallConfiguration configuration;
   private BuildWallConfigurationPanelImpl systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      configuration = new BuildWallConfigurationImpl();
      systemUnderTest = new BuildWallConfigurationPanelImpl( configuration );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> { 
         return new BuildWallConfigurationPanelImpl( configuration ); 
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
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.jobNameFontBox() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.buildNumberFontBox() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.completionEstimateFontBox() ) );
      
      TitledPane colourPane = systemUnderTest.colourPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( colourPane ) );
      
      GridPane colourContent = ( GridPane )colourPane.getContent();
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.jobNameColourPicker() ) );
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.buildNumberColourPicker() ) );
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.completionEstimateColourPicker() ) );
   }//End Method
   
   @Test public void shouldUseInitialConfiguration(){
      Assert.assertEquals( configuration.numberOfColumns().get(), systemUnderTest.columnsSpinner().getValue().intValue() );
      
      Assert.assertEquals( 
               configuration.jobNameFont().get().getFamily(), 
               systemUnderTest.jobNameFontBox().getSelectionModel().getSelectedItem() 
      );
      Assert.assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobNameColourPicker().valueProperty().get() );
      
      Assert.assertEquals( 
               configuration.buildNumberFont().get().getFamily(), 
               systemUnderTest.buildNumberFontBox().getSelectionModel().getSelectedItem() 
      );
      Assert.assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumberColourPicker().valueProperty().get() );
      
      Assert.assertEquals( 
               configuration.completionEstimateFont().get().getFamily(), 
               systemUnderTest.completionEstimateFontBox().getSelectionModel().getSelectedItem() 
      );
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
   
   @Test public void shouldUpdateJobNameFontFromConfiguration(){
      configuration.jobNameFont().set( Font.font( TEST_FONT_FAMILY_A ) );
      Assert.assertEquals( TEST_FONT_FAMILY_A, systemUnderTest.jobNameFontBox().getSelectionModel().getSelectedItem() );
      
      configuration.jobNameFont().set( Font.font( TEST_FONT_FAMILY_B ) );
      Assert.assertEquals( TEST_FONT_FAMILY_B, systemUnderTest.jobNameFontBox().getSelectionModel().getSelectedItem() );

      configuration.jobNameFont().set( Font.font( TEST_FONT_FAMILY_C ) );
      Assert.assertEquals( TEST_FONT_FAMILY_C, systemUnderTest.jobNameFontBox().getSelectionModel().getSelectedItem() );
   }//End Method
   
   @Test public void shouldSetJobNameFontInConfiguration(){
      systemUnderTest.jobNameFontBox().getSelectionModel().select( TEST_FONT_FAMILY_A );
      Assert.assertEquals( TEST_FONT_FAMILY_A, configuration.jobNameFont().get().getFamily() );
      
      systemUnderTest.jobNameFontBox().getSelectionModel().select( TEST_FONT_FAMILY_B );
      Assert.assertEquals( TEST_FONT_FAMILY_B, configuration.jobNameFont().get().getFamily() );
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
      configuration.buildNumberFont().set( Font.font( TEST_FONT_FAMILY_A ) );
      Assert.assertEquals( 
               TEST_FONT_FAMILY_A, 
               systemUnderTest.buildNumberFontBox().getSelectionModel().getSelectedItem() 
      );
      
      configuration.buildNumberFont().set( Font.font( TEST_FONT_FAMILY_B ) );
      Assert.assertEquals( 
               TEST_FONT_FAMILY_B, 
               systemUnderTest.buildNumberFontBox().getSelectionModel().getSelectedItem() 
      );

      configuration.buildNumberFont().set( Font.font( TEST_FONT_FAMILY_C ) );
      Assert.assertEquals( 
               TEST_FONT_FAMILY_C, 
               systemUnderTest.buildNumberFontBox().getSelectionModel().getSelectedItem() 
      );
   }//End Method
   
   @Test public void shouldSetBuildNumberFontInConfiguration(){
      systemUnderTest.buildNumberFontBox().getSelectionModel().select( TEST_FONT_FAMILY_A );
      Assert.assertEquals( TEST_FONT_FAMILY_A, configuration.buildNumberFont().get().getFamily() );
      
      systemUnderTest.buildNumberFontBox().getSelectionModel().select( TEST_FONT_FAMILY_B );
      Assert.assertEquals( TEST_FONT_FAMILY_B, configuration.buildNumberFont().get().getFamily() );
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
      configuration.completionEstimateFont().set( Font.font( TEST_FONT_FAMILY_A ) );
      Assert.assertEquals( TEST_FONT_FAMILY_A, systemUnderTest.completionEstimateFontBox().getSelectionModel().getSelectedItem() );
      
      configuration.completionEstimateFont().set( Font.font( TEST_FONT_FAMILY_B ) );
      Assert.assertEquals( TEST_FONT_FAMILY_B, systemUnderTest.completionEstimateFontBox().getSelectionModel().getSelectedItem() );

      configuration.completionEstimateFont().set( Font.font( TEST_FONT_FAMILY_C ) );
      Assert.assertEquals( TEST_FONT_FAMILY_C, systemUnderTest.completionEstimateFontBox().getSelectionModel().getSelectedItem() );
   }//End Method
   
   @Test public void shouldSetCompletionEstimateFontInConfiguration(){
      systemUnderTest.completionEstimateFontBox().getSelectionModel().select( TEST_FONT_FAMILY_A );
      Assert.assertEquals( TEST_FONT_FAMILY_A, configuration.completionEstimateFont().get().getFamily() );
      
      systemUnderTest.completionEstimateFontBox().getSelectionModel().select( TEST_FONT_FAMILY_B );
      Assert.assertEquals( TEST_FONT_FAMILY_B, configuration.completionEstimateFont().get().getFamily() );
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
