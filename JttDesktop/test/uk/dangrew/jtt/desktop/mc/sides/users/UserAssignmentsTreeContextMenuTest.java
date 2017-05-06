/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.users;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.javafx.contextmenu.ContextMenuWithCancel;
import uk.dangrew.jtt.desktop.mc.sides.users.UserAssignment;
import uk.dangrew.jtt.desktop.mc.sides.users.UserAssignmentEvent;
import uk.dangrew.jtt.desktop.mc.sides.users.UserAssignmentsTree;
import uk.dangrew.jtt.desktop.mc.sides.users.UserAssignmentsTreeContextMenu;
import uk.dangrew.jtt.model.event.structure.Event;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;

/**
 * {@link UserAssignmentsTreeContextMenu} test.
 */
public class UserAssignmentsTreeContextMenuTest {
   
   private static final long TIMESTAMP = 39487534L;
   
   private UserAssignmentEvent assignments;
   private JenkinsDatabase database;
   
   private JenkinsUser user1;
   
   private UserAssignment assignment1;
   private UserAssignment assignment2;
   private UserAssignment assignment3;
   
   private UserAssignmentsTree tree;
   private UserAssignmentsTreeContextMenu systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      assignments = new UserAssignmentEvent();
      assignments.clearAllSubscriptions();
      
      database = new TestJenkinsDatabaseImpl();
      database.store( user1 = new JenkinsUserImpl( "Dan" ) );
      
      tree = new UserAssignmentsTree( database );
      systemUnderTest = new UserAssignmentsTreeContextMenu( tree );
      
      assignment1 = new UserAssignment( user1, TIMESTAMP, "anything", "nothing" );
      assignment2 = new UserAssignment( user1, TIMESTAMP, "anything", "nothing" );
      assignment3 = new UserAssignment( user1, TIMESTAMP, "anything", "nothing" );
      
      assignments.fire( new Event<>( assignment1 ) );
      assignments.fire( new Event<>( assignment2 ) );
      assignments.fire( new Event<>( assignment3 ) );
   }//End Method
   
   @Test public void shouldHaveExpectedMenus() {
      assertThat( systemUnderTest.getItems(), hasSize( 3 ) );
   }//End Method
   
   @Test public void shouldProvideAssignOption() {
      assertThat( systemUnderTest.getItems().get( 0 ), is( systemUnderTest.completeMenu() ) );
   }//End Method
   
   @Test public void shouldProvideCancelOption() {
      assertThat( systemUnderTest, is( instanceOf( ContextMenuWithCancel.class ) ) );
   }//End Method
   
   @Test public void shouldRemoveSingleSelection(){
      tree.getSelectionModel().select( 1 );
      
      systemUnderTest.completeMenu().fire();
      assertThat( tree.getLayoutManager().contains( assignment1 ), is( false ) );
      assertThat( tree.getLayoutManager().contains( assignment2 ), is( true ) );
      assertThat( tree.getLayoutManager().contains( assignment3 ), is( true ) );
   }//End Method
   
   @Test public void shouldRemoveMultipleForMultipleSelection(){
      tree.getSelectionModel().selectIndices( 2, 3 );
      
      systemUnderTest.completeMenu().fire();
      assertThat( tree.getLayoutManager().contains( assignment1 ), is( true ) );
      assertThat( tree.getLayoutManager().contains( assignment2 ), is( false ) );
      assertThat( tree.getLayoutManager().contains( assignment3 ), is( false ) );
   }//End Method
   
   @Test public void shouldIgnoreNoSelection(){
      systemUnderTest.completeMenu().fire();
      assertThat( tree.getLayoutManager().contains( assignment1 ), is( true ) );
      assertThat( tree.getLayoutManager().contains( assignment2 ), is( true ) );
      assertThat( tree.getLayoutManager().contains( assignment3 ), is( true ) );
   }//End Method
   
   @Test public void shouldIgnoreNoNotificationItem(){
      tree.getSelectionModel().select( 0 );
      
      systemUnderTest.completeMenu().fire();
      assertThat( tree.getLayoutManager().contains( assignment1 ), is( true ) );
      assertThat( tree.getLayoutManager().contains( assignment2 ), is( true ) );
      assertThat( tree.getLayoutManager().contains( assignment3 ), is( true ) );
   }//End Method
   
   @Test public void shouldRaiseMultipleEventForMultipleSelectionIncludingHeaderItems(){
      tree.getSelectionModel().selectIndices( 0, 2, 3 );
      
      systemUnderTest.completeMenu().fire();
      assertThat( tree.getLayoutManager().contains( assignment1 ), is( true ) );
      assertThat( tree.getLayoutManager().contains( assignment2 ), is( false ) );
      assertThat( tree.getLayoutManager().contains( assignment3 ), is( false ) );
   }//End Method
   
}//End Class
