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
   static final String SHOW_DIGEST = "Show Digest";
   static final String HIDE_DIGEST = "Hide Digest";
   static final String CANCEL = "Cancel";
   
   private MenuItem digestControl;
   private final WrappedSystemDigest wrappedDigest;
   
   /**
    * Constructs a new {@link DualBuildWallContextMenu}.
    * @param display the {@link DualBuildWallDisplayImpl} to control. 
    */
   DualBuildWallContextMenu( DualBuildWallDisplayImpl display ) {
      wrappedDigest = new WrappedSystemDigest( display );
      
      applyControls( display );
      applyDigestControl();
      applyCancel();
      
      setAutoHide( true );
   }//End Constructor

   /**
    * Method to apply the functions available to the menu.
    * @param display the {@link DualBuildWallDisplayImpl} the controls are for.
    */
   private void applyControls( DualBuildWallDisplayImpl display ) {
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
      
      getItems().addAll( 
            leftWallControl, 
            rightWallControl,
            new SeparatorMenuItem(),
            configureLeft,
            configureRight,
            hideConfig
      );
   }//End Method
   
   /**
    * Method to apply the system digest controls if applicable.
    */
   private void applyDigestControl() {
      if ( !wrappedDigest.isSystemDigestAvailable() ) return;
      
      digestControl = new MenuItem( HIDE_DIGEST );
      digestControl.setOnAction( event -> controlDigest( digestControl ) );
      
      getItems().addAll( 
               new SeparatorMenuItem(),
               digestControl
      );
   }//End Method
   
   /**
    * Method to apply the cancel function to close the menu.
    */
   private void applyCancel() {
      MenuItem cancel = new MenuItem( CANCEL );
      cancel.setOnAction( event -> hide() );
      
      getItems().addAll( 
               new SeparatorMenuItem(),
               cancel
      );
   }//End Method

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
    * Method to control the left {@link GridWallImpl}.
    * @param display the {@link DualBuildWallDisplayImpl} to change.
    * @param digestControl the {@link MenuItem} to update.
    */
   private void controlDigest( MenuItem digestControl ) {
      if ( digestControl.getText() == SHOW_DIGEST ) {
         wrappedDigest.insertDigest();
         digestControl.setText( HIDE_DIGEST );
      } else {
         wrappedDigest.removeDigest();
         digestControl.setText( SHOW_DIGEST );
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

   MenuItem digestControl() {
      return digestControl;
   }//End Method

}//End Class
