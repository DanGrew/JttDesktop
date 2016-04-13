/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.dual;

import buildwall.layout.GridWallImpl;
import javafx.scene.control.SplitPane;

/**
 * {@link DualBuildWallSplitter} is responsible for handling the splitting of a left
 * and right hand {@link GridWallImpl} so that they can be added and removed and
 * split appropriately.
 */
public class DualBuildWallSplitter extends SplitPane {

   static final double DEFAULT_DIVIDER = 0.5;
   private final GridWallImpl rightGridWall;
   private final GridWallImpl leftGridWall;
   private double dividerPosition;
   
   /**
    * Constructs a new {@link DualBuildWallSplitter}.
    * @param left the left {@link GridWallImpl}.
    * @param right the right {@link GridWallImpl}.
    */
   DualBuildWallSplitter( GridWallImpl left, GridWallImpl right ) {
      super( left, right );
      this.rightGridWall = right;
      this.leftGridWall = left;
      this.dividerPosition = DEFAULT_DIVIDER;
   }//End Constructor
   
   /**
    * Method to update the record of the divider position for restoring later.
    * Will only take effect if precisely 2 items in this pane.
    */
   private void updateDividerPosition(){
      if ( getItems().size() != 2 ) {
         return;
      }
      dividerPosition = getDividerPositions()[ 0 ];
   }//End Method
   
   /**
    * Method to restore the previously recorded divider position.
    * Will only take effect if precisely 2 items in this pane.
    */
   private void restoreDividerPosition(){
      if ( getItems().size() != 2 ) {
         return;
      }
      setDividerPositions( dividerPosition );
   }//End Method
   
   /**
    * Method to hide the right {@link GridWallImpl}. The configuration will be hidden is for this {@link GridWallImpl}.
    */
   void hideRightWall() {
      updateDividerPosition();
      getItems().remove( rightGridWall );
   }//End Method
   
   /**
    * Method to show the right {@link GridWallImpl}, if not already showing.
    */
   void showRightWall() {
      if ( isRightWallShowing() ) return;
      
      getItems().add( rightGridWall );
      restoreDividerPosition();
   }//End Method
   
   /**
    * Method to determine whether the right wall is currently showing.
    * @return true if showing, false otherwise.
    */
   boolean isRightWallShowing(){
      return getItems().contains( rightGridWall );
   }//End Method
   
   /**
    * Method to hide the left {@link GridWallImpl}. The configuration will be hidden is for this {@link GridWallImpl}.
    */
   void hideLeftWall() {
      updateDividerPosition();
      getItems().remove( leftGridWall );
   }//End Method
   
   /**
    * Method to show the left {@link GridWallImpl}, if not already showing.
    */
   void showLeftWall() {
      if ( isLeftWallShowing() ) return;
      
      getItems().add( 0, leftGridWall );
      restoreDividerPosition();
   }//End Method
   
   /**
    * Method to determine whether the left wall is currently showing.
    * @return true if showing, false otherwise.
    */
   boolean isLeftWallShowing(){
      return getItems().contains( leftGridWall );
   }//End Method
}//End Class
