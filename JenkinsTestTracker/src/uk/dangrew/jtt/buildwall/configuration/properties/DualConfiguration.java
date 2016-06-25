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
import javafx.geometry.Orientation;

/**
 * {@link DualConfiguration} provides the interface for the configurable properties of
 * the {@link uk.dangrew.jtt.buildwall.dual.DualBuildWallDisplayImpl}.
 */
public interface DualConfiguration {

   /** 
    * Accesses the property for controlling the divider position.
    * @return the {@link DoubleProperty}.
    */
   public DoubleProperty dividerPositionProperty();

   /**
    * Accesses the property for the divider {@link Orientation}.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Orientation > dividerOrientationProperty();

}//End Interface
