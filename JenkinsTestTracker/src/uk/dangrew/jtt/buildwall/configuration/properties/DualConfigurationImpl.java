/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.properties;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Orientation;

/**
 * {@link DualConfigurationImpl} provides a basic implementation of {@link DualConfiguration}.
 */
public class DualConfigurationImpl implements DualConfiguration{
   
   static final double DEFAULT_DIVIDER_POSITION = 0.5;
   static final Orientation DEFAULT_DIVIDER_ORIENTATION = Orientation.HORIZONTAL;
   
   private final DoubleProperty dividerPositionProperty;
   private final ObjectProperty< Orientation > dividerOrientationProperty;
   
   /**
    * Constructs a new {@link DualConfigurationImpl}.
    */
   public DualConfigurationImpl() {
      this.dividerPositionProperty = new SimpleDoubleProperty( DEFAULT_DIVIDER_POSITION );
      this.dividerOrientationProperty = new SimpleObjectProperty< Orientation >( DEFAULT_DIVIDER_ORIENTATION );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public DoubleProperty dividerPositionProperty() {
      return dividerPositionProperty;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Orientation > dividerOrientationProperty() {
      return dividerOrientationProperty;
   }//End Method

}//End Class
