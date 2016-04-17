/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.effects.flasher;

import graphics.DecoupledPlatformImpl;
import javafx.beans.value.ObservableValue;

/**
 * {@link ImageFlasherRunnable} is responsible for defining the flashing operation that
 * can be run to flash an {@link ImageFlasher} given the {@link ImageFlasherConfiguration}.
 */
public class ImageFlasherRunnable {
   
   private final ImageFlasher flasher;
   private final ImageFlasherProperties properties;
   private Thread previousThread;

   /**
    * Constructs a new {@link ImageFlasherRunnable}.
    * @param flasher the {@link ImageFlasher} to flash.
    * @param properties the {@link ImageFlasherProperties} for defining how the flash behaves.
    */
   public ImageFlasherRunnable( ImageFlasher flasher, ImageFlasherProperties properties ) {
      this.flasher = flasher;
      this.properties = properties;
      
      this.properties.flashingSwitch().addListener( this::run );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   public void run( ObservableValue< ? extends Boolean > source, Boolean old, Boolean updated ) {
      if ( previousThread == null || !previousThread.isAlive() ) {
         previousThread = new Thread( this::tryToFlash );
         previousThread.start();
      }
   }//End Method
   
   /**
    * Method to try to perform the flash, catching any issues with threading.
    */
   private void tryToFlash(){
      final int numberOfFlashes = properties.numberOfFlashesProperty().get();
      final int flashOn = properties.flashOnProperty().get();
      final int flashOff = properties.flashOffProperty().get();
      
      for( int i = 0; i < numberOfFlashes; i++ ) {
         if ( !properties.flashingSwitch().get() ) {
            return;
         }
         
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
      
      properties.flashingSwitch().set( false );
   }//End Method

}//End Class
