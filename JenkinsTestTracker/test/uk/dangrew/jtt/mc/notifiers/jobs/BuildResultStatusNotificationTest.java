/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.notifiers.jobs;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;
import uk.dangrew.jtt.mc.view.tree.NotificationTreeController;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

/**
 * {@link BuildResultStatusNotification} test.
 */
public class BuildResultStatusNotificationTest {

   private JenkinsJob job;
   private BuildResultStatusNotification systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      job = new JenkinsJobImpl( "JenkinsJob" );
      systemUnderTest = new BuildResultStatusNotification( job, BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS );
   }//End Method
   
   @Test public void shouldProvideAssoicatedJob() {
      assertThat( systemUnderTest.getJenkinsJob(), is( job ) );
   }//End Method
   
   @Test public void shouldProvidePreviousStatus() {
      assertThat( systemUnderTest.getPreviousBuildResultStatus(), is( BuildResultStatus.FAILURE ) );
   }//End Method
   
   @Test public void shouldProvideNewStatus() {
      assertThat( systemUnderTest.getNewBuildResultStatus(), is( BuildResultStatus.SUCCESS ) );
   }//End Method
   
   @Test public void shouldConstructNotificationItem(){
      NotificationTreeController controller = mock( NotificationTreeController.class );
      NotificationTreeItem item = systemUnderTest.constructTreeItem( controller );
      assertThat( item, is( notNullValue() ) );
      assertThat( item, is( instanceOf( BuildResultStatusNotificationTreeItem.class ) ) );
      assertThat( item.getNotification(), is( systemUnderTest ) );
      assertThat( item.hasController( controller ), is( true ) );
   }//End Method
   
}//End Class
