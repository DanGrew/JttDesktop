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

import org.controlsfx.control.Notifications;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import uk.dangrew.jtt.desktop.graphics.JavaFxInitializer;
import uk.dangrew.jtt.desktop.mc.notifiers.jobs.BuildResultStatusDesktopNotification;
import uk.dangrew.jtt.desktop.mc.notifiers.jobs.BuildResultStatusHighLevelChange;
import uk.dangrew.jtt.desktop.mc.notifiers.jobs.BuildResultStatusNotification;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

/**
 * {@link BuildResultStatusDesktopNotification} test.
 */
public class BuildResultStatusDesktopNotificationTest {

   private static final int EXPECTED_DURATION = 94753;
   private static final String EXPECTED_JOB = "Jenkins Job Name";
   
   private BuildResultStatusNotification notification;
   @Spy private Notifications notifications;
   private BuildResultStatusDesktopNotification systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      notification = new BuildResultStatusNotification( 
               new JenkinsJobImpl( EXPECTED_JOB ), BuildResultStatus.ABORTED, BuildResultStatus.FAILURE 
      );
      systemUnderTest = new BuildResultStatusDesktopNotification();
   }//End Method
   
   @Test public void shouldUseDarkStyle() {
      systemUnderTest.showNotification( notification, notifications, EXPECTED_DURATION );
      verify( notifications ).darkStyle();
   }//End Method
   
   @Test public void shouldHideAfterDuration() {
      systemUnderTest.showNotification( notification, notifications, EXPECTED_DURATION );
      verify( notifications ).hideAfter( new Duration( EXPECTED_DURATION ) );
   }//End Method
   
   @Test public void shouldPositionInBottomRightCorner() {
      systemUnderTest.showNotification( notification, notifications, EXPECTED_DURATION );
      verify( notifications ).position( Pos.BOTTOM_RIGHT );
   }//End Method
   
   @Test public void shouldHaveTitle() {
      systemUnderTest.showNotification( notification, notifications, EXPECTED_DURATION );
      verify( notifications ).title( BuildResultStatusDesktopNotification.NEW_BUILD );
   }//End Method
   
   @Test public void shouldDisplayJobInText() {
      systemUnderTest.showNotification( notification, notifications, EXPECTED_DURATION );
      verify( notifications ).text( EXPECTED_JOB );
   }//End Method
   
   @Test public void shouldUseGraphicFromNotification() {
      ArgumentCaptor< ImageView > captor = ArgumentCaptor.forClass( ImageView.class );
      systemUnderTest.showNotification( notification, notifications, EXPECTED_DURATION );
      verify( notifications ).graphic( captor.capture() );
      assertThat( captor.getValue().getImage(), is( BuildResultStatusHighLevelChange.ActionRequired.constructImage().getImage() ) );
   }//End Method
   
   @Test public void shouldShowOnceConfigured() {
      systemUnderTest.showNotification( notification, notifications, EXPECTED_DURATION );
      verify( notifications ).show();
   }//End Method

}//End Class
