/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.layout;

import javafx.scene.Group;
import javafx.scene.Node;

/**
 * The {@link CenterScreenWrapper} provides a method of displaying a component in the center
 * of the {@link Node} as its calculated size.
 */
public class CenterScreenWrapper extends Group {
   
   /**
    * Constructs a new {@link CenterScreenWrapper} with nothing
    * at the center.
    */
   public CenterScreenWrapper() {
      //empty group
   }//End Constructor
   
   /**
    * Constructs a new {@link CenterScreenWrapper}.
    * @param center the {@link Node} at the center.
    */
   public CenterScreenWrapper( Node center ) {
      setCenter( center );
   }//End Constructor

   /**
    * Method to change the center of the {@link Node}.
    * @param content the {@link Node} to replace the center with.
    */
   public void setCenter( Node content ) {
      getChildren().clear();
      getChildren().add( content );
   }//End Method
   
   /**
    * Getter for the center of the screen.
    * @return the center.
    */
   public Node getCenter(){
      if ( getChildren().isEmpty() ) {
         return null;
      }
      return getChildren().get( 0 );
   }//End Method

}//End Class
