/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;

/**
 * The {@link ContentBoundary} provides a boundary for a {@link ContentArea} providing a particular
 * coordinate such as the translation position, or the opposite position (translation x + width for example).
 */
class ContentBoundary {

   private final BooleanProperty fixedProperty;
   private final DoubleProperty positionPercentageProperty;
   
   /**
    * Constructs a new {@link ContentBoundary}.
    * @param position the initial position in percentage of the dimension.
    */
   ContentBoundary( double position ) {
      this.positionPercentageProperty = new SimpleDoubleProperty( position );
      this.fixedProperty = new SimpleBooleanProperty( false );
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
      if ( isFixed() ) {
         return;
      }
      positionPercentageProperty.set( positionPercentageProperty.get() + change );
   }//End Method

   /**
    * Method to determine whether the {@link ContentBoundary} is fixed. If fixed, it cannot change
    * position.
    * @return true if fixed.
    */
   boolean isFixed() {
      return fixedProperty.get();
   }//End Method
   
   /**
    * Register for changes made to the fixed property.
    * @param listener the {@link ChangeListener} to notify.
    */
   void registerForFixedChanges( ChangeListener< ? super Boolean > listener  ) {
      fixedProperty.addListener( listener );
   }//End Method
   
   /**
    * Unregister for changes made to the fixed property.
    * @param listener the {@link ChangeListener} to stop notifying.
    */
   void unregisterForFixedChanges( ChangeListener< ? super Boolean > listener  ) {
      fixedProperty.removeListener( listener );
   }//End Method

   /**
    * Method to set whether the {@link ContentBoundary} is fixed or not.
    * @param fixed whether it should be fixed or not.
    */
   void setFixed( boolean fixed ) {
      fixedProperty.set( fixed );
   }//End Method
}//End Class
