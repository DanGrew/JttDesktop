/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.console;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javafx.scene.control.SplitPane;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.mc.sides.jobs.JobProgressTree;
import uk.dangrew.jtt.mc.sides.users.UserAssignmentsTree;
import uk.dangrew.jtt.mc.view.tree.NotificationTree;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;

/**
 * {@link ManagementConsole} test.
 */
public class ManagementConsoleTest {

   private JenkinsDatabase database;
   private ManagementConsole systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      database = new JenkinsDatabaseImpl();
      systemUnderTest = new ManagementConsole( database );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> systemUnderTest );
      
      Thread.sleep( 1000000 );
   }//End Method
   
   @Test public void shouldProvideNotificationsAndProgress() {
      SplitPane center = systemUnderTest.jobsNotificationsSplit();
      assertThat( center.getItems().get( 0 ), is( instanceOf( JobProgressTree.class ) ) );
      assertThat( center.getItems().get( 1 ), is( instanceOf( NotificationTree.class ) ) );
   }//End Method
   
   @Test public void shouldProvideAssignmentsSplitWithNotifications(){
      SplitPane center = systemUnderTest.notificationsAssignmentsSplit();
      assertThat( center.getItems().get( 0 ), is( systemUnderTest.jobsNotificationsSplit() ) );
      assertThat( center.getItems().get( 1 ), is( instanceOf( UserAssignmentsTree.class ) ) );
   }//End Method
   
   @Test public void shouldProvideAssignmentSplitInCenter(){
      assertThat( systemUnderTest.getCenter(), is( systemUnderTest.notificationsAssignmentsSplit() ) );
   }//End Method
   
   @Test public void shouldUseReasonableDividerPositionsInitially(){
      assertThat( systemUnderTest.jobsNotificationsSplit().getDividerPositions(), is( new double[]{ ManagementConsole.PROGRESS_DIVIDER_POSITION } ) );
      assertThat( systemUnderTest.notificationsAssignmentsSplit().getDividerPositions(), is( new double[]{ ManagementConsole.ASSIGNMENTS_DIVIDER_POSITION } ) );
   }//End Method

}//End Class
