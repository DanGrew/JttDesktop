/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.persistence;

import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.BuildWallJobPolicy;
import uk.dangrew.jtt.buildwall.panel.type.JobPanelDescriptionProviders;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jupa.json.parse.JsonParser;
import uk.dangrew.jupa.json.parse.handle.key.JsonArrayWithObjectParseHandler;
import uk.dangrew.jupa.json.parse.handle.type.DoubleParseHandle;
import uk.dangrew.jupa.json.parse.handle.type.EnumParseHandle;
import uk.dangrew.jupa.json.parse.handle.type.IntegerParseHandle;
import uk.dangrew.jupa.json.parse.handle.type.StringParseHandle;
import uk.dangrew.jupa.json.structure.JsonStructure;
import uk.dangrew.jupa.json.write.handle.key.JsonArrayWithObjectWriteHandler;
import uk.dangrew.jupa.json.write.handle.key.JsonValueWriteHandler;
import uk.dangrew.jupa.json.write.handle.type.JsonWriteHandleImpl;

/**
 * The {@link BuildWallConfigurationPersistence} provides the {@link BuildWallConfiguration} 
 * {@link JsonParser}s and {@link JsonStructure} for persisting the configuration.
 */
class BuildWallConfigurationPersistence {
   
   static final String BUILD_WALL = "BuildWall";
   
   static final String DIMENSIONS = "Dimensions";
   static final String COLUMNS = "Columns";
   static final String DESCRIPTION_TYPE = "DescriptionType";
   
   static final String JOB_POLICIES = "JobPolicies";
   static final String JOB = "Job";
   static final String JOB_NAME = "JobName";
   static final String POLICY = "Policy";
   
   static final String FONTS = "Fonts";
   static final String JOB_NAME_FAMILY = "JobNameFamily";
   static final String JOB_NAME_SIZE = "JobNameSize";
   static final String BUILD_NUMBER_FAMILY = "BuildNumberFamily";
   static final String BUILD_NUMBER_SIZE = "BuildNumberSize";
   static final String COMPLETION_ESTIMATE_FAMILY = "CompletionEstimateFamily";
   static final String COMPLETION_ESTIMATE_SIZE = "CompletionEstimateSize";
   static final String DETAIL_FAMILY = "DetailFamily";
   static final String DETAIL_SIZE = "DetailSize";
   
   static final String COLOURS = "Colours";
   static final String JOB_NAME_COLOUR = "JobNameColour";
   static final String BUILD_NUMBER_COLOUR = "BuildNumberColour";
   static final String COMPLETION_ESTIMATE_COLOUR = "CompletionEstimateColour";
   static final String DETAIL_COLOUR = "DetailColour";
   
   private final JsonStructure structure;
   private final JsonParser parserWithReadHandles;
   private final BuildWallConfigurationModel parseModel;
   private final JsonParser parserWithWriteHandles;
   private final BuildWallConfigurationModel writeModel;
   
   /**
    * Constructs a new {@link BuildWallConfigurationPersistence}.
    * @param configuration the {@link BuildWallConfiguration} to persist.
    * @param database the {@link JenkinsDatabase} for accessing {@link uk.dangrew.jtt.model.jobs.JenkinsJob}s.
    */
   BuildWallConfigurationPersistence( BuildWallConfiguration configuration, JenkinsDatabase database ) {
      this( new BuildWallConfigurationModel( configuration, database ), new BuildWallConfigurationModel( configuration, database ) );
   }//End Constructor
   
   /**
    * Constructs a new {@link BuildWallConfigurationPersistence}.
    * @param parseModel the {@link BuildWallConfigurationModel} to use for parsing in to.
    * @param writeModel the {@link BuildWallConfigurationModel} to use for writing from.
    */
   BuildWallConfigurationPersistence( BuildWallConfigurationModel parseModel, BuildWallConfigurationModel writeModel ) {
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
    * Method to construct the {@link JsonStructure} for the {@link BuildWallConfiguration}.
    */
   private void constructStructure(){
      structure.child( BUILD_WALL, structure.root() );

      structure.child( DIMENSIONS, BUILD_WALL );
      structure.child( COLUMNS, DIMENSIONS );
      structure.child( DESCRIPTION_TYPE, DIMENSIONS );

      structure.array( JOB_POLICIES, BUILD_WALL, writeModel::getNumberOfJobs );
      structure.child( JOB, JOB_POLICIES );
      structure.child( JOB_NAME, JOB );
      structure.child( POLICY, JOB );
      
      structure.child( FONTS, BUILD_WALL );
      structure.child( JOB_NAME_FAMILY, FONTS );
      structure.child( JOB_NAME_SIZE, FONTS );
      structure.child( BUILD_NUMBER_FAMILY, FONTS );
      structure.child( BUILD_NUMBER_SIZE, FONTS );
      structure.child( COMPLETION_ESTIMATE_FAMILY, FONTS );
      structure.child( COMPLETION_ESTIMATE_SIZE, FONTS );
      structure.child( DETAIL_FAMILY, FONTS );
      structure.child( DETAIL_SIZE, FONTS );
      
      structure.child( COLOURS, BUILD_WALL );
      structure.child( JOB_NAME_COLOUR, COLOURS );
      structure.child( BUILD_NUMBER_COLOUR, COLOURS );
      structure.child( COMPLETION_ESTIMATE_COLOUR, COLOURS );
      structure.child( DETAIL_COLOUR, COLOURS );
   }//End Method
   
   /**
    * Method to construct the {@link JsonParser} for reading.
    */
   private void constructReadHandles(){
      parserWithReadHandles.when( COLUMNS, new IntegerParseHandle( parseModel::setColumns ) );
      parserWithReadHandles.when( 
               DESCRIPTION_TYPE, 
               new EnumParseHandle<>( JobPanelDescriptionProviders.class, parseModel::setDescriptionType ) 
      );
      
      parserWithReadHandles.when( JOB_POLICIES, new StringParseHandle( new JsonArrayWithObjectParseHandler<>( 
               null, null, parseModel::startParsingJobs, null ) 
      ) );
      
      parserWithReadHandles.when( JOB_NAME, new StringParseHandle( parseModel::setJobName ) );
      parserWithReadHandles.when( POLICY, new EnumParseHandle<>( 
               BuildWallJobPolicy.class, parseModel::setJobPolicy 
      ) );
      
      parserWithReadHandles.when( JOB_NAME_FAMILY, new StringParseHandle( parseModel::setJobNameFontFamily ) );
      parserWithReadHandles.when( JOB_NAME_SIZE, new DoubleParseHandle( parseModel::setJobNameFontSize ) );
      
      parserWithReadHandles.when( BUILD_NUMBER_FAMILY, new StringParseHandle( parseModel::setBuildNumberFontFamily ) );
      parserWithReadHandles.when( BUILD_NUMBER_SIZE, new DoubleParseHandle( parseModel::setBuildNumberFontSize ) );
      
      parserWithReadHandles.when( COMPLETION_ESTIMATE_FAMILY, new StringParseHandle( parseModel::setCompletionEstimateFontFamily ) );
      parserWithReadHandles.when( COMPLETION_ESTIMATE_SIZE, new DoubleParseHandle( parseModel::setCompletionEstimateFontSize ) );
      
      parserWithReadHandles.when( DETAIL_FAMILY, new StringParseHandle( parseModel::setDetailFontFamily ) );
      parserWithReadHandles.when( DETAIL_SIZE, new DoubleParseHandle( parseModel::setDetailFontSize ) );
      
      parserWithReadHandles.when( JOB_NAME_COLOUR, new StringParseHandle( parseModel::setJobNameFontColour ) );
      parserWithReadHandles.when( BUILD_NUMBER_COLOUR, new StringParseHandle( parseModel::setBuildNumberFontColour ) );
      parserWithReadHandles.when( COMPLETION_ESTIMATE_COLOUR, new StringParseHandle( parseModel::setCompletionEstimateFontColour ) );
      parserWithReadHandles.when( DETAIL_COLOUR, new StringParseHandle( parseModel::setDetailFontColour ) );
   }//End Method
   
   /**
    * Method to construct the {@link JsonParser} for writing.
    */
   private void constructuWriteHandles(){
      parserWithWriteHandles.when( COLUMNS, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getColumns ) ) );
      parserWithWriteHandles.when( DESCRIPTION_TYPE, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getDescriptionType ) ) );
      
      parserWithWriteHandles.when( JOB_POLICIES, new JsonWriteHandleImpl( new JsonArrayWithObjectWriteHandler( 
               null, null, writeModel::startWritingJobs, null 
      ) ) );
      
      parserWithWriteHandles.when( JOB_NAME, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getJobName ) ) );
      parserWithWriteHandles.when( POLICY, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getJobPolicy ) ) );
      
      parserWithWriteHandles.when( JOB_NAME_FAMILY, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getJobNameFontFamily ) ) );
      parserWithWriteHandles.when( JOB_NAME_SIZE, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getJobNameFontSize ) ) );
      
      parserWithWriteHandles.when( BUILD_NUMBER_FAMILY, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getBuildNumberFontFamily ) ) );
      parserWithWriteHandles.when( BUILD_NUMBER_SIZE, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getBuildNumberFontSize ) ) );
      
      parserWithWriteHandles.when( COMPLETION_ESTIMATE_FAMILY, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getCompletionEstimateFontFamily ) ) );
      parserWithWriteHandles.when( COMPLETION_ESTIMATE_SIZE, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getCompletionEstimateFontSize ) ) );
      
      parserWithWriteHandles.when( DETAIL_FAMILY, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getDetailFontFamily ) ) );
      parserWithWriteHandles.when( DETAIL_SIZE, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getDetailFontSize ) ) );
      
      parserWithWriteHandles.when( JOB_NAME_COLOUR, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getJobNameFontColour ) ) );
      parserWithWriteHandles.when( BUILD_NUMBER_COLOUR, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getBuildNumberFontColour ) ) );
      parserWithWriteHandles.when( COMPLETION_ESTIMATE_COLOUR, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getCompletionEstimateFontColour ) ) );
      parserWithWriteHandles.when( DETAIL_COLOUR, new JsonWriteHandleImpl( new JsonValueWriteHandler( writeModel::getDetailFontColour ) ) );
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
