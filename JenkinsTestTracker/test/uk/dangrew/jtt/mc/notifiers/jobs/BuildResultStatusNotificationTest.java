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
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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

   @Mock private ChangeIdentifier changeIdentifier;
   private JenkinsJob job;
   private BuildResultStatusNotification systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
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
   
   @Test public void shouldFormatBuildResultStatusChange(){
      when( changeIdentifier.identifyChangeType( Mockito.any(), Mockito.any() ) ).thenReturn( BuildResultStatusChange.Unchanged );
      assertThat( 
               systemUnderTest.formatBuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.FAILURE ), 
               is( "Build has remained at FAILURE" )
      );
      when( changeIdentifier.identifyChangeType( Mockito.any(), Mockito.any() ) ).thenReturn( BuildResultStatusChange.ActionRequired );
      assertThat( 
               systemUnderTest.formatBuildResultStatusChange( BuildResultStatus.SUCCESS, BuildResultStatus.FAILURE ), 
               is( "Build has only achieved FAILURE when it was SUCCESS and may require action" )
      );
      when( changeIdentifier.identifyChangeType( Mockito.any(), Mockito.any() ) ).thenReturn( BuildResultStatusChange.Passed );
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
   
}//End Class
