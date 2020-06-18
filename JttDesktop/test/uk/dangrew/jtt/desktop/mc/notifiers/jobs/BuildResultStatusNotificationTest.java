/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.notifiers.jobs;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.controlsfx.control.Notifications;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import uk.dangrew.jtt.desktop.mc.view.item.NotificationTreeItem;
import uk.dangrew.jtt.desktop.mc.view.tree.NotificationTreeController;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.kode.launch.TestApplication;

/**
 * {@link BuildResultStatusNotification} test.
 */
public class BuildResultStatusNotificationTest {

   @Mock private BuildResultStatusDesktopNotification desktopNotification;
   @Spy private ChangeIdentifier changeIdentifier;
   private JenkinsJob job;
   private BuildResultStatusNotification systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      job = new JenkinsJobImpl( "JenkinsJob" );
      systemUnderTest = new BuildResultStatusNotification( changeIdentifier, desktopNotification, job, BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS );
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
   
   @Test public void shouldFormatBuildResultStatusChange(){
      when( changeIdentifier.identifyChangeType( Mockito.any(), Mockito.any() ) ).thenReturn( BuildResultStatusHighLevelChange.Unchanged );
      assertThat( 
               systemUnderTest.formatBuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.FAILURE ), 
               is( "Build has remained at FAILURE" )
      );
      when( changeIdentifier.identifyChangeType( Mockito.any(), Mockito.any() ) ).thenReturn( BuildResultStatusHighLevelChange.ActionRequired );
      assertThat( 
               systemUnderTest.formatBuildResultStatusChange( BuildResultStatus.SUCCESS, BuildResultStatus.FAILURE ), 
               is( "Build has only achieved FAILURE when it was SUCCESS and may require action" )
      );
      when( changeIdentifier.identifyChangeType( Mockito.any(), Mockito.any() ) ).thenReturn( BuildResultStatusHighLevelChange.Passed );
      assertThat( 
               systemUnderTest.formatBuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS ), 
               is( "Build has achieved SUCCESS from FAILURE" )
      );
   }//End Method
   
   @Test public void shouldProvideDescriptionAsChange(){
      assertThat( systemUnderTest.getDescription(), is( systemUnderTest.formatBuildResultStatusChange( 
               systemUnderTest.getPreviousBuildResultStatus(), systemUnderTest.getNewBuildResultStatus() ) 
      ) );
   }//End Method
   
   @Test public void shouldProvideChangeViaChangeIdentifier(){
      when( changeIdentifier.identifyChangeType( Mockito.any(), Mockito.any() ) ).thenReturn( BuildResultStatusHighLevelChange.ActionRequired );
      assertThat( systemUnderTest.identifyChange(), is( BuildResultStatusHighLevelChange.ActionRequired ) );
      
      when( changeIdentifier.identifyChangeType( Mockito.any(), Mockito.any() ) ).thenReturn( BuildResultStatusHighLevelChange.Passed );
      assertThat( systemUnderTest.identifyChange(), is( BuildResultStatusHighLevelChange.Passed ) );
      
      when( changeIdentifier.identifyChangeType( Mockito.any(), Mockito.any() ) ).thenReturn( BuildResultStatusHighLevelChange.Unchanged );
      assertThat( systemUnderTest.identifyChange(), is( BuildResultStatusHighLevelChange.Unchanged ) );
   }//End Method
   
   @Test public void shouldShowDesktopNotificationWithNewNotifications(){
      ArgumentCaptor< Notifications > captor = ArgumentCaptor.forClass( Notifications.class );
      
      systemUnderTest.showDesktopNotification();
      verify( desktopNotification ).showNotification( 
               Mockito.eq( systemUnderTest ), Mockito.any(), Mockito.eq( BuildResultStatusNotification.NOTIFICATION_DELAY ) 
      );
      systemUnderTest.showDesktopNotification();
      verify( desktopNotification, times( 2 ) ).showNotification( 
               Mockito.eq( systemUnderTest ), Mockito.any(), Mockito.eq( BuildResultStatusNotification.NOTIFICATION_DELAY ) 
      );
      systemUnderTest.showDesktopNotification();
      verify( desktopNotification, times( 3 ) ).showNotification( 
               Mockito.eq( systemUnderTest ), captor.capture(), Mockito.eq( BuildResultStatusNotification.NOTIFICATION_DELAY ) 
      );
      
      assertThat( captor.getAllValues(), hasSize( 3 ) );
      Notifications first = captor.getAllValues().get( 0 );
      Notifications second = captor.getAllValues().get( 1 );
      Notifications third = captor.getAllValues().get( 2 );
      
      assertThat( first, is( not( second ) ) );
      assertThat( second, is( not( third ) ) );
   }//End Method
   
}//End Class
