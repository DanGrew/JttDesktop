/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

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
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.configuration.BuildWallConfigurationPanelImpl;
import uk.dangrew.jtt.buildwall.configuration.BuildWallJobPolicy;
import uk.dangrew.jtt.buildwall.configuration.style.BuildWallConfigurationStyleTest;
import uk.dangrew.jtt.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.styling.FontFamilies;
import uk.dangrew.jtt.utility.TestCommon;
import uk.dangrew.jtt.utility.TestableFonts;

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
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
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
   
   @Test public void shouldUseConfiguration(){
      assertThat( systemUnderTest.usesConfiguration( configuration ), is( true ) );
      assertThat( systemUnderTest.usesConfiguration( mock( BuildWallConfiguration.class ) ), is( false ) );
   }//End Method
   
   @Test public void shouldContainNecessaryElements(){
      Label label = systemUnderTest.titleLabel();
      Assert.assertTrue( systemUnderTest.getChildren().contains( label ) );
      
      TitledPane dimensionsPane = systemUnderTest.dimensionsPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( dimensionsPane ) );
      
      TitledPane fontPane = systemUnderTest.fontPane();
      Assert.assertTrue( systemUnderTest.getChildren().contains( fontPane ) );
      
      GridPane fontContent = ( GridPane )fontPane.getContent();
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.jobNameFontLabel() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.jobNameFontSizeLabel() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.buildNumberFontLabel() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.buildNumberFontSizeLabel() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.completionEstimateFontLabel() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.completionEstimateFontSizeLabel() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.detailFontLabel() ) );
      Assert.assertTrue( fontContent.getChildren().contains( systemUnderTest.detailFontSizeLabel() ) );
      
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
      
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.jobNameColourLabel() ) );
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.buildNumberColourLabel() ) );
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.completionEstimateColourLabel() ) );
      Assert.assertTrue( colourContent.getChildren().contains( systemUnderTest.detailColourLabel() ) );
   }//End Method
   
   @Test public void jobNameFontFamilyShouldUseInitialConfiguration(){
      configuration.jobNameFont().set( Font.font( TestableFonts.commonFont() ) );
      assertEquals( 
               configuration.jobNameFont().get().getFamily(), 
               systemUnderTest.jobNameFontBox().getSelectionModel().getSelectedItem() 
      );
   }//End Method
   
   @Test public void jobNameColourShouldUseInitialConfiguration(){
      configuration.jobNameColour().set( Color.RED );
      assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobNameColourPicker().valueProperty().get() );
   }//End Method
   
   @Test public void jobNameFontSizeShouldUseInitialConfiguration(){
      configuration.jobNameFont().set( Font.font( 56 ) );
      assertThat( configuration.jobNameFont().get().getSize(), is( systemUnderTest.jobNameFontSizeSpinner().getValue().doubleValue() ) );
   }//End Method
   
   @Test public void buildNumberFontFamilyShouldUseInitialConfiguration(){
      configuration.jobNameFont().set( Font.font( TestableFonts.commonFont() ) );
      assertEquals( 
               configuration.buildNumberFont().get().getFamily(), 
               systemUnderTest.buildNumberFontBox().getSelectionModel().getSelectedItem() 
      );
   }//End Method
   
   @Test public void buildNumberColourShouldUseInitialConfiguration(){
      configuration.buildNumberColour().set( Color.ANTIQUEWHITE );
      assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumberColourPicker().valueProperty().get() );
   }//End Method
   
   @Test public void buildNumberFontColourShouldUseInitialConfiguration(){
      configuration.buildNumberFont().set( Font.font( 56 ) );
      assertThat( configuration.buildNumberFont().get().getSize(), is( systemUnderTest.buildNumberFontSizeSpinner().getValue().doubleValue() ) );
   }//End Method
   
   @Test public void completionEstimateFontFamilyShouldUseInitialConfiguration(){
      configuration.completionEstimateFont().set( Font.font( TestableFonts.commonFont() ) );
      assertEquals( 
               configuration.completionEstimateFont().get().getFamily(), 
               systemUnderTest.completionEstimateFontBox().getSelectionModel().getSelectedItem() 
      );
   }//End Method
   
   @Test public void completionEstimateColourShouldUseInitialConfiguration(){
      configuration.completionEstimateColour().set( Color.BISQUE );
      assertEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimateColourPicker().valueProperty().get() );
   }//End Method
   
   @Test public void completionEstimateFontSizeShouldUseInitialConfiguration(){
      configuration.completionEstimateFont().set( Font.font( 56 ) );
      assertThat( configuration.completionEstimateFont().get().getSize(), is( systemUnderTest.completionEstimateFontSizeSpinner().getValue().doubleValue() ) );
   }//End Method
   
   @Test public void detailFontFamilyShouldUseInitialConfiguration(){
      configuration.detailFont().set( Font.font( TestableFonts.commonFont() ) );
      assertEquals( 
               configuration.detailFont().get().getFamily(), 
               systemUnderTest.detailFontBox().getSelectionModel().getSelectedItem() 
      );
   }//End Method
   
   @Test public void detailColourShouldUseInitialConfiguration(){
      configuration.detailColour().set( Color.BURLYWOOD );
      assertEquals( configuration.detailColour().get(), systemUnderTest.detailColourPicker().valueProperty().get() );
   }//End Method
   
   @Test public void detailFontSizeShouldUseInitialConfiguration(){
      configuration.detailFont().set( Font.font( 56 ) );
      assertThat( configuration.detailFont().get().getSize(), is( systemUnderTest.detailFontSizeSpinner().getValue().doubleValue() ) );
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
      assertThat( systemUnderTest.jobNameFontSizeSpinner().isEditable(), is( true ) );
      assertThat( systemUnderTest.buildNumberFontSizeSpinner().isEditable(), is( true ) );
      assertThat( systemUnderTest.completionEstimateFontSizeSpinner().isEditable(), is( true ) );
      assertThat( systemUnderTest.detailFontSizeSpinner().isEditable(), is( true ) );
   }//End Method
   
   @Test public void eachSpinnerShouldAcceptInvalidInput(){
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
   
   @Test public void componentsShouldSpreadToTheWidth(){
      assertThat( systemUnderTest.jobNameFontBox().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.buildNumberFontBox().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.completionEstimateFontBox().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.detailFontBox().getMaxWidth(), is( Double.MAX_VALUE ) );
      
      assertThat( systemUnderTest.jobNameColourPicker().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.buildNumberColourPicker().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.completionEstimateColourPicker().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.detailColourPicker().getMaxWidth(), is( Double.MAX_VALUE ) );
      
      assertThat( systemUnderTest.jobNameFontSizeSpinner().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.buildNumberFontSizeSpinner().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.completionEstimateFontSizeSpinner().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.detailFontSizeSpinner().getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void colourPickersShouldTakeConfiguredValueInitially(){
      configuration.jobNameColour().set( Color.RED );
      configuration.buildNumberColour().set( Color.RED );
      configuration.completionEstimateColour().set( Color.RED );
      configuration.detailColour().set( Color.RED );
      
      systemUnderTest = new BuildWallConfigurationPanelImpl( TEST_TITLE, configuration );
      
      assertThat( systemUnderTest.jobNameColourPicker().getValue(), is( Color.RED ) );
      assertThat( systemUnderTest.buildNumberColourPicker().getValue(), is( Color.RED ) );
      assertThat( systemUnderTest.completionEstimateColourPicker().getValue(), is( Color.RED ) );
      assertThat( systemUnderTest.detailColourPicker().getValue(), is( Color.RED ) );
   }//End Method
}//End Class
