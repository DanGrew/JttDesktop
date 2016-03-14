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
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;

/**
 * The {@link DualBuildWallContextMenu} provides a {@link ContextMenu} that allows
 * the user to change the {@link DualBuildWallDisplayImpl}.
 */
public class DualBuildWallContextMenu extends ContextMenu {
   
   static final String SHOW_RIGHT = "Show Right";
   static final String SHOW_LEFT = "Show Left";
   static final String HIDE_RIGHT = "Hide Right";
   static final String HIDE_LEFT = "Hide Left";
   static final String CONFIGURE_RIGHT = "Configure Right";
   static final String CONFIGURE_LEFT = "Configure Left";
   static final String HIDE_CONFIGURATION = "Hide Configuration";
   static final String CANCEL = "Cancel";

   /**
    * Constructs a new {@link DualBuildWallContextMenu}.
    * @param display the {@link DualBuildWallDisplayImpl} to control. 
    */
   DualBuildWallContextMenu( DualBuildWallDisplayImpl display ) {
      MenuItem rightWallControl = new MenuItem( HIDE_RIGHT );
      rightWallControl.setOnAction( event -> controlRightWall( display, rightWallControl ) );
      
      MenuItem leftWallControl = new MenuItem( HIDE_LEFT );
      leftWallControl.setOnAction( event -> controlLeftWall( display, leftWallControl ) );
      
      MenuItem configureRight = new MenuItem( CONFIGURE_RIGHT );
      configureRight.setOnAction( event -> display.showRightConfiguration() );
      
      MenuItem configureLeft = new MenuItem( CONFIGURE_LEFT );
      configureLeft.setOnAction( event -> display.showLeftConfiguration() );
      
      MenuItem hideConfig = new MenuItem( HIDE_CONFIGURATION );
      hideConfig.setOnAction( event -> display.hideConfiguration() );
      
      MenuItem cancel = new MenuItem( CANCEL );
      cancel.setOnAction( event -> hide() );
      
      getItems().addAll( 
            leftWallControl, 
            rightWallControl,
            new SeparatorMenuItem(),
            configureLeft,
            configureRight,
            hideConfig,
            new SeparatorMenuItem(),
            cancel
      );
      
      setAutoHide( true );
   }//End Constructor

   /**
    * Method to control the left {@link GridWallImpl}.
    * @param display the {@link DualBuildWallDisplayImpl} to change.
    * @param leftWallControl the {@link MenuItem} to update.
    */
   private void controlLeftWall( DualBuildWallDisplayImpl display, MenuItem leftWallControl ) {
      if ( leftWallControl.getText() == SHOW_LEFT ) {
         display.showLeftWall();
         leftWallControl.setText( HIDE_LEFT );
      } else {
         display.hideLeftWall();
         leftWallControl.setText( SHOW_LEFT );
      }
   }//End Method

   /**
    * Method to control the right {@link GridWallImpl}.
    * @param display the {@link DualBuildWallDisplayImpl} to change.
    * @param rightWallControl the {@link MenuItem} to update.
    */
   private void controlRightWall( DualBuildWallDisplayImpl display, MenuItem rightWallControl ) {
      if ( rightWallControl.getText() == SHOW_RIGHT ) {
         display.showRightWall();
         rightWallControl.setText( HIDE_RIGHT );
      } else {
         display.hideRightWall();
         rightWallControl.setText( SHOW_RIGHT );
      }
   }//End Method
   
   /**
    * {@link ContextMenu#isShowing()}.
    * @return true if showing.
    */
   public boolean friendly_isShowing(){
      return isShowing();
   }//End Method
   
   /**
    * {@link ContextMenu#show(Node, double, double)}.
    * @param anchor the anchor for the {@link ContextMenu}.
    * @param screenX the location to place the {@link ContextMenu}.
    * @param screenY the location to place the {@link ContextMenu}.
    */
   public void friendly_show( Node anchor, double screenX, double screenY ) {
      show( anchor, screenX, screenY );
   }//End Method

}//End Class
