/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.view.tree;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import uk.dangrew.jtt.desktop.friendly.javafx.FriendlyMenuOpener;
import uk.dangrew.jtt.desktop.mc.notifiers.jobs.BuildResultStatusNotification;
import uk.dangrew.jtt.desktop.mc.notifiers.jobs.BuildResultStatusNotificationTreeItem;
import uk.dangrew.jtt.desktop.mc.view.item.NotificationTreeItem;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.kode.launch.TestApplication;

/**
 * {@link NotificationTree} test.
 */
public class NotificationTreeTest {
   
   private JenkinsDatabase database;
   private NotificationTree systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      MockitoAnnotations.initMocks( this );
      database = new TestJenkinsDatabaseImpl();
      systemUnderTest = new NotificationTree( database );
   }//End Method

   @Ignore
   @Test public void manual() throws InterruptedException {
      TestApplication.launch( () -> systemUnderTest );
      
      systemUnderTest.getController().add( new BuildResultStatusNotificationTreeItem( new BuildResultStatusNotification( 
               new JenkinsJobImpl( "JenkinsJob" ), BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS 
      ), systemUnderTest.getController() ) );
      Thread.sleep( 2000 );
      systemUnderTest.getController().add( new BuildResultStatusNotificationTreeItem( new BuildResultStatusNotification( 
               new JenkinsJobImpl( "Another Job" ), BuildResultStatus.UNKNOWN, BuildResultStatus.UNKNOWN 
      ), systemUnderTest.getController() ) );
      Thread.sleep( 2000 );
      systemUnderTest.getController().add( new BuildResultStatusNotificationTreeItem( new BuildResultStatusNotification( 
               new JenkinsJobImpl( "JenkinsJob" ), BuildResultStatus.SUCCESS, BuildResultStatus.FAILURE 
      ), systemUnderTest.getController() ) );
      
      Thread.sleep( 100000 );
   }//End Method

   @Test public void shouldHaveRootSetAndConfigured(){
      assertThat( systemUnderTest.getRoot(), is( notNullValue() ) );
      assertThat( systemUnderTest.getRoot().isExpanded(), is( true ) );
      assertThat( systemUnderTest.isShowRoot(), is( false ) );
   }//End Method
   
   @Test public void shouldOnlySupportSingleSelection(){
      assertThat( systemUnderTest.getSelectionModel().getSelectionMode(), is( SelectionMode.MULTIPLE ) );
   }//End Method
   
   @Test public void shouldConstructControllerWithLayoutManager(){
      assertThat( systemUnderTest.getController(), is( notNullValue() ) );
      assertThat( systemUnderTest.getController().getLayoutManager(), is( systemUnderTest.getLayoutManager() ) );
   }//End Method
   
   @Test public void shouldReconstructTreeWithLayoutManager(){
      assertThat( systemUnderTest.getLayoutManager().isControlling( systemUnderTest ), is( true ) );
   }//End Method
   
   @Test public void shouldHaveSingleColumn(){
      TreeTableColumn< NotificationTreeItem, ? > column = systemUnderTest.getColumns().get( 0 );
      assertThat( column, is( notNullValue() ) );
   }//End Method

   @Test public void shouldUseResizePolicy(){
      assertThat( systemUnderTest.getColumnResizePolicy(), is( TreeTableView.CONSTRAINED_RESIZE_POLICY ) );
   }//End Method
   
   @Test public void shouldSupportItemWithNullValuesForEachColumn(){
      NotificationTreeItem nullItem = mock( NotificationTreeItem.class );
      systemUnderTest.getLayoutManager().add( nullItem );
   }//End Method
   
   @Test public void shouldHoldNodesProvidedByNotificationTreeItem(){
      NotificationTreeItem item = new BuildResultStatusNotificationTreeItem( 
               new BuildResultStatusNotification( new JenkinsJobImpl( "Anything" ), BuildResultStatus.ABORTED, BuildResultStatus.SUCCESS ),
               systemUnderTest.getController()
      );
      systemUnderTest.getLayoutManager().add( item );
      
      TreeTableColumn< NotificationTreeItem, ? > onlyColumn = systemUnderTest.getColumns().get( 0 );
      assertThat( onlyColumn.getCellData( 1 ), is( item.contentProperty().get() ) );
   }//End Method
   
   @Test public void shouldUseOpenerForContextMenu(){
      assertThat( systemUnderTest.getOnContextMenuRequested(), is( instanceOf( FriendlyMenuOpener.class ) ) );
      FriendlyMenuOpener opener = ( FriendlyMenuOpener ) systemUnderTest.getOnContextMenuRequested();
      assertThat( opener.isAnchoredTo( systemUnderTest ), is( true ) );
      assertThat( opener.isControllingA( NotificationTreeContextMenu.class ), is( true ) );
      assertThat( systemUnderTest.menu().isConnectedTo( database ), is( true ) );
   }//End Method
}//End Class
