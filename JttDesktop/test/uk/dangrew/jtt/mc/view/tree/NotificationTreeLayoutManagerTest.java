/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.tree;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.control.TreeItem;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;

/**
 * {@link NotificationTreeLayoutManager} test.
 */
public class NotificationTreeLayoutManagerTest {

   private List< NotificationTreeItem > treeItems;
   @Mock private NotificationTreeItem item1;
   @Mock private NotificationTreeItem item2;
   @Mock private NotificationTreeItem item3;
   @Mock private NotificationTreeItem item4;
   
   private NotificationTree tree;
   private NotificationTreeLayoutManager systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      treeItems = Arrays.asList( item1, item2, item3, item4 );
      
      tree = new NotificationTree( new TestJenkinsDatabaseImpl() );
      systemUnderTest = new NotificationTreeLayoutManager( tree );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAcceptRootNotBeingPresentInTree() {
      tree.setRoot( null );
      systemUnderTest.reconstructTree( treeItems );
   }//End Method
   
   @Test public void shouldRemoveAllBranchesFromRoot() {
      tree.getRoot().getChildren().clear();
      TreeItem< NotificationTreeItem > branch1 = new TreeItem< NotificationTreeItem >( null );
      TreeItem< NotificationTreeItem > branch2 = new TreeItem< NotificationTreeItem >( null );
      TreeItem< NotificationTreeItem > branch3 = new TreeItem< NotificationTreeItem >( null );
      tree.getRoot().getChildren().addAll( Arrays.asList( branch1, branch2, branch3 ) );
      
      systemUnderTest.reconstructTree( treeItems );
      assertThat( tree.getRoot().getChildren().contains( branch1 ), is( false ) );
      assertThat( tree.getRoot().getChildren().contains( branch2 ), is( false ) );
      assertThat( tree.getRoot().getChildren().contains( branch3 ), is( false ) );
   }//End Method
   
   @Test public void shouldCreateSingleBranch() {
      systemUnderTest.reconstructTree( treeItems );
      assertThat( tree.getRoot().getChildren(), hasSize( 1 ) );
      assertThat( tree.getRoot().getChildren().get( 0 ).isExpanded(), is( true ) );
   }//End Method
   
   @Test public void shouldAddAllNotificationItemsInSingleBranch() {
      systemUnderTest.reconstructTree( treeItems );
      List< TreeItem< NotificationTreeItem > > branchChildren = tree.getRoot().getChildren().get( 0 ).getChildren();
      
      assertThat( branchChildren.get( 0 ).getValue(), is( item1 ) );
      assertThat( branchChildren.get( 1 ).getValue(), is( item2 ) );
      assertThat( branchChildren.get( 2 ).getValue(), is( item3 ) );
      assertThat( branchChildren.get( 3 ).getValue(), is( item4 ) );
   }//End Method

   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullItems() {
      systemUnderTest.reconstructTree( null );
   }//End Method

   @Test public void shouldClearAllAndLeaveSingleBranchIfNoItemsToLayout() {
      systemUnderTest.reconstructTree( new ArrayList<>() );
      assertThat( tree.getRoot().getChildren().get( 0 ).getChildren(), hasSize( 0 ) );
   }//End Method
   
   @Test public void shoudlPreserveExistingBranch(){
      systemUnderTest.reconstructTree( treeItems );
      TreeItem< NotificationTreeItem > branch = tree.getRoot().getChildren().get( 0 );
      
      systemUnderTest.reconstructTree( new ArrayList<>() );
      TreeItem< NotificationTreeItem > resultingBranch = tree.getRoot().getChildren().get( 0 );
      assertThat( resultingBranch, is( branch ) );
   }//End Method
   
   @Test public void shouldAddItemToBranch(){
      systemUnderTest.reconstructTree( new ArrayList<>() );
      systemUnderTest.add( item1 );
      assertThat( tree.getRoot().getChildren().get( 0 ).getChildren().get( 0 ).getValue(), is( item1 ) );
   }//End Method
   
   @Test public void shouldIgnoreNullItemToAdd(){
      systemUnderTest.reconstructTree( treeItems );
      systemUnderTest.add( null );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAcceptBranchNotPresentInTreeWhenAdding(){
      systemUnderTest.add( item1 );
   }//End Method
   
   @Test public void shouldRemoveItemFromBranch(){
      systemUnderTest.reconstructTree( treeItems );
      systemUnderTest.remove( item1 );
      assertThat( tree.getRoot().getChildren().get( 0 ).getChildren().get( 0 ).getValue(), is( not( item1 ) ) );
   }//End Method
   
   @Test public void shouldIgnoreNullItemToRemove(){
      systemUnderTest.reconstructTree( treeItems );
      systemUnderTest.remove( null );
   }//End Method
   
   @Test public void shouldIgnoreItemToRemoveWhenNotPresent(){
      systemUnderTest.reconstructTree( new ArrayList<>() );
      systemUnderTest.remove( item1 );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAcceptBranchNotPresentInTreeWhenRemoving(){
      systemUnderTest.remove( item1 );
   }//End Method
   
   @Test public void shouldDetermineWhetherTreeIsLaidOutWithThis(){
      assertThat( systemUnderTest.isControlling( null ), is( false ) );
      assertThat( systemUnderTest.isControlling( tree ), is( false ) );
      systemUnderTest.reconstructTree( treeItems );
      assertThat( systemUnderTest.isControlling( tree ), is( true ) );
      tree.getRoot().getChildren().clear();
      assertThat( systemUnderTest.isControlling( tree ), is( false ) );
   }//End Method
   
   @Test public void shouldContainNotifications(){
      systemUnderTest.reconstructTree( Arrays.asList( item1, item4 ) );
      assertThat( systemUnderTest.contains( item1 ), is( true ) );
      assertThat( systemUnderTest.contains( item2 ), is( false ) );
      assertThat( systemUnderTest.contains( item3 ), is( false ) );
      assertThat( systemUnderTest.contains( item4 ), is( true ) );
   }//End Method

}//End Class
