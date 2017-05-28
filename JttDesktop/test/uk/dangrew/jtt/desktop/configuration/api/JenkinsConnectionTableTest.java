/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.api;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.apache.http.client.HttpClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import uk.dangrew.jtt.connection.api.connections.ConnectionEvent;
import uk.dangrew.jtt.connection.api.connections.ConnectionState;
import uk.dangrew.jtt.connection.api.sources.JenkinsConnection;
import uk.dangrew.jtt.connection.api.sources.TestJenkinsConnection;
import uk.dangrew.jtt.model.event.structure.Event;
import uk.dangrew.sd.graphics.launch.TestApplication;

public class JenkinsConnectionTableTest {
   
   private JenkinsConnectionTable systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      TestApplication.startPlatform();
      systemUnderTest = new JenkinsConnectionTable();
   }//End Method

   @Ignore
   @Test public void manual() throws InterruptedException {
      TestApplication.launch( () -> new JenkinsConnectionTable() );
      
      new ConnectionEvent().fire( new Event<>( new Pair<>( 
               new TestJenkinsConnection( "192.1.0.3:324", "danielg", "p", Mockito.mock( HttpClient.class ) ), 
               ConnectionState.Connected 
      ) ) );
      
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void shouldBeSingleSelectionOnly() {
      assertThat( systemUnderTest.getSelectionModel().getSelectionMode(), is( SelectionMode.SINGLE ) );
   }//End Method
   
   @Test public void shouldProvideRowsInAFriendlyWay() {
      assertThat( systemUnderTest.getItems(), is( systemUnderTest.getRows() ) );
   }//End Method
   
   @Test public void shouldProvideAppropriateColumns() throws InterruptedException {
      TestApplication.launch( () -> new BorderPane( systemUnderTest ) );
      assertThat( systemUnderTest.getColumns(), hasSize( 3 ) );
      
      assertThat( systemUnderTest.getColumns().get( 0 ).getText(), is( JenkinsConnectionTable.COLUMN_TITLE_LOCATION ) );
      assertThat( systemUnderTest.getColumns().get( 1 ).getText(), is( JenkinsConnectionTable.COLUMN_TITLE_USER ) );
      assertThat( systemUnderTest.getColumns().get( 2 ).getText(), is( JenkinsConnectionTable.COLUMN_TITLE_CONNECTED ) );
      
      assertThat( systemUnderTest.getColumns().get( 0 ).getPrefWidth(), is( systemUnderTest.getWidth() / JenkinsConnectionTable.LOCATION_PROPORTION_WIDTH ) );
      assertThat( systemUnderTest.getColumns().get( 1 ).getPrefWidth(), is( systemUnderTest.getWidth() / JenkinsConnectionTable.USER_PROPORTION_WIDTH ) );
      assertThat( systemUnderTest.getColumns().get( 2 ).getPrefWidth(), is( systemUnderTest.getWidth() / JenkinsConnectionTable.CONNECTED_PROPORTION_WIDTH ) );
   }//End Method
   
   @Test public void shouldProvideCheckBoxForConnected(){
      assertThat( systemUnderTest.getColumns().get( 2 ).getCellFactory().call( new TableColumn<>() ), is( instanceOf( CheckBoxTableCell.class ) ) );
   }//End Method
   
   @Test public void shouldDisplayValuesInTable(){
      JenkinsConnection connection = new TestJenkinsConnection();
      systemUnderTest.getRows().add( new JenkinsConnectionTableRow( connection ) );
      
      assertThat( systemUnderTest.getColumns().get( 0 ).getCellData( 0 ), is( connection.location() ) );
      assertThat( systemUnderTest.getColumns().get( 1 ).getCellData( 0 ), is( connection.username() ) );
      assertThat( systemUnderTest.getColumns().get( 2 ).getCellData( 0 ), is( false ) );
   }//End Method

}//End Class
