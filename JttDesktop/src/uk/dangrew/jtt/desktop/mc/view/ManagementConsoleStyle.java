/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.view;

import javafx.geometry.Pos;
import javafx.scene.control.ButtonBase;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * {@link ManagementConsoleStyle} provides styling options for items within the 
 * {@link uk.dangrew.jtt.mc.view.console.ManagementConsole}.
 */
public class ManagementConsoleStyle {
   
   static final double BUTTON_WIDTH = 20;
   static final double BUTTON_HEIGHT = 20;
   
   /**
    * Method to configure the {@link ButtonBase} size and graphic to use the {@link Image}
    * in a consistent style for toolbox buttons.
    * @param button the {@link ButtonBase} to configure.
    * @param image the {@link Image} to apply.
    */
   public void styleButtonSize( ButtonBase button, Image image ) {
      ImageView view = new ImageView( image );
      view.setFitHeight( BUTTON_WIDTH );
      view.setFitWidth( BUTTON_HEIGHT );
      button.setGraphic( view );
      button.setPrefSize( BUTTON_WIDTH, BUTTON_HEIGHT );
      button.setMaxWidth( BUTTON_WIDTH );
      button.setMaxHeight( BUTTON_HEIGHT );
      button.setAlignment( Pos.CENTER );
   }//End Method

}//End Class
