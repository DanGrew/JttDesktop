/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.dual;

import javafx.scene.control.SplitPane;
import uk.dangrew.jtt.buildwall.configuration.properties.DualWallConfiguration;
import uk.dangrew.jtt.buildwall.layout.GridWallImpl;
import uk.dangrew.jtt.javafx.splitpane.SplitPaneDividerPositionListener;

/**
 * {@link DualBuildWallSplitter} is responsible for handling the splitting of a left
 * and right hand {@link GridWallImpl} so that they can be added and removed and
 * split appropriately.
 */
class DualBuildWallSplitter extends SplitPane {

   private final DualWallConfiguration configuration;
   private final GridWallImpl rightGridWall;
   private final GridWallImpl leftGridWall;
   
   /**
    * Constructs a new {@link DualBuildWallSplitter}.
    * @param configuration the {@link DualWallConfiguration} for controlling the split.
    * @param left the left {@link GridWallImpl}.
    * @param right the right {@link GridWallImpl}.
    */
   DualBuildWallSplitter( DualWallConfiguration configuration, GridWallImpl left, GridWallImpl right ) {
      super( left, right );
      this.configuration = configuration;
      this.rightGridWall = right;
      this.leftGridWall = left;
      
      setResizableWithParent( rightGridWall, false );
      setResizableWithParent( leftGridWall, false );
      
      configuration.dividerPositionProperty().addListener( ( source, old, updated ) -> restoreDividerPosition() );
      restoreDividerPosition();
      new SplitPaneDividerPositionListener( this, this::updateDividerPosition );
      
      configuration.dividerOrientationProperty().addListener( ( source, old, updated ) -> updateOrientation() );
      updateOrientation();
   }//End Constructor
   
   /**
    * Method to update the record of the divider position for restoring later.
    * Will only take effect if precisely 2 items in this pane.
    */
   private void updateDividerPosition(){
      if ( getItems().size() != 2 ) {
         return;
      }
      configuration.dividerPositionProperty().set( getDividerPositions()[ 0 ] );
   }//End Method
   
   /**
    * Method to restore the previously recorded divider position.
    * Will only take effect if precisely 2 items in this pane.
    */
   private void restoreDividerPosition(){
      if ( getItems().size() != 2 ) {
         return;
      }
      setDividerPositions( configuration.dividerPositionProperty().get() );
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
   
   /**
    * Method to update the {@link javafx.geometry.Orientation} of the {@link SplitPane}.
    */
   private void updateOrientation() {
      setOrientation( configuration.dividerOrientationProperty().get() );
   }//End Method
   
   GridWallImpl rightGridWall(){
      return rightGridWall;
   }//End Method
   
   GridWallImpl leftGridWall(){
      return leftGridWall;
   }//End Method
}//End Class
