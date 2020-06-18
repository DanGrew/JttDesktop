/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.api;

import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import uk.dangrew.jtt.connection.api.sources.TestJenkinsConnection;
import uk.dangrew.jtt.connection.login.JenkinsLoginDetails;
import uk.dangrew.kode.javafx.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.configuration.api.ButtonConfigurer.StateTranslator;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class JenkinsConnectionControlsTest {

   private static final String ENTERED_LOCATION = "entered location";
   private static final String ENTERED_USER = "entered user";
   private static final String ENTERED_PASS = "entered password";
   
   private TestJenkinsConnection connection;
   
   @Spy private JavaFxStyle styling;
   @Mock private StateTranslator translator;
   @Mock private ButtonConfigurer configurer;
   
   @Mock private ApiConfigurationController controller;
   private JenkinsConnectionControls systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      when( translator.apply( Mockito.any() ) ).thenReturn( configurer );
      
      connection = new TestJenkinsConnection();
      systemUnderTest = new JenkinsConnectionControls( styling, translator, controller );
      
      systemUnderTest.details().locationField().setText( ENTERED_LOCATION );
      systemUnderTest.details().usernameField().setText( ENTERED_USER );
      systemUnderTest.details().passwordField().setText( ENTERED_PASS );
      when( controller.connect( ENTERED_LOCATION, ENTERED_USER, ENTERED_PASS ) ).thenReturn( connection );
   }//End Method

   @Test public void shouldProvideDetailsInCenter() {
      assertThat( systemUnderTest.getCenter(), is( systemUnderTest.details() ) );
   }//End Method
   
   @Test public void shouldProvideControls() {
      assertThat( systemUnderTest.getBottom(), is( systemUnderTest.controls() ) );
      verify( styling ).configureConstraintsForEvenColumns( systemUnderTest.controls(), 3 );
      
      assertThat( systemUnderTest.controls().getChildren().contains( systemUnderTest.firstButton() ), is( true ) );
      assertThat( systemUnderTest.controls().getChildren().contains( systemUnderTest.secondButton() ), is( true ) );
      assertThat( systemUnderTest.controls().getChildren().contains( systemUnderTest.thirdButton() ), is( true ) );
      
      assertThat( systemUnderTest.firstButton().getPrefWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.secondButton().getPrefWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.thirdButton().getPrefWidth(), is( Double.MAX_VALUE ) );
   }//End Method
   
   @Test public void shouldShowConnectedConnection() {
      systemUnderTest.showConnection( connection, true );
      verifyButtonsConfiguredCorrectly( JenkinsControlButtonStates.SelectedConnected, 1 );
      assertThatDetailsAreEnabled( false );
      assertTextPresent( connection.location(), connection.username(), connection.password() );
   }//End Method
   
   @Test public void shouldShowDisconnectedConnection() {
      systemUnderTest.showConnection( connection, false );
      verifyButtonsConfiguredCorrectly( JenkinsControlButtonStates.SelectedDisconnected, 1 );
      assertThatDetailsAreEnabled( false );
      assertTextPresent( connection.location(), connection.username(), connection.password() );
   }//End Method
   
   @Test public void shouldProvideNewInputMethod() {
      systemUnderTest.createNew();
      verifyButtonsConfiguredCorrectly( JenkinsControlButtonStates.EnteringNew, 1 );
      assertThatDetailsAreEnabled( true );
      verify( controller ).clearSelection();
   }//End Method
   
   @Test public void shouldSaveNewConnection() {
      systemUnderTest.saveConnection();
      verifyButtonsConfiguredCorrectly( JenkinsControlButtonStates.SelectedDisconnected, 1 );
      assertThatDetailsAreEnabled( false );
      assertTextPresent( ENTERED_LOCATION, ENTERED_USER, ENTERED_PASS );
   }//End Method
   
   @Test public void shouldHandleRejectedNewConnection() {
      when( controller.connect( ENTERED_LOCATION, ENTERED_USER, ENTERED_PASS ) ).thenReturn( null );
      
      systemUnderTest.saveConnection();
      //only once in construction
      verify( translator, times( 1 ) ).apply( Mockito.any() );
      assertThatDetailsAreEnabled( true );
      assertTextPresent( ENTERED_LOCATION, ENTERED_USER, ENTERED_PASS );
   }//End Method
   
   @Test public void shouldCancelNewInputMethod() {
      systemUnderTest.cancelNew();
      verifyButtonsConfiguredCorrectly( JenkinsControlButtonStates.NoSelection, 2 );
      assertThatDetailsAreEnabled( false );
      assertTextPresent( null, null, null );
   }//End Method
   
   @Test public void shouldConnectExistingConnection() {
      systemUnderTest.saveConnection();
      systemUnderTest.connect();
      verifyButtonsConfiguredCorrectly( JenkinsControlButtonStates.SelectedConnected, 1 );
      assertThatDetailsAreEnabled( false );
      
      verify( controller ).connect( connection );
   }//End Method
   
   @Test public void shouldDisconnectExistingConnection() {
      systemUnderTest.saveConnection();
      systemUnderTest.connect();
      systemUnderTest.disconnect();
      verifyButtonsConfiguredCorrectly( JenkinsControlButtonStates.SelectedDisconnected, 2 );
      assertThatDetailsAreEnabled( false );
      
      verify( controller ).disconnect( connection );
   }//End Method
   
   @Test public void shouldForgetExistingConnection() {
      systemUnderTest.saveConnection();
      systemUnderTest.forget();
      verifyButtonsConfiguredCorrectly( JenkinsControlButtonStates.NoSelection, 2 );
      assertThatDetailsAreEnabled( false );
      
      assertTextPresent( null, null, null );
      verify( controller ).forget( connection );
   }//End Method
   
   @Test public void shouldClearSelectionBeforeTransitioning(){
      systemUnderTest.saveConnection();
      systemUnderTest.createNew();

      InOrder verifications = inOrder( controller, translator );
      verifications.verify( controller ).clearSelection();
      verifications.verify( translator ).apply( JenkinsControlButtonStates.EnteringNew );
   }//End Method

   private void verifyButtonsConfiguredCorrectly( JenkinsControlButtonStates state, int verifications ) {
      verify( translator, times( verifications ) ).apply( state );
      verify( configurer, atLeastOnce() ).configure( systemUnderTest.firstButton(), systemUnderTest.secondButton(), systemUnderTest.thirdButton(), systemUnderTest );
   }//End Method
   
   private void assertThatDetailsAreEnabled( boolean enabled ) {
      JenkinsLoginDetails details = ( JenkinsLoginDetails ) systemUnderTest.getCenter();
      assertThat( details.locationField().isDisabled(), is( !enabled ) );
      assertThat( details.usernameField().isDisabled(), is( !enabled ) );
      assertThat( details.passwordField().isDisabled(), is( !enabled ) );
   }//End Method
   
   private void assertTextPresent( String location, String username, String password ) {
      JenkinsLoginDetails details = ( JenkinsLoginDetails ) systemUnderTest.getCenter();
      assertThat( details.locationField().getText(), is( location ) );
      assertThat( details.usernameField().getText(), is( username ) );
      assertThat( details.passwordField().getText(), is( password ) );
   }//End Method
   
}//End Constructor
