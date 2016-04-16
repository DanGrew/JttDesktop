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
import graphics.DecoupledPlatformImpl;

/**
 * {@link ImageFlasherRunnable} is responsible for defining the flashing operation that
 * can be run to flash an {@link ImageFlasher} given the {@link ImageFlasherConfiguration}.
 */
public class ImageFlasherRunnable implements Runnable {
   
   private final ImageFlasher flasher;
   private final ImageFlasherConfiguration configuration;

   /**
    * Constructs a new {@link ImageFlasherRunnable}.
    * @param flasher the {@link ImageFlasher} to flash.
    * @param configuration the {@link ImageFlasherConfiguration} for defining how the flash behaves.
    */
   public ImageFlasherRunnable( ImageFlasher flasher, ImageFlasherConfiguration configuration ) {
      this.flasher = flasher;
      this.configuration = configuration;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public void run() {
      new Thread( this::tryToFlash ).start();
   }//End Method
   
   /**
    * Method to try to perform the flash, catching any issues with threading.
    */
   private void tryToFlash(){
      final int numberOfFlashes = configuration.numberOfFlashesProperty().get();
      final int flashOn = configuration.flashOnProperty().get();
      final int flashOff = configuration.flashOffProperty().get();
      
      for( int i = 0; i < numberOfFlashes; i++ ) {
         
         DecoupledPlatformImpl.runLater( flasher::flashOn );
         
         try {
            Thread.sleep( flashOn );
         } catch ( InterruptedException e ) {
            e.printStackTrace();
         }
         
         DecoupledPlatformImpl.runLater( flasher::flashOff );
         
         try {
            Thread.sleep( flashOff );
         } catch ( InterruptedException e ) {
            e.printStackTrace();
         }
      }
   }//End Method

}//End Class
