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
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.beans.property.ObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeTableColumn;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.javafx.tree.ColumnHeaderHider;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;

/**
 * {@link NotificationTree} test.
 */
public class NotificationTreeTest {

   @Spy private ColumnHeaderHider hider;
   private NotificationTree systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new NotificationTree( hider );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> systemUnderTest );
      
      systemUnderTest.controller().addItem( new ExampleNotification() );
      Thread.sleep( 2000 );
      systemUnderTest.controller().addItem( new ExampleNotification() );
      Thread.sleep( 2000 );
      systemUnderTest.controller().addItem( new ExampleNotification() );
      
      Thread.sleep( 100000 );
   }//End Method

   @Test public void shouldHaveRootSetAndConfigured(){
      assertThat( systemUnderTest.getRoot(), is( notNullValue() ) );
      assertThat( systemUnderTest.getRoot().isExpanded(), is( true ) );
      assertThat( systemUnderTest.isShowRoot(), is( false ) );
   }//End Method
   
   @Test public void shouldOnlySupportSingleSelection(){
      assertThat( systemUnderTest.getSelectionModel().getSelectionMode(), is( SelectionMode.SINGLE ) );
   }//End Method
   
   @Test public void shouldConstructControllerWithLayoutManager(){
      assertThat( systemUnderTest.controller(), is( notNullValue() ) );
      assertThat( systemUnderTest.controller().layoutManager(), is( systemUnderTest.layoutManager() ) );
   }//End Method
   
   @Test public void shouldHideColumnHeaders(){
      verify( hider ).hideColumnHeaders( systemUnderTest );
   }//End Method
   
   @Test public void shouldReconstructTreeWithLayoutManager(){
      assertThat( systemUnderTest.layoutManager().isControlling( systemUnderTest ), is( true ) );
   }//End Method
   
   @Test public void shouldHaveIconColumn(){
      TreeTableColumn< NotificationTreeItem, ? > column = systemUnderTest.getColumns().get( 0 );
      assertThat( column.getPrefWidth(), is( NotificationTree.ICON_COLUMN_WIDTH ) );
   }//End Method
   
   @Test public void shouldHaveTypeColumn(){
      TreeTableColumn< NotificationTreeItem, ? > column = systemUnderTest.getColumns().get( 1 );
      assertThat( column.getPrefWidth(), is( NotificationTree.TYPE_COLUMN_WIDTH ) );
   }//End Method
   
   @Test public void shouldHaveContentColumn(){
      TreeTableColumn< NotificationTreeItem, ? > column = systemUnderTest.getColumns().get( 2 );
      assertThat( column.getPrefWidth(), is( NotificationTree.CONTENT_COLUMN_WIDTH ) );
   }//End Method
   
   @Test public void shouldHaveActionColumn(){
      TreeTableColumn< NotificationTreeItem, ? > column = systemUnderTest.getColumns().get( 3 );
      assertThat( column.getPrefWidth(), is( NotificationTree.CONTROL_COLUMN_WIDTH ) );
   }//End Method
   
   @Test public void shouldHaveCancelColumn(){
      TreeTableColumn< NotificationTreeItem, ? > column = systemUnderTest.getColumns().get( 4 );
      assertThat( column.getPrefWidth(), is( NotificationTree.CONTROL_COLUMN_WIDTH ) );
   }//End Method
   
   @Test public void shouldSupportItemWithNullValuesForEachColumn(){
      NotificationTreeItem nullItem = new NotificationTreeItem() {
         @Override public ObjectProperty< Node > getNotificationType() { return null; }
         @Override public ObjectProperty< Node > getNotificationIcon() { return null; }
         @Override public ObjectProperty< Node > getContent() { return null; }
         @Override public ObjectProperty< Node > getCancelButton() { return null; }
         @Override public ObjectProperty< Node > getActionButton() { return null; }
      };
      systemUnderTest.layoutManager().add( nullItem );
   }//End Method
   
   @Test public void shouldHoldNodesProvidedByNotificationTreeItem(){
      NotificationTreeItem item = new ExampleNotification();
      systemUnderTest.layoutManager().add( item );
      
      TreeTableColumn< NotificationTreeItem, ? > iconColumn = systemUnderTest.getColumns().get( 0 );
      assertThat( iconColumn.getCellData( 1 ), is( item.getNotificationIcon().get() ) );
      
      TreeTableColumn< NotificationTreeItem, ? > typeColum = systemUnderTest.getColumns().get( 1 );
      assertThat( typeColum.getCellData( 1 ), is( item.getNotificationType().get() ) );
      
      TreeTableColumn< NotificationTreeItem, ? > contentColumn = systemUnderTest.getColumns().get( 2 );
      assertThat( contentColumn.getCellData( 1 ), is( item.getContent().get() ) );
      
      TreeTableColumn< NotificationTreeItem, ? > actionColumn = systemUnderTest.getColumns().get( 3 );
      assertThat( actionColumn.getCellData( 1 ), is( item.getActionButton().get() ) );
      
      TreeTableColumn< NotificationTreeItem, ? > cancelColumn = systemUnderTest.getColumns().get( 4 );
      assertThat( cancelColumn.getCellData( 1 ), is( item.getCancelButton().get() ) );
   }//End Method
   
}//End Class
