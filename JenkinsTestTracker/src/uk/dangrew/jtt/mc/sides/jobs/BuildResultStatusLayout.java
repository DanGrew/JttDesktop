/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.jobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.TreeItem;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link BuildResultStatusLayout} is responsible for laying out the {@link JenkinsJob}s
 * according to their current {@link BuildResultStatus}.
 */
public class BuildResultStatusLayout {

   private final JobProgressTree tree;
   private final Map< BuildResultStatus, TreeItem< JobProgressTreeItem > > branches;
   private final Map< JenkinsJob, TreeItem< JobProgressTreeItem > > jobItems;
   
   /**
    * Constructs a new {@link NotificationTreeLayoutManager}.
    * @param tree the {@link JobProgressTree} to organise.
    */
   BuildResultStatusLayout( JobProgressTree tree ) {
      this.tree = tree;
      this.jobItems = new HashMap<>();
      this.branches = new EnumMap<>( BuildResultStatus.class );
      for( BuildResultStatus status : BuildResultStatus.values() ) {
         this.branches.put( status, new TreeItem<>( new JobProgressTreeItemBranch( status.name() ) ) );
      }
   }//End Constructor

   /**
    * Method to reconstruct the tree. This will discard everything currently in the tree and
    * reconstruct its structure.
    * @param jobs the {@link JenkinsJob}s to add.
    */
   void reconstructTree( List< JenkinsJob > jobs ) {
      if ( jobs == null ) {
         throw new IllegalArgumentException( "Must provide non null list." );
      }
      
      if ( tree.getRoot() == null ) {
         throw new IllegalStateException( "Tree must have Root." );
      }
      
      tree.getRoot().getChildren().clear();
      List< BuildResultStatus > sortedStatus = new ArrayList<>();
      sortedStatus.addAll( Arrays.asList( BuildResultStatus.values() ) );
      Collections.sort( sortedStatus, ( a, b ) -> a.name().compareTo( b.name() ) );
      
      for ( BuildResultStatus status : sortedStatus ) {
         TreeItem< JobProgressTreeItem > branch = branches.get( status );
         tree.getRoot().getChildren().add( branch );
         branch.setExpanded( true );
      }
      
      for ( JenkinsJob job : jobs ) {
         add( job );
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
    * Method to add the given {@link JenkinsJob} to the tree.
    * @param job the {@link JenkinsJob} to add.
    */
   void add( JenkinsJob job ) {
      verifyConstructedWithThisManager();
      
      if ( jobItems.containsKey( job ) ) {
         return;
      }
      
      JobProgressTreeItem item = new JobProgressTreeItemImpl( job );
      TreeItem< JobProgressTreeItem > treeItem = new TreeItem<>( item );
      jobItems.put( job, treeItem );
      getBranch( job ).getChildren().add( treeItem );
   }//End Method
   
   /**
    * Getter for the {@link TreeItem} for the {@link JenkinsJob}.
    * @param job the {@link JenkinsJob} in question.
    * @return the {@link TreeItem}.
    */
   private TreeItem< JobProgressTreeItem > getBranch( JenkinsJob job ) {
      return branches.get( job.lastBuildStatusProperty().get() );
   }//End Method

   /**
    * Method to remove the given {@link JenkinsJob} from the tree.
    * @param job the {@link JenkinsJob} to remove.
    */
   void remove( JenkinsJob job ) {
      verifyConstructedWithThisManager();
      purge( job );
   }//End Method
   
   /**
    * Method to remove the {@link JenkinsJob} from the tree completely.
    * @param job the {@link JenkinsJob} to remove.
    */
   private void purge( JenkinsJob job ) {
      if ( !jobItems.containsKey( job ) ) {
         return;
      }
      for ( TreeItem< JobProgressTreeItem > branch : branches.values() ) {
         branch.getChildren().remove( jobItems.get( job ) );
      }
      jobItems.remove( job );
   }//End Method

   /**
    * Method to determine whether this layout is controlling the given {@link JobProgressTree}.
    * @param tree the {@link JobProgressTree} in question.
    * @return true if being controller by and laid out with the given.
    */
   boolean isControlling( JobProgressTree tree ) {
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
    * Method to update the {@link JenkinsJob} in the tree, moving it if needed.
    * @param job the {@link JenkinsJob} to update.
    */
   void update( JenkinsJob job ) {
      verifyConstructedWithThisManager();
      
      if ( !jobItems.containsKey( job ) ) {
         return;
      }
      if ( getBranch( job ).getChildren().contains( jobItems.get( job ) ) ) {
         return;
      }
      
      purge( job );
      add( job );
   }//End Method
   
   /**
    * Getter for the branch for the given {@link BuildResultStatus}.
    * @param status the {@link BuildResultStatus} the branch is for.
    * @return the {@link TreeItem} branch.
    */
   TreeItem< JobProgressTreeItem > getBranch( BuildResultStatus status ) {
      return branches.get( status );
   }//End Method
   
}//End Class
