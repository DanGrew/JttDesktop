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
import org.junit.Test;

public class SquareBoundaryTest {
   
   private ContentArea area;
   
   @Before public void initialiseSystemUnderTest(){
      area = new TestContentArea();
   }//End Method

   @Test public void shouldProvideLeftBoundary() {
      assertThat( SquareBoundary.Left.boundary( area ), is( area.leftBoundary() ) );
   }//End Method
   
   @Test public void shouldProvideTopBoundary() {
      assertThat( SquareBoundary.Top.boundary( area ), is( area.topBoundary() ) );
   }//End Method
   
   @Test public void shouldProvideRightBoundary() {
      assertThat( SquareBoundary.Right.boundary( area ), is( area.rightBoundary() ) );
   }//End Method
   
   @Test public void shouldProvideBottomBoundary() {
      assertThat( SquareBoundary.Bottom.boundary( area ), is( area.bottomBoundary() ) );
   }//End Method

   @Test public void shouldPushLeft(){
      double current = area.leftBoundary().positionPercentage();
      SquareBoundary.Left.push( area, 5 );
      assertThat( area.leftBoundary().positionPercentage(), is( current - 5 ) );
   }//End Method
   
   @Test public void shouldPullLeft(){
      double current = area.leftBoundary().positionPercentage();
      SquareBoundary.Left.pull( area, 5 );
      assertThat( area.leftBoundary().positionPercentage(), is( current + 5 ) );
   }//End Method
   
   @Test public void shouldPushRight(){
      double current = area.rightBoundary().positionPercentage();
      SquareBoundary.Right.push( area, 5 );
      assertThat( area.rightBoundary().positionPercentage(), is( current + 5 ) );
   }//End Method
   
   @Test public void shouldPullRight(){
      double current = area.rightBoundary().positionPercentage();
      SquareBoundary.Right.pull( area, 5 );
      assertThat( area.rightBoundary().positionPercentage(), is( current - 5 ) );
   }//End Method
   
   @Test public void shouldPushTop(){
      double current = area.topBoundary().positionPercentage();
      SquareBoundary.Top.push( area, 5 );
      assertThat( area.topBoundary().positionPercentage(), is( current - 5 ) );
   }//End Method
   
   @Test public void shouldPullTop(){
      double current = area.topBoundary().positionPercentage();
      SquareBoundary.Top.pull( area, 5 );
      assertThat( area.topBoundary().positionPercentage(), is( current + 5 ) );
   }//End Method
   
   @Test public void shouldPushBottom(){
      double current = area.bottomBoundary().positionPercentage();
      SquareBoundary.Bottom.push( area, 5 );
      assertThat( area.bottomBoundary().positionPercentage(), is( current + 5 ) );
   }//End Method
   
   @Test public void shouldPullBottom(){
      double current = area.bottomBoundary().positionPercentage();
      SquareBoundary.Bottom.pull( area, 5 );
      assertThat( area.bottomBoundary().positionPercentage(), is( current - 5 ) );
   }//End Method
   
   @Test public void shouldSetLeft(){
      ContentBoundary boundary = new ContentBoundary( 45 );
      SquareBoundary.Left.set( boundary, area );
      assertThat( area.leftBoundary(), is( boundary ) );
   }//End Method
   
   @Test public void shouldSetTop(){
      ContentBoundary boundary = new ContentBoundary( 45 );
      SquareBoundary.Top.set( boundary, area );
      assertThat( area.topBoundary(), is( boundary ) );
   }//End Method
   
   @Test public void shouldSetRight(){
      ContentBoundary boundary = new ContentBoundary( 45 );
      SquareBoundary.Right.set( boundary, area );
      assertThat( area.rightBoundary(), is( boundary ) );
   }//End Method
   
   @Test public void shouldSetBottom(){
      ContentBoundary boundary = new ContentBoundary( 45 );
      SquareBoundary.Bottom.set( boundary, area );
      assertThat( area.bottomBoundary(), is( boundary ) );
   }//End Method
   
   @Test public void shouldProvideOpposite(){
      assertThat( SquareBoundary.Left.opposite(), is( SquareBoundary.Right ) );
      assertThat( SquareBoundary.Top.opposite(), is( SquareBoundary.Bottom ) );
      assertThat( SquareBoundary.Right.opposite(), is( SquareBoundary.Left ) );
      assertThat( SquareBoundary.Bottom.opposite(), is( SquareBoundary.Top ) );
   }//End Method
}//End Class
