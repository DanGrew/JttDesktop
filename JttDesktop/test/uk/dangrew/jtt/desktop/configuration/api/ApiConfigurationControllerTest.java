/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.api;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.util.Pair;
import uk.dangrew.jtt.connection.api.connections.ConnectionEvent;
import uk.dangrew.jtt.connection.api.connections.ConnectionManager;
import uk.dangrew.jtt.connection.api.connections.ConnectionState;
import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;
import uk.dangrew.jtt.connection.api.sources.TestJenkinsConnection;
import uk.dangrew.jtt.model.event.structure.Event;
import uk.dangrew.sd.graphics.launch.TestApplication;

public class ApiConfigurationControllerTest {

   private ConnectionEvent connectionEvents;
   
   private List< JenkinsConnection > connections;
   private JenkinsConnection connection1;
   private JenkinsConnection connection2;
   private JenkinsConnection connection3;
   @Mock private ConnectionManager connectionManager;
   
   private JenkinsConnectionTable table;
   @Mock private JenkinsConnectionControls controls;
   private ApiConfigurationController systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      table = new JenkinsConnectionTable();
      connectionEvents = new ConnectionEvent();
      
      connection1 = new TestJenkinsConnection( "a", "b", "c", null );
      connection2 = new TestJenkinsConnection( "d", "e", "f", null );
      connection3 = new TestJenkinsConnection( "g", "h", "i", null );
      connections = Arrays.asList( connection1, connection2 );
      when( connectionManager.connections() ).thenReturn( connections );
      
      when( connectionManager.isActive( connection1 ) ).thenReturn( false );
      when( connectionManager.isActive( connection2 ) ).thenReturn( true );
      
      systemUnderTest = new ApiConfigurationController( connectionManager );
      systemUnderTest.control( table, controls );
   }//End Method

   @Test public void shouldAddExistingConnectionsToTable() {
      assertThat( table.getRows(), hasSize( 2 ) );
      assertThat( table.getRows().get( 0 ).getConnection(), is( connection1 ) );
      assertThat( table.getRows().get( 1 ).getConnection(), is( connection2 ) );
   }//End Method
   
   @Test public void shouldUpdateConnectedFlagToTrue(){
      assertThat( table.getRows().get( 0 ).connected().get(), is( false ) );
      connectionEvents.fire( new Event<>( new Pair<>( connection1, ConnectionState.Connected ) ) );
      assertThat( table.getRows().get( 0 ).connected().get(), is( true ) );
   }//End Method
   
   @Test public void shouldUpdateConnectedFlagToFalse(){
      assertThat( table.getRows().get( 1 ).connected().get(), is( true ) );
      connectionEvents.fire( new Event<>( new Pair<>( connection2, ConnectionState.Disconnected ) ) );
      assertThat( table.getRows().get( 1 ).connected().get(), is( false ) );
   }//End Method
   
   @Test public void shouldRemoveForgottenConnectionFromTable(){
      assertThat( table.getRows().get( 1 ).connected().get(), is( true ) );
      connectionEvents.fire( new Event<>( new Pair<>( connection2, ConnectionState.Forgotten ) ) );
      assertThat( table.getRows(), hasSize( 1 ) );
   }//End Method
   
   @Test public void shouldAddNewConnectionsToTable() {
      connectionEvents.fire( new Event<>( new Pair<>( connection3, ConnectionState.Disconnected ) ) );
      assertThat( table.getRows().get( 2 ).getConnection(), is( connection3 ) );
   }//End Method
   
   @Test public void shouldNotAddToTableIfAlreadyPresent() {
      connectionEvents.fire( new Event<>( new Pair<>( connection2, ConnectionState.Disconnected ) ) );
      assertThat( table.getRows(), hasSize( 2 ) );
      assertThat( table.getRows().get( 1 ).getConnection(), is( connection2 ) );
   }//End Method
   
   @Test public void shouldProvideControlsForSelection() {
      table.getSelectionModel().select( 0 );
      verify( controls ).showConnection( connection1, false );
      
      table.getSelectionModel().select( 1 );
      verify( controls ).showConnection( connection2, true );
   }//End Method
   
   @Test public void shouldResetControlsForDeselection() {
      table.getSelectionModel().select( 0 );
      table.getSelectionModel().clearSelection();
      
      //causes interactions with other functions so taking no action to deselection
      verify( controls, never() ).cancelNew();
   }//End Method
   
   @Test public void shouldMakeNewConnection() {
      String location = "location";
      String user = "user";
      String pass = "pass";
      
      when( connectionManager.makeConnection( location, user, pass ) ).thenReturn( connection3 );
      JenkinsConnection connection = systemUnderTest.connect( location, user, pass );
      assertThat( connection, is( connection3 ) );
   }//End Method
   
   @Test public void shouldHandleFailedNewConnection() {
      String location = "location";
      String user = "user";
      String pass = "pass";
      
      when( connectionManager.makeConnection( location, user, pass ) ).thenReturn( null );
      JenkinsConnection connection = systemUnderTest.connect( location, user, pass );
      assertThat( connection, is( nullValue() ) );
   }//End Method
   
   @Test public void shouldConnect() {
      systemUnderTest.connect( connection1 );
      verify( connectionManager ).connect( connection1 );
   }//End Method
   
   @Test public void shouldDisconnect() {
      systemUnderTest.disconnect( connection2 );
      verify( connectionManager ).disconnect( connection2 );
   }//End Method
   
   @Test public void shouldForget() {
      systemUnderTest.forget( connection1 );
      verify( connectionManager ).forget( connection1 );
   }//End Method
   
   @Test public void shouldClearSelection() {
      table.getSelectionModel().select( 0 );
      systemUnderTest.clearSelection();
      assertThat( table.getSelectionModel().getSelectedItems(), is( empty() ) );
   }//End Method

}//End Class
