/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.core;

import java.util.Optional;

import javafx.scene.control.Alert.AlertType;
import uk.dangrew.jtt.desktop.credentials.login.JenkinsLogin;
import uk.dangrew.jtt.desktop.friendly.controlsfx.FriendlyAlert;
import javafx.scene.control.ButtonType;

/**
 * The {@link JttApplicationController} is responsible for abstracting some of the 
 * launch functions away from the {@link JenkinsTestTracker}.
 */
class JttApplicationController {
   
   /**
    * Constructs a new {@link JttApplicationController}.
    */
   JttApplicationController() {}

   /**
    * Method to construct a {@link FriendlyAlert}, note that this must be done on the JavaFx
    * {@link Thread}.
    * @return a new {@link FriendlyAlert}.
    */
   FriendlyAlert constructAlert() {
      return new FriendlyAlert( AlertType.INFORMATION );
   }//End Method

   /**
    * Method to abstract the showing and waiting of the given {@link FriendlyAlert}.
    * @param friendlyAlert the {@link FriendlyAlert} to show and wait on.
    */
   Optional< ButtonType > showAndWait( FriendlyAlert friendlyAlert ) {
      return friendlyAlert.friendly_showAndWait();
   }//End Method

   /**
    * Method to login to the associated jenkins.
    * @param login the {@link JenkinsLogin} used for logging in.
    * @return true if logged in, false otherwise.
    */
   boolean login( JenkinsLogin login ) {
      FriendlyAlert alert = constructAlert();
      login.configureAlert( alert );
      Optional< ButtonType > result = showAndWait( alert );
      return login.isLoginResult( result.get() );
   }//End Method

}//End Class
