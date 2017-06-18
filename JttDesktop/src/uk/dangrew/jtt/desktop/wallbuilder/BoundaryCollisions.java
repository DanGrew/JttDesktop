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
import java.util.function.BiFunction;

/**
 * {@link BoundaryCollisions} provides a method of detecting colliding {@link ContentBoundary}s and
 * merging them by swapping {@link ContentArea}s references to them.
 */
class BoundaryCollisions {
   
   /** Function to check for collisions moving positively along an axis.**/
   private static final BiFunction< Double, Double, Boolean > FORWARD_COLLISION_CHECK =
            ( existingBoundary, movedBoundary ) -> existingBoundary >= movedBoundary;
   /** Function to check for collisions moving negatively along an axis.**/         
   private static final BiFunction< Double, Double, Boolean > BACKWARD_COLLISION_CHECK =
            ( existingBoundary, movedBoundary ) -> existingBoundary <= movedBoundary;
   
   /**
    * Method to merge {@link ContentBoundary}s if there are any collisions.
    * @param boundary the {@link ContentBoundary} being moved.
    */
   void mergeBoundaries( ContentBoundary boundary ) {
      applyReplacement( SquareBoundary.Left, boundary, FORWARD_COLLISION_CHECK );
      applyReplacement( SquareBoundary.Right, boundary, BACKWARD_COLLISION_CHECK );
      applyReplacement( SquareBoundary.Top, boundary, FORWARD_COLLISION_CHECK );
      applyReplacement( SquareBoundary.Bottom, boundary, BACKWARD_COLLISION_CHECK );
   }//End Method
   
   /**
    * Function to apply a replacement {@link ContentBoundary} onto {@link ContentArea}s if a collision
    * is detected.
    * @param boundaryType the {@link SquareBoundary} type to consider.
    * @param boundary the {@link ContentBoundary} in question.
    * @param collisionCheck the type of collision checking for.
    */
   private void applyReplacement( 
            SquareBoundary boundaryType, 
            ContentBoundary boundary,
            BiFunction< Double, Double, Boolean > collisionCheck
   ) {
      ContentBoundary replacement = null;
      Collection< ContentArea > boundedAreas = boundary.boundedAreas();
      
      for ( ContentArea area : boundedAreas ) {
         if ( boundaryType.opposite().boundary( area ) != boundary ) {
            continue;
         }
         if ( boundaryType.boundary( area ) == boundaryType.opposite().boundary( area ) ){
            continue;
         }
         
         if ( collisionCheck.apply( boundaryType.boundary( area ).positionPercentage(), boundary.positionPercentage() ) ) {
            if ( replacement != null && replacement != boundaryType.boundary( area ) ) {
               throw new IllegalStateException();
            }
            replacement = boundaryType.boundary( area );
            
            boundaryType.set( null, area );
            boundaryType.opposite().set( null, area );
         }
      }
      
      if ( replacement == null ) {
         return;
      }
      
      for ( ContentArea area : boundedAreas ) {
         if ( boundaryType.boundary( area ) == boundary ) {
            boundaryType.set( replacement, area );
         }
      }
   }//End Method
   
}//End Class
