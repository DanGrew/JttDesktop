/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.friendly.javafx;

import java.awt.Desktop;

/**
 * The {@link FriendlyDesktop} provides a non static friendlier versions of {@link Desktop}.
 */
public class FriendlyDesktop {
   
   /**
    * Getter for the {@link Desktop}.
    * @return {@link Desktop#getDesktop()}.
    */
   public Desktop getDesktop(){
      return Desktop.getDesktop();
   }//End Method

}//End Method
