/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package credentials.login;

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

   private static final int JENKINS_LOCATION_WIDTH = 300;
   private final TextField jenkinsField;
   private final TextField userNameField;
   private final PasswordField passwordField;
   private final Button loginButton;
   private final Text feedbackText;
   
   private final CredentialsVerifier credentialsVerifier;

   /**
    * Constructs a new {@link JenkinsLogin}.
    * @param verifier the {@link CredentialsVerifier} for logging in.
    */
   public JenkinsLogin( CredentialsVerifier verifier ) {
      this.credentialsVerifier = verifier;
      
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
      HBox hbBtn = new HBox( 10 );
      hbBtn.setAlignment( Pos.BOTTOM_RIGHT );
      hbBtn.getChildren().add( loginButton );
      add( hbBtn, 1, 4 );

      feedbackText = new Text();
      add( feedbackText, 1, 6 );

      loginButton.setOnAction( event -> prepareInputAndLogin() );
   }//End Constructor
   
   /**
    * Method to prepare the input for logging in and to log in. Log in will not be performed
    * if input is not valid.
    */
   private void prepareInputAndLogin(){
      String jenkinsLocation = jenkinsField.getText();
      if ( jenkinsLocation == null || jenkinsLocation.trim().length() == 0 ) return;
      String username = userNameField.getText();
      if ( username == null || username.trim().length() == 0 ) return;
      String password = passwordField.getText();
      if ( password == null || password.trim().length() == 0 ) return;
      credentialsVerifier.attemptLogin( jenkinsLocation, username, password );
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
    * Getter for the {@link Button} for logging in.
    * @return the {@link Button}.
    */
   Button getLoginButton() {
      return loginButton;
   }//End Method

   /**
    * Getter for the {@link Text} for the feedback label.
    * @return the {@link Text}.
    */
   Text getFeedbackText() {
      return feedbackText;
   }//End Method

}//End Class
