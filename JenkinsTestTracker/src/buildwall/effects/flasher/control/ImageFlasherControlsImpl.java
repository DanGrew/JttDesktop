/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.effects.flasher.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * {@link ImageFlasherControlsImpl} provides a basic implementation of the {@link ImageFlasherControls}.
 */
public class ImageFlasherControlsImpl implements ImageFlasherControls {

   private final BooleanProperty flashingSwitch;
   
   /**
    * Constructs a new {@link ImageFlasherControlsImpl}.
    */
   public ImageFlasherControlsImpl() {
      this.flashingSwitch = new SimpleBooleanProperty( false );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public BooleanProperty flashingSwitch() {
      return flashingSwitch;
   }//End Method

}//End Class
