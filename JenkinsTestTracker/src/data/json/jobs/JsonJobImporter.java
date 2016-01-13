/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.jobs;

import org.json.JSONException;
import org.json.JSONObject;

import api.handling.BuildState;
import api.sources.ExternalApi;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;

/**
 * The {@link JsonJobImporter} provides a method of converting json responses 
 * from an {@link ExternalApi} into {@link JenkinsJob} updates.
 */
public class JsonJobImporter {
   
   private static final String BUILDING_KEY = "building";
   private static final String NUMBER_KEY = "number";
   private static final String RESULT_KEY = "result";

   /**
    * Default constructor.
    */
   public JsonJobImporter() {}

   /**
    * Method to update the state of the build in the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to update.
    * @param response the {@link String} response from the {@link ExternalApi}, in json format.
    */
   public void updateBuildState( JenkinsJob jenkinsJob, String response ) {
      if ( response == null ) return;
      try {
         JSONObject object = new JSONObject( response );
         if ( !object.has( BUILDING_KEY ) ) return;
         
         boolean isBuilding = object.getBoolean( BUILDING_KEY );
         if ( isBuilding ) {
            jenkinsJob.buildStateProperty().set( BuildState.Building );
         } else {
            jenkinsJob.buildStateProperty().set( BuildState.Built );
         }
      } catch ( JSONException exception ) {
         return;
      }
   }//End Method

   /**
    * Method to update the job details in the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} to update.
    * @param response the {@link String} response from the {@link ExternalApi}, in json format.
    */
   public void updateJobDetails( JenkinsJob jenkinsJob, String response ) {
      if ( response == null || jenkinsJob == null ) return;
      try {
         JSONObject object = new JSONObject( response );
         
         if ( !object.has( NUMBER_KEY ) ) return;
         if ( !object.has( RESULT_KEY ) ) return;
         
         int lastBuildNumber = object.getInt( NUMBER_KEY );
         BuildResultStatus lastBuildResult = object.getEnum( BuildResultStatus.class, RESULT_KEY );
         
         jenkinsJob.lastBuildNumberProperty().set( lastBuildNumber );
         jenkinsJob.lastBuildStatusProperty().set( lastBuildResult );
      } catch ( JSONException exception ) {
         System.out.println( exception.getMessage() );
         return;
      }
   }//End Method

}//End Class
