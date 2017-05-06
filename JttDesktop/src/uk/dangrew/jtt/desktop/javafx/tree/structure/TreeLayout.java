/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.tree.structure;

import java.util.List;

/**
 * The {@link TreeLayout} provides an interface for any object responsible for managing the items
 * within a {@link Tree} and how they should be arranged.
 * @param <TreeItemValueT> the {@link TreeItemValue} associated with {@link TreeItem}s.
 * @param <ObjectTypeT> the underlying object being represented in the tree, can be the same as TreeItemValueT.
 */
public interface TreeLayout< TreeItemValueT extends TreeItemValue, ObjectTypeT > {

   /**
    * Method to reconstruct the tree. This will discard everything currently in the tree and
    * reconstruct its structure.
    * @param jobs the ObjectTypeTs to add.
    */
   public void reconstructTree( List< ObjectTypeT > jobs );

   /**
    * Method to add the given ObjectTypeT to the tree.
    * @param object the ObjectTypeT to add.
    */
   public void add( ObjectTypeT object );
   
   /**
    * Method to remove the given ObjectTypeT from the tree.
    * @param object the ObjectTypeT to remove.
    */
   public void remove( ObjectTypeT object );
   
   /**
    * Method to update the ObjectTypeT in the tree, moving it if needed.
    * @param object the ObjectTypeT to update.
    */
   public void update( ObjectTypeT object );   
   
   /**
    * Method to determine whether the given object is located within the associated {@link Tree}.
    * @param object the object to check.
    * @return true if contained in the tree.
    */
   public boolean contains( ObjectTypeT object );
   
   /**
    * Method to determine whether this layout is controlling the given {@link Tree}.
    * @param tree the {@link Tree} in question.
    * @return true if being controller by and laid out with the given.
    */
   public boolean isControlling( Tree< TreeItemValueT, ObjectTypeT, ?, ? > tree );
}//End Class
