/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.credentials.login;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogEvent;
import uk.dangrew.jtt.desktop.credentials.login.JenkinsLogin;
import uk.dangrew.jtt.desktop.credentials.login.JenkinsLoginProcessor;
import uk.dangrew.jtt.desktop.friendly.controlsfx.FriendlyAlert;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;

/**
 * {@link JenkinsLoginProcessor} test.
 */
public class JenkinsLoginProcessorTest {
   
   @Mock private DialogEvent event;
   private Button loginButton;
   @Mock private FriendlyAlert alert;
   private ButtonType loginButtonType;
   @Mock private JenkinsLogin jenkinsLogin;
   private JenkinsLoginProcessor systemUnderTest;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      loginButtonType = new ButtonType( "login" );
      loginButton = new Button();
      
      when( alert.friendly_dialogLookup( loginButtonType ) ).thenReturn( loginButton );
      systemUnderTest = new JenkinsLoginProcessor( alert, loginButtonType, jenkinsLogin );
   }//End Method

   @Test public void handleShouldNotDoAnythingIfButtonTypeIsNotLogin() {
      when( alert.friendly_getResult() ).thenReturn( new ButtonType( "anything" ) );
      
      systemUnderTest.handle( event );
      verifyZeroInteractions( jenkinsLogin, event );
   }//End Method
   
   @Test public void shouldConsumeEventIfLogin(){
      when( alert.friendly_getResult() ).thenReturn( loginButtonType );
      
      systemUnderTest.handle( event );
      verify( event ).consume();
   }//End Method
   
   @Test public void shouldDisableButtonProcessThenEnableButton(){
      when( alert.friendly_getResult() ).thenReturn( loginButtonType );
      
      CountDownLatch latch = new CountDownLatch( 2 );
      
      @SuppressWarnings("unchecked") Consumer< Boolean > consumer = mock( Consumer.class );
      loginButton.disableProperty().addListener( ( source, old, updated ) -> {
         consumer.accept( updated );
         latch.countDown();
      } );
      
      systemUnderTest.handle( event );
      
      try {
         latch.await( 5000, TimeUnit.MILLISECONDS );
      } catch ( InterruptedException e ) {
         fail( "Interrupted wait for latch." );
      }
      
      InOrder order = inOrder( consumer, alert, jenkinsLogin );
      order.verify( consumer ).accept( true );
      order.verify( jenkinsLogin ).prepareInputAndLogin( event, alert );
      order.verify( alert ).friendly_separateThreadProcessingComplete();
      order.verify( consumer ).accept( false );
   }//End Method

}//End Class
