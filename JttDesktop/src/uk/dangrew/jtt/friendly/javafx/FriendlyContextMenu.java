/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.friendly.javafx;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;

/**
 * {@link FriendlyContextMenu} is a {@link ContextMenu} with some friendly methods
 * enabling mocking and verifying.
 */
public class FriendlyContextMenu extends ContextMenu {

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

