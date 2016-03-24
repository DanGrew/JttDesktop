/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package credentials.login;

import java.util.function.Predicate;

import org.apache.http.client.HttpClient;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import com.sun.javafx.application.PlatformImpl;

import api.sources.ExternalApi;
import core.message.Messages;
import core.progress.Progresses;
import friendly.controlsfx.FriendlyAlert;
import graphics.DecoupledPlatformImpl;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import viewer.basic.DigestViewer;

/**
 * The {@link JenkinsLogin} provides a {@link GridPane} for a user logging into a
 * jenkins instance.
 */
public class JenkinsLogin {

   static final String LOGGING_IN = "Logging in";
   static final String JENKINS_LOCATION_IS_NOT_VALID = "Jenkins Location is not valid";
   static final String USER_NAME_IS_NOT_VALID = "User Name is not valid";
   static final String PASSWORD_IS_NOT_VALID = "Password is not valid";
   static final double LOGGIN_IN_PROGRESS = 5;
   static final String HEADER = "Welcome! Please log in.";
   static final String TITLE = "Jenkins Test Tracker";
   static final String VALIDATION_MESSAGE = "Text must be present";
   static final EventHandler< DialogEvent > LOGIN_ACCEPTED_CLOSER = dialogEvent -> {};

   private static final int JENKINS_LOCATION_WIDTH = 300;
   
   private TextField jenkinsField;
   private TextField userNameField;
   private PasswordField passwordField;
   private BorderPane content;

   private ButtonType login;
   private ButtonType cancel;
   
   private JenkinsLoginDigest digest;
   private final ValidationSupport validation;
   private InputValidator validator;
   private final ExternalApi externalApi;
   
   /** Private class responsible for validating the text in the {@link TextField}s.**/
   static class InputValidator implements Predicate< String > {

      /**
       * {@inheritDoc}
       */
      @Override public boolean test( String field ) {
         return field != null && field.trim().length() > 0;
      }//End Method
      
   }//End Class

   /**
    * Constructs a new {@link JenkinsLogin}.
    * @param externalApi the {@link ExternalApi} for logging in.
    */
   public JenkinsLogin( ExternalApi externalApi ) {
      this( externalApi, new JenkinsLoginDigest() );
   }//End Constructor
   
   /**
    * Constructs a new {@link JenkinsLogin}.
    * @param externalApi the {@link ExternalApi} for loggin in.
    * @param digest the {@link JenkinsLoginDigest} to use.
    */
   JenkinsLogin( ExternalApi externalApi, JenkinsLoginDigest digest ) {
      this.externalApi = externalApi;
      this.validation = new ValidationSupport();
      this.digest = digest;
      this.digest.attachSource( this );
      login = new ButtonType( "Login" );
      cancel = new ButtonType( "Cancel" );
      initialiseContent();
   }//End Constructor
   
   /**
    * Method to configure the {@link FriendlyAlert} to display the login screen.
    * @param alert the {@link FriendlyAlert} to display the login.
    */
   public void configureAlert( FriendlyAlert alert ) {
      alert.friendly_setAlertType( Alert.AlertType.INFORMATION );
      alert.friendly_setTitle( TITLE );
      
      alert.friendly_setHeaderText( HEADER );
      alert.friendly_initModality( Modality.NONE );

      alert.friendly_getButtonTypes().setAll( login, cancel );
      
      alert.friendly_setOnCloseRequest( event -> {
         ButtonType type = alert.friendly_getResult();
         if ( type.equals( login ) ) {
            event.consume();
            new Thread( () -> {
               prepareInputAndLogin( event, alert );
               alert.friendly_separateThreadProcessingComplete();
            } ).start();
         }
      } );
      
      alert.friendly_dialogSetContent( content );
   }//End Method
   
   /**
    * Method to initialise the content of the {@link Alert}.
    */
   private void initialiseContent(){
      GridPane loginContent = new GridPane();
      loginContent.setAlignment( Pos.CENTER );
      loginContent.setHgap( 10 );
      loginContent.setVgap( 10 );
      loginContent.setPadding( new Insets( 25, 25, 25, 25 ) );

      Label jenkinsLocation = new Label( "Jenkins:" );
      loginContent.add( jenkinsLocation, 0, 1 );

      jenkinsField = new TextField();
      jenkinsField.setPrefWidth( JENKINS_LOCATION_WIDTH );
      loginContent.add( jenkinsField, 1, 1 );

      Label userName = new Label( "User Name:" );
      loginContent.add( userName, 0, 2 );

      userNameField = new TextField();
      loginContent.add( userNameField, 1, 2 );

      Label pw = new Label( "Password:" );
      loginContent.add( pw, 0, 3 );

      passwordField = new PasswordField();
      loginContent.add( passwordField, 1, 3 );

      content = new BorderPane( loginContent );
      TitledPane digestPane = new TitledPane( "System Digest", new DigestViewer( 600, 200 ) );
      digestPane.setExpanded( false );
      content.setBottom( digestPane );
      digest.resetLoginProgress();
      
      applyValidation();
   }//End Method
   
   /**
    * Method to apply the validation to the {@link TextField}s.
    */
   private void applyValidation(){
      validator = new InputValidator();
      DecoupledPlatformImpl.runLater( () -> {
         validation.registerValidator(
                  jenkinsField, 
                  Validator.createPredicateValidator( validator, VALIDATION_MESSAGE ) 
         );
         validation.registerValidator(
                  userNameField, 
                  Validator.createPredicateValidator( validator, VALIDATION_MESSAGE ) 
         );
         validation.registerValidator(
                  passwordField, 
                  Validator.createPredicateValidator( validator, VALIDATION_MESSAGE ) 
         );
      } );
   }//End Method
   
   /**
    * Method to prepare the input for logging in and to log in. Log in will not be performed
    * if input is not valid.
    * @param event the {@link DialogEvent} fired when clicking buttons.
    * @param alert the {@link FriendlyAlert} that can be closed when needed.
    */
   private void prepareInputAndLogin( DialogEvent event, FriendlyAlert alert ){
      digest.progress( Progresses.simpleProgress( LOGGIN_IN_PROGRESS ), Messages.simpleMessage( LOGGING_IN ) );
      String jenkinsLocation = jenkinsField.getText();
      if ( !validator.test( jenkinsLocation ) ) {
         digest.validationError( JENKINS_LOCATION_IS_NOT_VALID );
         return;
      }
      String username = userNameField.getText();
      if ( !validator.test( username ) ) {
         digest.validationError( USER_NAME_IS_NOT_VALID );
         return;
      }
      String password = passwordField.getText();
      if ( !validator.test( password ) ) {
         digest.validationError( PASSWORD_IS_NOT_VALID );
         return;
      }
      
      digest.acceptCredentials();
      HttpClient client = externalApi.attemptLogin( jenkinsLocation, username, password );
      if ( client == null ) {
         digest.loginFailed();
      } else {
         digest.loginSuccessful();
         alert.friendly_setOnCloseRequest( LOGIN_ACCEPTED_CLOSER );
         PlatformImpl.runLater( () -> alert.friendly_close() );
      }
   }//End Method
   
   /**
    * Method to determine if the given {@link ButtonType} corresponds to logging in.
    * @param loginButtonType the {@link ButtonType} observed.
    * @return true if logging in result.
    */
   public boolean isLoginResult( ButtonType loginButtonType ) {
      if ( loginButtonType == null ) return false;
      return loginButtonType.equals( this.login );
   }//End Method
   
   /**
    * Getter for the {@link TextField} for the Jenkins location.
    * @return the {@link TextField}.
    */
   TextField getJenkinsLocationField() {
      return jenkinsField;
   }//End Method

   /**
    * Getter for the {@link TextField} for the user name.
    * @return the {@link TextField}.
    */
   TextField getUserNameField() {
      return userNameField;
   }//End Method

   /**
    * Getter for the {@link PasswordField} for the password.
    * @return the {@link PasswordField}.
    */
   PasswordField getPasswordField() {
      return passwordField;
   }//End Method
   
   /**
    * Getter for the login {@link ButtonType}.
    * @return the {@link ButtonType}.
    */
   ButtonType loginButtonType(){
      return login;
   }//End Method

   /**
    * Getter for the cancel {@link ButtonType}.
    * @return the {@link ButtonType}.
    */
   ButtonType cancelButtonType(){
      return cancel;
   }//End Method
   
   /**
    * Getter for the {@link ValidationSupport} used to display validation on the 
    * login details.
    * @return the {@link ValidationSupport} used.
    */
   ValidationSupport validationMechanism(){
      return validation;
   }//End Method

}//End Class
