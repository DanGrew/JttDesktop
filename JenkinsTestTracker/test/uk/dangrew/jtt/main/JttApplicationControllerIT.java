/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.main;

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
import uk.dangrew.jtt.credentials.login.JenkinsLogin;
import uk.dangrew.jtt.friendly.controlsfx.FriendlyAlert;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.main.JttApplicationController;
import uk.dangrew.jtt.main.selector.ToolSelector;

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
   
   @Test public void shouldProvideToolsChoiceFunction(){
      //this test will use only one method fully...
      ToolSelector selector = mock( ToolSelector.class );
      systemUnderTest = Mockito.mock( JttApplicationController.class );
      //...the tools selector method...
      doCallRealMethod().when( systemUnderTest ).selectTool( selector );
      
      //...we will intercept the alerts because alerts are nasty...
      FriendlyAlert selectorAlert = mock( FriendlyAlert.class );
      when( systemUnderTest.constructAlert() ).thenReturn( selectorAlert );
      //...providing junk as a response...
      when( systemUnderTest.showAndWait( selectorAlert ) ).thenReturn( Optional.of( new ButtonType( "anything" ) ) );
      
      //...we shall fix the result...
      when( selector.isLaunchResult( Mockito.any() ) ).thenReturn( true );
      assertThat( systemUnderTest.selectTool( selector ), is( true ) );
      
      //...for both answers...
      when( selector.isLaunchResult( Mockito.any() ) ).thenReturn( false );
      assertThat( systemUnderTest.selectTool( selector ), is( false ) );
      
      //...and because we don't like spying, verify no further interactions...
      //...where we expect 2 for each, one for each login...
      verify( systemUnderTest, times( 2 ) ).constructAlert();
      verify( systemUnderTest, times( 2 ) ).showAndWait( selectorAlert );
      verify( systemUnderTest, times( 2 ) ).selectTool( selector );
      //...and ansolutely NOTHING else...
      verifyNoMoreInteractions( systemUnderTest );
      
      //...finally verifying the alert was configured both times.
      verify( selector, times( 2 ) ).configureAlert( selectorAlert );
   }//End Method

}//End Class
