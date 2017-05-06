/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.persistence.dualwall;

import javafx.geometry.Orientation;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.DualWallConfiguration;

/**
 * The {@link DualWallConfigurationModel} provides the model to read from and write to when converting
 * a {@link DualWallConfiguration} to and from json data.
 */
class DualWallConfigurationModel {
   
   private final DualWallConfiguration configuration;
   
   /**
    * Constructs a new {@link DualWallConfigurationModel}.
    * @param configuration the {@link DualWallConfiguration} being serialized.
    */
   DualWallConfigurationModel( DualWallConfiguration configuration ) {
      if ( configuration == null ) {
         throw new NullPointerException( "Arguments must not be null." );
      }
      
      this.configuration = configuration;
   }//End Constructor

   /**
    * Method to set the divider position from parse.
    * @param key the key parsed.
    * @param value the value to set. Note that this is filtered if not in correct range.
    */
   void setDividerPosition( String key, Double value ) {
      if ( value == null ) {
         return;
      }
      
      if ( value < 0 ) {
         return;
      } else if ( value > 1 ) {
         return;
      }
      
      configuration.dividerPositionProperty().set( value );
   }//End Method
   
   /**
    * Getter for the currently configured divider position.
    * @param key the key to get for.
    * @return the current divider position.
    */
   Double getDividerPosition( String key ) {
      return configuration.dividerPositionProperty().get();
   }//End Method
   
   /**
    * Setter for the divider {@link Orientation} from parse.
    * @param key the key parsed.
    * @param value the value parsed.
    */
   void setDividerOrientation( String key, Orientation value ) {
      if ( value == null ) {
         return;
      }
      configuration.dividerOrientationProperty().set( value );
   }//End Method
   
   /**
    * Getter for the currently configured divider {@link Orientation}.
    * @param key the key retrieving for.
    * @return the {@link Orientation}.
    */
   Orientation getDividerOrientation( String key ) {
      return configuration.dividerOrientationProperty().get();
   }//End Method
   
}//End Class
