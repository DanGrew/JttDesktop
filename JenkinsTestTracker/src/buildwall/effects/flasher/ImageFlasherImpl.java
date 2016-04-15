/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.effects.flasher;

import javafx.registrations.ChangeListenerRegistrationImpl;
import javafx.registrations.RegisteredComponent;
import javafx.registrations.RegistrationManager;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * {@link ImageFlasherImpl} provides a basic implementation of the {@link ImageFlasher}.
 */
public class ImageFlasherImpl extends BorderPane implements ImageFlasher, RegisteredComponent {

   private final ImageFlasherRunnable flasher;
   private final ImageView flashingImage;
   private final ImageFlasherConfiguration configuration;
   private RegistrationManager registrations;
   
   /**
    * Constructs a new {@link ImageFlasherImpl}.
    * @param image the {@link Image} to flash on and off.
    * @param configuration the {@link ImageFlasherConfiguration} to configure the flashing.
    */
   public ImageFlasherImpl( Image image, ImageFlasherConfiguration configuration ) {
      flashingImage = new ImageView();
      flashingImage.setImage( image );
      flasher = new ImageFlasherRunnable( this, configuration );
      
      this.configuration = configuration;
      updateOpacity();
      
      applyRegistrations();
   }//End Constructor
   
   /**
    * Method to apply the registrations for the information this {@link ImageFlasher} depends on.
    */
   private void applyRegistrations(){
      this.registrations = new RegistrationManager();
      this.registrations.apply( 
            new ChangeListenerRegistrationImpl< Double >( 
                     configuration.transparencyProperty().asObject(), 
                     ( source, old, updated ) -> updateOpacity() 
            )
      );
   }//End Method
   
   /**
    * Method to update the opacity of the image.
    */
   private void updateOpacity(){
      flashingImage.setOpacity( configuration.transparencyProperty().get() );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public void flashOn() {
      setCenter( flashingImage );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void flashOff() {
      setCenter( null );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public void detachFromSystem() {
      registrations.shutdown();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public boolean isDetached() {
      return false;
   }//End Method
   
   ImageFlasherRunnable flasher(){
      return flasher;
   }//End Method

}//End Class
