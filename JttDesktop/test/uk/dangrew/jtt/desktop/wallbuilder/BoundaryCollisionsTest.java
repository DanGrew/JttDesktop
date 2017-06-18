/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class BoundaryCollisionsTest {

   private static final double DIMENSION = 1000;
   
   private BoundaryCollisions systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new BoundaryCollisions();
   }//End Method

   @Test public void shouldHandleNoCollisions() {
      ContentBoundary left1 = new ContentBoundary( 0 );
      ContentBoundary top1 = new ContentBoundary( 0 );
      ContentBoundary right1 = new ContentBoundary( 50 );
      ContentBoundary bottom1 = new ContentBoundary( 100 );
      
      ContentBoundary left2 = right1;
      ContentBoundary top2 = top1;
      ContentBoundary right2 = new ContentBoundary( 100 );
      ContentBoundary bottom2 = bottom1;
      
      ContentArea area1 = new ContentArea( left1, top1, right1, bottom1, DIMENSION, DIMENSION );
      ContentArea area2 = new ContentArea( left2, top2, right2, bottom2, DIMENSION, DIMENSION );
      
      right1.changePosition( 10 );
      systemUnderTest.mergeBoundaries( right1 );
      
      assertThat( area1.leftBoundary(), is( left1 ) );
      assertThat( area1.topBoundary(), is( top1 ) );
      assertThat( area1.rightBoundary(), is( right1 ) );
      assertThat( area1.bottomBoundary(), is( bottom1 ) );
      
      assertThat( area2.leftBoundary(), is( left2 ) );
      assertThat( area2.topBoundary(), is( top2 ) );
      assertThat( area2.rightBoundary(), is( right2 ) );
      assertThat( area2.bottomBoundary(), is( bottom2 ) );
   }//End Method
   
   @Test public void shouldMergeVerticalCollisionWithLeft() {
      ContentBoundary left1 = new ContentBoundary( 0 );
      ContentBoundary top1 = new ContentBoundary( 0 );
      ContentBoundary right1 = new ContentBoundary( 50 );
      ContentBoundary bottom1 = new ContentBoundary( 100 );
      
      ContentBoundary left2 = right1;
      ContentBoundary top2 = top1;
      ContentBoundary right2 = new ContentBoundary( 100 );
      ContentBoundary bottom2 = bottom1;
      
      ContentArea area1 = new ContentArea( left1, top1, right1, bottom1, DIMENSION, DIMENSION );
      ContentArea area2 = new ContentArea( left2, top2, right2, bottom2, DIMENSION, DIMENSION );
      
      right1.changePosition( -60 );
      systemUnderTest.mergeBoundaries( right1 );
      
      assertThat( area1.leftBoundary(), is( nullValue() ) );
      assertThat( area1.topBoundary(), is( top1 ) );
      assertThat( area1.rightBoundary(), is( nullValue() ) );
      assertThat( area1.bottomBoundary(), is( bottom1 ) );
      
      assertThat( area2.leftBoundary(), is( left1 ) );
      assertThat( area2.topBoundary(), is( top2 ) );
      assertThat( area2.rightBoundary(), is( right2 ) );
      assertThat( area2.bottomBoundary(), is( bottom2 ) );
   }//End Method
   
   @Test public void shouldMergeVerticalCollisionWithRight() {
      ContentBoundary left1 = new ContentBoundary( 0 );
      ContentBoundary top1 = new ContentBoundary( 0 );
      ContentBoundary right1 = new ContentBoundary( 50 );
      ContentBoundary bottom1 = new ContentBoundary( 100 );
      
      ContentBoundary left2 = right1;
      ContentBoundary top2 = top1;
      ContentBoundary right2 = new ContentBoundary( 100 );
      ContentBoundary bottom2 = bottom1;
      
      ContentArea area1 = new ContentArea( left1, top1, right1, bottom1, DIMENSION, DIMENSION );
      ContentArea area2 = new ContentArea( left2, top2, right2, bottom2, DIMENSION, DIMENSION );
      
      right1.changePosition( 60 );
      systemUnderTest.mergeBoundaries( right1 );
      
      assertThat( area1.leftBoundary(), is( left1 ) );
      assertThat( area1.topBoundary(), is( top1 ) );
      assertThat( area1.rightBoundary(), is( right2 ) );
      assertThat( area1.bottomBoundary(), is( bottom1 ) );
      
      assertThat( area2.leftBoundary(), is( nullValue() ) );
      assertThat( area2.topBoundary(), is( top2 ) );
      assertThat( area2.rightBoundary(), is( nullValue() ) );
      assertThat( area2.bottomBoundary(), is( bottom2 ) );
   }//End Method
   
   @Test public void shouldMergeHorizontalCollisionWithTop() {
      ContentBoundary left1 = new ContentBoundary( 0 );
      ContentBoundary top1 = new ContentBoundary( 0 );
      ContentBoundary right1 = new ContentBoundary( 100 );
      ContentBoundary bottom1 = new ContentBoundary( 50 );
      
      ContentBoundary left2 = left1;
      ContentBoundary top2 = bottom1;
      ContentBoundary right2 = right1;
      ContentBoundary bottom2 = new ContentBoundary( 100 );
      
      ContentArea area1 = new ContentArea( left1, top1, right1, bottom1, DIMENSION, DIMENSION );
      ContentArea area2 = new ContentArea( left2, top2, right2, bottom2, DIMENSION, DIMENSION );
      
      bottom1.changePosition( -60 );
      systemUnderTest.mergeBoundaries( bottom1 );
      
      assertThat( area1.leftBoundary(), is( left1 ) );
      assertThat( area1.topBoundary(), is( nullValue() ) );
      assertThat( area1.rightBoundary(), is( right1 ) );
      assertThat( area1.bottomBoundary(), is( nullValue() ) );
      
      assertThat( area2.leftBoundary(), is( left2 ) );
      assertThat( area2.topBoundary(), is( top1 ) );
      assertThat( area2.rightBoundary(), is( right2 ) );
      assertThat( area2.bottomBoundary(), is( bottom2 ) );
   }//End Method
   
   @Test public void shouldMergeHorizontalCollisionWithBottom() {
      ContentBoundary left1 = new ContentBoundary( 0 );
      ContentBoundary top1 = new ContentBoundary( 0 );
      ContentBoundary right1 = new ContentBoundary( 100 );
      ContentBoundary bottom1 = new ContentBoundary( 50 );
      
      ContentBoundary left2 = left1;
      ContentBoundary top2 = bottom1;
      ContentBoundary right2 = right1;
      ContentBoundary bottom2 = new ContentBoundary( 100 );
      
      ContentArea area1 = new ContentArea( left1, top1, right1, bottom1, DIMENSION, DIMENSION );
      ContentArea area2 = new ContentArea( left2, top2, right2, bottom2, DIMENSION, DIMENSION );
      
      bottom1.changePosition( 60 );
      systemUnderTest.mergeBoundaries( bottom1 );
      
      assertThat( area1.leftBoundary(), is( left1 ) );
      assertThat( area1.topBoundary(), is( top1 ) );
      assertThat( area1.rightBoundary(), is( right1 ) );
      assertThat( area1.bottomBoundary(), is( bottom2 ) );
      
      assertThat( area2.leftBoundary(), is( left2 ) );
      assertThat( area2.topBoundary(), is( nullValue() ) );
      assertThat( area2.rightBoundary(), is( right2 ) );
      assertThat( area2.bottomBoundary(), is( nullValue() ) );
   }//End Method

   @Test public void shouldMergeTwoAreasOntoBoundary() {
      ContentBoundary left1 = new ContentBoundary( 0 );
      ContentBoundary top1 = new ContentBoundary( 0 );
      ContentBoundary right1 = new ContentBoundary( 50 );
      ContentBoundary bottom1 = new ContentBoundary( 50 );
      
      ContentBoundary left2 = left1;
      ContentBoundary top2 = bottom1;
      ContentBoundary right2 = right1;
      ContentBoundary bottom2 = new ContentBoundary( 100 );
      
      ContentBoundary left3 = right1;
      ContentBoundary top3 = top1;
      ContentBoundary right3 = new ContentBoundary( 100 );
      ContentBoundary bottom3 = bottom2;
      
      ContentArea area1 = new ContentArea( left1, top1, right1, bottom1, DIMENSION, DIMENSION );
      ContentArea area2 = new ContentArea( left2, top2, right2, bottom2, DIMENSION, DIMENSION );
      ContentArea area3 = new ContentArea( left3, top3, right3, bottom3, DIMENSION, DIMENSION );
      
      right1.changePosition( 60 );
      systemUnderTest.mergeBoundaries( right1 );
      
      assertThat( area1.leftBoundary(), is( left1 ) );
      assertThat( area1.topBoundary(), is( top1 ) );
      assertThat( area1.rightBoundary(), is( right3 ) );
      assertThat( area1.bottomBoundary(), is( bottom1 ) );
      
      assertThat( area2.leftBoundary(), is( left2 ) );
      assertThat( area2.topBoundary(), is( top2 ) );
      assertThat( area2.rightBoundary(), is( right3 ) );
      assertThat( area2.bottomBoundary(), is( bottom2 ) );
      
      assertThat( area3.leftBoundary(), is( nullValue() ) );
      assertThat( area3.topBoundary(), is( top3 ) );
      assertThat( area3.rightBoundary(), is( nullValue() ) );
      assertThat( area3.bottomBoundary(), is( bottom3 ) );
   }//End Method

   @Test public void shouldMergeBoundaryOntoTwoAreas() {
      ContentBoundary left1 = new ContentBoundary( 0 );
      ContentBoundary top1 = new ContentBoundary( 0 );
      ContentBoundary right1 = new ContentBoundary( 50 );
      ContentBoundary bottom1 = new ContentBoundary( 50 );
      
      ContentBoundary left2 = left1;
      ContentBoundary top2 = bottom1;
      ContentBoundary right2 = right1;
      ContentBoundary bottom2 = new ContentBoundary( 100 );
      
      ContentBoundary left3 = right1;
      ContentBoundary top3 = top1;
      ContentBoundary right3 = new ContentBoundary( 100 );
      ContentBoundary bottom3 = bottom2;
      
      ContentArea area1 = new ContentArea( left1, top1, right1, bottom1, DIMENSION, DIMENSION );
      ContentArea area2 = new ContentArea( left2, top2, right2, bottom2, DIMENSION, DIMENSION );
      ContentArea area3 = new ContentArea( left3, top3, right3, bottom3, DIMENSION, DIMENSION );
      
      right1.changePosition( -50 );
      systemUnderTest.mergeBoundaries( right1 );
      
      assertThat( area1.leftBoundary(), is( nullValue() ) );
      assertThat( area1.topBoundary(), is( top1 ) );
      assertThat( area1.rightBoundary(), is( nullValue() ) );
      assertThat( area1.bottomBoundary(), is( bottom1 ) );
      
      assertThat( area2.leftBoundary(), is( nullValue() ) );
      assertThat( area2.topBoundary(), is( top2 ) );
      assertThat( area2.rightBoundary(), is( nullValue() ) );
      assertThat( area2.bottomBoundary(), is( bottom2 ) );
      
      assertThat( area3.leftBoundary(), is( left1 ) );
      assertThat( area3.topBoundary(), is( top3 ) );
      assertThat( area3.rightBoundary(), is( right3 ) );
      assertThat( area3.bottomBoundary(), is( bottom3 ) );
   }//End Method
   
   @Test public void shouldCollopseMiddleAreaWhenMergedRight() {
      ContentBoundary left1 = new ContentBoundary( 0 );
      ContentBoundary top1 = new ContentBoundary( 0 );
      ContentBoundary right1 = new ContentBoundary( 25 );
      ContentBoundary bottom1 = new ContentBoundary( 100 );
      
      ContentBoundary left2 = right1;
      ContentBoundary top2 = top1;
      ContentBoundary right2 = new ContentBoundary( 50 );
      ContentBoundary bottom2 = bottom1;
      
      ContentBoundary left3 = right2;
      ContentBoundary top3 = top1;
      ContentBoundary right3 = new ContentBoundary( 100 );
      ContentBoundary bottom3 = bottom1;
      
      ContentArea area1 = new ContentArea( left1, top1, right1, bottom1, DIMENSION, DIMENSION );
      ContentArea area2 = new ContentArea( left2, top2, right2, bottom2, DIMENSION, DIMENSION );
      ContentArea area3 = new ContentArea( left3, top3, right3, bottom3, DIMENSION, DIMENSION );
      
      right1.changePosition( 25 );
      systemUnderTest.mergeBoundaries( right1 );
      
      assertThat( area1.leftBoundary(), is( left1 ) );
      assertThat( area1.topBoundary(), is( top1 ) );
      assertThat( area1.rightBoundary(), is( left3 ) );
      assertThat( area1.bottomBoundary(), is( bottom1 ) );
      
      assertThat( area2.leftBoundary(), is( nullValue() ) );
      assertThat( area2.topBoundary(), is( top2 ) );
      assertThat( area2.rightBoundary(), is( nullValue() ) );
      assertThat( area2.bottomBoundary(), is( bottom2 ) );

      assertThat( area3.leftBoundary(), is( left3 ) );
      assertThat( area3.topBoundary(), is( top3 ) );
      assertThat( area3.rightBoundary(), is( right3 ) );
      assertThat( area3.bottomBoundary(), is( bottom3 ) );
   }//End Method
   
}//End Class
