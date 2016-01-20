/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package credentials.login;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import graphics.JavaFxInitializer;

/**
 * {@link JenkinsLogin} test.
 */
public class JenkinsLoginTest {

   private CredentialsVerifier verifier;
   private JenkinsLogin systemUnderTest;
   
   /**
    * Method to initialise the system under test.
    */
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      verifier = Mockito.mock( CredentialsVerifier.class );
      systemUnderTest = new JenkinsLogin( verifier );
   }//End Method
   
   @Ignore
   @Test public void manualInspection() throws InterruptedException {
      JavaFxInitializer.threadedLaunch( () -> { return new JenkinsLogin( null ); } );
      
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void shouldAttemptToConnect(){
      final String jenkinsLocation = "any location";
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      systemUnderTest.getLoginButton().fire();
      Mockito.verify( verifier ).attemptLogin( jenkinsLocation, user, password );
   }//End Method
   
   @Test public void shouldNotAttemptWithNullLocation(){
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( null );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      systemUnderTest.getLoginButton().fire();
      Mockito.verifyNoMoreInteractions( verifier );
   }//End Method
   
   @Test public void shouldNotAttemptWithNullUsername(){
      final String jenkinsLocation = "any location";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( null );
      systemUnderTest.getPasswordField().setText( password );
      
      systemUnderTest.getLoginButton().fire();
      Mockito.verifyNoMoreInteractions( verifier );
   }//End Method
   
   @Test public void shouldNotAttemptWithNullPassword(){
      final String jenkinsLocation = "any location";
      final String user = "any user";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( null );
      
      systemUnderTest.getLoginButton().fire();
      Mockito.verifyNoMoreInteractions( verifier );
   }//End Method
   
   @Test public void shouldNotAttemptWithEmptyLocation(){
      final String jenkinsLocation = "     ";
      final String user = "any user";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      systemUnderTest.getLoginButton().fire();
      Mockito.verifyNoMoreInteractions( verifier );
   }//End Method
   
   @Test public void shouldNotAttemptWithEmptyUsername(){
      final String jenkinsLocation = "any location";
      final String user = "     ";
      final String password = "any password";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      systemUnderTest.getLoginButton().fire();
      Mockito.verifyNoMoreInteractions( verifier );
   }//End Method
   
   @Test public void shouldNotAttemptWithEmptyPassword(){
      final String jenkinsLocation = "any location";
      final String user = "any user";
      final String password = "     ";
      
      systemUnderTest.getJenkinsLocationField().setText( jenkinsLocation );
      systemUnderTest.getUserNameField().setText( user );
      systemUnderTest.getPasswordField().setText( password );
      
      systemUnderTest.getLoginButton().fire();
      Mockito.verifyNoMoreInteractions( verifier );
   }//End Method

}//End Class
