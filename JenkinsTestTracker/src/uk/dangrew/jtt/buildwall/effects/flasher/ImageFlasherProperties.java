/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.flasher;

import uk.dangrew.jtt.buildwall.effects.flasher.configuration.ImageFlasherConfiguration;
import uk.dangrew.jtt.buildwall.effects.flasher.control.ImageFlasherControls;

/**
 * {@link ImageFlasherProperties} provides the interface for the complete set of properties
 * configurable and controllable for the {@link ImageFlasher}.
 */
public interface ImageFlasherProperties extends ImageFlasherConfiguration, ImageFlasherControls {}
