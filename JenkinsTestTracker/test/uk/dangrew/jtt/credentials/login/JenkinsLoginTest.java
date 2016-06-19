/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.credentials.login;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.concurrent.CountDownLatch;

import org.apache.http.client.HttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sun.javafx.application.PlatformImpl;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import uk.dangrew.jtt.api.sources.ClientHandler;
import uk.dangrew.jtt.api.sources.ExternalApi;
import uk.dangrew.jtt.api.sources.JenkinsApiImpl;
import uk.dangrew.jtt.credentials.login.JenkinsLogin.InputValidator;
import uk.dangrew.jtt.friendly.controlsfx.FriendlyAlert;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;
import uk.dangrew.sd.core.message.Message;
import uk.dangrew.sd.core.progress.Progress;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * {@link JenkinsLogin} test.
 */
public class JenkinsLoginTest {

   @Mock private FriendlyAlert alert;
   private EventHandler< DialogEvent > onCloseHandler;
   @Mock private DialogEvent loginEvent;
   private Node content;
   private ObservableList< ButtonType > buttonTypes;
   @Mock private ExternalApi handler;
   @Mock private DigestViewer digestViewer;
   @Mock private JenkinsLoginDigest digest;
   @Captor private ArgumentCaptor< Progress > progressCaptor;
   @Captor private ArgumentCaptor< Message > messageCaptor;
   private JenkinsLogin systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
   }//End Method
   
   /**
    * Method to initialise the system under test.
    */
   @SuppressWarnings("unchecked") //Simply mocking genericized objects. 
   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      JavaFxInitializer.startPlatform();
      
      buttonTypes = FXCollections.observableArrayList();
      Mockito.when( alert.friendly_getButtonTypes() ).thenReturn( buttonTypes );
      
      Mockito.doAnswer( invocation -> {
         return onCloseHandler = ( EventHandler< DialogEvent > )invocation.getArguments()[ 0 ];
      } ).when( alert ).friendly_setOnCloseRequest( Mockito.any() );
      
      Mockito.doAnswer( invocation -> {
         return content = ( Node )invocation.getArguments()[ 0 ];
      } ).when( alert ).friendly_dialogSetContent( Mockito.any() );
      
      when( alert.friendly_dialogLookup( Mockito.any() ) ).thenReturn( new Button( "login" ) );
      PlatformImpl.runAndWait( () -> {
         /* Run the launch on PlatformImpl because its possible that two threads interact with StyleManager
          * in JavaFx at the same time and it is not thread safe in 8u40 but should be in at least 8u77. This
          * could be caused by JavaFx thread running in parallel to this initialisation from the test thread.*/
         systemUnderTest = new JenkinsLogin( handler, digestViewer, digest );
         systemUnderTest.configureAlert( alert );
      } );
   }//End Method
   
   @Ignore
   @Test public void manualInspection() {
      runOnFxThreadAndWait( () -> {
         FriendlyAlert alert = new FriendlyAlert( AlertType.INFORMATION );
         systemUnderTest.configureAlert( alert );
         alert.showAndWait();
      } );
   }//End Method
   
   @Ignore
   @Test public void manualConnectionTest() {
      runOnFxThreadAndWait( () -> {
         FriendlyAlert alert = new FriendlyAlert( AlertType.INFORMATION );
         systemUnderTest = new JenkinsLogin( new JenkinsApiImpl( new ClientHandler() ), new DigestViewer( 600, 200 ) );
         systemUnderTest.configureAlert( alert );
         alert.showAndWait();
      } );
   }//End Method
   
   @Test public void shouldAttemptToConnect(){
      final String jenkinsLocation = "any location";
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      login();
      verify( handler ).attemptLogin( jenkinsLocation, user, password );
      verify( digest ).acceptCredentials();
   }//End Method
   
   @Test public void shouldDigestSuccess(){
      when( handler.attemptLogin( anyString(), anyString(), anyString() ) ).thenReturn( mock( HttpClient.class ) );
      shouldAttemptToConnect();
      verify( digest ).loginSuccessful();
   }//End Method
   
   @Test public void shouldDigestFailure(){
      when( handler.attemptLogin( anyString(), anyString(), anyString() ) ).thenReturn( null );
      shouldAttemptToConnect();
      verify( digest ).loginFailed();
   }//End Method
   
   @Test public void successfulLoginShouldReplaceCloseHandlerSoEventIsNotConsumedAndCloseDialog(){
      shouldDigestSuccess();
      verify( alert ).friendly_setOnCloseRequest( JenkinsLogin.LOGIN_ACCEPTED_CLOSER );
      PlatformImpl.runAndWait( () -> {} );
      verify( alert ).friendly_close();
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
      verifyNoMoreInteractions( handler );
      verify( loginEvent ).consume();
      
      verify( digest ).validationError( JenkinsLogin.JENKINS_LOCATION_IS_NOT_VALID );
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
      
      verify( digest ).validationError( JenkinsLogin.USER_NAME_IS_NOT_VALID );
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
      
      verify( digest ).validationError( JenkinsLogin.PASSWORD_IS_NOT_VALID );
   }//End Method
   
   @Test public void shouldNotAttemptWithInitialValues(){
      login();
      Mockito.verifyNoMoreInteractions( handler );
      Mockito.verify( loginEvent ).consume();
   }//End Method
   
   @Test public void shouldContainAllTextElementsInChildren(){
      Assert.assertNotNull( content );
      Assert.assertTrue( content instanceof BorderPane );
      BorderPane borderPane = ( BorderPane )content;
      Assert.assertTrue( borderPane.getCenter() instanceof GridPane );
      GridPane gridPane = ( GridPane )borderPane.getCenter();
      
      Assert.assertTrue( gridPane.getChildren().contains( systemUnderTest.getJenkinsLocationField() ) );
      Assert.assertTrue( gridPane.getChildren().contains( systemUnderTest.getUserNameField() ) );
      Assert.assertTrue( gridPane.getChildren().contains( systemUnderTest.getPasswordField() ) );
   }//End Method
   
   @Test public void digestShouldBeProvidedInCollapsedPane(){
      Assert.assertNotNull( content );
      Assert.assertTrue( content instanceof BorderPane );
      BorderPane borderPane = ( BorderPane )content;
      
      Assert.assertTrue( borderPane.getBottom() instanceof TitledPane );
      TitledPane digestPane = ( TitledPane )borderPane.getBottom();
      assertThat( digestPane.getContent(), is( digestViewer ) );
      assertThat( digestPane.isExpanded(), is( false ) );
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
      runOnFxThreadAndWait( () -> {} );
      
      final String jenkinsLocation = null;
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      runOnFxThreadAndWait( () -> {} );
      Assert.assertTrue( systemUnderTest.validationMechanism().isInvalid() );
   }//End Method
   
   @Test public void shouldProvideVisualValidationForUsername(){
      runOnFxThreadAndWait( () -> {} );
      
      final String jenkinsLocation = "any location";
      final String user = null;
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      runOnFxThreadAndWait( () -> {} );
      Assert.assertTrue( systemUnderTest.validationMechanism().isInvalid() );
   }//End Method
   
   @Test public void shouldProvideVisualValidationForPassword(){
      runOnFxThreadAndWait( () -> {} );
      
      final String jenkinsLocation = "any user";
      final String user = "any user";
      final String password = null;
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      runOnFxThreadAndWait( () -> {} );
      Assert.assertTrue( systemUnderTest.validationMechanism().isInvalid() );
   }//End Method
   
   @Test public void shouldHaveControlsRegistered(){
      runOnFxThreadAndWait( () -> {} );
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
      final CountDownLatch latch = new CountDownLatch( 1 );
      Mockito.doAnswer( invocation -> { 
         latch.countDown(); 
         return null; 
      } ).when( alert ).friendly_separateThreadProcessingComplete();
      
      Assert.assertNotNull( onCloseHandler );
      Mockito.when( alert.friendly_getResult() ).thenReturn( systemUnderTest.loginButtonType() );
      onCloseHandler.handle( loginEvent );
      PlatformImpl.runAndWait( () -> {} );
      
      try {
         latch.await();
      } catch ( InterruptedException e ) {
         Assert.fail( "CountDownLatch interrupted." );
      }
      
      verify( loginEvent ).consume();
      verify( digest ).progress( progressCaptor.capture(), messageCaptor.capture() );
      assertThat( progressCaptor.getValue().getPercentage(), is( JenkinsLogin.LOGGIN_IN_PROGRESS ) );
      assertThat( messageCaptor.getValue().getMessage(), is( JenkinsLogin.LOGGING_IN ) );
   }//End Method
   
   /**
    * Method to run the {@link Runnable} on the fx thread, and wait the time millis for it to complete,
    * synchronising the threads.
    * @param runnable the {@link Runnable} to run.
    */
   private void runOnFxThreadAndWait( Runnable runnable ) {
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
   
   @Test public void shouldHaveResetProgressOnStartUp() {
      verify( digest ).resetLoginProgress();
   }//End Method

}//End Class
