/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.core;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javafx.scene.control.ButtonType;
import uk.dangrew.jtt.desktop.core.JttApplicationController;
import uk.dangrew.jtt.desktop.credentials.login.JenkinsLogin;
import uk.dangrew.jtt.desktop.friendly.controlsfx.FriendlyAlert;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;

/**
 * {@link JttApplicationController} test.
 */
public class JttApplicationControllerIT {

   private JttApplicationController systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new JttApplicationController();
   }//End Method
   
   @Test public void shouldProvideFriendlyAlertsForInput() {
      JavaFxInitializer.runAndWait( () -> {
         FriendlyAlert firstRequest = systemUnderTest.constructAlert();
         assertThat( firstRequest, notNullValue() );
         assertThat( systemUnderTest.constructAlert(), not( firstRequest ) );
      } );
   }//End Method
   
   @Test public void shouldShowAlertAndWaitForResponse() {
      FriendlyAlert alert = mock( FriendlyAlert.class );
      
      Optional< ButtonType > result = Optional.empty();
      
      when( alert.friendly_showAndWait() ).thenReturn( result );
      assertThat( systemUnderTest.showAndWait( alert ), is( result ) );
      
      verify( alert ).friendly_showAndWait();
      verifyNoMoreInteractions( alert );
   }//End Method
   
   @Test public void shouldProvideLoggingInFunction(){
      //this test will use only one method fully...
      JenkinsLogin login = mock( JenkinsLogin.class );
      systemUnderTest = Mockito.mock( JttApplicationController.class );
      //...the login method...
      doCallRealMethod().when( systemUnderTest ).login( login );
      
      //...we will intercept the alerts because alerts are nasty...
      FriendlyAlert loginAlert = mock( FriendlyAlert.class );
      when( systemUnderTest.constructAlert() ).thenReturn( loginAlert );
      //...providing junk as a response...
      when( systemUnderTest.showAndWait( loginAlert ) ).thenReturn( Optional.of( new ButtonType( "anything" ) ) );
      
      //...we shall fix the result...
      when( login.isLoginResult( Mockito.any() ) ).thenReturn( true );
      assertThat( systemUnderTest.login( login ), is( true ) );
      
      //...for both answers...
      when( login.isLoginResult( Mockito.any() ) ).thenReturn( false );
      assertThat( systemUnderTest.login( login ), is( false ) );
      
      //...and because we don't like spying, verify no further interactions...
      //...where we expect 2 for each, one for each login...
      verify( systemUnderTest, times( 2 ) ).constructAlert();
      verify( systemUnderTest, times( 2 ) ).showAndWait( loginAlert );
      verify( systemUnderTest, times( 2 ) ).login( login );
      //...and ansolutely NOTHING else...
      verifyNoMoreInteractions( systemUnderTest );
      
      //...finally verifying the alert was configured both times.
      verify( login, times( 2 ) ).configureAlert( loginAlert );
   }//End Method
   
}//End Class
