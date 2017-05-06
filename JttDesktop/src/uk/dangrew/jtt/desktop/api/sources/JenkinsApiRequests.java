/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.api.sources;

import org.apache.http.client.methods.HttpGet;

import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JenkinsApiRequests} defines the strings and values for the requests that can be
 * made to the Jenkins instance api.
 */
class JenkinsApiRequests {
   
   static final String LOCATION_PREFIX = "http://";
   static final String BASE_REQUEST = "/api/json?tree=jobs[name]&pretty=true";
   static final String LAST_BUILD_BUILDING = "/lastBuild/api/json?tree=building,estimatedDuration,timestamp,number,builtOn";
   static final String LIST_OF_BUILDS = "/api/json?tree=builds[number]";
   static final String LAST_BUILD_DETAILS = "/lastCompletedBuild/api/json?tree=number,result,culprits[fullName]";
   
   static final String BUILD_DETAILS = "[actions[failCount,skipCount,totalCount],building,duration,estimatedDuration,"
            + "result,number,timestamp,builtOn,culprits[fullName]]]";
   static final String CURRENT_JOB_DETAILS       = "/api/json?tree=jobs[name,lastCompletedBuild[result],lastBuild" + BUILD_DETAILS;
   static final String LAST_COMPLETE_JOB_DETAILS = "/api/json?tree=jobs[name,lastCompletedBuild" + BUILD_DETAILS;
   
   static final String TEST_CASES_PROPERTIES = "duration,name,className,failedSince,skipped,status,age";
   static final String TEST_CLASS_PROPERTIES = "duration,name,cases[" + TEST_CASES_PROPERTIES +"]";
   static final String TEST_SUITES = "suites[" + TEST_CLASS_PROPERTIES + "]";
   static final String TEST_RESULTS_WRAPPING_ELEMENTS = "childReports[result[" + TEST_SUITES + "]]";
   static final String TEST_RESULTS_PATH = "/lastCompletedBuild/testReport/api/json?pretty=true&tree=";
   static final String LAST_BUILD_TEST_RESULTS_WRAPPED = TEST_RESULTS_PATH + TEST_RESULTS_WRAPPING_ELEMENTS;
   static final String LAST_BUILD_TEST_RESULTS_UNWRAPPED = TEST_RESULTS_PATH + TEST_SUITES;
   
   static final String JOBS_LIST = "/api/json?tree=jobs[name]&pretty=true";
   static final String JOB = "/job/";
   
   static final String USERS_LIST = "/asynchPeople/api/json?pretty=true&tree=users[user[fullName]]";
   
   private static final JenkinsApiRequests INSTANCE = new JenkinsApiRequests();
   
   /**
    * Gets the singleton instance.
    * @return the {@link JenkinsApiRequests}.
    */
   public static JenkinsApiRequests get(){
      return INSTANCE;
   }//End Method
   
   /**
    * Method to prefix the given jenkins location, if needed, with http://.
    * @param jenkinsLocation the location to prefix.
    * @return the location if already prefixed, or with the prefix.
    */
   public String prefixJenkinsLocation( String jenkinsLocation ) {
      if ( jenkinsLocation.startsWith( LOCATION_PREFIX ) ) {
         return jenkinsLocation;
      } else {
         return LOCATION_PREFIX + jenkinsLocation;
      }
   }//End Method
   
   /**
    * Method to extract the part of the path to the given {@link JenkinsJob}.
    * @param jenkinsJob the {@link JenkinsJob} in question.
    * @return the path to the job.
    */
   String extractAndPrefixJob( JenkinsJob jenkinsJob ) {
      return JOB + jenkinsJob.nameProperty().get().replaceAll( " ", "%20" );
   }//End Method
   
   /**
    * Method to wrap the given item in forward slashes.
    * @param item the item to wrap.
    * @return the wrapped item.
    */
   String wrap( String item ) {
      return "/" + item + "/";
   }//End Method

   /**
    * Method to construct the base request for establishing a connection.
    * @param jenkinsLocation the location, can be missing prefix.
    * @return the {@link HttpGet} to execute.
    */
   HttpGet constructBaseRequest( String jenkinsLocation ) {
      return new HttpGet( prefixJenkinsLocation( jenkinsLocation ) + BASE_REQUEST );
   }//End Method

   /**
    * Method to construct the request for getting all jobs from jenkins.
    * @param jenkinsLocation the location.
    * @return the {@link HttpGet} to execute.
    */
   HttpGet constructJobListRequest( String jenkinsLocation ) {
      return new HttpGet( jenkinsLocation + JOBS_LIST );
   }//End Method
   
   /**
    * Method to construct the request for getting all users from jenkins.
    * @param jenkinsLocation the location.
    * @return the {@link HttpGet} to execute.
    */
   HttpGet constructUserListRequest( String jenkinsLocation ) {
      return new HttpGet( jenkinsLocation + USERS_LIST );
   }//End Method
   
   /**
    * Method to construct the request for getting the last build building state.
    * @param jenkinsLocation the location.
    * @param jenkinsJob the {@link JenkinsJob} in question.
    * @return the {@link HttpGet} to execute.
    */
   HttpGet constructLastBuildBuildingStateRequest( String jenkinsLocation, JenkinsJob jenkinsJob ) {
      return new HttpGet( jenkinsLocation + extractAndPrefixJob( jenkinsJob ) + LAST_BUILD_BUILDING );
   }//End Method

   /**
    * Method to construct the request for getting the last build details.
    * @param jenkinsLocation the location.
    * @param jenkinsJob the {@link JenkinsJob} in question.
    * @return the {@link HttpGet} to execute.
    */
   HttpGet constructLastBuildJobDetailsRequest( String jenkinsLocation, JenkinsJob jenkinsJob ) {
      return new HttpGet( jenkinsLocation + extractAndPrefixJob( jenkinsJob ) + LAST_BUILD_DETAILS );
   }//End Method
   
   /**
    * Method to construct the request for getting the list of builds associated with a {@link JenkinsJob}.
    * @param jenkinsLocation the location.
    * @param jenkinsJob the {@link JenkinsJob} in question.
    * @return the {@link HttpGet} to execute.
    */
   HttpGet constructListOfBuildsRequest( String jenkinsLocation, JenkinsJob jenkinsJob ) {
      return new HttpGet( jenkinsLocation + extractAndPrefixJob( jenkinsJob ) + LIST_OF_BUILDS );
   }//End Method
   
   /**
    * Method to construct the request for getting details of all builds in jenkins and their
    * current state.
    * @param jenkinsLocation the location.
    * @return the {@link HttpGet} to execute.
    */
   HttpGet constructCurrentJobDetailsRequest( String jenkinsLocation ) {
      return new HttpGet( jenkinsLocation + CURRENT_JOB_DETAILS );
   }//End Method
   
   /**
    * Method to construct the request for getting details of all builds in jenkins and the
    * state of their last completed build.
    * @param jenkinsLocation the location.
    * @return the {@link HttpGet} to execute.
    */
   HttpGet constructLastCompleteJobDetailsRequest( String jenkinsLocation ) {
      return new HttpGet( jenkinsLocation + LAST_COMPLETE_JOB_DETAILS );
   }//End Method

   /**
    * Method to construct the request for getting the last build test results using the wrapped api request.
    * @param jenkinsLocation the location.
    * @param jenkinsJob the {@link JenkinsJob} in question.
    * @return the {@link HttpGet} to execute.
    */
   public HttpGet constructLastBuildTestResultsWrappedRequest( String jenkinsLocation, JenkinsJob jenkinsJob ) {
      return new HttpGet( jenkinsLocation + extractAndPrefixJob( jenkinsJob ) + LAST_BUILD_TEST_RESULTS_WRAPPED );
   }//End Method
   
   /**
    * Method to construct the request for getting the last build test results using the unwrapped api request.
    * @param jenkinsLocation the location.
    * @param jenkinsJob the {@link JenkinsJob} in question.
    * @return the {@link HttpGet} to execute.
    */
   public HttpGet constructLastBuildTestResultsUnwrappedRequest( String jenkinsLocation, JenkinsJob jenkinsJob ) {
      return new HttpGet( jenkinsLocation + extractAndPrefixJob( jenkinsJob ) + LAST_BUILD_TEST_RESULTS_UNWRAPPED );
   }//End Method

}//End Class
