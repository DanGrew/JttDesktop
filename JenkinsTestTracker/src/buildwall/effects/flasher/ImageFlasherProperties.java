/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.effects.flasher;

import buildwall.effects.flasher.configuration.ImageFlasherConfiguration;
import buildwall.effects.flasher.control.ImageFlasherControls;

/**
 * {@link ImageFlasherProperties} provides the interface for the complete set of properties
 * configurable and controllable for the {@link ImageFlasher}.
 */
public interface ImageFlasherProperties extends ImageFlasherConfiguration, ImageFlasherControls {}
