/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import buildwall.configuration.style.BuildWallConfigurationStyleTest;
import buildwall.panel.type.JobPanelDescriptionProviders;
import graphics.JavaFxInitializer;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.jobs.JenkinsJobImpl;
import styling.FontFamilies;
import utility.TestCommon;

/**
 * {@link BuildWallConfigurationPanelImpl} test.
 */
public class BuildWallConfigurationPaneImplTest {

   private static final String TEST_TITLE = "Some Title";
   private static final String TEST_FONT_FAMILY_A = FontFamilies.getUsableFontFamilies().get( 2 );
   private static final String TEST_FONT_FAMILY_B = FontFamilies.getUsableFontFamilies().get( 10 );
   private static final String TEST_FONT_FAMILY_C = FontFamilies.getUsableFontFamilies().get( 5 );
   private BuildWallConfiguration configuration;
   private BuildWallConfigurationPanelImpl systemUnderTest;
   
   @BeforeClass public static void debugFontFamilies(){
      for ( String family : FontFamilies.getUsableFontFamilies() ) {
         System.out.println( family );
         System.out.println( Font.font( family ).getFamily() );
      }
   }
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      configuration = new BuildWallConfigurationImpl();
      systemUnderTest = new BuildWallConfigurationPanelImpl( TEST_TITLE, configuration );
   }//End Method
   
   @Ignore //For manual inspection.
   @Test public void manualInspection() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> { 
         for ( int i = 0; i < 100; i++ ) {
            configuration.jobPolicies().put( new JenkinsJobImpl( "job " + i ), BuildWallJobPolicy.values()[ i % 3 ] );
         }
         return new BuildWallConfigurationPanelImpl( TEST_TITLE, configuration ); 
      } );
      
      Thread.sleep( 10000000 );
   }//End Method
   
   @Test public void shouldContainNecessaryElements(){
      Label label = systemUnderTest.titleLabel();
      Assert.assertTrue( systemUnderTest.getChildren().contains( label ) );
      
      TitledPane dimensionsPane = systemUnderTest.dimensionsPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( dimensionsPane ) );
      
      GridPane dimensionsContent = ( GridPane )dimensionsPane.getContent();
      Assert.assertTrue( dimensionsContent.getChildren().contains( systemUnderTest.columnsSpinner() ) );
      Assert.assertTrue( dimensionsContent.getChildren().contains( systemUnderTest.simpleDescriptionButton() ) );
      Assert.assertTrue( dimensionsContent.getChildren().contains( systemUnderTest.defaultDescriptionButton() ) );
      Assert.assertTrue( dimensionsContent.getChildren().contains( systemUnderTest.detailedDescriptionButton() ) );
      
      TitledPane fontPane = systemUnderTest.fontPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( fontPane ) );
      
      GridPane fontContent = ( GridPane )fontPane.getContent();
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.jobNameFontBox() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.buildNumberFontBox() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.completionEstimateFontBox() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.detailFontBox() ) );
      
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.jobNameFontSizeSpinner() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.buildNumberFontSizeSpinner() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.completionEstimateFontSizeSpinner() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.detailFontSizeSpinner() ) );
      
      TitledPane policiesPane = systemUnderTest.jobPoliciesPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( policiesPane ) );
      
      TitledPane colourPane = systemUnderTest.colourPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( colourPane ) );
      
      GridPane colourContent = ( GridPane )colourPane.getContent();
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.jobNameColourPicker() ) );
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.buildNumberColourPicker() ) );
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.completionEstimateColourPicker() ) );
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.detailColourPicker() ) );
   }//End Method
   
   @Test public void shouldUseInitialConfiguration(){
      Assert.assertEquals( configuration.numberOfColumns().get(), systemUnderTest.columnsSpinner().getValue().intValue() );
      
      Assert.assertEquals( 
               configuration.jobNameFont().get().getFamily(), 
               systemUnderTest.jobNameFontBox().getSelectionModel().getSelectedItem() 
      );
      Assert.assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobNameColourPicker().valueProperty().get() );
      assertThat( configuration.jobNameFont().get().getSize(), is( systemUnderTest.jobNameFontSizeSpinner().getValue().doubleValue() ) );
      
      Assert.assertEquals( 
               configuration.buildNumberFont().get().getFamily(), 
               systemUnderTest.buildNumberFontBox().getSelectionModel().getSelectedItem() 
      );
      Assert.assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumberColourPicker().valueProperty().get() );
      assertThat( configuration.buildNumberFont().get().getSize(), is( systemUnderTest.buildNumberFontSizeSpinner().getValue().doubleValue() ) );
      
      Assert.assertEquals( 
               configuration.completionEstimateFont().get().getFamily(), 
               systemUnderTest.completionEstimateFontBox().getSelectionModel().getSelectedItem() 
      );
      Assert.assertEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimateColourPicker().valueProperty().get() );
      assertThat( configuration.completionEstimateFont().get().getSize(), is( systemUnderTest.completionEstimateFontSizeSpinner().getValue().doubleValue() ) );
      
      Assert.assertEquals( 
               configuration.detailFont().get().getFamily(), 
               systemUnderTest.detailFontBox().getSelectionModel().getSelectedItem() 
      );
      Assert.assertEquals( configuration.detailColour().get(), systemUnderTest.detailColourPicker().valueProperty().get() );
      assertThat( configuration.detailFont().get().getSize(), is( systemUnderTest.detailFontSizeSpinner().getValue().doubleValue() ) );
      
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( false ) );
      assertThat( systemUnderTest.defaultDescriptionButton().isSelected(), is( true ) );
      assertThat( systemUnderTest.detailedDescriptionButton().isSelected(), is( false ) );
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

   @Test public void shouldUpdateJobNameFontSizeSpinnerWhenConfigurationChanges(){
      assertThat( configuration.jobNameFont().get().getSize(), is( systemUnderTest.jobNameFontSizeSpinner().getValue().doubleValue() ) );
      configuration.jobNameFont().set( Font.font( 54 ) );
      assertThat( systemUnderTest.jobNameFontSizeSpinner().getValue(), is( 54 ) );
   }//End Method
   
   @Test public void shouldUpdateConfigurationWhenJobNameFontSizeSpinnerChanges(){
      assertThat( configuration.jobNameFont().get().getSize(), is( systemUnderTest.jobNameFontSizeSpinner().getValue().doubleValue() ) );
      systemUnderTest.jobNameFontSizeSpinner().getValueFactory().setValue( 34 );
      assertThat( configuration.jobNameFont().get().getSize(), is( 34.0 ) );
   }//End Method
   
   @Test public void jobNameFontSizeSpinnerShouldBeBound(){
      IntegerSpinnerValueFactory factory = ( IntegerSpinnerValueFactory )systemUnderTest.jobNameFontSizeSpinner().getValueFactory();
      assertThat( factory.getMin(), is( BuildWallConfigurationPanelImpl.MINIMUM_FONT_SIZE ) );
      assertThat( factory.getMax(), is( BuildWallConfigurationPanelImpl.MAXIMUM_FONT_SIZE ) );
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
   
   @Test public void shouldUpdateBuildNumberFontSizeSpinnerWhenConfigurationChanges(){
      assertThat( configuration.buildNumberFont().get().getSize(), is( systemUnderTest.buildNumberFontSizeSpinner().getValue().doubleValue() ) );
      configuration.buildNumberFont().set( Font.font( 54 ) );
      assertThat( systemUnderTest.buildNumberFontSizeSpinner().getValue(), is( 54 ) );
   }//End Method
   
   @Test public void shouldUpdateConfigurationWhenBuildNumberFontSizeSpinnerChanges(){
      assertThat( configuration.buildNumberFont().get().getSize(), is( systemUnderTest.buildNumberFontSizeSpinner().getValue().doubleValue() ) );
      systemUnderTest.buildNumberFontSizeSpinner().getValueFactory().setValue( 34 );
      assertThat( configuration.buildNumberFont().get().getSize(), is( 34.0 ) );
   }//End Method
   
   @Test public void buildNumberFontSizeSpinnerShouldBeBound(){
      IntegerSpinnerValueFactory factory = ( IntegerSpinnerValueFactory )systemUnderTest.buildNumberFontSizeSpinner().getValueFactory();
      assertThat( factory.getMin(), is( BuildWallConfigurationPanelImpl.MINIMUM_FONT_SIZE ) );
      assertThat( factory.getMax(), is( BuildWallConfigurationPanelImpl.MAXIMUM_FONT_SIZE ) );
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
   
   @Test public void shouldUpdateCompletionEstimateFontSizeSpinnerWhenConfigurationChanges(){
      assertThat( configuration.completionEstimateFont().get().getSize(), is( systemUnderTest.completionEstimateFontSizeSpinner().getValue().doubleValue() ) );
      configuration.completionEstimateFont().set( Font.font( 54 ) );
      assertThat( systemUnderTest.completionEstimateFontSizeSpinner().getValue(), is( 54 ) );
   }//End Method
   
   @Test public void shouldUpdateConfigurationWhenCompletionEstimateFontSizeSpinnerChanges(){
      assertThat( configuration.completionEstimateFont().get().getSize(), is( systemUnderTest.completionEstimateFontSizeSpinner().getValue().doubleValue() ) );
      systemUnderTest.completionEstimateFontSizeSpinner().getValueFactory().setValue( 34 );
      assertThat( configuration.completionEstimateFont().get().getSize(), is( 34.0 ) );
   }//End Method
   
   @Test public void completionEstimateFontSizeSpinnerShouldBeBound(){
      IntegerSpinnerValueFactory factory = ( IntegerSpinnerValueFactory )systemUnderTest.completionEstimateFontSizeSpinner().getValueFactory();
      assertThat( factory.getMin(), is( BuildWallConfigurationPanelImpl.MINIMUM_FONT_SIZE ) );
      assertThat( factory.getMax(), is( BuildWallConfigurationPanelImpl.MAXIMUM_FONT_SIZE ) );
   }//End Method
   
   @Test public void shouldUpdateDetailFontFromConfiguration(){
      configuration.detailFont().set( Font.font( TEST_FONT_FAMILY_A ) );
      Assert.assertEquals( TEST_FONT_FAMILY_A, systemUnderTest.detailFontBox().getSelectionModel().getSelectedItem() );
      
      configuration.detailFont().set( Font.font( TEST_FONT_FAMILY_B ) );
      Assert.assertEquals( TEST_FONT_FAMILY_B, systemUnderTest.detailFontBox().getSelectionModel().getSelectedItem() );

      configuration.detailFont().set( Font.font( TEST_FONT_FAMILY_C ) );
      Assert.assertEquals( TEST_FONT_FAMILY_C, systemUnderTest.detailFontBox().getSelectionModel().getSelectedItem() );
   }//End Method
   
   @Test public void shouldSetDetailFontInConfiguration(){
      systemUnderTest.detailFontBox().getSelectionModel().select( TEST_FONT_FAMILY_A );
      Assert.assertEquals( TEST_FONT_FAMILY_A, configuration.detailFont().get().getFamily() );
      
      systemUnderTest.detailFontBox().getSelectionModel().select( TEST_FONT_FAMILY_B );
      Assert.assertEquals( TEST_FONT_FAMILY_B, configuration.detailFont().get().getFamily() );
   }//End Method
   
   @Test public void shouldUpdateDetailColourFromConfiguration(){
      configuration.detailColour().set( Color.BISQUE );
      Assert.assertEquals( configuration.detailColour().get(), systemUnderTest.detailColourPicker().valueProperty().get() );
      
      configuration.detailColour().set( Color.DARKORCHID );
      Assert.assertEquals( configuration.detailColour().get(), systemUnderTest.detailColourPicker().valueProperty().get() );
      
      configuration.detailColour().set( Color.MOCCASIN );
      Assert.assertEquals( configuration.detailColour().get(), systemUnderTest.detailColourPicker().valueProperty().get() );
   }//End Method
   
   @Test public void shouldSetCulpritsColourInConfiguration(){
      Assert.assertNotEquals( Color.AQUA, configuration.detailColour().get() );
      systemUnderTest.detailColourPicker().valueProperty().set( Color.AQUA );
      Assert.assertEquals( Color.AQUA, configuration.detailColour().get() );
   }//End Method
   
   @Test public void shouldUpdateDetailFontSizeSpinnerWhenConfigurationChanges(){
      assertThat( configuration.detailFont().get().getSize(), is( systemUnderTest.detailFontSizeSpinner().getValue().doubleValue() ) );
      configuration.detailFont().set( Font.font( 54 ) );
      assertThat( systemUnderTest.detailFontSizeSpinner().getValue(), is( 54 ) );
   }//End Method
   
   @Test public void shouldUpdateConfigurationWhenCulpritsFontSizeSpinnerChanges(){
      assertThat( configuration.detailFont().get().getSize(), is( systemUnderTest.detailFontSizeSpinner().getValue().doubleValue() ) );
      systemUnderTest.detailFontSizeSpinner().getValueFactory().setValue( 34 );
      assertThat( configuration.detailFont().get().getSize(), is( 34.0 ) );
   }//End Method
   
   @Test public void detailFontSizeSpinnerShouldBeBound(){
      IntegerSpinnerValueFactory factory = ( IntegerSpinnerValueFactory )systemUnderTest.detailFontSizeSpinner().getValueFactory();
      assertThat( factory.getMin(), is( BuildWallConfigurationPanelImpl.MINIMUM_FONT_SIZE ) );
      assertThat( factory.getMax(), is( BuildWallConfigurationPanelImpl.MAXIMUM_FONT_SIZE ) );
   }//End Method
   
   @Test public void eachSpinnerShouldBeEditable(){
      assertThat( systemUnderTest.columnsSpinner().isEditable(), is( true ) );
      assertThat( systemUnderTest.jobNameFontSizeSpinner().isEditable(), is( true ) );
      assertThat( systemUnderTest.buildNumberFontSizeSpinner().isEditable(), is( true ) );
      assertThat( systemUnderTest.completionEstimateFontSizeSpinner().isEditable(), is( true ) );
      assertThat( systemUnderTest.detailFontSizeSpinner().isEditable(), is( true ) );
   }//End Method
   
   @Test public void eachSpinnerShouldAcceptInvalidInput(){
      assertThat( 
               systemUnderTest.columnsSpinner().getValueFactory().getConverter().fromString( "anything" ), 
               is( systemUnderTest.columnsSpinner().getValue() ) 
      );
      assertThat( 
               systemUnderTest.jobNameFontSizeSpinner().getValueFactory().getConverter().fromString( "anything" ), 
               is( systemUnderTest.jobNameFontSizeSpinner().getValue() ) 
      );
      assertThat( 
               systemUnderTest.buildNumberFontSizeSpinner().getValueFactory().getConverter().fromString( "anything" ), 
               is( systemUnderTest.buildNumberFontSizeSpinner().getValue() ) 
      );
      assertThat( 
               systemUnderTest.completionEstimateFontSizeSpinner().getValueFactory().getConverter().fromString( "anything" ), 
               is( systemUnderTest.completionEstimateFontSizeSpinner().getValue() ) 
      );
      assertThat( 
               systemUnderTest.detailFontSizeSpinner().getValueFactory().getConverter().fromString( "anything" ), 
               is( systemUnderTest.detailFontSizeSpinner().getValue() ) 
      );
   }//End Method
   
   @Test public void shouldUseBoldLabels(){
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.titleLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.columnsSpinnerLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.desriptionTypeLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.jobNameFontLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.jobNameColourLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.jobNameFontSizeLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.buildNumberFontLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.buildNumberColourLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.buildNumberFontSizeLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.completionEstimateFontLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.completionEstimateColourLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.completionEstimateFontSizeLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.detailFontLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.detailColourLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.detailFontSizeLabel().getFont().getStyle() ) );
   }//End Method
   
   @Test public void eachPaneShouldCoverEntireWidth() {
      Assert.assertEquals( 1, systemUnderTest.getColumnConstraints().size() );
      Assert.assertEquals( 100, systemUnderTest.getColumnConstraints().get( 0 ).getPercentWidth(), TestCommon.precision() );
      Assert.assertEquals( Priority.ALWAYS, systemUnderTest.getColumnConstraints().get( 0 ).getHgrow() );
   }//End Method
   
   @Test public void eachPaneShouldShareWidthAmongstColumns() {
      assertColumnConstraints( ( GridPane )systemUnderTest.dimensionsPane().getContent() );
      assertColumnConstraints( ( GridPane )systemUnderTest.jobPoliciesPane().getContent() );
      assertColumnConstraints( ( GridPane )systemUnderTest.fontPane().getContent() );
      assertColumnConstraints( ( GridPane )systemUnderTest.colourPane().getContent() );
   }//End Method
   
   /**
    * Method to assert that the {@link ColumnConstraints} are present in the given {@link GridPane}.
    * @param grid the {@link GridPane} to check.
    */
   private void assertColumnConstraints( GridPane grid ){
      Assert.assertEquals( 
               BuildWallConfigurationStyleTest.CONTROLS_PERCENTAGE_WIDTH, 
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
   
   @Test public void simpleDescriptionShouldUpdateConfiguration(){
      systemUnderTest.defaultDescriptionButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.jobPanelDescriptionProvider().get(), not( JobPanelDescriptionProviders.Simple ) );
      
      systemUnderTest.simpleDescriptionButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.jobPanelDescriptionProvider().get(), is( JobPanelDescriptionProviders.Simple ) );
      assertThat( systemUnderTest.defaultDescriptionButton().isSelected(), is( false ) );
   }//End Method
   
   @Test public void defaultDescriptionShouldUpdateConfiguration(){
      systemUnderTest.simpleDescriptionButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.jobPanelDescriptionProvider().get(), not( JobPanelDescriptionProviders.Default ) );
      
      systemUnderTest.defaultDescriptionButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.jobPanelDescriptionProvider().get(), is( JobPanelDescriptionProviders.Default ) );
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( false ) );
   }//End Method
   
   @Test public void detailedDescriptionShouldUpdateConfiguration(){
      systemUnderTest.simpleDescriptionButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.jobPanelDescriptionProvider().get(), not( JobPanelDescriptionProviders.Detailed ) );
      
      systemUnderTest.detailedDescriptionButton().getOnAction().handle( new ActionEvent() );
      assertThat( configuration.jobPanelDescriptionProvider().get(), is( JobPanelDescriptionProviders.Detailed ) );
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( false ) );
   }//End Method
   
   @Test public void configurationShouldUpdateSimpleDescription(){
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Default );
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( false ) );
      
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Simple );
      assertThat( systemUnderTest.simpleDescriptionButton().isSelected(), is( true ) );
   }//End Method
   
   @Test public void configurationShouldUpdateDefaultDescription(){
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Simple );
      assertThat( systemUnderTest.defaultDescriptionButton().isSelected(), is( false ) );
      
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Default );
      assertThat( systemUnderTest.defaultDescriptionButton().isSelected(), is( true ) );
   }//End Method
   
   @Test public void configurationShouldUpdateDetailedDescription(){
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Simple );
      assertThat( systemUnderTest.detailedDescriptionButton().isSelected(), is( false ) );
      
      configuration.jobPanelDescriptionProvider().set( JobPanelDescriptionProviders.Detailed );
      assertThat( systemUnderTest.detailedDescriptionButton().isSelected(), is( true ) );
   }//End Method
   
   @Test public void shouldCreateTitleWithExpectedProperties(){
      Label titleLabel = systemUnderTest.titleLabel();
      assertThat( titleLabel.getFont().getSize(), closeTo( BuildWallConfigurationStyleTest.TITLE_FONT_SIZE, TestCommon.precision() ) );
      assertThat( GridPane.getColumnIndex( titleLabel ), is( 0 ) );
      assertThat( GridPane.getRowIndex( titleLabel ), is( 0 ) );
      assertThat( GridPane.getColumnSpan( titleLabel ), is( 2 ) );
      assertThat( GridPane.getRowSpan( titleLabel ), is( 1 ) );
      assertThat( GridPane.getHalignment( titleLabel ), is( HPos.CENTER ) );
      assertThat( GridPane.getValignment( titleLabel ), is( VPos.CENTER ) );
   }//End Method
   
   @Test public void titledPanesShouldBeClosedByDefault(){
      assertThat( systemUnderTest.dimensionsPane().isExpanded(), is( true ) );
      assertThat( systemUnderTest.jobPoliciesPane().isExpanded(), is( false ) );
      assertThat( systemUnderTest.fontPane().isExpanded(), is( true ) );
      assertThat( systemUnderTest.colourPane().isExpanded(), is( true ) );
   }//End Method
}//End Class
