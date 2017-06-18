/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import java.util.Collection;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * {@link ContentAreaRemover} is responsible for removing {@link ContentArea}s that have been detached
 * from {@link ContentBoundary}s.
 */
class ContentAreaRemover {

   private List< Node > nodes;
   
   /**
    * Constructs a new {@link ContentAreaRemover}.
    */
   ContentAreaRemover(){}
   
   /**
    * Associates the {@link Node}s being removed.
    * @param nodes the {@link ObservableList} of {@link Node}s to remove from.
    */
   void setNodes( ObservableList< Node > nodes ) {
      this.nodes = nodes;
   }//End Constructor
   
   /**
    * Method to verify the {@link ContentBoundary}s of the given {@link ContentArea}s. Any that do
    * not have {@link ContentBoundary}s will be completely detached and removed from the {@link Node}s.
    * @param areas the {@link ContentArea}s to consider.
    */
   void verifyBoundaries( Collection< ContentArea > areas ){
      for ( ContentArea area : areas ) {
         boolean shouldRemove = false;
         for ( SquareBoundary boundaryType : SquareBoundary.values() ) {
            if ( boundaryType.boundary( area ) == null ) {
               shouldRemove = true;
               break;
            }
         }
   
         if ( shouldRemove ) {
            for ( SquareBoundary boundaryType : SquareBoundary.values() ) {
               boundaryType.set( null, area );
            }
            nodes.remove( area );
         }
      }
   }//End Method
   
}//End Class
