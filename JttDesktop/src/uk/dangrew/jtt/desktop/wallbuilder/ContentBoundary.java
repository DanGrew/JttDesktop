/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;

/**
 * The {@link ContentBoundary} provides a boundary for a {@link ContentArea} providing a particular
 * coordinate such as the translation position, or the opposite position (translation x + width for example).
 */
class ContentBoundary {

   private DoubleProperty positionPercentageProperty;
   
   /**
    * Constructs a new {@link ContentBoundary}.
    * @param position the initial position in percentage of the dimension.
    */
   ContentBoundary( double position ) {
      this.positionPercentageProperty = new SimpleDoubleProperty( position );
   }//End Constructor

   /**
    * Provides the position as a percentage of the dimension.
    * @return the position.
    */
   double positionPercentage() {
      return positionPercentageProperty.get();
   }//End Method
   
   /**
    * Register for changes made to the position.
    * @param listener the {@link ChangeListener} to notify.
    */
   void registerForPositionChanges( ChangeListener< ? super Number > listener  ) {
      positionPercentageProperty.addListener( listener );
   }//End Method
   
   /**
    * Unregister for changes made to the position.
    * @param listener the {@link ChangeListener} to stop notifying.
    */
   void unregisterForPositionChanges( ChangeListener< ? super Number > listener  ) {
      positionPercentageProperty.removeListener( listener );
   }//End Method
   
   /**
    * Method to change the position by the given percentage.
    * @param change the change, positive or negative.
    */
   void changePosition( double change ) {
      positionPercentageProperty.set( positionPercentageProperty.get() + change );
   }//End Method
}//End Class
