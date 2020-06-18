/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.notifiers.jobs;

import org.controlsfx.control.Notifications;



import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.util.Duration;
import uk.dangrew.kode.javafx.platform.JavaFxThreading;

/**
 * {@link BuildResultStatusDesktopNotification} is responsible for showing a desktop notification
 * when triggered. 
 */
public class BuildResultStatusDesktopNotification {
   
   static final String NEW_BUILD = "Build Complete!";
   
   /**
    * Method to show a notification using {@link Notifications} in the correct style.
    * @param notification the {@link BuildResultStatusNotification} to show.
    * @param notifications the {@link Notifications} to use.
    * @param duration the duration to display for.
    */
   public void showNotification( BuildResultStatusNotification notification, Notifications notifications, int duration ) {
       JavaFxThreading.runAndWait( () -> {
         notifications
            .darkStyle()
            .hideAfter( new Duration( duration ) )
            .position( Pos.BOTTOM_RIGHT )
            .title( NEW_BUILD )
            .text( createText( notification ) )
            .graphic( createGraphic( notification ) )
            .show();
      } );
   }//End Method
   
   /**
    * Method to create the text for the {@link Notifications}.
    * @param notification the {@link BuildResultStatusNotification} to create the text for.
    * @return the text for the notification.
    */
   private String createText( BuildResultStatusNotification notification ) {
      return notification.getJenkinsJob().nameProperty().get();
   }//End Method

   /**
    * Method to create the {@link Node} graphic for the {@link Notifications}.
    * @param notification the {@link BuildResultStatusNotification} to create the graphic for.
    * @return the graphic for the notification.
    */
   private Node createGraphic( BuildResultStatusNotification notification ) {
      return notification.identifyChange().constructImage();
   }//End Method

}//End Class