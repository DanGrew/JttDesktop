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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import uk.dangrew.jtt.buildwall.configuration.style.BuildWallConfigurationStyleTest;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.styling.FontFamilies;
import uk.dangrew.jtt.utility.TestCommon;

/**
 * {@link BuildWallConfigurationPanelImpl} test.
 */
public class BuildWallConfigurationPaneImplTest {

   private static final String TEST_TITLE = "Some Title";
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
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.titleLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.jobNameColourLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.buildNumberColourLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.completionEstimateColourLabel().getFont().getStyle() ) );
      Assert.assertEquals( FontWeight.BOLD, FontWeight.findByName( systemUnderTest.detailColourLabel().getFont().getStyle() ) );
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
      
      systemUnderTest = new BuildWallConfigurationPanelImpl( TEST_TITLE, configuration );
      
      assertThat( systemUnderTest.jobNameColourPicker().getValue(), is( Color.RED ) );
      assertThat( systemUnderTest.buildNumberColourPicker().getValue(), is( Color.RED ) );
      assertThat( systemUnderTest.completionEstimateColourPicker().getValue(), is( Color.RED ) );
      assertThat( systemUnderTest.detailColourPicker().getValue(), is( Color.RED ) );
   }//End Method
}//End Class
