/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import javafx.collections.ObservableList;
import javafx.scene.Node;

/**
 * {@link ContentAreaIntersections} calculates intersections between {@link ContentArea}s
 * and adjusts the sizes appropriately.
 */
class ContentAreaIntersections {

   private ObservableList< Node > areas;
   
   /**
    * Constructs a new {@link ContentAreaIntersections}.
    */
   ContentAreaIntersections() {
   }//End Constructor
   
   /**
    * Method to set the {@link Node}s being observed.
    * @param areas the {@link Node}s in the scene that can be selected.
    */
   void setNodes( ObservableList< Node > nodes ) {
      this.areas = nodes;
   }//End Method

   /**
    * Method to extract the {@link ContentArea} from a {@link Node}.
    * @param node the {@link Node} that could be a {@link ContentArea}.
    * @return the {@link ContentArea} or null.
    */
   private ContentArea extractContentArea( Node node ) {
      if ( !( node instanceof ContentArea ) ) {
         return null;
      }
      
      return ( ContentArea )node;
   }//End Method
   
   /**
    * Method to check intersections following a change in x translation.
    * @param area the {@link ContentArea} changed.
    */
   void checkTranslationXIntersection( ContentArea area ){
      for ( Node node : areas ) {
         if ( node == area ) {
            continue;
         }
         ContentArea otherArea = extractContentArea( node );
         if ( otherArea == null ) {
            continue;
         }
         
         double existingPosition = otherArea.xPositionPercentage() + otherArea.percentageWidth();
         double areaNewPosition = area.xPositionPercentage();
         double overlap = existingPosition - areaNewPosition;
         otherArea.changeWidthPercentageBy( -overlap );
      }
   }//End Method
   
   /**
    * Method to check intersections following a change in y translation.
    * @param area the {@link ContentArea} changed.
    */
   void checkTranslationYIntersection( ContentArea area ){
      for ( Node node : areas ) {
         if ( node == area ) {
            continue;
         }
         ContentArea otherArea = extractContentArea( node );
         if ( otherArea == null ) {
            continue;
         }
         
         double existingPosition = otherArea.yPositionPercentage() + otherArea.percentageHeight();
         double areaNewPosition = area.yPositionPercentage();
         double overlap = existingPosition - areaNewPosition;
         otherArea.changeHeightPercentageBy( -overlap );
      }
   }//End Method
   
   /**
    * Method to check intersections following a change in width.
    * @param area the {@link ContentArea} changed.
    */
   void checkWidthIntersection( ContentArea area ){
      for ( Node node : areas ) {
         if ( node == area ) {
            continue;
         }
         ContentArea otherArea = extractContentArea( node );
         if ( otherArea == null ) {
            continue;
         }
         
         double existingPosition = otherArea.xPositionPercentage();
         double areaNewPosition = area.xPositionPercentage() + area.percentageWidth();
         double overlap = areaNewPosition - existingPosition;
         otherArea.changeXPositionPercentageBy( overlap );
      }
   }//End Method
   
   /**
    * Method to check intersections following a change in height.
    * @param area the {@link ContentArea} changed.
    */
   void checkHeightIntersection( ContentArea area ){
      for ( Node node : areas ) {
         if ( node == area ) {
            continue;
         }
         ContentArea otherArea = extractContentArea( node );
         if ( otherArea == null ) {
            continue;
         }
         
         double existingPosition = otherArea.yPositionPercentage();
         double areaNewPosition = area.yPositionPercentage() + area.percentageHeight();
         double overlap = areaNewPosition - existingPosition;
         otherArea.changeYPositionPercentageBy( overlap );
      }
   }//End Method
   
}//End Class
