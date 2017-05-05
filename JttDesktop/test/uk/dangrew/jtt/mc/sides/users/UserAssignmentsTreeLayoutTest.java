/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.users;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.javafx.application.PlatformImpl;

import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.TestJenkinsDatabaseImpl;

/**
 * {@link UserAssignmentsTreeLayout} test.
 */
public class UserAssignmentsTreeLayoutTest {
   
   private JenkinsUser user1;
   private JenkinsUser user2;
   private JenkinsUser user3;
   
   @Mock private UserAssignment user1Assignment1;
   @Mock private UserAssignment user1Assignment2;
   @Mock private UserAssignment user2Assignment1;
   @Mock private UserAssignment user2Assignment2;
   @Mock private UserAssignment user3Assignment1;
   
   @Mock private UserAssignmentsTreeItem user1Assignment1Item;
   @Mock private UserAssignmentsTreeItem user1Assignment2Item;
   @Mock private UserAssignmentsTreeItem user2Assignment1Item;
   @Mock private UserAssignmentsTreeItem user2Assignment2Item;
   @Mock private UserAssignmentsTreeItem user3Assignment1Item;
   
   private JenkinsDatabase database;
   private UserAssignmentsTree tree;
   private UserAssignmentsTreeLayout systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      MockitoAnnotations.initMocks( this );
      
      user1 = new JenkinsUserImpl( "User1" );
      user2 = new JenkinsUserImpl( "User2" );
      user3 = new JenkinsUserImpl( "User3" );
      
      when( user1Assignment1.getJenkinsUser() ).thenReturn( user1 );
      when( user1Assignment2.getJenkinsUser() ).thenReturn( user1 );
      when( user2Assignment1.getJenkinsUser() ).thenReturn( user2 );
      when( user2Assignment2.getJenkinsUser() ).thenReturn( user2 );
      when( user3Assignment1.getJenkinsUser() ).thenReturn( user3 );
      
      when( user1Assignment1.constructTreeItem() ).thenReturn( user1Assignment1Item );
      when( user1Assignment2.constructTreeItem() ).thenReturn( user1Assignment2Item );
      when( user2Assignment1.constructTreeItem() ).thenReturn( user2Assignment1Item );
      when( user2Assignment2.constructTreeItem() ).thenReturn( user2Assignment2Item );
      when( user3Assignment1.constructTreeItem() ).thenReturn( user3Assignment1Item );
      
      database = new TestJenkinsDatabaseImpl();
      tree = new UserAssignmentsTree( database );
      systemUnderTest = new UserAssignmentsTreeLayout( tree );
   }//End Method

   @Test( expected = IllegalStateException.class ) public void shouldNotConstructItemsIfNoRootInTree() {
      tree.setRoot( null );
      systemUnderTest.reconstructTree( new ArrayList<>() );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotConstructItemsIfNullProvided() {
      systemUnderTest.reconstructTree( null );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotConstructBranchesIfNoRootInTree() {
      tree.setRoot( null );
      systemUnderTest.reconstructBranches( new ArrayList<>() );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotConstructBranchesIfNullProvided() {
      systemUnderTest.reconstructBranches( null );
   }//End Method
   
   @Test public void shouldAddBranchPerUserWhenConstructed() {
      systemUnderTest.reconstructBranches( Arrays.asList( user1, user2, user3 ) );
      assertThat( tree.getRoot().getChildren(), hasSize( 3 ) );
      
      for ( int i = 0; i < 3; i++ ) {
         assertThat( tree.getRoot().getChildren().get( i ).isExpanded(), is( true ) );
      }
      
      assertThat( tree.getRoot().getChildren().get( 0 ).getValue().getJenkinsUser(), is( user1 ) );
      assertThat( tree.getRoot().getChildren().get( 1 ).getValue().getJenkinsUser(), is( user2 ) );
      assertThat( tree.getRoot().getChildren().get( 2 ).getValue().getJenkinsUser(), is( user3 ) );
   }//End Method
   
   @Test public void shouldClearExistingBranchesWhenConstructed() {
      systemUnderTest.reconstructBranches( Arrays.asList( user1, user2 ) );
      assertThat( tree.getRoot().getChildren(), hasSize( 2 ) );
      
      UserAssignmentsTreeItem item1 = tree.getRoot().getChildren().get( 0 ).getValue();
      UserAssignmentsTreeItem item2 = tree.getRoot().getChildren().get( 1 ).getValue();
      
      systemUnderTest.reconstructBranches( Arrays.asList( user1, user2, user3 ) );
      assertThat( tree.getRoot().getChildren(), hasSize( 3 ) );
      assertThat( tree.getRoot().getChildren().get( 0 ).getValue(), is( not( item1 ) ) );
      assertThat( tree.getRoot().getChildren().get( 1 ).getValue(), is( not( item2 ) ) );
      assertThat( tree.getRoot().getChildren().get( 2 ).getValue().getJenkinsUser(), is( user3 ) );
      
      assertThat( item1.isDetachedFromSystem(), is( true ) );
      assertThat( item2.isDetachedFromSystem(), is( true ) );
   }//End Method
   
   @Test public void shouldAddUserBranchIfNotPresent() {
      systemUnderTest.reconstructBranches( Arrays.asList( user1, user2 ) );
      systemUnderTest.addBranch( user3 );
      
      assertThat( tree.getRoot().getChildren(), hasSize( 3 ) );
      assertThat( tree.getRoot().getChildren().get( 2 ).getValue().getJenkinsUser(), is( user3 ) );
      assertThat( tree.getRoot().getChildren().get( 2 ).isExpanded(), is( true ) );
   }//End Method
   
   @Test public void shouldNotAddUserBranchIfAlreadyPresent() {
      systemUnderTest.reconstructBranches( Arrays.asList( user1, user2 ) );
      systemUnderTest.addBranch( user2 );
      
      assertThat( tree.getRoot().getChildren(), hasSize( 2 ) );
   }//End Method
   
   @Test public void shouldRemoveUserFromTree() {
      systemUnderTest.reconstructBranches( Arrays.asList( user1, user2, user3 ) );
      UserAssignmentsTreeItem item = systemUnderTest.getBranch( user3 ).getValue();
      systemUnderTest.removeBranch( user3 );
      assertThat( item.isDetachedFromSystem(), is( true ) );
      
      assertThat( tree.getRoot().getChildren(), hasSize( 2 ) );
   }//End Method
   
   @Test public void shouldAddUserBackInTreeAfterBeingRemoved() {
      systemUnderTest.reconstructBranches( Arrays.asList( user1, user2, user3 ) );
      systemUnderTest.removeBranch( user3 );
      systemUnderTest.addBranch( user3 );
      
      assertThat( tree.getRoot().getChildren(), hasSize( 3 ) );
      assertThat( tree.getRoot().getChildren().get( 2 ).getValue().getJenkinsUser(), is( user3 ) );
   }//End Method
   
   @Test public void shouldControlConstructedTreeOnly() {
      tree.setRoot( null );
      assertThat( systemUnderTest.isControlling( tree ), is( false ) );
      
      assertThat( systemUnderTest.isControlling( null ), is( false ) );
      
      tree = new UserAssignmentsTree( database );
      systemUnderTest = new UserAssignmentsTreeLayout( tree );
      PlatformImpl.runAndWait( () -> {} );
      systemUnderTest.reconstructBranches( Arrays.asList( user1, user2, user3 ) );
      assertThat( systemUnderTest.isControlling( tree ), is( true ) );
   }//End Method
   
   @Test public void shouldUpdateUserIfPresent() {
      systemUnderTest.reconstructBranches( Arrays.asList( user1 ) );
      UserAssignmentsTreeItem item = tree.getRoot().getChildren().get( 0 ).getValue();
      
      systemUnderTest.updateBranch( user1 );
      assertThat( tree.getRoot().getChildren().get( 0 ).getValue(), is( not( item ) ) );
   }//End Method
   
   @Test public void shouldNotUpdateUserIfNotPresent() {
      systemUnderTest.reconstructBranches( Arrays.asList( user1 ) );
      UserAssignmentsTreeItem item = tree.getRoot().getChildren().get( 0 ).getValue();
      
      systemUnderTest.updateBranch( user2 );
      assertThat( tree.getRoot().getChildren().get( 0 ).getValue(), is( item ) );
   }//End Method
   
   @Test public void shouldAddAssignmentPerUserWhenConstructed() {
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1, user2Assignment1, user3Assignment1 ) );
      assertThat( tree.getRoot().getChildren(), hasSize( 3 ) );
      
      assertUserBranchIsPopulatedWith( user1, user1Assignment1Item );
      assertUserBranchIsPopulatedWith( user2, user2Assignment1Item );
      assertUserBranchIsPopulatedWith( user3, user3Assignment1Item );
   }//End Method
   
   private void assertUserBranchIsPopulatedWith( JenkinsUser user, UserAssignmentsTreeItem... items ) {
      ObservableList< TreeItem< UserAssignmentsTreeItem > > treeItems = systemUnderTest.getBranch( user ).getChildren();
      assertThat( treeItems, hasSize( items.length ) );
      for ( int i = 0; i < items.length; i++ ) {
         assertThat( treeItems.get( i ).getValue(), is( items[ i ] ) );
      }
   }//End Method
   
   @Test public void shouldSupportMultipleAssignmentsInBranchesWhenConstructed() {
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1, user1Assignment2, user3Assignment1 ) );
      assertThat( tree.getRoot().getChildren(), hasSize( 2 ) );
      
      assertUserBranchIsPopulatedWith( user1, user1Assignment1Item, user1Assignment2Item );
      assertUserBranchIsPopulatedWith( user3, user3Assignment1Item );
   }//End Method
   
   @Test public void shouldClearExistingAssignmentWhenConstructed() {
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1, user2Assignment1, user3Assignment1 ) );
      assertThat( tree.getRoot().getChildren(), hasSize( 3 ) );
      
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment2, user2Assignment2 ) );
      assertThat( tree.getRoot().getChildren(), hasSize( 2 ) );
      
      assertUserBranchIsPopulatedWith( user1, user1Assignment2Item );
      assertUserBranchIsPopulatedWith( user2, user2Assignment2Item );
      
      verify( user1Assignment1Item ).detachFromSystem();
      verify( user2Assignment1Item ).detachFromSystem();
      verify( user3Assignment1Item ).detachFromSystem();
   }//End Method
   
   @Test public void shouldAddUserAndAssignmentIfNotPresent() {
      systemUnderTest.reconstructBranches( new ArrayList<>() );
      systemUnderTest.add( user1Assignment1 );
      
      assertThat( tree.getRoot().getChildren(), hasSize( 1 ) );
      assertUserBranchIsPopulatedWith( user1, user1Assignment1Item );
   }//End Method
   
   @Test public void shouldExpandUserWhenAssignmentAddedAndUserAlreadyPresent() {
      systemUnderTest.reconstructBranches( Arrays.asList( user1 ) );
      systemUnderTest.add( user1Assignment1 );
      
      assertThat( tree.getRoot().getChildren(), hasSize( 1 ) );
      assertUserBranchIsPopulatedWith( user1, user1Assignment1Item );
      assertThat( tree.getRoot().getChildren().get( 0 ).isExpanded(), is( true ) );
   }//End Method
   
   @Test public void shouldNotAddAssignmentIfAlreadyPresent() {
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1 ) );
      systemUnderTest.add( user1Assignment1 );
      
      assertThat( tree.getRoot().getChildren(), hasSize( 1 ) );
      assertUserBranchIsPopulatedWith( user1, user1Assignment1Item );
   }//End Method
   
   @Test public void shouldRemoveAssignmentFromTree() {
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1, user1Assignment2 ) );
      systemUnderTest.remove( user1Assignment1 );
      
      assertThat( tree.getRoot().getChildren(), hasSize( 1 ) );
      assertUserBranchIsPopulatedWith( user1, user1Assignment2Item );
      
      verify( user1Assignment1Item ).detachFromSystem();
   }//End Method
   
   @Test public void shouldAddAssignmentBackInTreeAfterBeingRemoved() {
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1, user1Assignment2 ) );
      systemUnderTest.remove( user1Assignment1 );
      systemUnderTest.add( user1Assignment1 );
      
      assertThat( tree.getRoot().getChildren(), hasSize( 1 ) );
      assertUserBranchIsPopulatedWith( user1, user1Assignment2Item, user1Assignment1Item );
   }//End Method
   
   @Test public void shouldAddAgainAfterReconstruction(){
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1, user1Assignment2 ) );
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment2 ) );
      systemUnderTest.add( user1Assignment1 );
      
      assertThat( tree.getRoot().getChildren(), hasSize( 1 ) );
      assertUserBranchIsPopulatedWith( user1, user1Assignment2Item, user1Assignment1Item );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAddBranchIfManagedElsewhere(){
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1 ) );
      tree.getRoot().getChildren().clear();
      
      systemUnderTest.addBranch( user2 );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotUpdateBranchIfManagedElsewhere(){
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1 ) );
      tree.getRoot().getChildren().clear();
      
      systemUnderTest.updateBranch( user1 );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotRemoveBranchIfManagedElsewhere(){
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1 ) );
      tree.getRoot().getChildren().clear();
      
      systemUnderTest.removeBranch( user1 );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAddAssignmentIfManagedElsewhere(){
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1 ) );
      tree.getRoot().getChildren().clear();
      
      systemUnderTest.add( user2Assignment1 );
   }//End Method
   
   @Test public void shouldIgnoreRemoveUserIfNotPresent(){
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1 ) );
      systemUnderTest.removeBranch( user2 );
   }//End Method
   
   @Test public void shouldIgnoreRemoveAssignmentIfNotPresent(){
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1 ) );
      systemUnderTest.remove( user2Assignment2 );
   }//End Method
   
   @Test public void shouldNotBeControllingIfBranchMissing(){
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1, user2Assignment1 ) );
      tree.getRoot().getChildren().remove( 0 );
      assertThat( systemUnderTest.isControlling( tree ), is( false ) );
   }//End Method
   
   @Test public void shouldContainsAssignmentsPresent(){
      systemUnderTest.reconstructTree( Arrays.asList( user1Assignment1, user3Assignment1 ) );
      assertThat( systemUnderTest.contains( user1Assignment1 ), is( true ) );
      assertThat( systemUnderTest.contains( user1Assignment2 ), is( false ) );
      assertThat( systemUnderTest.contains( user2Assignment1 ), is( false ) );
      assertThat( systemUnderTest.contains( user2Assignment2 ), is( false ) );
      assertThat( systemUnderTest.contains( user1Assignment1 ), is( true ) );
   }//End Method
   
}//End Class
