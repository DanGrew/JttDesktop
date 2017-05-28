/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
 package uk.dangrew.jtt.desktop.configuration.api;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;

/**
 * The {@link JenkinsConnectionTableRow} is responsible for providing a read only object that the {@link JenkinsConnectionTable}
 * can interact with and display, representing a single row of data.
 */
public class JenkinsConnectionTableRow {

   private final JenkinsConnection connection;
   private final BooleanProperty connected;
   
   /**
    * Constructs a new {@link JenkinsConnectionTableRow}.
    * @param connection the {@link JenkinsConnection} to display in a row.
    */
   public JenkinsConnectionTableRow( JenkinsConnection connection ) {
      if ( connection == null ) {
         throw new IllegalArgumentException( "Null connection provided." );
      }
      
      this.connection = connection;
      this.connected = new SimpleBooleanProperty( false );
   }//End Constructor
   
   /**
    * Access to the associated {@link JenkinsConnection}.
    * @return the {@link JenkinsConnection}.
    */
   public JenkinsConnection getConnection(){
      return connection;
   }//End Method
   
   /**
    * Access to the location associated with the {@link JenkinsConnection}.
    * @return the location.
    */
   public String getLocation() {
      return connection.location();
   }//End Method

   /**
    * Access to the user associated with the {@link JenkinsConnection}.
    * @return the user.
    */
   public String getUser() {
      return connection.username();
   }//End Method

   /**
    * Access to a property representing whether the connection is actively being polled.
    * @return the property.
    */
   public BooleanProperty connected() {
      return connected;
   }//End Method

}//End Class
