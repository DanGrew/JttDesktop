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

   private final GridWallImpl rightGridWall;
   private final GridWallImpl leftGridWall;
   
   /**
    * Constructs a new {@link DualBuildWallSplitter}.
    * @param left the left {@link GridWallImpl}.
    * @param right the right {@link GridWallImpl}.
    */
   DualBuildWallSplitter( GridWallImpl left, GridWallImpl right ) {
      super( left, right );
      this.rightGridWall = right;
      this.leftGridWall = left;
   }//End Constructor
   
   /**
    * Method to hide the right {@link GridWallImpl}. The configuration will be hidden is for this {@link GridWallImpl}.
    */
   void hideRightWall() {
      getItems().remove( rightGridWall );
   }//End Method
   
   /**
    * Method to show the right {@link GridWallImpl}, if not already showing.
    */
   void showRightWall() {
      if ( isRightWallShowing() ) return;
      
      getItems().add( rightGridWall );
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
      getItems().remove( leftGridWall );
   }//End Method
   
   /**
    * Method to show the left {@link GridWallImpl}, if not already showing.
    */
   void showLeftWall() {
      if ( isLeftWallShowing() ) return;
      
      getItems().add( 0, leftGridWall );
   }//End Method
   
   /**
    * Method to determine whether the left wall is currently showing.
    * @return true if showing, false otherwise.
    */
   boolean isLeftWallShowing(){
      return getItems().contains( leftGridWall );
   }//End Method
}//End Class
