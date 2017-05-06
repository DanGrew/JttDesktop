/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.view.tree;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.javafx.application.PlatformImpl;

import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.javafx.contextmenu.ContextMenuWithCancel;
import uk.dangrew.jtt.desktop.mc.model.Notification;
import uk.dangrew.jtt.desktop.mc.notifiers.jobs.BuildResultStatusNotification;
import uk.dangrew.jtt.desktop.mc.sides.users.UserAssignment;
import uk.dangrew.jtt.desktop.mc.sides.users.UserAssignmentEvent;
import uk.dangrew.jtt.desktop.mc.sides.users.shared.AssignmentMenu;
import uk.dangrew.jtt.desktop.mc.view.tree.NotificationEvent;
import uk.dangrew.jtt.desktop.mc.view.tree.NotificationTree;
import uk.dangrew.jtt.desktop.mc.view.tree.NotificationTreeContextMenu;
import uk.dangrew.jtt.desktop.utility.time.InstantProvider;
import uk.dangrew.jtt.model.event.structure.Event;
import uk.dangrew.jtt.model.event.structure.EventSubscription;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;

/**
 * {@link NotificationTreeContextMenu} test.
 */
public class NotificationTreeContextMenuTest {
   
   private static final long TIMESTAMP = 39487534L;
   
   private NotificationEvent notifications;
   private UserAssignmentEvent assignments;
   @Mock private EventSubscription< UserAssignment > subscription;
   @Captor private ArgumentCaptor< Event< UserAssignment > > captor;
   
   private JenkinsUser user1;
   private JenkinsUser user2;
   
   private Notification notification1;
   private Notification notification2;
   
   private JenkinsDatabase database;
   private NotificationTree tree;
   @Mock private InstantProvider instantProvider;
   private NotificationTreeContextMenu systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      notification1 = new BuildResultStatusNotification( new JenkinsJobImpl( "Job1" ), BuildResultStatus.ABORTED, BuildResultStatus.SUCCESS );
      notification2 = new BuildResultStatusNotification( new JenkinsJobImpl( "Job2" ), BuildResultStatus.SUCCESS, BuildResultStatus.UNSTABLE );
      
      notifications = new NotificationEvent();
      notifications.clearAllSubscriptions();
      
      assignments = new UserAssignmentEvent();
      assignments.clearAllSubscriptions();
      assignments.register( subscription );
      
      database = new TestJenkinsDatabaseImpl();
      database.store( user1 = new JenkinsUserImpl( "Dan" ) );
      database.store( user2 = new JenkinsUserImpl( "Liz" ) );
      
      tree = new NotificationTree( database );
      when( instantProvider.get() ).thenReturn( TIMESTAMP );
      systemUnderTest = new NotificationTreeContextMenu( tree, database, instantProvider );
   }//End Method
   
   @Test public void shouldHaveExpectedMenus() {
      assertThat( systemUnderTest.getItems(), hasSize( 3 ) );
   }//End Method
   
   @Test public void shouldProvideAssignOption() {
      assertThat( systemUnderTest.getItems().get( 0 ), is( instanceOf( AssignmentMenu.class ) ) );
   }//End Method
   
   @Test public void shouldProvideSeparator() {
      assertThat( systemUnderTest.getItems().get( 1 ), is( instanceOf( SeparatorMenuItem.class ) ) );
   }//End Method
   
   @Test public void shouldProvideCancelOption() {
      assertThat( systemUnderTest, is( instanceOf( ContextMenuWithCancel.class ) ) );
   }//End Method
   
   @Test public void shouldHideWhenCancelled() {
      BorderPane pane = new BorderPane();
      JavaFxInitializer.launchInWindow( () -> pane );

      PlatformImpl.runAndWait( () -> systemUnderTest.show( pane, 0, 0 ) );
      assertThat( systemUnderTest.isShowing(), is( true ) );
      PlatformImpl.runAndWait( () -> systemUnderTest.getItems().get( 2 ).fire() );
      assertThat( systemUnderTest.isShowing(), is( false ) );
   }//End Method
   
   @Test public void shouldAutoHide() {
      assertThat( systemUnderTest.isAutoHide(), is( true ) );
   }//End Method
   
   @Test public void shouldBeConnectedToDatabase(){
      assertThat( systemUnderTest.isConnectedTo( database ), is( true ) );
      assertThat( systemUnderTest.isConnectedTo( new TestJenkinsDatabaseImpl() ), is( false ) );
   }//End Method
   
   @Test public void shouldRaiseSingleEventForSingleSelection(){
      notifications.fire( new Event<>( notification1 ) );
      tree.getSelectionModel().select( 1 );
      
      systemUnderTest.assignMenu().getItems().get( 1 ).fire();
      verify( subscription ).notify( captor.capture() );
      
      UserAssignment assignment = captor.getValue().getValue();
      assertThat( assignment.getJenkinsUser(), is( user2 ) );
      assertThat( assignment.timestampProperty().get(), is( TIMESTAMP ) );
      assertThat( assignment.descriptionProperty().get(), is( notification1.getDescription() ) );
      assertThat( assignment.detailProperty().get(), is( NotificationTreeContextMenu.ENTER_DETAIL ) );
   }//End Method
   
   @Test public void shouldRaiseMultipleEventForMultipleSelection(){
      notifications.fire( new Event<>( notification1 ) );
      notifications.fire( new Event<>( notification2 ) );
      tree.getSelectionModel().selectIndices( 1, 2 );
      
      systemUnderTest.assignMenu().getItems().get( 0 ).fire();
      verify( subscription, times( 2 ) ).notify( captor.capture() );
      
      UserAssignment assignment = captor.getAllValues().get( 0 ).getValue();
      assertThat( assignment.getJenkinsUser(), is( user1 ) );
      assertThat( assignment.timestampProperty().get(), is( TIMESTAMP ) );
      assertThat( assignment.descriptionProperty().get(), is( notification1.getDescription() ) );
      assertThat( assignment.detailProperty().get(), is( NotificationTreeContextMenu.ENTER_DETAIL ) );
      
      assignment = captor.getAllValues().get( 1 ).getValue();
      assertThat( assignment.getJenkinsUser(), is( user1 ) );
      assertThat( assignment.timestampProperty().get(), is( TIMESTAMP ) );
      assertThat( assignment.descriptionProperty().get(), is( notification2.getDescription() ) );
      assertThat( assignment.detailProperty().get(), is( NotificationTreeContextMenu.ENTER_DETAIL ) );
   }//End Method
   
   @Test public void shouldIgnoreNoSelection(){
      systemUnderTest.assignMenu().getItems().get( 0 ).fire();
      verify( subscription, times( 0 ) ).notify( captor.capture() );
   }//End Method
   
   @Test public void shouldIgnoreNoNotificationItem(){
      tree.getSelectionModel().select( 0 );
      
      systemUnderTest.assignMenu().getItems().get( 0 ).fire();
      verify( subscription, times( 0 ) ).notify( captor.capture() );
   }//End Method
   
   @Test public void shouldRaiseMultipleEventForMultipleSelectionIncludingHeaderItems(){
      notifications.fire( new Event<>( notification1 ) );
      notifications.fire( new Event<>( notification2 ) );
      tree.getSelectionModel().selectIndices( 0, 1, 2 );
      
      systemUnderTest.assignMenu().getItems().get( 0 ).fire();
      verify( subscription, times( 2 ) ).notify( captor.capture() );
      
      UserAssignment assignment = captor.getAllValues().get( 0 ).getValue();
      assertThat( assignment.getJenkinsUser(), is( user1 ) );
      assertThat( assignment.timestampProperty().get(), is( TIMESTAMP ) );
      assertThat( assignment.descriptionProperty().get(), is( notification1.getDescription() ) );
      assertThat( assignment.detailProperty().get(), is( NotificationTreeContextMenu.ENTER_DETAIL ) );
      
      assignment = captor.getAllValues().get( 1 ).getValue();
      assertThat( assignment.getJenkinsUser(), is( user1 ) );
      assertThat( assignment.timestampProperty().get(), is( TIMESTAMP ) );
      assertThat( assignment.descriptionProperty().get(), is( notification2.getDescription() ) );
      assertThat( assignment.detailProperty().get(), is( NotificationTreeContextMenu.ENTER_DETAIL ) );
   }//End Method
   
}//End Class
