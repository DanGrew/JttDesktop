/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static uk.dangrew.jtt.model.utility.TestCommon.precision;

import org.junit.Before;
import org.junit.Test;

import javafx.scene.paint.Color;

public class ContentAreaTest {

   private static final double LEFT_POSITION = 45;
   private static final double RIGHT_POSITION = 80;
   private static final double TOP_POSITION = 5;
   private static final double BOTTOM_POSITION = 70;
   
   private static final double INITIAL_PARENT_WIDTH = 50;
   private static final double INITIAL_PARENT_HEIGHT = 200;
   
   private ContentBoundary left;
   private ContentBoundary right;
   private ContentBoundary top;
   private ContentBoundary bottom;
   private ContentArea systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      left = new ContentBoundary( LEFT_POSITION );
      right = new ContentBoundary( RIGHT_POSITION );
      top = new ContentBoundary( TOP_POSITION );
      bottom = new ContentBoundary( BOTTOM_POSITION );
      
      systemUnderTest = new ContentArea( 
               left, top, right, bottom,
               INITIAL_PARENT_WIDTH, INITIAL_PARENT_HEIGHT
      );
   }//End Method

   @Test public void shouldProvideContentStyle() {
      assertThat( systemUnderTest.getFill(), is( Color.BLACK ) );
   }//End Method
   
   @Test public void shouldCalculateInitialDimensions(){
      assertThat( systemUnderTest.getTranslateX(), is( 22.5 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 10.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 17.5 ) );
      assertThat( systemUnderTest.getHeight(), is( 130.0 ) );
   }//End Method
   
   @Test public void shouldChangeLeftBoundary(){
      ContentBoundary newLeft = new ContentBoundary( 20 );
      systemUnderTest.setLeftBoundary( newLeft );
      
      assertThat( systemUnderTest.getTranslateX(), is( 10.0 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 10.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 17.5 ) );
      assertThat( systemUnderTest.getHeight(), is( 130.0 ) );
   }//End Method
   
   @Test public void shouldRespondToLeftBoundary(){
      left.changePosition( 10 );
      
      assertThat( systemUnderTest.getTranslateX(), is( closeTo( 27.5, precision() ) ) );
      assertThat( systemUnderTest.getTranslateY(), is( 10.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 12.5 ) );
      assertThat( systemUnderTest.getHeight(), is( 130.0 ) );
   }//End Method
   
   @Test public void shouldNotRespondToOldLeftBoundary(){
      ContentBoundary newLeft = new ContentBoundary( LEFT_POSITION );
      systemUnderTest.setLeftBoundary( newLeft );
      
      left.changePosition( 10 );
      
      assertThat( systemUnderTest.getTranslateX(), is( 22.5 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 10.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 17.5 ) );
      assertThat( systemUnderTest.getHeight(), is( 130.0 ) );
   }//End Method
   
   @Test public void shouldChangeRightBoundary(){
      ContentBoundary newRight = new ContentBoundary( 90 );
      systemUnderTest.setRightBoundary( newRight );
      
      assertThat( systemUnderTest.getTranslateX(), is( 22.5 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 10.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 22.5 ) );
      assertThat( systemUnderTest.getHeight(), is( 130.0 ) );
   }//End Method
   
   @Test public void shouldRespondToRightBoundary(){
      right.changePosition( 10 );
      
      assertThat( systemUnderTest.getTranslateX(), is( 22.5 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 10.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 22.5 ) );
      assertThat( systemUnderTest.getHeight(), is( 130.0 ) );
   }//End Method
   
   @Test public void shouldNotRespondToOldRightBoundary(){
      ContentBoundary newRight = new ContentBoundary( RIGHT_POSITION );
      systemUnderTest.setRightBoundary( newRight );
      
      right.changePosition( 10 );
      
      assertThat( systemUnderTest.getTranslateX(), is( 22.5 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 10.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 17.5 ) );
      assertThat( systemUnderTest.getHeight(), is( 130.0 ) );
   }//End Method
   
   @Test public void shouldChangeTopBoundary(){
      ContentBoundary newTop = new ContentBoundary( 10 );
      systemUnderTest.setTopBoundary( newTop );
      
      assertThat( systemUnderTest.getTranslateX(), is( 22.5 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 20.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 17.5 ) );
      assertThat( systemUnderTest.getHeight(), is( 130.0 ) );
   }//End Method
   
   @Test public void shouldRespondToTopBoundary(){
      top.changePosition( 10 );
      
      assertThat( systemUnderTest.getTranslateX(), is( 22.5 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 30.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 17.5 ) );
      assertThat( systemUnderTest.getHeight(), is( closeTo( 110.0, precision() ) ) );
   }//End Method
   
   @Test public void shouldNotRespondToOldTopBoundary(){
      ContentBoundary newTop = new ContentBoundary( TOP_POSITION );
      systemUnderTest.setTopBoundary( newTop );
      
      top.changePosition( 10 );
      
      assertThat( systemUnderTest.getTranslateX(), is( 22.5 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 10.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 17.5 ) );
      assertThat( systemUnderTest.getHeight(), is( 130.0 ) );
   }//End Method
   
   @Test public void shouldChangeBottomBoundary(){
      ContentBoundary newBottom = new ContentBoundary( 80 );
      systemUnderTest.setBottomBoundary( newBottom );
      
      assertThat( systemUnderTest.getTranslateX(), is( 22.5 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 10.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 17.5 ) );
      assertThat( systemUnderTest.getHeight(), is( 150.0 ) );
   }//End Method
   
   @Test public void shouldRespondToBottomBoundary(){
      bottom.changePosition( 10 );
      
      assertThat( systemUnderTest.getTranslateX(), is( 22.5 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 10.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 17.5 ) );
      assertThat( systemUnderTest.getHeight(), is( 150.0 ) );
   }//End Method
   
   @Test public void shouldNotRespondToOldBottomBoundary(){
      ContentBoundary newBottom = new ContentBoundary( BOTTOM_POSITION );
      systemUnderTest.setBottomBoundary( newBottom );
      
      bottom.changePosition( 10 );
      
      assertThat( systemUnderTest.getTranslateX(), is( 22.5 ) );
      assertThat( systemUnderTest.getTranslateY(), is( 10.0 ) );
      assertThat( systemUnderTest.getWidth(), is( 17.5 ) );
      assertThat( systemUnderTest.getHeight(), is( 130.0 ) );
   }//End Method
   
   @Test public void shouldProvideBoundaries(){
      assertThat( systemUnderTest.leftBoundary(), is( left ) );
      assertThat( systemUnderTest.rightBoundary(), is( right ) );
      assertThat( systemUnderTest.topBoundary(), is( top ) );
      assertThat( systemUnderTest.bottomBoundary(), is( bottom ) );
   }//End Method
}//End Class
