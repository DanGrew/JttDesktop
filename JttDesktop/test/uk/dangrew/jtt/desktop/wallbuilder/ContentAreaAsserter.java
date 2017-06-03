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
   
   private Double translateX;
   private Double translateY;
   private Double positionPercentageX;
   private Double positionPercentageY;
   
   private Double width;
   private Double height;
   private Double widthPercentage;
   private Double heightPercentage;
   
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
    * @param x provides the expected position value.
    * @param y provides the expected position value.
    * @return this.
    */
   ContentAreaAsserter withPositionPercentages( double x, double y ) {
      this.positionPercentageX = x;
      this.positionPercentageY = y;
      return this;
   }//End Method
   
   /**
    * Builder pattern.
    * @param w provides the expected width value.
    * @param h provides the expected height value.
    * @return this.
    */
   ContentAreaAsserter withDimensionPercentages( double w, double h ) {
      this.widthPercentage = w;
      this.heightPercentage = h;
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
      
      if ( positionPercentageX != null ) {
         assertThat( systemUnderTest.xPositionPercentage(), is( positionPercentageX.doubleValue() ) );
      }
      
      if ( positionPercentageY != null ) {
         assertThat( systemUnderTest.yPositionPercentage(), is( positionPercentageY.doubleValue() ) );
      }
      
      if ( width != null ) {
         assertThat( systemUnderTest.getWidth(), is( width.doubleValue() ) );
      }
      
      if ( height != null ) {
         assertThat( systemUnderTest.getHeight(), is( height.doubleValue() ) );
      }
      
      if ( widthPercentage != null ) {
         assertThat( systemUnderTest.percentageWidth(), is( widthPercentage.doubleValue() ) );
      }
      
      if ( heightPercentage != null ) {
         assertThat( systemUnderTest.percentageHeight(), is( heightPercentage.doubleValue() ) );
      }
   }//End Method
}//End Class
