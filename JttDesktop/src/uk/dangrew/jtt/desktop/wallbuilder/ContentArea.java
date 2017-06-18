/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

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
      
      refreshDimensions();
   }//End Method
   
   /**
    * Method to refresh the dimensions of the {@link ContentArea}.
    */
   void refreshDimensions(){
      updateTranslationX();
      updateWidth();
      
      updateTranslationY();
      updateHeight();
   }//End Method
   
   /**
    * Method to update the {@link #translateXProperty()} given the {@link ContentBoundary}s.
    */
   private void updateTranslationX(){
      if ( left == null ) {
         return;
      }
      setTranslateX( ( left.positionPercentage() / 100 ) * parentWidth );
   }//End Method
   
   /**
    * Method to update the {@link #translateYProperty()} given the {@link ContentBoundary}s.
    */
   private void updateTranslationY(){
      if ( top == null ) {
         return;
      }
      setTranslateY( ( top.positionPercentage() / 100 ) * parentHeight );
   }//End Method
   
   /**
    * Method to update the {@link #widthProperty()} given the {@link ContentBoundary}s.
    */
   private void updateWidth(){
      if ( right == null || left == null ) {
         return;
      }
      setWidth( ( ( right.positionPercentage() - left.positionPercentage() ) / 100 ) * parentWidth );
   }//End Method
   
   /**
    * Method to update the {@link #heightProperty()} given the {@link ContentBoundary}s.
    */
   private void updateHeight(){
      if ( top == null || bottom == null ) {
         return;
      }
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
         left.unregisterForPositionChanges( this );
      }
      
      left = boundary;
      
      if ( left != null ) {
         left.registerForPositionChanges( this );
         updateTranslationX();
      }
   }//End Method
   
   /**
    * Set the right {@link ContentBoundary}. This will bind the edge of the {@link ContentArea} to
    * this boundary and unbind from the previous.
    * @param boundary the {@link ContentBoundary}.
    */
   void setRightBoundary( ContentBoundary boundary ) {
      if ( right != null ) {
         //removes memory leak but has not functional need
         right.unregisterForPositionChanges( this );
      }
      
      right = boundary;
      
      if ( right != null ) {
         right.registerForPositionChanges( this );
         updateWidth();
      }
   }//End Method
   
   /**
    * Set the top {@link ContentBoundary}. This will bind the edge of the {@link ContentArea} to
    * this boundary and unbind from the previous.
    * @param boundary the {@link ContentBoundary}.
    */
   void setTopBoundary( ContentBoundary boundary ) {
      if ( top != null ) {
         //removes memory leak but has not functional need
         top.unregisterForPositionChanges( this );
      }
      
      top = boundary;
      
      if ( top != null ) {
         top.registerForPositionChanges( this );
         updateTranslationY();
      }
   }//End Method
   
   /**
    * Set the bottom {@link ContentBoundary}. This will bind the edge of the {@link ContentArea} to
    * this boundary and unbind from the previous.
    * @param boundary the {@link ContentBoundary}.
    */
   void setBottomBoundary( ContentBoundary boundary ) {
      if ( bottom != null ) {
         //removes memory leak but has not functional need
         bottom.unregisterForPositionChanges( this );
      }
      
      bottom = boundary;
      
      if ( bottom != null ) {
         bottom.registerForPositionChanges( this );
         updateHeight();
      }
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
