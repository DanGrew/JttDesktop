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

/**
 * {@link ContentAreaAsserter} provides a convenient, builder pattern like, asserter for
 * {@link ContentArea}s.
 */
class ContentAreaAsserter {

   private final ContentArea systemUnderTest;
   
   private Double left;
   private Double top;
   private Double right;
   private Double bottom;
   
   private Double translateX;
   private Double translateY;
   private Double width;
   private Double height;
   
   /**
    * Constructs a new {@link ContentAreaAsserter}.
    * @param area the {@link ContentArea} to test.
    */
   ContentAreaAsserter( ContentArea area ) {
      this.systemUnderTest = area;
   }//End Method
   
   /**
    * Builder pattern.
    * @param x provides the expected translation value.
    * @param y provides the expected translation value.
    * @return this.
    */
   ContentAreaAsserter withTranslation( double x, double y ) {
      this.translateX = x;
      this.translateY = y;
      return this;
   }//End Method
   
   /**
    * Builder pattern.
    * @param w provides the expected width value.
    * @param h provides the expected height value.
    * @return this.
    */
   ContentAreaAsserter withDimensions( double w, double h ) {
      this.width = w;
      this.height = h;
      return this;
   }//End Method
   
   /**
    * Builder pattern.
    * @param t provides the expected position value.
    * @param l provides the expected position value.
    * @return this.
    */
   ContentAreaAsserter withTopLeft( double t, double l ) {
      this.left = l;
      this.top = t;
      return this;
   }//End Method
   
   /**
    * Builder pattern.
    * @param b provides the expected position value.
    * @param r provides the expected position value.
    * @return this.
    */
   ContentAreaAsserter withBottomRight( double b, double r ) {
      this.right = r;
      this.bottom = b;
      return this;
   }//End Method
   
   /**
    * Perform the assertions for the values provided.
    */
   void assertArea() {
      if ( translateX != null ) {
         assertThat( systemUnderTest.getTranslateX(), is( translateX.doubleValue() ) );
      }
      
      if ( translateY != null ) {
         assertThat( systemUnderTest.getTranslateY(), is( translateY.doubleValue() ) );
      }
      
      if ( left != null ) {
         assertThat( systemUnderTest.leftBoundary().positionPercentage(), is( left.doubleValue() ) );
      }
      
      if ( top != null ) {
         assertThat( systemUnderTest.topBoundary().positionPercentage(), is( top.doubleValue() ) );
      }
      
      if ( width != null ) {
         assertThat( systemUnderTest.getWidth(), is( width.doubleValue() ) );
      }
      
      if ( height != null ) {
         assertThat( systemUnderTest.getHeight(), is( height.doubleValue() ) );
      }
      
      if ( right != null ) {
         assertThat( systemUnderTest.rightBoundary().positionPercentage(), is( right.doubleValue() ) );
      }
      
      if ( bottom != null ) {
         assertThat( systemUnderTest.bottomBoundary().positionPercentage(), is( bottom.doubleValue() ) );
      }
   }//End Method
}//End Class
