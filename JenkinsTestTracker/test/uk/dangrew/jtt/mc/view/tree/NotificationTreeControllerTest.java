/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.tree;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.mc.model.Notification;
import uk.dangrew.jtt.mc.notifiers.jobs.BuildResultStatusNotification;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

/**
 * {@link NotificationTreeController} test.
 */
public class NotificationTreeControllerTest {

   @Mock private NotificationTreeItem item;
   @Mock private NotificationTreeLayoutManager layoutManager;
   private NotificationTreeController systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      new NotificationEvent().clearAllSubscriptions();
      systemUnderTest = new NotificationTreeController( layoutManager );
   }//End Method
   
   @Test public void shouldAddNewItem() {
      systemUnderTest.addItem( item );
      verify( layoutManager ).add( item );
   }//End Method
   
   @Test public void shouldRemoveItem(){
      systemUnderTest.removeItem( item );
      verify( layoutManager ).remove( item );
   }//End Method

   @Test public void shouldReceiveNotificationsAndAddToLayoutManager(){
      Notification notification = new BuildResultStatusNotification( new JenkinsJobImpl( "Job" ), BuildResultStatus.ABORTED, BuildResultStatus.ABORTED );
      
      new NotificationEvent().fire( new Event< Void, Notification >( null, notification ) );
      ArgumentCaptor< NotificationTreeItem > itemCaptor = ArgumentCaptor.forClass( NotificationTreeItem.class );
      
      verify( layoutManager ).add( itemCaptor.capture() );
      assertThat( itemCaptor.getValue().getNotification(), is( notification ) );
   }//End Method
}//End Class
