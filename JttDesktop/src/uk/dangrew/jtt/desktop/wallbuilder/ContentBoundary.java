/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

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
   private final Set< ContentArea > boundedAreas;
   
   /**
    * Constructs a new {@link ContentBoundary}.
    * @param position the initial position in percentage of the dimension.
    */
   ContentBoundary( double position ) {
      this.positionPercentageProperty = new SimpleDoubleProperty( position );
      this.fixedProperty = new SimpleBooleanProperty( false );
      this.boundedAreas = new LinkedHashSet<>();
   }//End Constructor

   /**
    * Provides the position as a percentage of the dimension.
    * @return the position.
    */
   double positionPercentage() {
      return positionPercentageProperty.get();
   }//End Method
   
   /**
    * Defensive access to the {@link ContentArea}s bound to this {@link ContentBoundary}.
    * @return a {@link Collection} of {@link ContentArea}s.
    */
   Collection< ContentArea > boundedAreas() {
      return new ArrayList<>( boundedAreas );
   }//End Method
   
   /**
    * Register for changes made to the position.
    * @param area the {@link ContentArea} to bind to this {@link ContentBoundary}.
    */
   void registerForPositionChanges( ContentArea area ) {
      boundedAreas.add( area );
   }//End Method
   
   /**
    * Unregister for changes made to the position.
    * @param area the {@link ContentArea} to unbind from this {@link ContentBoundary}.
    */
   void unregisterForPositionChanges( ContentArea area ) {
      boundedAreas.remove( area );
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
      boundedAreas.forEach( a -> a.refreshDimensions() );
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
   
   /**
    * {@inheritDoc}
    */
   @Override public String toString(){
      return "Boundary @ " + positionPercentageProperty.get();
   }//End Method
}//End Class
