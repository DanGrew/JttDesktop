/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.flasher.control;

import javafx.beans.property.BooleanProperty;

/**
 * The {@link ImageFlasherControls} provides controls that can be accessed that change
 * the behaviour of the gui in relation to the {@link uk.dangrew.jtt.buildwall.effects.flasher.ImageFlasher}.
 */
public interface ImageFlasherControls {
   
   /**
    * Provides the switch that can be turned on an off to start and stop the flashing behaviour.
    * @return the {@link BooleanProperty}.
    */
   public BooleanProperty flashingSwitch();

}//End Interface
