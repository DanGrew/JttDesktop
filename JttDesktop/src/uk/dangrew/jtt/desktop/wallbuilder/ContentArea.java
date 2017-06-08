/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import javafx.beans.value.ChangeListener;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * {@link ContentArea} respresents an area on the {@link WallBuilder} for displaying a certain
 * piece of build wall content.
 */
class ContentArea extends Rectangle {

   private double parentWidth = 0;
   private double parentHeight = 0;
   
   private ContentBoundary top;
   private ContentBoundary bottom;
   private ContentBoundary left;
   private ContentBoundary right;
   
   private final ChangeListener< Number > horizontalListener;
   private final ChangeListener< Number > verticalListener;
   
   /**
    * Constructs a new {@link ContentArea}.
    * @param left the {@link ContentBoundary} on the left.
    * @param top the {@link ContentBoundary} on the top.
    * @param right the {@link ContentBoundary} on the right.
    * @param bottom the {@link ContentBoundary} on the bottom.
    * @param currentParentWidth the current width of the parent.
    * @param currentParentHeight the current height of the parent.
    */
   ContentArea( 
            ContentBoundary left,
            ContentBoundary top,
            ContentBoundary right,
            ContentBoundary bottom,
            double currentParentWidth,
            double currentParentHeight
   ) {
      setFill( Color.BLACK );
      setStroke( Color.WHITE );
      setStrokeWidth( 5 );
      
      horizontalListener = ( s, o, n ) -> {
         updateTranslationX();
         updateWidth();
      };
      verticalListener = ( s, o, n ) -> {
         updateTranslationY();
         updateHeight();
      };
      
      setLeftBoundary( left );
      setTopBoundary( top );
      setRightBoundary( right );
      setBottomBoundary( bottom );
      
      setParentDimensions( currentParentWidth, currentParentHeight );
   }//End Constructor

   /**
    * Method to set the parent dimensions which will rescale the {@link ContentArea}.
    * @param width the width of the parent.
    * @param height the height of the parent.
    */
   void setParentDimensions( double width, double height ) {
      this.parentWidth = width;
      this.parentHeight = height;
      
      updateTranslationX();
      updateTranslationY();
      
      updateWidth();
      updateHeight();
   }//End Method
   
   /**
    * Method to update the {@link #translateXProperty()} given the {@link ContentBoundary}s.
    */
   private void updateTranslationX(){
      setTranslateX( ( left.positionPercentage() / 100 ) * parentWidth );
   }//End Method
   
   /**
    * Method to update the {@link #translateYProperty()} given the {@link ContentBoundary}s.
    */
   private void updateTranslationY(){
      setTranslateY( ( top.positionPercentage() / 100 ) * parentHeight );
   }//End Method
   
   /**
    * Method to update the {@link #widthProperty()} given the {@link ContentBoundary}s.
    */
   private void updateWidth(){
      setWidth( ( ( right.positionPercentage() - left.positionPercentage() ) / 100 ) * parentWidth );
   }//End Method
   
   /**
    * Method to update the {@link #heightProperty()} given the {@link ContentBoundary}s.
    */
   private void updateHeight(){
      setHeight( ( ( bottom.positionPercentage() - top.positionPercentage() ) / 100 ) * parentHeight );
   }//End Method
   
   /**
    * Set the left {@link ContentBoundary}. This will bind the edge of the {@link ContentArea} to
    * this boundary and unbind from the previous.
    * @param boundary the {@link ContentBoundary}.
    */
   void setLeftBoundary( ContentBoundary boundary ) {
      if ( left != null ) {
         //removes memory leak but has not functional need
         left.unregisterForPositionChanges( horizontalListener );
      }
      
      left = boundary;
      left.registerForPositionChanges( horizontalListener );
      updateTranslationX();
   }//End Method
   
   /**
    * Set the right {@link ContentBoundary}. This will bind the edge of the {@link ContentArea} to
    * this boundary and unbind from the previous.
    * @param boundary the {@link ContentBoundary}.
    */
   void setRightBoundary( ContentBoundary boundary ) {
      if ( right != null ) {
         //removes memory leak but has not functional need
         right.unregisterForPositionChanges( horizontalListener );
      }
      
      right = boundary;
      right.registerForPositionChanges( horizontalListener );
      updateWidth();
   }//End Method
   
   /**
    * Set the top {@link ContentBoundary}. This will bind the edge of the {@link ContentArea} to
    * this boundary and unbind from the previous.
    * @param boundary the {@link ContentBoundary}.
    */
   void setTopBoundary( ContentBoundary boundary ) {
      if ( top != null ) {
         //removes memory leak but has not functional need
         top.unregisterForPositionChanges( verticalListener );
      }
      
      top = boundary;
      top.registerForPositionChanges( verticalListener );
      updateTranslationY();
   }//End Method
   
   /**
    * Set the bottom {@link ContentBoundary}. This will bind the edge of the {@link ContentArea} to
    * this boundary and unbind from the previous.
    * @param boundary the {@link ContentBoundary}.
    */
   void setBottomBoundary( ContentBoundary boundary ) {
      if ( bottom != null ) {
         //removes memory leak but has not functional need
         bottom.unregisterForPositionChanges( verticalListener );
      }
      
      bottom = boundary;
      bottom.registerForPositionChanges( verticalListener );
      updateHeight();
   }//End Method
   
   /**
    * Access to the left {@link ContentBoundary}.
    * @return the {@link ContentBoundary}.
    */
   ContentBoundary leftBoundary(){
      return left;
   }//End Method
   
   /**
    * Access to the right {@link ContentBoundary}.
    * @return the {@link ContentBoundary}.
    */
   ContentBoundary rightBoundary(){
      return right;
   }//End Method
   
   /**
    * Access to the top {@link ContentBoundary}.
    * @return the {@link ContentBoundary}.
    */
   ContentBoundary topBoundary(){
      return top;
   }//End Method
   
   /**
    * Access to the bottom {@link ContentBoundary}.
    * @return the {@link ContentBoundary}.
    */
   ContentBoundary bottomBoundary(){
      return bottom;
   }//End Method
   
}//End Class
