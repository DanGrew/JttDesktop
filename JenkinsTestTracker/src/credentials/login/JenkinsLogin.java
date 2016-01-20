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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * The {@link JenkinsLogin} provides a {@link GridPane} for a user logging into a
 * jenkins instance.
 */
public class JenkinsLogin extends GridPane {

   private static final String VALIDATION_MESSAGE = "Text must be present";

   private static final int JENKINS_LOCATION_WIDTH = 300;
   
   private final TextField jenkinsField;
   private final TextField userNameField;
   private final PasswordField passwordField;
   private final HBox loginButtonWrapper;
   private final Button loginButton;
   
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
      this.credentialsVerifier = verifier;
      this.validation = new ValidationSupport();
      
      setAlignment( Pos.CENTER );
      setHgap( 10 );
      setVgap( 10 );
      setPadding( new Insets( 25, 25, 25, 25 ) );

      Text scenetitle = new Text( "Welcome!" );
      scenetitle.setFont( Font.font( "Tahoma", FontWeight.NORMAL, 20 ) );
      add( scenetitle, 0, 0, 2, 1 );

      Label jenkinsLocation = new Label( "Jenkins:" );
      add( jenkinsLocation, 0, 1 );

      jenkinsField = new TextField();
      jenkinsField.setPrefWidth( JENKINS_LOCATION_WIDTH );
      add( jenkinsField, 1, 1 );

      Label userName = new Label( "User Name:" );
      add( userName, 0, 2 );

      userNameField = new TextField();
      add( userNameField, 1, 2 );

      Label pw = new Label( "Password:" );
      add( pw, 0, 3 );

      passwordField = new PasswordField();
      add( passwordField, 1, 3 );

      loginButton = new Button( "Log in" );
      loginButton.setOnAction( event -> prepareInputAndLogin() );
      loginButtonWrapper = new HBox( 10 );
      loginButtonWrapper.setAlignment( Pos.BOTTOM_RIGHT );
      loginButtonWrapper.getChildren().add( loginButton );
      add( loginButtonWrapper, 1, 4 );

      applyValidation();
   }//End Constructor
   
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
      
      //Change value to trigger initial validation.
      jenkinsField.setText( "anything" );
      jenkinsField.setText( "" );
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
    * Getter for the wrapper of the login {@link Button}.
    * @return the wrapper.
    */
   HBox getLoginButtonWrapper(){
      return loginButtonWrapper;
   }//End Method
   
   /**
    * Getter for the {@link Button} for logging in.
    * @return the {@link Button}.
    */
   Button getLoginButton() {
      return loginButton;
   }//End Method

}//End Class
