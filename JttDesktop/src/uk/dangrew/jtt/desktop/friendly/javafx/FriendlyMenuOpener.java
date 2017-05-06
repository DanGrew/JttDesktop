/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.friendly.javafx;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.ContextMenuEvent;

/**
 * {@link FriendlyMenuOpener} provides an {@link EventHandler} to control
 * the opening and closing of a {@link FriendlyContextMenu}.
 */
public class FriendlyMenuOpener implements EventHandler< ContextMenuEvent >{

   private final Node display;
   private final FriendlyContextMenu contextMenu;
   
   /**
    * Constructs a new {@link FriendlyMenuOpener}.
    * @param display the {@link Node} the {@link FriendlyContextMenu} is for.
    * @param menu the {@link FriendlyContextMenu} to show and hide.
    */
   public FriendlyMenuOpener( Node display, FriendlyContextMenu menu ) {
      this.display = display;
      this.contextMenu = menu;
      
      display.setOnContextMenuRequested( this );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void handle( ContextMenuEvent event ) {
      if ( contextMenu.friendly_isShowing() ) {
         return;
      }
      
      contextMenu.friendly_show( display, event.getScreenX(), event.getScreenY() );
   }//End Method
   
   /**
    * Method to determine whether the opener is anchored to the given {@link Node}.
    * @param display the {@link Node} in question.
    * @return true if anchored to.
    */
   public boolean isAnchoredTo( Node display ) {
      return this.display == display;
   }//End Method
   
   /**
    * Method to determine whether the opener is controlling the given type of menu.
    * @param menuType the {@link Class} of {@link FriendlyContextMenu}.
    * @return true if the controlled type is the same class, exactly.
    */
   public boolean isControllingA( Class< ? extends FriendlyContextMenu > menuType ) {
      return contextMenu.getClass().equals( menuType );
   }//End Method
   
}//End Class
