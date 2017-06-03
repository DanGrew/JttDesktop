/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import uk.dangrew.sd.graphics.launch.TestApplication;

public class ContentAreaTest {

   private static final double PARENT_WIDTH = 100;
   private static final double PARENT_HIEGHT = 200;
   
   private static final double POSITION_X_PERCENTAGE = 40;
   private static final double POSITION_Y_PERCENTAGE = 60;
   
   private static final double WIDTH_PERCENTAGE = 90;
   private static final double HEIGHT_PERCENTAGE = 80;
   private ContentArea systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new ContentArea( 
               PARENT_WIDTH, PARENT_HIEGHT,
               POSITION_X_PERCENTAGE, POSITION_Y_PERCENTAGE,
               WIDTH_PERCENTAGE, HEIGHT_PERCENTAGE
      );
   }//End Method

   @Ignore
   @Test public void manual() throws InterruptedException{
      TestApplication.launch( () -> new Group( systemUnderTest ) );
      
      Thread.sleep( 5000 );
      systemUnderTest.changeWidthPercentageBy( 30 );
      systemUnderTest.changeHeightPercentageBy( 90 );
      
      Thread.sleep( 5000 );
      systemUnderTest.changeXPositionPercentageBy( 10 );
      systemUnderTest.changeXPositionPercentageBy( -10 );
      
      Thread.sleep( 5000 );
      systemUnderTest.setParentDimensions( 300, 200 );
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldProvideContentStyle() {
      assertThat( systemUnderTest.getFill(), is( Color.BLACK ) );
   }//End Method
   
   @Test public void shouldCalculateInitialPosition(){
      assertThat( systemUnderTest.getTranslateX(), is( 40.0 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 120.0 ) );
   }//End Method
   
   @Test public void shouldCalculateInitialWidthAndHeight(){
      assertThat( systemUnderTest.getWidth(), is( 90.0 ) );
      assertThat( systemUnderTest.getHeight(), is( 160.0 ) );
   }//End Method
   
   @Test public void shouldUpdateXPositionUsingPercentage(){
      systemUnderTest.changeXPositionPercentageBy( 30 );
      assertThat( systemUnderTest.getTranslateX(), is( 70.0 ) );
   }//End Method
   
   @Test public void shouldUpdateYPositionUsingPercentage(){
      systemUnderTest.changeYPositionPercentageBy( 10 );
      assertThat( systemUnderTest.getTranslateY(), is( 140.0 ) );
   }//End Method
   
   @Test public void shouldRecalculateWhenParentDimensionChanges(){
      systemUnderTest.setParentDimensions( 300, 100 );
      assertThat( systemUnderTest.getTranslateX(), is( 120.0 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 60.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 270.0 ) );
      assertThat( systemUnderTest.getHeight(), is( 80.0 ) );
   }//End Method
   
   @Test public void shouldUpdateWidthUsingParentPercentage(){
      systemUnderTest.changeWidthPercentageBy( -50 );
      assertThat( systemUnderTest.getWidth(), is( 40.0 ) );
   }//End Method
   
   @Test public void shouldUpdateHeightUsingParentPercentage(){
      systemUnderTest.changeHeightPercentageBy( -40 );
      assertThat( systemUnderTest.getHeight(), is( 80.0 ) );
   }//End Method
   
   @Test public void shouldCapTranslationAtMax(){
      systemUnderTest.changeXPositionPercentageBy( 100 );
      assertThat( systemUnderTest.getTranslateX(), is( PARENT_WIDTH ) );
      assertThat( systemUnderTest.xPositionPercentage(), is( 100.0 ) );
      
      systemUnderTest.changeYPositionPercentageBy( 100 );
      assertThat( systemUnderTest.getTranslateY(), is( PARENT_HIEGHT ) );
      assertThat( systemUnderTest.yPositionPercentage(), is( 100.0 ) );
   }//End Method
   
   @Test public void shouldCapTranslationAtMin(){
      systemUnderTest.changeXPositionPercentageBy( -100 );
      assertThat( systemUnderTest.getTranslateX(), is( 0.0 ) );
      assertThat( systemUnderTest.xPositionPercentage(), is( 0.0 ) );
      
      systemUnderTest.changeYPositionPercentageBy( -100 );
      assertThat( systemUnderTest.getTranslateY(), is( 0.0 ) );
      assertThat( systemUnderTest.yPositionPercentage(), is( 0.0 ) );
   }//End Method
   
   @Test public void shouldCapDimensionsAtMax(){
      systemUnderTest.changeWidthPercentageBy( 100 );
      assertThat( systemUnderTest.getWidth(), is( PARENT_WIDTH ) );
      assertThat( systemUnderTest.percentageWidth(), is( 100.0 ) );
      
      systemUnderTest.changeHeightPercentageBy( 100 );
      assertThat( systemUnderTest.getHeight(), is( PARENT_HIEGHT ) );
      assertThat( systemUnderTest.percentageHeight(), is( 100.0 ) );
   }//End Method
   
   @Test public void shouldCapDimensionsAtMin(){
      systemUnderTest.changeWidthPercentageBy( -100 );
      assertThat( systemUnderTest.getWidth(), is( 0.0 ) );
      assertThat( systemUnderTest.percentageWidth(), is( 0.0 ) );
      
      systemUnderTest.changeHeightPercentageBy( -100 );
      assertThat( systemUnderTest.getHeight(), is( 0.0 ) );
      assertThat( systemUnderTest.percentageHeight(), is( 0.0 ) );
   }//End Method
   
   @Test public void shouldUpdateWidthWhenTranslationChanges(){
      double initialWidth = systemUnderTest.percentageWidth();
      systemUnderTest.changeXPositionPercentageBy( 10 );
      assertThat( systemUnderTest.percentageWidth(), is( initialWidth - 10 ) );
      
      double initialHeight = systemUnderTest.percentageHeight();
      systemUnderTest.changeYPositionPercentageBy( 10 );
      assertThat( systemUnderTest.percentageHeight(), is( initialHeight - 10 ) );
   }//End Method

}//End Class
