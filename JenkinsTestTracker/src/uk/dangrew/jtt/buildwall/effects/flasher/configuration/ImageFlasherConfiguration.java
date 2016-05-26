/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.flasher.configuration;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.image.Image;

/**
 * The {@link ImageFlasherConfiguration} provides the interface to supporting configuration
 * of {@link ImageFlasher}s.
 */
public interface ImageFlasherConfiguration {

   /**
    * Method to get the number of milliseconds to flash on for.
    * @return the {@link IntegerProperty}.
    */
   public IntegerProperty flashOnProperty();//End Method

   /**
    * Method to get the number of milliseconds to wait before flashing on again.
    * @return the {@link IntegerProperty}.
    */
   public IntegerProperty flashOffProperty();//End Method

   /**
    * Method to get the number of flashes to apply.
    * @return the {@link IntegerProperty}.
    */
   public IntegerProperty numberOfFlashesProperty();//End Method

   /**
    * Method to get the transparency of the image.
    * @return the {@link DoubleProperty}, should be between 0.0 and 1.0 as per 
    * {@link uk.dangrew.jtt.javafx.scene.image.ImageView#setOpacity(double)}.
    */
   public DoubleProperty transparencyProperty();//End Method

   /**
    * Method to get the {@link Image} to be shown when flashing.
    * @return the {@link ObjectProperty}. The value associated is allowed to be null.
    */
   public ObjectProperty< Image > imageProperty();//End Method

}//End Interface