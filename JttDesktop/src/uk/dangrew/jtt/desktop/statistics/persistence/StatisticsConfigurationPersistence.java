/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.persistence;

import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jupa.json.parse.JsonParser;
import uk.dangrew.jupa.json.parse.handle.key.JsonArrayParseHandler;
import uk.dangrew.jupa.json.parse.handle.type.DoubleParseHandle;
import uk.dangrew.jupa.json.parse.handle.type.StringParseHandle;
import uk.dangrew.jupa.json.structure.JsonStructure;
import uk.dangrew.jupa.json.write.handle.key.JsonArrayWriteHandler;
import uk.dangrew.jupa.json.write.handle.key.JsonValueWriteHandler;
import uk.dangrew.jupa.json.write.handle.type.JsonWriteHandleImpl;

/**
 * The {@link StatisticsConfigurationPersistence} provides the {@link StatisticsConfiguration} 
 * {@link JsonParser}s and {@link JsonStructure} for persisting the configuration.
 */
class StatisticsConfigurationPersistence {
   
   static final String STATISTICS = "Statistics";
   
   static final String EXCLUSIONS = "Exclusions";
   
   static final String FONTS = "Fonts";
   static final String VALUE_TEXT_FAMILY = "ValueTextFamily";
   static final String VALUE_TEXT_SIZE = "ValueTextSize";
   static final String DESCRIPTION_TEXT_FAMILY = "DescriptionTextFamily";
   static final String DESCRIPTION_TEXT_SIZE = "DescriptionTextSize";
   
   static final String COLOURS = "Colours";
   static final String BACKGROUND_COLOUR = "BackgroundColour";
   static final String TEXT_COLOUR = "TextColour";
   
   private final JsonStructure structure;
   private final JsonParser parserWithReadHandles;
   private final StatisticsConfigurationModel parseModel;
   private final JsonParser parserWithWriteHandles;
   private final StatisticsConfigurationModel writeModel;
   
   /**
    * Constructs a new {@link StatisticsConfigurationPersistence}.
    * @param configuration the {@link StatisticsConfiguration} to persist.
    */
   StatisticsConfigurationPersistence( StatisticsConfiguration configuration ) {
      this( new StatisticsConfigurationModel( configuration ), new StatisticsConfigurationModel( configuration ) );
   }//End Constructor
   
   /**
    * Constructs a new {@link StatisticsConfigurationPersistence}.
    * @param parseModel the {@link StatisticsConfigurationModel} to use for parsing in to.
    * @param writeModel the {@link StatisticsConfigurationModel} to use for writing from.
    */
   StatisticsConfigurationPersistence( StatisticsConfigurationModel parseModel, StatisticsConfigurationModel writeModel ) {
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
    * Method to construct the {@link JsonStructure} for the {@link StatisticsConfiguration}.
    */
   private void constructStructure(){
      structure.child( STATISTICS, structure.root() );

      structure.array( EXCLUSIONS, STATISTICS, writeModel::getNumberOfExclusions );

      structure.child( FONTS, STATISTICS );
      structure.child( VALUE_TEXT_FAMILY, FONTS );
      structure.child( VALUE_TEXT_SIZE, FONTS );
      structure.child( DESCRIPTION_TEXT_FAMILY, FONTS );
      structure.child( DESCRIPTION_TEXT_SIZE, FONTS );
      
      structure.child( COLOURS, STATISTICS );
      structure.child( BACKGROUND_COLOUR, COLOURS );
      structure.child( TEXT_COLOUR, COLOURS );
   }//End Method
   
   /**
    * Method to construct the {@link JsonParser} for reading.
    */
   private void constructReadHandles(){
      parserWithReadHandles.when( EXCLUSIONS, new StringParseHandle( new JsonArrayParseHandler<>( 
               parseModel::setExclusion, parseModel::startReadingExclusions, null
      ) ) );
      
      parserWithReadHandles.when( VALUE_TEXT_FAMILY, new StringParseHandle( parseModel::setValueTextFontFamily ) );
      parserWithReadHandles.when( VALUE_TEXT_SIZE, new DoubleParseHandle( parseModel::setValueTextFontSize ) );
      
      parserWithReadHandles.when( DESCRIPTION_TEXT_FAMILY, new StringParseHandle( parseModel::setDescriptionTextFontFamily ) );
      parserWithReadHandles.when( DESCRIPTION_TEXT_SIZE, new DoubleParseHandle( parseModel::setDescriptionTextFontSize ) );
      
      parserWithReadHandles.when( BACKGROUND_COLOUR, new StringParseHandle( parseModel::setBackgroundColour ) );
      parserWithReadHandles.when( TEXT_COLOUR, new StringParseHandle( parseModel::setTextColour ) );
   }//End Method
   
   /**
    * Method to construct the {@link JsonParser} for writing.
    */
   private void constructuWriteHandles(){
      parserWithWriteHandles.when( EXCLUSIONS, new JsonWriteHandleImpl( new JsonArrayWriteHandler( writeModel::getExclusion, writeModel::startWritingExclusions, null ) ) );
      
      parserWithWriteHandles.when( VALUE_TEXT_FAMILY, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getValueTextFontFamily ) ) );
      parserWithWriteHandles.when( VALUE_TEXT_SIZE, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getValueTextFontSize ) ) );
      
      parserWithWriteHandles.when( DESCRIPTION_TEXT_FAMILY, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getDescriptionTextFontFamily ) ) );
      parserWithWriteHandles.when( DESCRIPTION_TEXT_SIZE, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getDescriptionTextFontSize ) ) );
      
      parserWithWriteHandles.when( BACKGROUND_COLOUR, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getBackgroundColour ) ) );
      parserWithWriteHandles.when( TEXT_COLOUR, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getTextColour ) ) );
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
