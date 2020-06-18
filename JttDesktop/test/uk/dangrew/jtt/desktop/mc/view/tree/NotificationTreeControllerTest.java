/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.view.tree;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import uk.dangrew.jtt.desktop.mc.model.Notification;
import uk.dangrew.jtt.desktop.mc.notifiers.jobs.BuildResultStatusNotification;
import uk.dangrew.jtt.desktop.mc.view.item.NotificationTreeItem;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.kode.event.structure.Event;
import uk.dangrew.kode.launch.TestApplication;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

/**
 * {@link NotificationTreeController} test.
 */
public class NotificationTreeControllerTest {

   @Mock private NotificationTreeItem item;
   @Mock private NotificationTreeLayoutManager layoutManager;
   private NotificationTreeController systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      new NotificationEvent().clearAllSubscriptions();
      systemUnderTest = new NotificationTreeController( layoutManager );
   }//End Method
   
   @Test public void shouldAddNewItem() {
      systemUnderTest.add( item );
      verify( layoutManager ).add( item );
   }//End Method
   
   @Test public void shouldRemoveItem(){
      systemUnderTest.remove( item );
      verify( layoutManager ).remove( item );
   }//End Method

   @Test public void shouldReceiveNotificationsAndAddToLayoutManager(){
      Notification notification = new BuildResultStatusNotification( new JenkinsJobImpl( "Job" ), BuildResultStatus.ABORTED, BuildResultStatus.ABORTED );
      
      new NotificationEvent().fire( new Event< Notification >( notification ) );
      ArgumentCaptor< NotificationTreeItem > itemCaptor = ArgumentCaptor.forClass( NotificationTreeItem.class );
      
      verify( layoutManager ).add( itemCaptor.capture() );
      assertThat( itemCaptor.getValue().getNotification(), is( notification ) );
   }//End Method
}//End Class
