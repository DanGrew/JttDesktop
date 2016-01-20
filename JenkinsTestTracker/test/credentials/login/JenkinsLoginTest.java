/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package credentials.login;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import com.sun.javafx.application.PlatformImpl;

import graphics.JavaFxInitializer;
import javafx.scene.layout.GridPane;

/**
 * {@link JenkinsLogin} test.
 */
public class JenkinsLoginTest {

   private CredentialsVerifier verifier;
   private JenkinsLogin systemUnderTest;
   
   /**
    * Method to initialise the system under test.
    */
   @Before public void initialiseSystemUnderTest() {
      JavaFxInitializer.startPlatform();
      verifier = Mockito.mock( CredentialsVerifier.class );
      runOnFxThreadAndWait( () -> {
         systemUnderTest = new JenkinsLogin( verifier );
      }, 10000 );
   }//End Method
   
   @Ignore
   @Test public void manualInspection() {
      runOnFxThreadAndWait( () -> systemUnderTest.showAndWait(), 100000 );
   }//End Method
   
   @Test public void shouldAttemptToConnect(){
      final String jenkinsLocation = "any location";
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      login();
      Mockito.verify( verifier ).attemptLogin( jenkinsLocation, user, password );
   }//End Method
   
   @Test public void shouldNotAttemptWithNullLocation(){
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( null );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      login();
      Mockito.verifyNoMoreInteractions( verifier );
   }//End Method
   
   @Test public void shouldNotAttemptWithNullUsername(){
      final String jenkinsLocation = "any location";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( null );
      systemUnderTest.getPasswordField().setText( password );
      
      login();
      Mockito.verifyNoMoreInteractions( verifier );
   }//End Method
   
   @Test public void shouldNotAttemptWithNullPassword(){
      final String jenkinsLocation = "any location";
      final String user = "any user";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( null );
      
      login();
      Mockito.verifyNoMoreInteractions( verifier );
   }//End Method
   
   @Test public void shouldNotAttemptWithEmptyLocation(){
      final String jenkinsLocation = "     ";
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      login();
      Mockito.verifyNoMoreInteractions( verifier );
   }//End Method
   
   @Test public void shouldNotAttemptWithEmptyUsername(){
      final String jenkinsLocation = "any location";
      final String user = "     ";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      login();
      Mockito.verifyNoMoreInteractions( verifier );
   }//End Method
   
   @Test public void shouldNotAttemptWithEmptyPassword(){
      final String jenkinsLocation = "any location";
      final String user = "any user";
      final String password = "     ";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      login();
      Mockito.verifyNoMoreInteractions( verifier );
   }//End Method
   
   @Test public void shouldNotAttemptWithInitialValues(){
      login();
      Mockito.verifyNoMoreInteractions( verifier );
   }//End Method
   
   @Test public void shouldContainAllTextElementsInChildren(){
      Assert.assertTrue( systemUnderTest.getButtonTypes().contains( systemUnderTest.loginButtonType() ) );
      Assert.assertTrue( systemUnderTest.getButtonTypes().contains( systemUnderTest.cancelButtonType() ) );
      
      GridPane wrapper = ( GridPane )systemUnderTest.getDialogPane().getContent();
      Assert.assertTrue( wrapper.getChildren().contains( systemUnderTest.getJenkinsLocationField() ) );
      Assert.assertTrue( wrapper.getChildren().contains( systemUnderTest.getUserNameField() ) );
      Assert.assertTrue( wrapper.getChildren().contains( systemUnderTest.getPasswordField() ) );
   }//End Method
   
   /**
    * Convenience method for performing a login on the fx thread.
    */
   private void login(){
      runOnFxThreadAndWait( () -> {
         systemUnderTest.resultProperty().setValue( systemUnderTest.loginButtonType() );
      }, 5000 );
   }//End Method
   
   /**
    * Method to run the {@link Runnable} on the fx thread, and wait the time millis for it to complete,
    * synchronising the threads.
    * @param runnable the {@link Runnable} to run.
    * @param time the milliseconds to wait for the {@link Runnable} to be executed.
    */
   private void runOnFxThreadAndWait( Runnable runnable, long time ) {
      final Thread testThread = Thread.currentThread();
      PlatformImpl.runLater( () -> {
         runnable.run();
         testThread.interrupt();
      } );
      try {
         Thread.sleep( time );
         Assert.fail( "JavaFx did not respond in time." );
      } catch ( InterruptedException e ) {
         //Carry on.
      }
   }//End Method

}//End Class
