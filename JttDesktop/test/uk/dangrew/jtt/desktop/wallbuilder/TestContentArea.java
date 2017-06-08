/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

/**
 * {@link ContentArea} with some default {@link ContentBoundary}s.
 */
public class TestContentArea extends ContentArea {

   /**
    * Constructs a new {@link TestContentArea}.
    */
   TestContentArea() {
      super( 
               new ContentBoundary( 30 ),
               new ContentBoundary( 20 ),
               new ContentBoundary( 70 ),
               new ContentBoundary( 80 ),
               1000,
               1000
      );
   }//End Constructor

}//End Class
