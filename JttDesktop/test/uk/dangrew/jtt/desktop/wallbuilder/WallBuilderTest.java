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
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.dangrew.sd.graphics.launch.TestApplication;

public class WallBuilderTest {
   
   private static final double PREFERRED_WIDTH = 600;
   private static final double PREFERRED_HEIGHT = 400;

   private ContentArea initial;
   
   @Mock private ContentAreaSelector selector;
   @Spy private ContentAreaIntersections intersections;
   private WallBuilder systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new WallBuilder( selector, intersections );
      systemUnderTest.setPrefSize( PREFERRED_WIDTH, PREFERRED_HEIGHT );
      initial = getContent( 0 );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException{
      TestApplication.launch( () -> new WallBuilder() );
      
      Thread.sleep( 5000 );
      
      systemUnderTest.splitVertically();
      
      Thread.sleep( 5000 );
      
      systemUnderTest.splitHorizontally();
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldSetNodesOnSelector(){
      verify( selector ).setNodes( systemUnderTest.getChildren() );
   }//End Method
   
   @Test public void shouldSetNodesOnIntersections(){
      verify( intersections ).setNodes( systemUnderTest.getChildren() );
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
   
   @Test public void shouldIgnoreSplitVeritcallyIfNoSelection(){
      systemUnderTest.splitVertically();
   }//End Method
   
   @Test public void shouldIgnoreSplitHorizontllyIfNoSelection(){
      systemUnderTest.splitHorizontally();
   }//End Method
   
   @Test public void shouldSplitInitialContentVertically(){
      when( selector.getSelection() ).thenReturn( initial );
      
      systemUnderTest.splitVertically();
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
      when( selector.getSelection() ).thenReturn( initial );
      
      systemUnderTest.splitHorizontally();
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
      
      when( selector.getSelection() ).thenReturn( initial );
      systemUnderTest.splitVertically();
      
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
      
      when( selector.getSelection() ).thenReturn( initial );
      systemUnderTest.splitHorizontally();
      
      new ContentAreaAsserter( initial )
         .withDimensionPercentages( 40, 60 )
         .withPositionPercentages( 20, 40 )
         .assertArea();
      new ContentAreaAsserter( getContent( 1 ) )
         .withDimensionPercentages( 40, 60 )
         .withPositionPercentages( 60, 40 )
         .assertArea();
   }//End Method
   
   @Test public void shouldStretchLeft(){
      when( selector.getSelection() ).thenReturn( initial );
      systemUnderTest.splitHorizontally();
      ContentArea subject = getContent( 1 );
      
      when( selector.getSelection() ).thenReturn( subject );
      
      systemUnderTest.stretchLeft( 10 );
      assertThat( subject.percentageWidth(), is( 60.0 ) );
      
      verify( intersections ).checkTranslationXIntersection( subject );
      assertThat( initial.percentageWidth(), is( 40.0 ) );
   }//End Method
   
   @Test public void shouldStretchLeft2(){
      when( selector.getSelection() ).thenReturn( initial );
      systemUnderTest.splitHorizontally();
      ContentArea subject = getContent( 0 );
      
      when( selector.getSelection() ).thenReturn( subject );
      
      systemUnderTest.stretchLeft( 10 );
      assertThat( subject.percentageWidth(), is( 50.0 ) );
      
      verify( intersections ).checkTranslationXIntersection( subject );
      assertThat( getContent( 1 ).percentageWidth(), is( 50.0 ) );
   }//End Method
   
   @Test public void shouldStretchRight(){
      when( selector.getSelection() ).thenReturn( initial );
      systemUnderTest.splitHorizontally();
      ContentArea subject = getContent( 0 );
      
      when( selector.getSelection() ).thenReturn( subject );
      
      systemUnderTest.stretchRight( 10 );
      assertThat( subject.percentageWidth(), is( 60.0 ) );
      
      verify( intersections ).checkWidthIntersection( subject );
   }//End Method
   
   @Test public void shouldStretchUp(){
      when( selector.getSelection() ).thenReturn( initial );
      systemUnderTest.splitVertically();
      ContentArea subject = getContent( 1 );
      
      when( selector.getSelection() ).thenReturn( subject );
      
      systemUnderTest.stretchUp( 10 );
      assertThat( subject.percentageHeight(), is( 60.0 ) );
      
      verify( intersections ).checkTranslationYIntersection( subject );
   }//End Method
   
   @Test public void shouldStretchDown(){
      when( selector.getSelection() ).thenReturn( initial );
      systemUnderTest.splitVertically();
      ContentArea subject = getContent( 0 );
      
      when( selector.getSelection() ).thenReturn( subject );
      
      systemUnderTest.stretchDown( 10 );
      assertThat( subject.percentageHeight(), is( 60.0 ) );
      
      verify( intersections ).checkHeightIntersection( subject );
   }//End Method
   
   @Test public void shouldIgnoreStretchesWithNoSelection(){
      systemUnderTest.stretchLeft( 10 );
      systemUnderTest.stretchRight( 10 );
      systemUnderTest.stretchUp( 10 );
      systemUnderTest.stretchDown( 10 );
      
      verify( intersections ).setNodes( systemUnderTest.getChildren() );
      verifyNoMoreInteractions( intersections );
   }//End Method
   
   @Test public void shouldProvideSelectionController(){
      assertThat( systemUnderTest.selectionController(), is( instanceOf( ContentAreaSelector.class ) ) );
   }//End Method
   
   private ContentArea getContent( int index ) {
      assertThat( systemUnderTest.getChildren(), hasSize( greaterThanOrEqualTo( index + 1 ) ) );
      return ( ContentArea )systemUnderTest.getChildren().get( index );
   }//End Method

}//End Class
