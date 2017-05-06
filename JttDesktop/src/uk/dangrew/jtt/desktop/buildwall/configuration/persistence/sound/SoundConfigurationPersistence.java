/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.persistence.sound;

import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jupa.json.parse.JsonParser;
import uk.dangrew.jupa.json.parse.handle.key.JsonArrayWithObjectParseHandler;
import uk.dangrew.jupa.json.parse.handle.type.EnumParseHandle;
import uk.dangrew.jupa.json.parse.handle.type.StringParseHandle;
import uk.dangrew.jupa.json.structure.JsonStructure;
import uk.dangrew.jupa.json.write.handle.key.JsonArrayWithObjectWriteHandler;
import uk.dangrew.jupa.json.write.handle.key.JsonValueWriteHandler;
import uk.dangrew.jupa.json.write.handle.type.JsonWriteHandleImpl;

/**
 * The {@link SoundConfigurationPersistence} provides the {@link SoundConfiguration} 
 * {@link JsonParser}s and {@link JsonStructure} for persisting the configuration.
 */
class SoundConfigurationPersistence {
   
   static final String SOUND = "Sound";
   
   static final String STATUS_CHANGES = "StatusChanges";
   static final String CHANGE = "Change";
   static final String PREVIOUS_STATE = "Previous";
   static final String CURRENT_STATE = "Current";
   static final String FILENAME = "Filename";
   
   static final String JOB_EXCLUSIONS = "JobExclusions";
   static final String EXCLUSION = "Exclusion";
   static final String EXCLUSION_JOB_NAME = "ExclusionJobName";
   
   private final JsonStructure structure;
   private final JsonParser parserWithReadHandles;
   private final SoundConfigurationParseModel parseModel;
   private final JsonParser parserWithWriteHandles;
   private final SoundConfigurationWriteModel writeModel;
   
   /**
    * Constructs a new {@link SoundConfigurationPersistence}.
    * @param configuration the {@link SoundConfiguration} to persist.
    */
   SoundConfigurationPersistence( SoundConfiguration configuration, JenkinsDatabase database ) {
      this( new SoundConfigurationParseModel( configuration, database ), new SoundConfigurationWriteModel( configuration ) );
   }//End Constructor
   
   /**
    * Constructs a new {@link SoundConfigurationPersistence}.
    * @param parseModel the {@link SoundConfigurationParseModel} to use for parsing in to.
    * @param writeModel the {@link SoundConfigurationWriteModel} to use for writing from.
    */
   SoundConfigurationPersistence( SoundConfigurationParseModel parseModel, SoundConfigurationWriteModel writeModel ) {
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
    * Method to construct the {@link JsonStructure} for the {@link SoundConfiguration}.
    */
   private void constructStructure(){
      structure.child( SOUND, structure.root() );

      structure.array( STATUS_CHANGES, SOUND, writeModel::getNumberOfStatusChanges );
      structure.child( CHANGE, STATUS_CHANGES );
      structure.child( PREVIOUS_STATE, CHANGE );
      structure.child( CURRENT_STATE, CHANGE );
      structure.child( FILENAME, CHANGE );
      
      structure.array( JOB_EXCLUSIONS, SOUND, writeModel::getNumberOfJobExclusions );
      structure.child( EXCLUSION, JOB_EXCLUSIONS );
      structure.child( EXCLUSION_JOB_NAME, EXCLUSION );
   }//End Method
   
   /**
    * Method to construct the {@link JsonParser} for reading.
    */
   private void constructReadHandles(){
      parserWithReadHandles.when( STATUS_CHANGES, new StringParseHandle( new JsonArrayWithObjectParseHandler<>( 
               parseModel::startParsingChange, null, null, null ) 
      ) );
      
      parserWithReadHandles.when( PREVIOUS_STATE, new EnumParseHandle<>( 
               BuildResultStatus.class, parseModel::setPreviousState 
      ) );
      parserWithReadHandles.when( CURRENT_STATE, new EnumParseHandle<>( 
               BuildResultStatus.class, parseModel::setCurrentState 
      ) );
      parserWithReadHandles.when( FILENAME, new StringParseHandle( parseModel::setFilename ) );
      
      parserWithReadHandles.when( JOB_EXCLUSIONS, new StringParseHandle( new JsonArrayWithObjectParseHandler<>( 
               null, null, parseModel::startParsingExclusions, null ) 
      ) );
      parserWithReadHandles.when( EXCLUSION_JOB_NAME, new StringParseHandle( parseModel::setExclusion ) );
   }//End Method
   
   /**
    * Method to construct the {@link JsonParser} for writing.
    */
   private void constructuWriteHandles(){
      parserWithWriteHandles.when( STATUS_CHANGES, new JsonWriteHandleImpl( new JsonArrayWithObjectWriteHandler( 
               null, null, writeModel::startWritingChanges, null 
      ) ) );
      
      parserWithWriteHandles.when( PREVIOUS_STATE, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getPreviousState ) ) );
      parserWithWriteHandles.when( CURRENT_STATE, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getCurrentState ) ) );
      parserWithWriteHandles.when( FILENAME, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getFilename ) ) );
      
      parserWithWriteHandles.when( JOB_EXCLUSIONS, new JsonWriteHandleImpl( new JsonArrayWithObjectWriteHandler( 
               null, null, writeModel::startWritingExclusions, null 
      ) ) );
      parserWithWriteHandles.when( EXCLUSION_JOB_NAME, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getExclusion ) ) );
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
