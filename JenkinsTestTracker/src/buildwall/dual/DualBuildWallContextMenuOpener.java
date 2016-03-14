/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.dual;

import javafx.event.EventHandler;
import javafx.scene.input.ContextMenuEvent;

/**
 * {@link DualBuildWallContextMenuOpener} provides an {@link EventHandler} to control
 * the opening and closing of the {@link DualBuildWallContextMenu}.
 */
public class DualBuildWallContextMenuOpener implements EventHandler< ContextMenuEvent >{

   private DualBuildWallDisplayImpl display;
   private DualBuildWallContextMenu contextMenu;
   
   /**
    * Constructs a new {@link DualBuildWallContextMenuOpener}.
    * @param display the {@link DualBuildWallDisplayImpl} the {@link DualBuildWallContextMenu}
    * is for.
    */
   public DualBuildWallContextMenuOpener( DualBuildWallDisplayImpl display ) {
      this( display, new DualBuildWallContextMenu( display ) );
   }//End Constructor
   
   /**
    * Constructs a new {@link DualBuildWallContextMenuOpener}.
    * @param display the {@link DualBuildWallDisplayImpl} the {@link DualBuildWallContextMenu}
    * is for.
    * @param menu the {@link DualBuildWallContextMenu} to show and hide.
    */
   DualBuildWallContextMenuOpener( DualBuildWallDisplayImpl display, DualBuildWallContextMenu menu ) {
      this.display = display;
      this.contextMenu = menu;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void handle( ContextMenuEvent event ) {
      if ( contextMenu.friendly_isShowing() ) return;
      
      contextMenu.friendly_show( display, event.getScreenX(), event.getScreenY() );
   }//End Method

}//End Class
