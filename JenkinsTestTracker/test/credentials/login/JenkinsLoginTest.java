/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package credentials.login;

import java.util.concurrent.CountDownLatch;

import org.apache.http.client.HttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.sun.javafx.application.PlatformImpl;

import api.sources.ClientHandler;
import api.sources.ExternalApi;
import api.sources.JenkinsApiImpl;
import credentials.login.JenkinsLogin.InputValidator;
import friendly.controlsfx.FriendlyAlert;
import graphics.JavaFxInitializer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;

/**
 * {@link JenkinsLogin} test.
 */
public class JenkinsLoginTest {

   private FriendlyAlert alert;
   private EventHandler< DialogEvent > onCloseHandler;
   private DialogEvent loginEvent;
   private Node content;
   private ObservableList< ButtonType > buttonTypes;
   private ExternalApi handler;
   private JenkinsLogin systemUnderTest;
   
   /**
    * Method to initialise the system under test.
    */
   @SuppressWarnings("unchecked") //Simply mocking genericized objects. 
   @Before public void initialiseSystemUnderTest() {
      JavaFxInitializer.startPlatform();
      alert = Mockito.mock( FriendlyAlert.class );
      
      buttonTypes = FXCollections.observableArrayList();
      Mockito.when( alert.friendly_getButtonTypes() ).thenReturn( buttonTypes );
      
      loginEvent = Mockito.mock( DialogEvent.class );
      Mockito.doAnswer( invocation -> {
         return onCloseHandler = ( EventHandler< DialogEvent > )invocation.getArguments()[ 0 ];
      } ).when( alert ).friendly_setOnCloseRequest( Mockito.any() );
      
      Mockito.doAnswer( invocation -> {
         return content = ( Node )invocation.getArguments()[ 0 ];
      } ).when( alert ).friendly_dialogSetContent( Mockito.any() );
      
      handler = Mockito.mock( ExternalApi.class );
      systemUnderTest = new JenkinsLogin( handler );
      systemUnderTest.configureAlert( alert );
   }//End Method
   
   @Ignore
   @Test public void manualInspection() {
      runOnFxThreadAndWait( () -> {
         FriendlyAlert alert = new FriendlyAlert( AlertType.INFORMATION );
         systemUnderTest.configureAlert( alert );
         alert.showAndWait();
      }, 100000 );
   }//End Method
   
   @Ignore
   @Test public void manualConnectionTest() {
      runOnFxThreadAndWait( () -> {
         FriendlyAlert alert = new FriendlyAlert( AlertType.INFORMATION );
         systemUnderTest = new JenkinsLogin( new JenkinsApiImpl( new ClientHandler() ) );
         systemUnderTest.configureAlert( alert );
         alert.showAndWait();
      }, 100000 );
   }//End Method
   
   @Test public void shouldAttemptToConnect(){
      final String jenkinsLocation = "any location";
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      login();
      Mockito.verify( handler ).attemptLogin( jenkinsLocation, user, password );
      Mockito.verify( loginEvent ).consume();
   }//End Method
   
   @Test public void shouldShutDownAlertWhenLoginAccepted(){
      final String jenkinsLocation = "any location";
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      Mockito.when( handler.attemptLogin( jenkinsLocation, user, password ) ).thenReturn( Mockito.mock( HttpClient.class ) );
      login();
      Mockito.verify( handler ).attemptLogin( jenkinsLocation, user, password );
   }//End Method
   
   @Test public void shouldNotAttemptToConnectOnCancel(){
      final String jenkinsLocation = "any location";
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      Assert.assertNotNull( onCloseHandler );
      Mockito.when( alert.friendly_getResult() ).thenReturn( systemUnderTest.cancelButtonType() );
      onCloseHandler.handle( null );
      Mockito.verifyNoMoreInteractions( handler );
   }//End Method
   
   @Test public void shouldNotAttemptWithNullLocation(){
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( null );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      login();
      Mockito.verifyNoMoreInteractions( handler );
      Mockito.verify( loginEvent ).consume();
   }//End Method
   
   @Test public void shouldNotAttemptWithNullUsername(){
      final String jenkinsLocation = "any location";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( null );
      systemUnderTest.getPasswordField().setText( password );
      
      login();
      Mockito.verifyNoMoreInteractions( handler );
      Mockito.verify( loginEvent ).consume();
   }//End Method
   
   @Test public void shouldNotAttemptWithNullPassword(){
      final String jenkinsLocation = "any location";
      final String user = "any user";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( null );
      
      login();
      Mockito.verifyNoMoreInteractions( handler );
      Mockito.verify( loginEvent ).consume();
   }//End Method
   
   @Test public void shouldNotAttemptWithEmptyLocation(){
      final String jenkinsLocation = "     ";
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      login();
      Mockito.verifyNoMoreInteractions( handler );
      Mockito.verify( loginEvent ).consume();
   }//End Method
   
   @Test public void shouldNotAttemptWithEmptyUsername(){
      final String jenkinsLocation = "any location";
      final String user = "     ";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      login();
      Mockito.verifyNoMoreInteractions( handler );
      Mockito.verify( loginEvent ).consume();
   }//End Method
   
   @Test public void shouldNotAttemptWithEmptyPassword(){
      final String jenkinsLocation = "any location";
      final String user = "any user";
      final String password = "     ";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      login();
      Mockito.verifyNoMoreInteractions( handler );
      Mockito.verify( loginEvent ).consume();
   }//End Method
   
   @Test public void shouldNotAttemptWithInitialValues(){
      login();
      Mockito.verifyNoMoreInteractions( handler );
      Mockito.verify( loginEvent ).consume();
   }//End Method
   
   @Test public void shouldContainAllTextElementsInChildren(){
      Assert.assertNotNull( content );
      Assert.assertTrue( content instanceof GridPane );
      GridPane gridPane = ( GridPane )content;
      Assert.assertTrue( gridPane.getChildren().contains( systemUnderTest.getJenkinsLocationField() ) );
      Assert.assertTrue( gridPane.getChildren().contains( systemUnderTest.getUserNameField() ) );
      Assert.assertTrue( gridPane.getChildren().contains( systemUnderTest.getPasswordField() ) );
   }//End Method
   
   @Test public void shouldValidateText(){
      InputValidator validator = new InputValidator();
      Assert.assertTrue( validator.test( "anything" ) );
      Assert.assertTrue( validator.test( "anything with space" ) );
      Assert.assertTrue( validator.test( "a" ) );
   }//End Method
   
   @Test public void shouldNotValidateText(){
      InputValidator validator = new InputValidator();
      Assert.assertFalse( validator.test( null ) );
      Assert.assertFalse( validator.test( "" ) );
      Assert.assertFalse( validator.test( "    " ) );
   }//End Method
   
   @Test public void shouldProvideVisualValidationForLocation(){
      final String jenkinsLocation = null;
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      runOnFxThreadAndWait( () -> {}, 2000 );
      Assert.assertTrue( systemUnderTest.validationMechanism().isInvalid() );
   }//End Method
   
   @Test public void shouldProvideVisualValidationForUsername(){
      final String jenkinsLocation = "any location";
      final String user = null;
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      runOnFxThreadAndWait( () -> {}, 2000 );
      Assert.assertTrue( systemUnderTest.validationMechanism().isInvalid() );
   }//End Method
   
   @Test public void shouldProvideVisualValidationForPassword(){
      final String jenkinsLocation = "any user";
      final String user = "any user";
      final String password = null;
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      runOnFxThreadAndWait( () -> {}, 2000 );
      Assert.assertTrue( systemUnderTest.validationMechanism().isInvalid() );
   }//End Method
   
   @Test public void shouldHaveControlsRegistered(){
      Assert.assertTrue( systemUnderTest.validationMechanism().getRegisteredControls().contains( 
               systemUnderTest.getJenkinsLocationField() ) 
      );
      Assert.assertTrue( systemUnderTest.validationMechanism().getRegisteredControls().contains( 
               systemUnderTest.getUserNameField() ) 
      );
      Assert.assertTrue( systemUnderTest.validationMechanism().getRegisteredControls().contains( 
               systemUnderTest.getPasswordField() ) 
      );
   }//End Method
   
   @Test public void shoudlConfigureAlert(){
      Mockito.verify( alert ).friendly_setAlertType( Alert.AlertType.INFORMATION );
      Mockito.verify( alert ).friendly_setTitle( JenkinsLogin.TITLE );
      Mockito.verify( alert ).friendly_setHeaderText( JenkinsLogin.HEADER );
      Mockito.verify( alert ).friendly_initModality( Modality.NONE );
      
      Assert.assertTrue( buttonTypes.contains( systemUnderTest.loginButtonType() ) );
      Assert.assertTrue( buttonTypes.contains( systemUnderTest.cancelButtonType() ) );
      Assert.assertNotNull( onCloseHandler );
      
      Mockito.verify( alert ).friendly_dialogSetContent( content );
   }//End Method
   
   @Test public void shouldDetermineLoginAccepted(){
      Assert.assertTrue( systemUnderTest.isLoginResult( systemUnderTest.loginButtonType() ) );
      Assert.assertFalse( systemUnderTest.isLoginResult( systemUnderTest.cancelButtonType() ) );
      Assert.assertFalse( systemUnderTest.isLoginResult( null ) );
   }//End Method
   
   /**
    * Convenience method for performing a login on the fx thread.
    */
   private void login(){
      Assert.assertNotNull( onCloseHandler );
      Mockito.when( alert.friendly_getResult() ).thenReturn( systemUnderTest.loginButtonType() );
      onCloseHandler.handle( loginEvent );
   }//End Method
   
   /**
    * Method to run the {@link Runnable} on the fx thread, and wait the time millis for it to complete,
    * synchronising the threads.
    * @param runnable the {@link Runnable} to run.
    * @param time the milliseconds to wait for the {@link Runnable} to be executed.
    */
   private void runOnFxThreadAndWait( Runnable runnable, long time ) {
      CountDownLatch latch = new CountDownLatch( 1 );
      BooleanProperty result = new SimpleBooleanProperty( false );
      PlatformImpl.runLater( () -> {
         runnable.run();
         result.set( true );
         latch.countDown();  
      } );
      try {
         latch.await();
         Assert.assertTrue( result.get() );
      } catch ( InterruptedException e ) {
         Assert.fail( "CountDownLatch failed." );
      }
   }//End Method

}//End Class
