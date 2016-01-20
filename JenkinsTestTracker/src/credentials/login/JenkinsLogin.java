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

import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

/**
 * The {@link JenkinsLogin} provides a {@link GridPane} for a user logging into a
 * jenkins instance.
 */
public class JenkinsLogin extends Alert {

   private static final String VALIDATION_MESSAGE = "Text must be present";

   private static final int JENKINS_LOCATION_WIDTH = 300;
   
   private TextField jenkinsField;
   private TextField userNameField;
   private PasswordField passwordField;

   private ButtonType login;
   private ButtonType cancel;
   
   private final ValidationSupport validation;
   private final CredentialsVerifier credentialsVerifier;
   
   /** Private class responsible for validating the text in the {@link TextField}s.**/
   private static class InputValidator implements Predicate< String > {

      /**
       * {@inheritDoc}
       */
      @Override public boolean test( String field ) {
         return field != null && field.trim().length() > 0;
      }//End Method
      
   }//End Class

   /**
    * Constructs a new {@link JenkinsLogin}.
    * @param verifier the {@link CredentialsVerifier} for logging in.
    */
   public JenkinsLogin( CredentialsVerifier verifier ) {
      super( Alert.AlertType.INFORMATION );
      this.credentialsVerifier = verifier;
      this.validation = new ValidationSupport();
      initialiseAlert();
      initialiseContent();
   }//End Constructor
   
   /**
    * Method to initialise the {@link Alert} elements of the login.
    */
   private void initialiseAlert(){
      setTitle( "Jenkins Test Tracker" );
      
      setHeaderText( "Welcome! Please log in." );
      initModality( Modality.NONE );

      login = new ButtonType( "Login" );
      cancel = new ButtonType( "Cancel" );
      
      getButtonTypes().setAll( login, cancel );
      
      setOnCloseRequest( event -> {
         ButtonType type = getResult();
         if ( type.equals( login ) ) prepareInputAndLogin();
      } );
   }//End Method
   
   /**
    * Method to initialise the content of the {@link Alert}.
    */
   private void initialiseContent(){
      GridPane content = new GridPane();
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
      
      getDialogPane().setContent( content );
   }//End Method
   
   /**
    * Method to apply the validation to the {@link TextField}s.
    */
   private void applyValidation(){
      validation.registerValidator(
               jenkinsField, 
               Validator.createPredicateValidator( new InputValidator(), VALIDATION_MESSAGE ) 
      );
      validation.registerValidator(
               userNameField, 
               Validator.createPredicateValidator( new InputValidator(), VALIDATION_MESSAGE ) 
      );
      validation.registerValidator(
               passwordField, 
               Validator.createPredicateValidator( new InputValidator(), VALIDATION_MESSAGE ) 
      );
   }//End Method
   
   /**
    * Method to prepare the input for logging in and to log in. Log in will not be performed
    * if input is not valid.
    */
   private void prepareInputAndLogin(){
      String jenkinsLocation = jenkinsField.getText();
      String username = userNameField.getText();
      String password = passwordField.getText();
      if ( !validation.isInvalid() ) {
         credentialsVerifier.attemptLogin( jenkinsLocation, username, password );
      }
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

}//End Class
