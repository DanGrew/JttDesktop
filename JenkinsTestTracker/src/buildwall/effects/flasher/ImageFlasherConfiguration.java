/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.effects.flasher;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

/**
 * The {@link ImageFlasherConfiguration} is responsible for providing
 * configuration to the {@link ImageFlasher}.
 */
public class ImageFlasherConfiguration {

   static final int DEFAULT_FLASH_ON = 1000;
   static final int DEFAULT_FLASH_OFF = 500;
   static final int DEFAULT_NUMBER_OF_FLASHES = 5;
   static final double DEFAULT_TRANSPARENCY = 0.8;
   
   private final ObjectProperty< Image > imageProperty;
   private final IntegerProperty flashOnPeriodProperty;
   private final IntegerProperty flashOffPeriodProperty;
   private final IntegerProperty numberOfFlashesProperty;
   private final DoubleProperty transparencyProperty;
   
   /**
    * Constructs a new {@link ImageFlasherConfiguration}.
    */
   public ImageFlasherConfiguration() {
      imageProperty = new SimpleObjectProperty<>();
      flashOnPeriodProperty = new SimpleIntegerProperty( DEFAULT_FLASH_ON );
      flashOffPeriodProperty = new SimpleIntegerProperty( DEFAULT_FLASH_OFF );
      numberOfFlashesProperty = new SimpleIntegerProperty( DEFAULT_NUMBER_OF_FLASHES );
      transparencyProperty = new SimpleDoubleProperty( DEFAULT_TRANSPARENCY );
   }//End Constructor

   /**
    * Method to get the number of milliseconds to flash on for.
    * @return the {@link IntegerProperty}.
    */
   public IntegerProperty flashOnPeriodProperty() {
      return flashOnPeriodProperty;
   }//End Method

   /**
    * Method to get the number of milliseconds to wait before flashing on again.
    * @return the {@link IntegerProperty}.
    */
   public IntegerProperty flashOffPeriodProperty() {
      return flashOffPeriodProperty;
   }//End Method

   /**
    * Method to get the number of flashes to apply.
    * @return the {@link IntegerProperty}.
    */
   public IntegerProperty numberOfFlashesProperty() {
      return numberOfFlashesProperty;
   }//End Method

   /**
    * Method to get the transparency of the image.
    * @return the {@link DoubleProperty}, should be between 0.0 and 1.0 as per 
    * {@link javafx.scene.image.ImageView#setOpacity(double)}.
    */
   public DoubleProperty transparencyProperty() {
      return transparencyProperty;
   }//End Method

   /**
    * Method to get the {@link Image} to be shown when flashing.
    * @return the {@link ObjectProperty}. The value associated is allowed to be null.
    */
   public ObjectProperty< Image > imageProperty() {
      return imageProperty;
   }//End Method

}//End Class
