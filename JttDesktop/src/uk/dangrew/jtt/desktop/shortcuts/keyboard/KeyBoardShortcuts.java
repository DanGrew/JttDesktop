/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.shortcuts.keyboard;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

/**
 * The {@link KeyBoardShortcuts} provides a single place for accessing and configuring
 * key board shortcuts for the application.
 */
public class KeyBoardShortcuts {
   
   static KeyCombination cmdShiftO = new KeyCodeCombination( KeyCode.O, KeyCombination.SHORTCUT_DOWN );

   /**
    * Method to configure the Cmd + shift + O shortcut, running the given {@link Runnable} when
    * triggered.
    * @param scene the {@link Scene} to cpature the shortcut.
    * @param runnable the {@link Runnable} to run.
    */
   public static void cmdShiftO( Scene scene, Runnable runnable ) {
      scene.getAccelerators().put( 
               KeyBoardShortcuts.cmdShiftO,
               runnable
      );
   }//End Method

   /**
    * Getter for the {@link Runnable} associated with the Cmd + shift + O shortcut in the 
    * given {@link Scene}.
    * @param scene the {@link Scene} the shortcut is associated with.
    * @return the {@link Runnable} if it exists.
    */
   public static Runnable getCmdShiftO( Scene scene ) {
      return scene.getAccelerators().get( cmdShiftO );
   }//End Method

}//End Class
