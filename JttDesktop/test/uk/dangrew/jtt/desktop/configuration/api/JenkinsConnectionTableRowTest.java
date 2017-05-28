/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.api;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;
import uk.dangrew.jtt.connection.api.sources.TestJenkinsConnection;

public class JenkinsConnectionTableRowTest {

   private JenkinsConnection connection;
   private JenkinsConnectionTableRow systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      connection = new TestJenkinsConnection();
      systemUnderTest = new JenkinsConnectionTableRow( connection );
   }//End Method

   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullConnection() {
      new JenkinsConnectionTableRow( null );
   }//End Method
   
   @Test public void shouldProvideConnection() {
      assertThat( systemUnderTest.getConnection(), is( connection ) );
   }//End Method
   
   @Test public void shouldProvideLocation() {
      assertThat( systemUnderTest.getLocation(), is( connection.location() ) );
   }//End Method
   
   @Test public void shouldProvideUsername() {
      assertThat( systemUnderTest.getUser(), is( connection.username() ) );
   }//End Method
   
   @Test public void shouldProvideConnectedProperty() {
      assertThat( systemUnderTest.connected().get(), is( false ) );
      systemUnderTest.connected().set( true );
      assertThat( systemUnderTest.connected().get(), is( true ) );
   }//End Method

}//End Class
