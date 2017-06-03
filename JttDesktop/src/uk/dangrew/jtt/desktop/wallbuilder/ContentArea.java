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
   
   private double xPositionPercentage = 0;
   private double yPositionPercentage = 0;
   
   private double percentageWidth = 0;
   private double percentageHeight = 0;
   
   /**
    * Constructs a new {@link ContentArea}.
    * @param initialParentWidth the current width of the parent.
    * @param initialParentHeight the current height of the parent.
    * @param initialPositionXPercentage the initial x position as a percentage of the parent width.
    * @param initialPositionYPercentage the initial y position as a percentage of the parent height.
    * @param initialPercentageWidth the initial width as a percent of the parent width. 
    * @param initialPercentageHeight the initial height as a percent of the parent height. 
    */
   ContentArea( 
            double initialParentWidth, double initialParentHeight,
            double initialPositionXPercentage, double initialPositionYPercentage,
            double initialPercentageWidth, double initialPercentageHeight
   ) {
      setFill( Color.BLACK );
      setStroke( Color.WHITE );
      setStrokeWidth( 5 );
      
      this.xPositionPercentage = initialPositionXPercentage;
      this.yPositionPercentage = initialPositionYPercentage;
      this.percentageWidth = initialPercentageWidth;
      this.percentageHeight = initialPercentageHeight;
      setParentDimensions( initialParentWidth, initialParentHeight );
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
    * Method to update the {@link #translateXProperty()} given the {@link #xPositionPercentage()}.
    */
   private void updateTranslationX(){
      setTranslateX( ( xPositionPercentage / 100 ) * parentWidth );
   }//End Method
   
   /**
    * Method to update the {@link #translateYProperty()} given the {@link #yPositionPercentage()}.
    */
   private void updateTranslationY(){
      setTranslateY( ( yPositionPercentage / 100 ) * parentHeight );
   }//End Method
   
   /**
    * Method to update the {@link #widthProperty()} given the {@link #percentageWidth()}.
    */
   private void updateWidth(){
      setWidth( ( percentageWidth / 100 ) * parentWidth );
   }//End Method
   
   /**
    * Method to update the {@link #heightProperty()} given the {@link #percentageHeight()}.
    */
   private void updateHeight(){
      setHeight( ( percentageHeight / 100 ) * parentHeight );
   }//End Method
   
   /**
    * Method to clamp the given value as a percentage.
    * @param value the value to clamp.
    * @return the clamped percentage.
    */
   private double clampPercentage( double value ) {
      value = Math.max( value, 0 );
      value = Math.min( value, 100 );
      return value;
   }//End Method

   /**
    * Method to change the current x position, as a percentage of the parent width, by the given amount.
    * @param change the change in percentage, + or -.
    */
   void changeXPositionPercentageBy( double change ) {
      this.xPositionPercentage += change;
      this.xPositionPercentage = clampPercentage( xPositionPercentage );
      changeWidthPercentageBy( -change );
      updateTranslationX();
   }//End Method
   
   /**
    * Method to change the current y position, as a percentage of the parent height, by the given amount.
    * @param change the change in percentage, + or -.
    */
   void changeYPositionPercentageBy( double change ) {
      this.yPositionPercentage += change;
      this.yPositionPercentage = clampPercentage( yPositionPercentage );
      changeHeightPercentageBy( -change );
      updateTranslationY();
   }//End Method

   /**
    * Method to change the width, as a percentage of the parent width, by the given amount.
    * @param change the change in percentage, + or -.
    */
   void changeWidthPercentageBy( double change ) {
      this.percentageWidth += change;
      this.percentageWidth = clampPercentage( percentageWidth );
      updateWidth();
   }//End Method

   /**
    * Method to change the height, as a percentage of the parent height, by the given amount.
    * @param change the change in percentage, + or -.
    */
   void changeHeightPercentageBy( double change ) {
      this.percentageHeight += change;
      this.percentageHeight = clampPercentage( percentageHeight );
      updateHeight();
   }//End Method

   /**
    * Access to the x position as a percentage of the parent width.
    * @return the value.
    */
   double xPositionPercentage() {
      return xPositionPercentage;
   }//End Method

   /**
    * Access to the y position as a percentage of the parent height.
    * @return the value.
    */
   double yPositionPercentage() {
      return yPositionPercentage;
   }//End Method

   /**
    * Access to the width as a percentage of the parent width.
    * @return the value.
    */
   double percentageWidth() {
      return percentageWidth;
   }//End Method

   /**
    * Access to the height as a percentage of the parent height.
    * @return the value.
    */
   double percentageHeight() {
      return percentageHeight;
   }//End Method
   
}//End Class
