/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
 package uk.dangrew.jtt.desktop.configuration.api;

import javafx.util.Pair;
import uk.dangrew.jtt.connection.api.connections.ConnectionEvent;
import uk.dangrew.jtt.connection.api.connections.ConnectionManager;
import uk.dangrew.jtt.connection.api.connections.ConnectionState;
import uk.dangrew.jtt.connection.api.connections.SystemWideConnectionManager;
import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;
import uk.dangrew.jtt.model.utility.observable.FunctionListChangeListenerImpl;
import uk.dangrew.kode.event.structure.Event;

/**
 * The {@link ApiConfigurationController} is responsible for controlling all elements associated with
 * the {@link JenkinsConnection} configuration and {@link JenkinsConnectionTable}.
 */
public class ApiConfigurationController {

   private final ConnectionManager connectionManager;
   private JenkinsConnectionTable jenkinsConnectionTable;
   private JenkinsConnectionControls controls;
   
   /**
    * Constructs a new {@link ApiConfigurationController}.
    */
   ApiConfigurationController() {
      this( new SystemWideConnectionManager().get() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ApiConfigurationController}.
    * @param connectionManager the {@link ConnectionManager}.
    */
   ApiConfigurationController( ConnectionManager connectionManager ) {
      this.connectionManager = connectionManager;
   }//End Constructor
   
   /**
    * Method to control the given elements of the configuration.
    * @param table the {@link JenkinsConnectionTable}.
    * @param controls the {@link JenkinsConnectionControls}.
    */
   void control( JenkinsConnectionTable table, JenkinsConnectionControls controls ) {
      this.jenkinsConnectionTable = table;
      this.controls = controls;
      
      connectionManager.connections().forEach( this::addRowToTable );
      
      this.jenkinsConnectionTable.getSelectionModel().getSelectedItems().addListener( 
               new FunctionListChangeListenerImpl<>( 
                        this::handleSelect, this::handleDeselect 
      ) );
      new ConnectionEvent().register( this::handleConnectionEvent );
   }//End Method
   
   /**
    * Method to find the {@link JenkinsConnectionTableRow} for the given {@link JenkinsConnection}.
    * @param connection the {@link JenkinsConnection} in question.
    * @return the first {@link JenkinsConnectionTableRow} with matching {@link JenkinsConnection}.
    */
   private JenkinsConnectionTableRow findRow( JenkinsConnection connection ) {
      for ( JenkinsConnectionTableRow row : jenkinsConnectionTable.getRows() ) {
         if ( row.getConnection() == connection ) {
            return row;
         }
      }
      return null;
   }//End Method
   
   /**
    * Method to handle a {@link ConnectionEvent} of state change.
    * @param value the event received.
    */
   private void handleConnectionEvent( Event< Pair< JenkinsConnection, ConnectionState > > value ) {
      JenkinsConnectionTableRow row = findRow( value.getValue().getKey() );
      if ( row == null ) {
         addRowToTable( value.getValue().getKey() );
      } else {
         if ( value.getValue().getValue() == ConnectionState.Connected ) {
            row.connected().set( true );
         } else if ( value.getValue().getValue() == ConnectionState.Disconnected ) {
            row.connected().set( false );
         } else if ( value.getValue().getValue() == ConnectionState.Forgotten ) {
            jenkinsConnectionTable.getRows().remove( row );
         }
      }
   }//End Method
   
   /**
    * Method to add a new {@link JenkinsConnection} to the {@link JenkinsConnectionTable}.
    * @param connection the {@link JenkinsConnection} to add.
    */
   private void addRowToTable( JenkinsConnection connection ) {
      JenkinsConnectionTableRow row = new JenkinsConnectionTableRow( connection );
      row.connected().set( connectionManager.isActive( connection ) );
      jenkinsConnectionTable.getRows().add( row );
   }//End Method
   
   /**
    * Method to handle the selecting of a {@link JenkinsConnectionTableRow}.
    * @param row the row selected.
    */
   private void handleSelect( JenkinsConnectionTableRow row ){
      controls.showConnection( row.getConnection(), row.connected().get() );
   }//End Method
   
   /**
    * Method to handle the deselecting of a {@link JenkinsConnectionTableRow}.
    * @param row the row deselected.
    */
   private void handleDeselect( JenkinsConnectionTableRow row ){
      //do nothing
   }//End Method
   
   /**
    * Method to establish a {@link JenkinsConnection} for the gien credentials.
    * @param location the location.
    * @param username the username.
    * @param password the password.
    * @return the {@link JenkinsConnection} if successful.
    */
   JenkinsConnection connect( String location, String username, String password ) {
      return connectionManager.makeConnection( location, username, password );
   }//End Method
   
   /**
    * Method to connect the given {@link JenkinsConnection}.
    * @param connection the {@link JenkinsConnection} to apply to.
    */
   void connect( JenkinsConnection connection ) {
      connectionManager.connect( connection );
   }//End Method
   
   /**
    * Method to disconnect the given {@link JenkinsConnection}.
    * @param connection the {@link JenkinsConnection} to apply to.
    */
   void disconnect( JenkinsConnection connection ) {
      connectionManager.disconnect( connection );
   }//End Method
   
   /**
    * Method to disconnect the given {@link JenkinsConnection}.
    * @param connection the {@link JenkinsConnection} to apply to.
    */
   void forget( JenkinsConnection connection ) {
      connectionManager.forget( connection );
   }//End Method
   
   /**
    * Method to clear the current {@link JenkinsConnection} selection in the table.
    */
   void clearSelection(){
      jenkinsConnectionTable.getSelectionModel().clearSelection();
   }//End Method

}//End Class
