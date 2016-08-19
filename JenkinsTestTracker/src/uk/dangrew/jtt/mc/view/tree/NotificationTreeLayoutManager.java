/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.tree;

import java.util.Iterator;
import java.util.List;

import javafx.scene.control.TreeItem;
import uk.dangrew.jtt.javafx.tree.structure.Tree;
import uk.dangrew.jtt.javafx.tree.structure.TreeLayout;
import uk.dangrew.jtt.mc.view.item.NotificationTreeItem;

/**
 * The {@link NotificationTreeLayoutManager} provides a method of organising the elements within
 * the {@link NotificationTree}.
 */
class NotificationTreeLayoutManager implements TreeLayout< NotificationTreeItem, NotificationTreeItem > {

   private final NotificationTree tree;
   private final TreeItem< NotificationTreeItem > branch;
   
   /**
    * Constructs a new {@link NotificationTreeLayoutManager}.
    * @param tree the {@link NotificationTree} to organise.
    */
   NotificationTreeLayoutManager( NotificationTree tree ) {
      this.tree = tree;
      this.branch = new TreeItem<>();
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void reconstructTree( List< NotificationTreeItem > treeItems ) {
      if ( treeItems == null ) {
         throw new IllegalArgumentException( "Must provide non null list." );
      }
      
      if ( tree.getRoot() == null ) {
         throw new IllegalStateException( "Tree must have Root." );
      }
      
      tree.getRoot().getChildren().clear();
      tree.getRoot().getChildren().add( branch );
      for ( NotificationTreeItem item : treeItems ) {
         branch.getChildren().add( new TreeItem<>( item ) );
      }
      branch.setExpanded( true );
   }//End Method

   /**
    * Method to verify that the tree is currently using this layout.
    */
   private void verifyConstructedWithThisManager(){
      if ( !tree.getRoot().getChildren().contains( branch ) ) {
         throw new IllegalStateException( "Cannot modify to tree when tree has been constructed with another layout manager." );
      }
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void add( NotificationTreeItem item ) {
      verifyConstructedWithThisManager();
      branch.getChildren().add( new TreeItem<>( item ) );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void remove( NotificationTreeItem item ) {
      verifyConstructedWithThisManager();
      
      if ( item == null ) {
         return;
      }
      
      for ( Iterator< TreeItem< NotificationTreeItem > > iterator = branch.getChildren().iterator(); iterator.hasNext(); ) {
         TreeItem< NotificationTreeItem > treeItem = iterator.next();
         if ( item.equals( treeItem.getValue() ) ) {
            iterator.remove();
            return;
         }
      }
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void update( NotificationTreeItem object ) {}

   /**
    * {@inheritDoc}
    */
   @Override public boolean isControlling( Tree< NotificationTreeItem, NotificationTreeItem, ? , ? > tree ) {
      if ( tree == null ) {
         return false;
      }
      
      if ( tree.getRoot() == null ) {
         return false;
      }
      
      if ( tree.getRoot().getChildren().contains( branch ) ) {
         return true;
      }
      
      return false;
   }//End Method

}//End Class
