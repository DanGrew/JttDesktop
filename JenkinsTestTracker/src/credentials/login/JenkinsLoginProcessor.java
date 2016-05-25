/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package credentials.login;

import friendly.controlsfx.FriendlyAlert;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;

/**
 * The {@link JenkinsLoginProcessor} is responsible for threading the login process
 * and controlling the buttons on the dialog.
 */
public class JenkinsLoginProcessor implements EventHandler< DialogEvent > {
   
   private final FriendlyAlert alert;
   private final ButtonType loginButtonType;
   private final Node loginButton;
   private final JenkinsLogin jenkinsLogin;
   
   /**
    * Constructs a new {@link JenkinsLoginProcessor}.
    * @param alert the {@link FriendlyAlert} to control.
    * @param loginButtonType the {@link ButtonType} representing the login.
    * @param jenkinsLogin the {@link JenkinsLogin} to control.
    */
   JenkinsLoginProcessor( 
            FriendlyAlert alert, 
            ButtonType loginButtonType, 
            JenkinsLogin jenkinsLogin
   ) {
      this.alert = alert;
      this.loginButtonType = loginButtonType;
      this.loginButton = alert.friendly_dialogLookup( loginButtonType );
      this.jenkinsLogin = jenkinsLogin;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public void handle( DialogEvent event ) {
      ButtonType type = alert.friendly_getResult();
      if ( type.equals( loginButtonType ) ) {
         event.consume();
         Thread loginThread = new Thread( () -> {
            loginButton.setDisable(true);
            
            jenkinsLogin.prepareInputAndLogin( event, alert );
            alert.friendly_separateThreadProcessingComplete();
            
            loginButton.setDisable(false);
         } );
         loginThread.start();
      }
   }//End Method
   
}//End Class
