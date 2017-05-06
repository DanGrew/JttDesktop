/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.dual;

import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;

/**
 * The {@link DualBuildWallAutoHider} is responsible for automatically hiding and showing
 * the individual {@link uk.dangrew.jtt.buildwall.layout.GridWallImpl}s in the {@link DualBuildWallDisplayImpl}.
 */
public class DualBuildWallAutoHider {
   
   private final DualBuildWallDisplayImpl display;

   /**
    * Constructs a new {@link DualBuildWallAutoHider}.
    * @param display the {@link DualBuildWallDisplayImpl} to control.
    * @param leftEmpty empty property for the left {@link uk.dangrew.jtt.buildwall.layout.GridWallImpl}.
    * @param rightEmpty empty property for the right {@link uk.dangrew.jtt.buildwall.layout.GridWallImpl}.
    */
   DualBuildWallAutoHider( DualBuildWallDisplayImpl display, BooleanProperty leftEmpty, BooleanProperty rightEmpty ) {
      this.display = display;

      handleLeftEmptyChange( leftEmpty, leftEmpty.getValue(), leftEmpty.getValue() );
      handleRightEmptyChange( rightEmpty, rightEmpty.getValue(), rightEmpty.getValue() );
      
      leftEmpty.addListener( this::handleLeftEmptyChange );
      rightEmpty.addListener( this::handleRightEmptyChange );
   }//End Constructor
   
   /**
    * Method to handle the changing of the emptiness of the left wall.
    * @param source the source of the change.
    * @param old the old value.
    * @param updated the new value.
    */
   private void handleLeftEmptyChange( ObservableValue< ? extends Boolean > source, Boolean old, Boolean updated ) {
      if ( updated ) {
         display.hideLeftWall();
      } else { 
         display.showLeftWall();
      }
   }//End Method
   
   /**
    * Method to handle the changing of the emptiness of the right wall.
    * @param source the source of the change.
    * @param old the old value.
    * @param updated the new value.
    */
   private void handleRightEmptyChange( ObservableValue< ? extends Boolean > source, Boolean old, Boolean updated ) {
      if ( updated ) {
         display.hideRightWall();
      } else { 
         display.showRightWall();
      }
   }//End Method

}//End Class
