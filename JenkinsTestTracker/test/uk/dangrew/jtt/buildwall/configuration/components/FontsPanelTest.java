/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.control.SpinnerValueFactory.IntegerSpinnerValueFactory;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.configuration.style.BuildWallConfigurationStyle;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.styling.FontFamilies;
import uk.dangrew.jtt.utility.TestableFonts;

/**
 * {@link FontsPanel} test.
 */
public class FontsPanelTest {

   private static final String TEST_FONT_FAMILY_A = FontFamilies.getUsableFontFamilies().get( 2 );
   private static final String TEST_FONT_FAMILY_B = FontFamilies.getUsableFontFamilies().get( 10 );
   private static final String TEST_FONT_FAMILY_C = FontFamilies.getUsableFontFamilies().get( 5 );
   
   @Spy private BuildWallConfigurationStyle styling;
   private BuildWallConfiguration configuration;
   private FontsPanel systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      configuration = new BuildWallConfigurationImpl();
      systemUnderTest = new FontsPanel( configuration, styling );
   }//End Method
   
   @Test public void shouldContainAllChildElements() {
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.jobNameFontLabel() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.jobNameFontSizeLabel() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.buildNumberFontLabel() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.buildNumberFontSizeLabel() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.completionEstimateFontLabel() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.completionEstimateFontSizeLabel() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.detailFontLabel() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.detailFontSizeLabel() ) );
      
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.jobNameFontBox() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.buildNumberFontBox() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.completionEstimateFontBox() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.detailFontBox() ) );
      
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.jobNameFontSizeSpinner() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.buildNumberFontSizeSpinner() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.completionEstimateFontSizeSpinner() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.detailFontSizeSpinner() ) );
   }//End Method
   
   @Test public void jobNameFontFamilyShouldUseInitialConfiguration(){
      configuration.jobNameFont().set( Font.font( TestableFonts.commonFont() ) );
      assertEquals( 
               configuration.jobNameFont().get().getFamily(), 
               systemUnderTest.jobNameFontBox().getSelectionModel().getSelectedItem() 
      );
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
      assertThat( factory.getMin(), is( FontsPanel.MINIMUM_FONT_SIZE ) );
      assertThat( factory.getMax(), is( FontsPanel.MAXIMUM_FONT_SIZE ) );
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
      assertThat( factory.getMin(), is( FontsPanel.MINIMUM_FONT_SIZE ) );
      assertThat( factory.getMax(), is( FontsPanel.MAXIMUM_FONT_SIZE ) );
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
      assertThat( factory.getMin(), is( FontsPanel.MINIMUM_FONT_SIZE ) );
      assertThat( factory.getMax(), is( FontsPanel.MAXIMUM_FONT_SIZE ) );
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
      assertThat( factory.getMin(), is( FontsPanel.MINIMUM_FONT_SIZE ) );
      assertThat( factory.getMax(), is( FontsPanel.MAXIMUM_FONT_SIZE ) );
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
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.jobNameFontLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.jobNameFontSizeLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.buildNumberFontLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.buildNumberFontSizeLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.completionEstimateFontLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.completionEstimateFontSizeLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.detailFontLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.detailFontSizeLabel().getFont().getStyle() ) );
   }//End Method

   @Test public void componentsShouldSpreadToTheWidth(){
      assertThat( systemUnderTest.jobNameFontBox().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.buildNumberFontBox().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.completionEstimateFontBox().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.detailFontBox().getMaxWidth(), is( Double.MAX_VALUE ) );
      
      assertThat( systemUnderTest.jobNameFontSizeSpinner().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.buildNumberFontSizeSpinner().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.completionEstimateFontSizeSpinner().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.detailFontSizeSpinner().getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void shouldDetermineWhetherItHasGivenConfiguration(){
      assertThat( systemUnderTest.hasConfiguration( configuration ), is( true ) );
      assertThat( systemUnderTest.hasConfiguration( new BuildWallConfigurationImpl() ), is( false ) );
   }//End Method
   
}//End Class
