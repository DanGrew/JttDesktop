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
import uk.dangrew.jupa.json.write.JsonStructure;
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
   static final String BUILD_TIME_FAMILY = "BuildTimeFamily";
   static final String BUILD_TIME_SIZE = "BuildTimeSize";
   static final String DETAIL_FAMILY = "DetailFamily";
   static final String DETAIL_SIZE = "DetailSize";
   
   static final String COLOURS = "Colours";
   static final String JOB_NAME_COLOUR = "JobNameColour";
   static final String BUILD_NUMBER_COLOUR = "BuildNumberColour";
   static final String BUILD_TIME_COLOUR = "BuildTimeColour";
   static final String DETAIL_COLOUR = "DetailColour";
   
   private final BuildWallConfigurationModel model;
   private final JsonStructure structure;
   private final JsonParser parserWithReadHandles;
   private final JsonParser parserWithWriteHandles;
   
   /**
    * Constructs a new {@link BuildWallConfigurationPersistence}.
    * @param configuration the {@link BuildWallConfiguration} to persist.
    * @param database the {@link JenkinsDatabase} for accessing {@link uk.dangrew.jtt.model.jobs.JenkinsJob}s.
    */
   BuildWallConfigurationPersistence( BuildWallConfiguration configuration, JenkinsDatabase database ) {
      this( new BuildWallConfigurationModel( configuration, database ) );
   }//End Constructor
   
   /**
    * Constructs a new {@link BuildWallConfigurationPersistence}.
    * @param model the {@link BuildWallConfigurationModel} to use for {@link JsonParser}s and the {@link JsonStructure}.
    */
   BuildWallConfigurationPersistence( BuildWallConfigurationModel model ) {
      this.model = model;
      this.structure = new JsonStructure();
      this.parserWithReadHandles = new JsonParser();
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

      structure.array( JOB_POLICIES, BUILD_WALL, model::getNumberOfJobs );
      structure.child( JOB, JOB_POLICIES );
      structure.child( JOB_NAME, JOB );
      structure.child( POLICY, JOB );
      
      structure.child( FONTS, BUILD_WALL );
      structure.child( JOB_NAME_FAMILY, FONTS );
      structure.child( JOB_NAME_SIZE, FONTS );
      structure.child( BUILD_NUMBER_FAMILY, FONTS );
      structure.child( BUILD_NUMBER_SIZE, FONTS );
      structure.child( BUILD_TIME_FAMILY, FONTS );
      structure.child( BUILD_TIME_SIZE, FONTS );
      structure.child( DETAIL_FAMILY, FONTS );
      structure.child( DETAIL_SIZE, FONTS );
      
      structure.child( COLOURS, BUILD_WALL );
      structure.child( JOB_NAME_COLOUR, COLOURS );
      structure.child( BUILD_NUMBER_COLOUR, COLOURS );
      structure.child( BUILD_TIME_COLOUR, COLOURS );
      structure.child( DETAIL_COLOUR, COLOURS );
   }//End Method
   
   /**
    * Method to construct the {@link JsonParser} for reading.
    */
   private void constructReadHandles(){
      parserWithReadHandles.when( COLUMNS, new IntegerParseHandle( model::setColumns ) );
      parserWithReadHandles.when( 
               DESCRIPTION_TYPE, 
               new EnumParseHandle<>( JobPanelDescriptionProviders.class, model::setDescriptionType ) 
      );
      
      parserWithReadHandles.when( JOB_POLICIES, new StringParseHandle( new JsonArrayWithObjectParseHandler<>( 
               null, null, model::startParsingJobs, null ) 
      ) );
      
      parserWithReadHandles.when( JOB_NAME, new StringParseHandle( model::setJobName ) );
      parserWithReadHandles.when( POLICY, new EnumParseHandle<>( 
               BuildWallJobPolicy.class, model::setJobPolicy 
      ) );
      
      parserWithReadHandles.when( JOB_NAME_FAMILY, new StringParseHandle( model::setJobNameFontFamily ) );
      parserWithReadHandles.when( JOB_NAME_SIZE, new DoubleParseHandle( model::setJobNameFontSize ) );
      
      parserWithReadHandles.when( BUILD_NUMBER_FAMILY, new StringParseHandle( model::setBuildNumberFontFamily ) );
      parserWithReadHandles.when( BUILD_NUMBER_SIZE, new DoubleParseHandle( model::setBuildNumberFontSize ) );
      
      parserWithReadHandles.when( BUILD_TIME_FAMILY, new StringParseHandle( model::setCompletionEstimateFontFamily ) );
      parserWithReadHandles.when( BUILD_TIME_SIZE, new DoubleParseHandle( model::setCompletionEstimateFontSize ) );
      
      parserWithReadHandles.when( DETAIL_FAMILY, new StringParseHandle( model::setDetailFontFamily ) );
      parserWithReadHandles.when( DETAIL_SIZE, new DoubleParseHandle( model::setDetailFontSize ) );
      
      parserWithReadHandles.when( JOB_NAME_COLOUR, new StringParseHandle( model::setJobNameFontColour ) );
      parserWithReadHandles.when( BUILD_NUMBER_COLOUR, new StringParseHandle( model::setBuildNumberFontColour ) );
      parserWithReadHandles.when( BUILD_TIME_COLOUR, new StringParseHandle( model::setCompletionEstimateFontColour ) );
      parserWithReadHandles.when( DETAIL_COLOUR, new StringParseHandle( model::setDetailFontColour ) );
   }//End Method
   
   /**
    * Method to construct the {@link JsonParser} for writing.
    */
   private void constructuWriteHandles(){
      parserWithWriteHandles.when( COLUMNS, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getColumns ) ) );
      parserWithWriteHandles.when( DESCRIPTION_TYPE, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getDescriptionType ) ) );
      
      parserWithWriteHandles.when( JOB_POLICIES, new JsonWriteHandleImpl( new JsonArrayWithObjectWriteHandler( 
               null, null, model::startWritingJobs, null 
      ) ) );
      
      parserWithWriteHandles.when( JOB_NAME, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getJobName ) ) );
      parserWithWriteHandles.when( POLICY, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getJobPolicy ) ) );
      
      parserWithWriteHandles.when( JOB_NAME_FAMILY, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getJobNameFontFamily ) ) );
      parserWithWriteHandles.when( JOB_NAME_SIZE, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getJobNameFontSize ) ) );
      
      parserWithWriteHandles.when( BUILD_NUMBER_FAMILY, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getBuildNumberFontFamily ) ) );
      parserWithWriteHandles.when( BUILD_NUMBER_SIZE, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getBuildNumberFontSize ) ) );
      
      parserWithWriteHandles.when( BUILD_TIME_FAMILY, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getCompletionEstimateFontFamily ) ) );
      parserWithWriteHandles.when( BUILD_TIME_SIZE, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getCompletionEstimateFontSize ) ) );
      
      parserWithWriteHandles.when( DETAIL_FAMILY, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getDetailFontFamily ) ) );
      parserWithWriteHandles.when( DETAIL_SIZE, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getDetailFontSize ) ) );
      
      parserWithWriteHandles.when( JOB_NAME_COLOUR, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getJobNameFontColour ) ) );
      parserWithWriteHandles.when( BUILD_NUMBER_COLOUR, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getBuildNumberFontColour ) ) );
      parserWithWriteHandles.when( BUILD_TIME_COLOUR, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getCompletionEstimateFontColour ) ) );
      parserWithWriteHandles.when( DETAIL_COLOUR, new JsonWriteHandleImpl( new JsonValueWriteHandler( model::getDetailFontColour ) ) );
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
