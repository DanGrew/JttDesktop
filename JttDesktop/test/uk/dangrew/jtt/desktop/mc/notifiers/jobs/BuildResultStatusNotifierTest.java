/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.notifiers.jobs;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.desktop.api.handling.live.JobBuiltEvent;
import uk.dangrew.jtt.desktop.api.handling.live.JobBuiltResult;
import uk.dangrew.jtt.desktop.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundTriggerEvent;
import uk.dangrew.jtt.desktop.mc.model.Notification;
import uk.dangrew.jtt.desktop.mc.view.tree.NotificationEvent;
import uk.dangrew.jtt.model.event.structure.Event;
import uk.dangrew.jtt.model.event.structure.EventSubscription;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

public class BuildResultStatusNotifierTest {

   private JenkinsJob job;
   
   private JobBuiltEvent builtEvents;
   @Mock private EventSubscription< Notification > notificationSubscriber;
   @Mock private EventSubscription< BuildResultStatusChange > soundSubscriber;
   @Captor private ArgumentCaptor< Event< Notification > > notificationCaptor;
   @Captor private ArgumentCaptor< Event< BuildResultStatusChange > > soundCaptor;
   private BuildResultStatusNotifier systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      job = new JenkinsJobImpl( "Job" );
      builtEvents = new JobBuiltEvent();
      builtEvents.clearAllSubscriptions();
      
      new NotificationEvent().clearAllSubscriptions();
      new NotificationEvent().register( notificationSubscriber );
      new SoundTriggerEvent().clearAllSubscriptions();
      new SoundTriggerEvent().register( soundSubscriber );
      systemUnderTest = new BuildResultStatusNotifier();
   }//End Method

   @Test public void shouldRaiseNotificationWhenJobBuiltEventReceived() {
      builtEvents.notify( new Event< JobBuiltResult >( new JobBuiltResult( job, BuildResultStatus.NOT_BUILT, BuildResultStatus.SUCCESS ) ) );
      verify( notificationSubscriber ).notify( notificationCaptor.capture() );
      
      BuildResultStatusNotification notification = ( BuildResultStatusNotification ) notificationCaptor.getValue().getValue();
      assertThat( notification.getJenkinsJob(), is( job ) );
      assertThat( notification.getPreviousBuildResultStatus(), is( BuildResultStatus.NOT_BUILT ) );
      assertThat( notification.getNewBuildResultStatus(), is( BuildResultStatus.SUCCESS ) );
   }//End Method
   
   @Test public void shouldRaiseSoundEventWhenJobBuiltEventReceived() {
      builtEvents.notify( new Event< JobBuiltResult >( new JobBuiltResult( job, BuildResultStatus.NOT_BUILT, BuildResultStatus.SUCCESS ) ) );
      verify( soundSubscriber ).notify( soundCaptor.capture() );
      
      BuildResultStatusChange change = ( BuildResultStatusChange ) soundCaptor.getValue().getValue();
      assertThat( change.getPreviousStatus(), is( BuildResultStatus.NOT_BUILT ) );
      assertThat( change.getCurrentStatus(), is( BuildResultStatus.SUCCESS ) );
   }//End Method

}//End Class
