/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.javafx.tree.structure;

/**
 * The {@link TreeController} is responsible for controlling the information in the 
 * {@link Tree}.
 * @param <TreeItemValueT> the {@link TreeItemValue} associated with {@link TreeItem}s.
 * @param <ObjectTypeT> the underlying object being represented in the tree, can be the same as TreeItemValueT.
 * @param <LayoutManagerT> the associated {@link TreeLayout} manager.
 */
public class TreeController< TreeItemValueT extends TreeItemValue, ObjectTypeT, LayoutManagerT extends TreeLayout< TreeItemValueT, ObjectTypeT > > {

   private final LayoutManagerT layout;
   
   /**
    * Constructs a new {@link TreeController}.
    * @param layout the {@link TreeLayout} for positioning items in the tree.
    */
   public TreeController( LayoutManagerT layout ) {
      this.layout = layout;
   }//End Constructor
   
   /**
    * Method to add the given ObjectTypeT to the {@link Tree}.
    * @param object the ObjectTypeT to add.
    */
   public void add( ObjectTypeT object ) {
      layout.add( object );
   }//End MethodT
   
   /**
    * Method to remove the given ObjectTypeT from the {@link Tree}.
    * @param object the ObjectTypeT to remove.
    */
   public void remove( ObjectTypeT object ) {
      layout.remove( object );
   }//End Method
   
   /**
    * Method to update the given ObjectTypeT in the {@link Tree}.
    * @param object the ObjectTypeT to update.
    */
   public void update( ObjectTypeT object ) {
      layout.update( object );
   }//End Method
   
   /**
    * Protected access to the {@link TreeLayout}.
    * @return the associated {@link TreeLayout}.
    */
   protected LayoutManagerT getLayoutManager(){
      return layout;
   }//End Method
   
}//End Class
