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
   private final ImageFlasherProperties properties;
   private RegistrationManager registrations;
   
   /**
    * Constructs a new {@link ImageFlasherImpl}.
    * @param configuration the {@link ImageFlasherProperties} to configure the flashing.
    */
   public ImageFlasherImpl( ImageFlasherProperties configuration ) {
      flashingImage = new ImageView();
      flasher = new ImageFlasherRunnable( this, configuration );
      
      this.properties = configuration;
      updateOpacity();
      updateImage();
      
      applyRegistrations();
   }//End Constructor
   
   /**
    * Method to apply the registrations for the information this {@link ImageFlasher} depends on.
    */
   private void applyRegistrations(){
      registrations = new RegistrationManager();
      registrations.apply( 
            new ChangeListenerRegistrationImpl< Double >( 
                     properties.transparencyProperty().asObject(), 
                     ( source, old, updated ) -> updateOpacity() 
            )
      );
      registrations.apply( 
               new ChangeListenerRegistrationImpl< Image >( 
                        properties.imageProperty(), 
                        ( source, old, updated ) -> updateImage() 
               )
         );
   }//End Method
   
   /**
    * Method to update the opacity of the image.
    */
   private void updateOpacity(){
      flashingImage.setOpacity( properties.transparencyProperty().get() );
   }//End Method
   
   /**
    * Method to update the image used.
    */
   private void updateImage(){
      flashingImage.setImage( properties.imageProperty().get() );
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

   ImageView imageView() {
      return flashingImage;
   }//End Method

}//End Class
