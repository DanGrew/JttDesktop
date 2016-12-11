/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling.live;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jupa.json.parse.JsonParser;
import uk.dangrew.jupa.json.parse.handle.key.JsonArrayWithObjectParseHandler;
import uk.dangrew.jupa.json.parse.handle.type.BooleanParseHandle;
import uk.dangrew.jupa.json.parse.handle.type.EnumParseHandle;
import uk.dangrew.jupa.json.parse.handle.type.IntegerParseHandle;
import uk.dangrew.jupa.json.parse.handle.type.LongParseHandle;
import uk.dangrew.jupa.json.parse.handle.type.StringParseHandle;

/**
 * The {@link JobDetailsParser} provides a {@link JsonParser} for parsing the jenkins
 * response containing {@link uk.dangrew.jtt.model.jobs.JenkinsJob}s and their details.
 */
public class JobDetailsParser extends JsonParser {
   
   private static final String ARRAY_JOBS = "jobs";
   private static final String KEY_NAME = "name";
   
   private static final String KEY_FAIL_COUNT = "failCount";
   private static final String KEY_SKIP_COUNT = "skipCount";
   private static final String KEY_TOTAL_COUNT = "totalCount";
   
   private static final String KEY_BUILDING = "building";
   private static final String KEY_DURATION = "duration";
   private static final String KEY_ESTIAMTED_DURATION = "estimatedDuration";
   private static final String KEY_NUMBER = "number";
   private static final String KEY_RESULT = "result";
   private static final String KEY_TIMESTAMP = "timestamp";
   private static final String KEY_BUILT_ON = "builtOn";
   
   private static final String KEY_FULL_NAME = "fullName";
   
   /**
    * Constructs a new {@link JobDetailsParser}.
    * @param model the {@link JobDetailsModel} associated.
    */
   public JobDetailsParser( JobDetailsModel model ) {
      when( ARRAY_JOBS, new StringParseHandle( new JsonArrayWithObjectParseHandler<>( 
               model::startJob, model::finishJob, null, null 
      ) ) );
      
      when( KEY_NAME, new StringParseHandle( model::setJobName ) );
      when( KEY_BUILDING, new BooleanParseHandle( model::setBuildingState ) );
      when( KEY_BUILT_ON, new StringParseHandle( model::setBuiltOn ) );
      when( KEY_DURATION, new LongParseHandle( model::setDuration ) );
      when( KEY_ESTIAMTED_DURATION, new LongParseHandle( model::setEstimatedDuration ) );
      when( KEY_FAIL_COUNT, new IntegerParseHandle( model::setFailCount ) );
      when( KEY_FULL_NAME, new StringParseHandle( model::addCulprit ) );
      when( KEY_NUMBER, new IntegerParseHandle( model::setBuildNumber ) );
      when( KEY_RESULT, new EnumParseHandle<>( BuildResultStatus.class, model::setResultingState ) );
      when( KEY_SKIP_COUNT, new IntegerParseHandle( model::setSkipCount ) );
      when( KEY_TIMESTAMP, new LongParseHandle( model::setTimestamp ) );
      when( KEY_TOTAL_COUNT, new IntegerParseHandle( model::setTotalTestCount ) );
   }//End Constructor
   
}//End Class
