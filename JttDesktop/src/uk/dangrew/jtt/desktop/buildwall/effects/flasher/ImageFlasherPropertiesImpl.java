/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.flasher;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.configuration.ImageFlasherConfiguration;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.configuration.ImageFlasherConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.control.ImageFlasherControls;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.control.ImageFlasherControlsImpl;

/**
 * {@link ImageFlasherPropertiesImpl} provides a basic implementation of {@link ImageFlasherProperties}.
 */
public class ImageFlasherPropertiesImpl implements ImageFlasherProperties {
   
   private final ImageFlasherConfiguration configuration;
   private final ImageFlasherControls controls;
   
   /**
    * Constructs a new {@link ImageFlasherPropertiesImpl}.
    */
   public ImageFlasherPropertiesImpl() {
      this( new ImageFlasherConfigurationImpl(), new ImageFlasherControlsImpl() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ImageFlasherPropertiesImpl}.
    * @param configuration the {@link ImageFlasherConfiguration} to use for configuring.
    * @param controls the {@link ImageFlasherControls} to use for controlling.
    */
   ImageFlasherPropertiesImpl( ImageFlasherConfiguration configuration, ImageFlasherControls controls ) {
      this.configuration = configuration;
      this.controls = controls;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public IntegerProperty flashOnProperty() {
      return configuration.flashOnProperty();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public IntegerProperty flashOffProperty() {
      return configuration.flashOffProperty();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public IntegerProperty numberOfFlashesProperty() {
      return configuration.numberOfFlashesProperty();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public DoubleProperty transparencyProperty() {
      return configuration.transparencyProperty();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Image > imageProperty() {
      return configuration.imageProperty();
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public BooleanProperty flashingSwitch() {
      return controls.flashingSwitch();
   }//End Method

}//End Class
