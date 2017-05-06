/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import uk.dangrew.jtt.desktop.buildwall.configuration.components.ColoursPanel;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;

/**
 * {@link ColoursPanel} test.
 */
public class ColoursPanelTest {

   @Spy private JavaFxStyle styling;
   private BuildWallConfiguration configuration;
   private ColoursPanel systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      configuration = new BuildWallConfigurationImpl();
      systemUnderTest = new ColoursPanel( configuration, styling );
   }//End Method
   
   @Test public void shouldContainNecessaryElements(){
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.jobNameColourPicker() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.buildNumberColourPicker() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.completionEstimateColourPicker() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.detailColourPicker() ) );
      
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.jobNameColourLabel() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.buildNumberColourLabel() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.completionEstimateColourLabel() ) );
      Assert.assertTrue( systemUnderTest.getChildren().contains( systemUnderTest.detailColourLabel() ) );
   }//End Method

   @Test public void jobNameColourShouldUseInitialConfiguration(){
      configuration.jobNameColour().set( Color.RED );
      assertEquals( configuration.jobNameColour().get(), systemUnderTest.jobNameColourPicker().valueProperty().get() );
   }//End Method
   
   @Test public void buildNumberColourShouldUseInitialConfiguration(){
      configuration.buildNumberColour().set( Color.ANTIQUEWHITE );
      assertEquals( configuration.buildNumberColour().get(), systemUnderTest.buildNumberColourPicker().valueProperty().get() );
   }//End Method
   
   @Test public void completionEstimateColourShouldUseInitialConfiguration(){
      configuration.completionEstimateColour().set( Color.BISQUE );
      assertEquals( configuration.completionEstimateColour().get(), systemUnderTest.completionEstimateColourPicker().valueProperty().get() );
   }//End Method
   
   @Test public void detailColourShouldUseInitialConfiguration(){
      configuration.detailColour().set( Color.BURLYWOOD );
      assertEquals( configuration.detailColour().get(), systemUnderTest.detailColourPicker().valueProperty().get() );
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
   
   @Test public void shouldUseBoldLabels(){
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.jobNameColourLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.buildNumberColourLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.completionEstimateColourLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.detailColourLabel().getFont().getStyle() ) );
   }//End Method
   
   @Test public void componentsShouldSpreadToTheWidth(){
      assertThat( systemUnderTest.jobNameColourPicker().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.buildNumberColourPicker().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.completionEstimateColourPicker().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.detailColourPicker().getMaxWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void colourPickersShouldTakeConfiguredValueInitially(){
      configuration.jobNameColour().set( Color.RED );
      configuration.buildNumberColour().set( Color.RED );
      configuration.completionEstimateColour().set( Color.RED );
      configuration.detailColour().set( Color.RED );
      
      systemUnderTest = new ColoursPanel( configuration );
      
      assertThat( systemUnderTest.jobNameColourPicker().getValue(), is( Color.RED ) );
      assertThat( systemUnderTest.buildNumberColourPicker().getValue(), is( Color.RED ) );
      assertThat( systemUnderTest.completionEstimateColourPicker().getValue(), is( Color.RED ) );
      assertThat( systemUnderTest.detailColourPicker().getValue(), is( Color.RED ) );
   }//End Method
   
   @Test public void shouldApplyStylingToPanel(){
      verify( styling ).configureColumnConstraints( systemUnderTest );
   }//End Method
   
   @Test public void shouldDetermineWhetherItHasGivenConfiguration(){
      assertThat( systemUnderTest.hasConfiguration( configuration ), is( true ) );
      assertThat( systemUnderTest.hasConfiguration( new BuildWallConfigurationImpl() ), is( false ) );
   }//End Method
}//End Class
