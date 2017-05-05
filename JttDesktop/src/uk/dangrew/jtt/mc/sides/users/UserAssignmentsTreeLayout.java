/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.TreeItem;
import uk.dangrew.jtt.javafx.tree.structure.Tree;
import uk.dangrew.jtt.javafx.tree.structure.TreeLayout;
import uk.dangrew.jtt.model.users.JenkinsUser;

/**
 * The {@link UserAssignmentsTreeLayout} is responsible for managing the layout of {@link JenkinsUser}s
 * and their {@link UserAssignment}s.
 */
public class UserAssignmentsTreeLayout implements TreeLayout< UserAssignmentsTreeItem, UserAssignment >{

   private final UserAssignmentsTree tree;
   private final Map< JenkinsUser, TreeItem< UserAssignmentsTreeItem > > branches;
   private final Map< UserAssignment, TreeItem< UserAssignmentsTreeItem > > assignmentItems;

   /**
    * Constructs a new {@link UserAssignmentsTreeLayout}.
    * @param tree the {@link JobProgressTree} to organise.
    */
   UserAssignmentsTreeLayout( UserAssignmentsTree tree ) {
      this.tree = tree;
      this.branches = new HashMap<>();
      this.assignmentItems = new HashMap<>();
   }//End Constructor

   /**
    * Method to reconstruct the branches of the tree given the {@link JenkinsUser}s.
    * @param users the {@link JenkinsUser}s to add as branches.
    */
   void reconstructBranches( List< JenkinsUser > users ) {
      if ( users == null ) {
         throw new IllegalArgumentException( "Must provide non null list." );
      }
      
      if ( tree.getRoot() == null ) {
         throw new IllegalStateException( "Tree must have Root." );
      }
      
      clearLayout();
      for ( JenkinsUser user : users ) {
         addBranch( user );
      }
   }//End Method
   
   /**
    * Method to clear the layout tracking information.
    */
   private void clearLayout(){
      tree.getRoot().getChildren().clear();
      branches.values().forEach( item -> item.getValue().detachFromSystem() );
      branches.clear();
      assignmentItems.values().forEach( item -> item.getValue().detachFromSystem() );
      assignmentItems.clear();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void reconstructTree( List< UserAssignment > users ) {
      if ( users == null ) {
         throw new IllegalArgumentException( "Must provide non null list." );
      }
      
      if ( tree.getRoot() == null ) {
         throw new IllegalStateException( "Tree must have Root." );
      }
      
      clearLayout();
      for ( UserAssignment assignment : users ) {
         add( assignment );
      }
   }//End Method

   /**
    * Method to verify that the tree is currently using this layout.
    */
   private void verifyConstructedWithThisManager(){
      if ( !tree.getRoot().getChildren().containsAll( branches.values() ) ) {
         throw new IllegalStateException( "Cannot modify to tree when tree has been constructed with another layout manager." );
      }
   }//End Method
   
   /**
    * Method to add the given {@link JenkinsUser}s as a branch.
    * @param user the {@link JenkinsUser} to add.
    */
   void addBranch( JenkinsUser user ) {
      verifyConstructedWithThisManager();
      
      if ( branches.containsKey( user ) ) {
         return;
      }
      
      UserTreeItem item = new UserTreeItem( user );
      TreeItem< UserAssignmentsTreeItem > treeItem = new TreeItem<>( item );
      branches.put( user, treeItem );
      tree.getRoot().getChildren().add( treeItem );
      treeItem.setExpanded( true );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void add( UserAssignment assignment ) {
      verifyConstructedWithThisManager();
      
      if ( assignmentItems.containsKey( assignment ) ) {
         return;
      }
      
      addBranch( assignment.getJenkinsUser() );
      TreeItem< UserAssignmentsTreeItem > branch = getBranch( assignment.getJenkinsUser() );
      
      UserAssignmentsTreeItem item = assignment.constructTreeItem();
      TreeItem< UserAssignmentsTreeItem > treeItem = new TreeItem<>( item );
      assignmentItems.put( assignment, treeItem );
      branch.getChildren().add( treeItem );
   }//End Method

   /**
    * Method to remove the given {@link JenkinsUser} branch.
    * @param user the {@link JenkinsUser} to remove.
    */
   void removeBranch( JenkinsUser user ) {
      verifyConstructedWithThisManager();
      purgeBranch( user );
   }//End Method
   
   /**
    * Method to remove the {@link JenkinsUser} from the tree completely.
    * @param user the {@link JenkinsUser} to remove.
    */
   private void purgeBranch( JenkinsUser user ) {
      if ( !branches.containsKey( user ) ) {
         return;
      }
      TreeItem< UserAssignmentsTreeItem > item = branches.get( user );
      tree.getRoot().getChildren().remove( item );
      item.getValue().detachFromSystem();
      branches.remove( user );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void remove( UserAssignment assignment ) {
      if ( !assignmentItems.containsKey( assignment ) ) {
         return;
      }
      
      TreeItem< UserAssignmentsTreeItem > item = assignmentItems.get( assignment );
      getBranch( assignment.getJenkinsUser() ).getChildren().remove( item );
      item.getValue().detachFromSystem();
      assignmentItems.remove( assignment );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean isControlling( Tree< UserAssignmentsTreeItem, UserAssignment, ?, ? > tree ) {
      if ( tree == null ) {
         return false;
      }
      
      if ( tree.getRoot() == null ) {
         return false;
      }
      
      if ( tree.getRoot().getChildren().containsAll( branches.values() ) ) {
         return true;
      }
      
      return false;
   }//End Method
   
   /**
    * Method to update the given {@link JenkinsUser} branch.
    * @param user the {@link JenkinsUser} to update.
    */
   void updateBranch( JenkinsUser user ) {
      verifyConstructedWithThisManager();
      
      if ( !branches.containsKey( user ) ) {
         return;
      }
      
      purgeBranch( user );
      addBranch( user );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void update( UserAssignment assignment ) {
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean contains( UserAssignment object ) {
      return assignmentItems.containsKey( object );
   }//End Method
   
   /**
    * Getter for the branch for the given {@link JenkinsUser}.
    * @param user the {@link JenkinsUser} to get the branch for.
    * @return the {@link TreeItem} branch.
    */
   TreeItem< UserAssignmentsTreeItem > getBranch( JenkinsUser user ) {
      return branches.get( user );
   }//End Method
}//End Class
