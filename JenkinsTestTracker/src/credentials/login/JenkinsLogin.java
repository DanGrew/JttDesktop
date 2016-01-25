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

import api.sources.ExternalApi;
import friendly.controlsfx.FriendlyAlert;
import graphics.DecoupledPlatformImpl;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

/**
 * The {@link JenkinsLogin} provides a {@link GridPane} for a user logging into a
 * jenkins instance.
 */
public class JenkinsLogin {

   static final String HEADER = "Welcome! Please log in.";
   static final String TITLE = "Jenkins Test Tracker";
   static final String VALIDATION_MESSAGE = "Text must be present";

   private static final int JENKINS_LOCATION_WIDTH = 300;
   
   private TextField jenkinsField;
   private TextField userNameField;
   private PasswordField passwordField;
   private GridPane content;

   private ButtonType login;
   private ButtonType cancel;
   
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
      this.externalApi = externalApi;
      this.validation = new ValidationSupport();
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
         if ( type.equals( login ) ) prepareInputAndLogin( event );
      } );
      
      alert.friendly_dialogSetContent( content );
   }//End Method
   
   /**
    * Method to initialise the content of the {@link Alert}.
    */
   private void initialiseContent(){
      content = new GridPane();
      content.setAlignment( Pos.CENTER );
      content.setHgap( 10 );
      content.setVgap( 10 );
      content.setPadding( new Insets( 25, 25, 25, 25 ) );

      Label jenkinsLocation = new Label( "Jenkins:" );
      content.add( jenkinsLocation, 0, 1 );

      jenkinsField = new TextField();
      jenkinsField.setPrefWidth( JENKINS_LOCATION_WIDTH );
      content.add( jenkinsField, 1, 1 );

      Label userName = new Label( "User Name:" );
      content.add( userName, 0, 2 );

      userNameField = new TextField();
      content.add( userNameField, 1, 2 );

      Label pw = new Label( "Password:" );
      content.add( pw, 0, 3 );

      passwordField = new PasswordField();
      content.add( passwordField, 1, 3 );

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
    */
   private void prepareInputAndLogin( DialogEvent event ){
      String jenkinsLocation = jenkinsField.getText();
      if ( !validator.test( jenkinsLocation ) ) {
         event.consume();
         return;
      }
      String username = userNameField.getText();
      if ( !validator.test( username ) ) {
         event.consume();
         return;
      }
      String password = passwordField.getText();
      if ( !validator.test( password ) ) {
         event.consume();
         return;
      }
      
      HttpClient client = externalApi.attemptLogin( jenkinsLocation, username, password );
      if ( client == null ) event.consume();
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
