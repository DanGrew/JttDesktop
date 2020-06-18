/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc;

import org.junit.Before;
import org.junit.Test;
import uk.dangrew.jtt.desktop.mc.model.Notification;
import uk.dangrew.jtt.desktop.mc.view.tree.NotificationEvent;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.kode.event.structure.Event;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * {@link NotificationCenter} test.
 */
public class NotificationCenterTest {
   
   private NotificationCenter systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new NotificationCenter( new TestJenkinsDatabaseImpl() );
   }//End Method

   @Test public void shouldHaveNotifier() {
      assertThat( systemUnderTest.buildResultStatusNotifier(), is( notNullValue() ) );
   }//End Method
   
   @Test public void shouldTriggerDesktopNotification(){
      Notification notification = mock( Notification.class );
      new NotificationEvent().fire( new Event< Notification >( notification ) );
      verify( notification ).showDesktopNotification();
   }//End Method

}//End Class
