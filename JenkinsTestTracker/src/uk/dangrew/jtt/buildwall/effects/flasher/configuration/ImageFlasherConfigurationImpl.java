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
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

/**
 * The {@link ImageFlasherConfigurationImpl} is responsible for providing
 * configuration to the {@link ImageFlasher}.
 */
public class ImageFlasherConfigurationImpl implements ImageFlasherConfiguration {

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
    * Constructs a new {@link ImageFlasherConfigurationImpl}.
    */
   public ImageFlasherConfigurationImpl() {
      imageProperty = new SimpleObjectProperty<>();
      flashOnPeriodProperty = new SimpleIntegerProperty( DEFAULT_FLASH_ON );
      flashOffPeriodProperty = new SimpleIntegerProperty( DEFAULT_FLASH_OFF );
      numberOfFlashesProperty = new SimpleIntegerProperty( DEFAULT_NUMBER_OF_FLASHES );
      transparencyProperty = new SimpleDoubleProperty( DEFAULT_TRANSPARENCY );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public IntegerProperty flashOnProperty() {
      return flashOnPeriodProperty;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public IntegerProperty flashOffProperty() {
      return flashOffPeriodProperty;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public IntegerProperty numberOfFlashesProperty() {
      return numberOfFlashesProperty;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public DoubleProperty transparencyProperty() {
      return transparencyProperty;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Image > imageProperty() {
      return imageProperty;
   }//End Method

}//End Class
