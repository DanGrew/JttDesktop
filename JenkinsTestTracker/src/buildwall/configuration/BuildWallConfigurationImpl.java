/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * The {@link BuildWallConfigurationImpl} provides an implementation of the {@link BuildWallConfiguration}.
 */
public class BuildWallConfigurationImpl implements BuildWallConfiguration {
   
   static final Font DEFAULT_PROPERTIES_FONT = new Font( 18 );
   static final Font DEFAULT_JOB_NAME_FONT = new Font( 30 );
   
   static final Color DEFAULT_TEXT_COLOUR = Color.WHITE;
   static final int DEFAULT_NUMBER_OF_COLUMNS = 2;
   
   private ObjectProperty< Font > propertiesFont;
   private ObjectProperty< Color > propertiesColour;
   
   private ObjectProperty< Font > jobNameFont;
   private ObjectProperty< Color > jobNameColour;
   
   private IntegerProperty numberOfColumns;
   
   /**
    * Constructs a new {@link BuildWallConfigurationImpl}.
    */
   public BuildWallConfigurationImpl() {
      propertiesColour = new SimpleObjectProperty< Color >( DEFAULT_TEXT_COLOUR );
      propertiesFont = new SimpleObjectProperty< Font >( DEFAULT_PROPERTIES_FONT );
      
      jobNameColour = new SimpleObjectProperty< Color >( DEFAULT_TEXT_COLOUR );
      jobNameFont = new SimpleObjectProperty< Font >( DEFAULT_JOB_NAME_FONT );
      
      numberOfColumns = new SimpleIntegerProperty( DEFAULT_NUMBER_OF_COLUMNS );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Font > propertiesFont() {
      return propertiesFont;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Color > propertiesColour() {
      return propertiesColour;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Font > jobNameFont() {
      return jobNameFont;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public ObjectProperty< Color > jobNameColour() {
      return jobNameColour;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public IntegerProperty numberOfColumns() {
      return numberOfColumns;
   }//End Method

}//End Class
