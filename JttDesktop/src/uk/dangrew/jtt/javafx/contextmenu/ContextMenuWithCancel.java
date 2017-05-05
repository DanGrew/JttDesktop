/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.contextmenu;

import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import uk.dangrew.jtt.friendly.javafx.FriendlyContextMenu;

/**
 * The {@link ContextMenuWithCancel} provides a {@link ContextMenu} that allows
 * has a built in cancel item.
 */
public abstract class ContextMenuWithCancel extends FriendlyContextMenu {
   
   static final String CANCEL_TEXT = "Cancel";
   
   private MenuItem separator;
   private MenuItem cancel;
   
   /**
    * Constructs a new {@link ContextMenuWithCancel}.
    */
   protected ContextMenuWithCancel() {}//End Constructor

   /**
    * Method to initialise the menu. This should be called in the constructor of the 
    * child once all variables have been assigned and initialised.
    */
   protected final void initialise(){
      applyControls();
      applyCancel();
      
      setAutoHide( true );
   }//End Method

   /**
    * Method to apply the functions available to the menu.
    */
   protected abstract void applyControls();
   
   /**
    * Method to apply the cancel function to close the menu.
    */
   void applyCancel() {
      cancel = new MenuItem( CANCEL_TEXT );
      cancel.setOnAction( event -> hide() );
      
      getItems().addAll( 
               separator = new SeparatorMenuItem(),
               cancel
      );
   }//End Method
   
   MenuItem separator(){
      return separator;
   }//End Method
   
   MenuItem cancel(){
      return cancel;
   }//End Method
   
}//End Class
