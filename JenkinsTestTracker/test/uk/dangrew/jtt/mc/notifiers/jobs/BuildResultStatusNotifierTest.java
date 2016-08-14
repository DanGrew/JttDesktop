/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.notifiers.jobs;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.event.structure.EventSubscription;
import uk.dangrew.jtt.mc.model.Notification;
import uk.dangrew.jtt.mc.view.tree.NotificationEvent;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;

/**
 * {@link BuildResultStatusNotifier} test.
 */
public class BuildResultStatusNotifierTest {

   @Mock private EventSubscription< Void, Notification > notificationSubscriber;
   @Captor private ArgumentCaptor< Event< Void, Notification > > notificationCaptor;
   private JenkinsJob job;
   private JenkinsDatabase database;
   private BuildResultStatusNotifier systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      new NotificationEvent().clearAllSubscriptions();
      new NotificationEvent().register( notificationSubscriber );
      database = new JenkinsDatabaseImpl();
      job = new JenkinsJobImpl( "Initialised Job" );
      database.store( job );
      systemUnderTest = new BuildResultStatusNotifier( database );
   }//End Method
   
   @Test public void shouldRaiseEventWhenJobStateChanges() {
      job.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      verify( notificationSubscriber ).notify( notificationCaptor.capture() );
      
      BuildResultStatusNotification notification = ( BuildResultStatusNotification ) notificationCaptor.getValue().getValue();
      assertThat( notification.getJenkinsJob(), is( job ) );
      assertThat( notification.getPreviousBuildResultStatus(), is( BuildResultStatus.NOT_BUILT ) );
      assertThat( notification.getNewBuildResultStatus(), is( BuildResultStatus.SUCCESS ) );
   }//End Method   
   
   @Test public void shouldRaiseEventWhenNewJobStateChanges() {
      JenkinsJob newJob = new JenkinsJobImpl( "something new" );
      database.store( newJob );
      newJob.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      
      verify( notificationSubscriber ).notify( notificationCaptor.capture() );
      
      BuildResultStatusNotification notification = ( BuildResultStatusNotification ) notificationCaptor.getValue().getValue();
      assertThat( notification.getJenkinsJob(), is( newJob ) );
      assertThat( notification.getPreviousBuildResultStatus(), is( BuildResultStatus.NOT_BUILT ) );
      assertThat( notification.getNewBuildResultStatus(), is( BuildResultStatus.SUCCESS ) );
   }//End Method  

}//End Class
