/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.effects.flasher;

/**
 * The {@link ImageFlasher} provides the interface to something that can be 
 * flashed on an off by an {@link ImageFlasherRunnable}.
 */
public interface ImageFlasher {
   
   /**
    * Method to flash on. This would typically show an image.
    */
   public void flashOn();
   
   /**
    * Method to flash off. This would typically remove an image.
    */
   public void flashOff();

}//End Interface
