/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.jobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.scene.control.TreeItem;
import uk.dangrew.jtt.desktop.javafx.tree.structure.Tree;
import uk.dangrew.jtt.desktop.javafx.tree.structure.TreeLayout;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link BuildResultStatusLayout} is responsible for laying out the {@link JenkinsJob}s
 * according to their current {@link BuildResultStatus}.
 */
public class BuildResultStatusLayout implements TreeLayout< JobProgressTreeItem, JenkinsJob >{

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
    * {@inheritDoc}
    */
   @Override public void reconstructTree( List< JenkinsJob > jobs ) {
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
    * {@inheritDoc}
    */
   @Override public void add( JenkinsJob job ) {
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
      return branches.get( job.buildProperty().get().getValue() );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void remove( JenkinsJob job ) {
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
    * {@inheritDoc}
    */
   @Override public boolean isControlling( Tree< JobProgressTreeItem, JenkinsJob, ?, ? > tree ) {
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
    * {@inheritDoc}
    */
   @Override public void update( JenkinsJob job ) {
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
    * {@inheritDoc}
    */
   @Override public boolean contains( JenkinsJob object ) {
      return jobItems.containsKey( object );
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
