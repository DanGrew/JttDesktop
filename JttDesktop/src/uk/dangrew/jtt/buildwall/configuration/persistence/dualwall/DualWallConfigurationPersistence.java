/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence.dualwall;

import javafx.geometry.Orientation;
import uk.dangrew.jtt.buildwall.configuration.properties.DualWallConfiguration;
import uk.dangrew.jupa.json.parse.JsonParser;
import uk.dangrew.jupa.json.parse.handle.type.DoubleParseHandle;
import uk.dangrew.jupa.json.parse.handle.type.EnumParseHandle;
import uk.dangrew.jupa.json.structure.JsonStructure;
import uk.dangrew.jupa.json.write.handle.key.JsonValueWriteHandler;
import uk.dangrew.jupa.json.write.handle.type.JsonWriteHandleImpl;

/**
 * The {@link DualWallConfigurationPersistence} provides the {@link DualWallConfiguration} 
 * {@link JsonParser}s and {@link JsonStructure} for persisting the configuration.
 */
class DualWallConfigurationPersistence {
   
   static final String DUAL_BUILD_WALL = "DualWall";
   
   static final String DIVIDER = "Divider";
   static final String DIVIDER_POSITION = "Positon";
   static final String DIVIDER_ORIENTATION = "Orientation";
   
   private final JsonStructure structure;
   private final JsonParser parserWithReadHandles;
   private final DualWallConfigurationModel parseModel;
   private final JsonParser parserWithWriteHandles;
   private final DualWallConfigurationModel writeModel;
   
   /**
    * Constructs a new {@link DualWallConfigurationPersistence}.
    * @param configuration the {@link DualWallConfiguration} to persist.
    */
   DualWallConfigurationPersistence( DualWallConfiguration configuration ) {
      this( new DualWallConfigurationModel( configuration ), new DualWallConfigurationModel( configuration ) );
   }//End Constructor
   
   /**
    * Constructs a new {@link DualWallConfigurationPersistence}.
    * @param parseModel the {@link DualWallConfigurationModel} to use for parsing in to.
    * @param writeModel the {@link DualWallConfiguration} to use for writing from.
    */
   DualWallConfigurationPersistence( DualWallConfigurationModel parseModel, DualWallConfigurationModel writeModel ) {
      this.structure = new JsonStructure();
      this.parseModel = parseModel;
      this.parserWithReadHandles = new JsonParser();
      this.writeModel = writeModel;
      this.parserWithWriteHandles = new JsonParser();
      
      constructStructure();
      constructReadHandles();
      constructuWriteHandles();
   }//End Constructor
   
   /**
    * Method to construct the {@link JsonStructure} for the {@link DualWallConfiguration}.
    */
   private void constructStructure(){
      structure.child( DUAL_BUILD_WALL, structure.root() );

      structure.child( DIVIDER, DUAL_BUILD_WALL );
      structure.child( DIVIDER_POSITION, DIVIDER );
      structure.child( DIVIDER_ORIENTATION, DIVIDER );
   }//End Method
   
   /**
    * Method to construct the {@link JsonParser} for reading.
    */
   private void constructReadHandles(){
      parserWithReadHandles.when( DIVIDER_POSITION, new DoubleParseHandle( parseModel::setDividerPosition ) );
      parserWithReadHandles.when( 
               DIVIDER_ORIENTATION, 
               new EnumParseHandle<>( Orientation.class, parseModel::setDividerOrientation ) 
      );
   }//End Method
   
   /**
    * Method to construct the {@link JsonParser} for writing.
    */
   private void constructuWriteHandles(){
      parserWithWriteHandles.when( DIVIDER_POSITION, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getDividerPosition ) ) );
      parserWithWriteHandles.when( DIVIDER_ORIENTATION, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getDividerOrientation ) ) );
   }//End Method
   
   JsonStructure structure(){
      return structure;
   }//End Method
   
   JsonParser readHandles(){
      return parserWithReadHandles;
   }//End Method

   JsonParser writeHandles(){
      return parserWithWriteHandles;
   }//End Method
}//End Class
