/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import uk.dangrew.sd.graphics.launch.TestApplication;

public class WallBuilderTest {
   
   private static final double PREFERRED_WIDTH = 600;
   private static final double PREFERRED_HEIGHT = 400;

   private ContentArea initial;
   private WallBuilder systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      systemUnderTest = new WallBuilder();
      systemUnderTest.setPrefSize( PREFERRED_WIDTH, PREFERRED_HEIGHT );
      initial = getContent( 0 );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException{
      TestApplication.launch( () -> systemUnderTest );
      
      Thread.sleep( 5000 );
      
      systemUnderTest.splitVertically( initial );
      
      Thread.sleep( 5000 );
      
      systemUnderTest.splitHorizontally( initial );
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldUpdateContentAreasWithDimensionUpdates(){
      systemUnderTest.resize( 100, 101 );
      assertThat( initial.getWidth(), is( 100.0 ) );
      assertThat( initial.getHeight(), is( 101.0 ) );
   }//End Method

   @Test public void shouldHaveSingleContentAreaInitially() {
      assertThat( initial.getWidth(), is( systemUnderTest.getWidth() ) );
      assertThat( initial.getHeight(), is( systemUnderTest.getHeight() ) );
      assertThat( initial.getTranslateX(), is( 0.0 ) );
      assertThat( initial.getTranslateY(), is( 0.0 ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotSplitVeritcallyIfNotPresent(){
      systemUnderTest.splitVertically( mock( ContentArea.class ) );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotSplitHorizontallyIfNotPresent(){
      systemUnderTest.splitHorizontally( mock( ContentArea.class ) );
   }//End Method
   
   @Test public void shouldSplitInitialContentVertically(){
      systemUnderTest.splitVertically( initial );
      new ContentAreaAsserter( initial )
         .withDimensionPercentages( 100, 50 )
         .withPositionPercentages( 0, 0 )
         .assertArea();
      new ContentAreaAsserter( getContent( 1 ) )
         .withDimensionPercentages( 100, 50 )
         .withPositionPercentages( 0, 50 )
         .assertArea();
   }//End Method
   
   @Test public void shouldSplitInitialContentHorizontally(){
      systemUnderTest.splitHorizontally( initial );
      new ContentAreaAsserter( initial )
         .withDimensionPercentages( 50, 100 )
         .withPositionPercentages( 0, 0 )
         .assertArea();
      new ContentAreaAsserter( getContent( 1 ) )
         .withDimensionPercentages( 50, 100 )
         .withPositionPercentages( 50, 0 )
         .assertArea();
   }//End Method
   
   @Test public void shouldSplitContentVerticallyWithTranslation(){
      initial.changeXPositionPercentageBy( 20 );
      initial.changeYPositionPercentageBy( 40 );
      
      new ContentAreaAsserter( initial )
         .withDimensionPercentages( 80, 60 )
         .withPositionPercentages( 20, 40 )
         .assertArea();
      systemUnderTest.splitVertically( initial );
      
      new ContentAreaAsserter( initial )
         .withDimensionPercentages( 80, 30 )
         .withPositionPercentages( 20, 40 )
         .assertArea();
      new ContentAreaAsserter( getContent( 1 ) )
         .withDimensionPercentages( 80, 30 )
         .withPositionPercentages( 20, 70 )
         .assertArea();
   }//End Method
   
   @Test public void shouldSplitInitialContentHorizontallyWithTranslation(){
      initial.changeXPositionPercentageBy( 20 );
      initial.changeYPositionPercentageBy( 40 );
      
      new ContentAreaAsserter( initial )
         .withDimensionPercentages( 80, 60 )
         .withPositionPercentages( 20, 40 )
         .assertArea();
      systemUnderTest.splitHorizontally( initial );
      
      new ContentAreaAsserter( initial )
         .withDimensionPercentages( 40, 60 )
         .withPositionPercentages( 20, 40 )
         .assertArea();
      new ContentAreaAsserter( getContent( 1 ) )
         .withDimensionPercentages( 40, 60 )
         .withPositionPercentages( 60, 40 )
         .assertArea();
   }//End Method
   
   @Test public void shouldProvideSelectionController(){
      assertThat( systemUnderTest.selectionController(), is( instanceOf( ContentAreaSelector.class ) ) );
   }//End Method
   
   private ContentArea getContent( int index ) {
      assertThat( systemUnderTest.getChildren(), hasSize( lessThanOrEqualTo( index + 1 ) ) );
      return ( ContentArea )systemUnderTest.getChildren().get( index );
   }//End Method

}//End Class
