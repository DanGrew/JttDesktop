/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.sources;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import model.jobs.JenkinsJob;

/**
 * The {@link JenkinsApiImpl} is responsible for connecting to Jenkins and logging in.
 */
public class JenkinsApiImpl implements ExternalApi {

   static final String LOCATION_PREFIX = "http://";
   static final String BASE_REQUEST = "/api/json?tree=jobs[name]&pretty=true";
   static final String LAST_BUILD_BUILDING = "/lastBuild/api/json?tree=building";
   static final String LAST_BUILD_DETAILS = "/lastBuild/api/json?tree=number,result";
   static final String LAST_BUILD_TEST_RESULTS = "/lastBuild/testReport/api/json?pretty=true";
   static final String JOBS_LIST = "/api/json?tree=jobs[name]&pretty=true";
   static final String JOB = "/job/";
   
   private final ClientHandler clientHandler;
   private String jenkinsLocation;
   private HttpClient connectedClient;
   private HttpContext getContext;

   /**
    * Constructs a new {@link JenkinsApiImpl}.
    * @param clientHandler the {@link ClientHandler} used to handle interactions with jenkins.
    */
   public JenkinsApiImpl( ClientHandler clientHandler ) {
      this.clientHandler = clientHandler;
      // Generate BASIC scheme object and stick it to the execution context
      BasicScheme basicAuth = new BasicScheme();
      getContext = new BasicHttpContext();
      getContext.setAttribute( "preemptive-auth", basicAuth );
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public HttpClient attemptLogin( String jenkinsLocation, String user, String password ) {
      connectedClient = clientHandler.constructClient( jenkinsLocation, user, password );
      if ( connectedClient == null ) return null;
      this.jenkinsLocation = prefixJenkinsLocation( jenkinsLocation );
      
      HttpGet get = constructBaseRequest( jenkinsLocation );
      String responseString = executeRequestAndUnpack( get );
      if ( responseString == null ) {
         connectedClient = null;
         return null;
      } else {
         return connectedClient;
      }
   }//End Method
   
   /**
    * Convenience method to handle the execution of {@link HttpGet}s and unpack the responses
    * to a {@link String}.
    * @param getRequest the {@link HttpGet} to execute.
    * @return the unpacked response, or null.
    */
   String executeRequestAndUnpack( HttpGet getRequest ){
      try {
         HttpResponse response = connectedClient.execute( getRequest, getContext );
         String responseString = clientHandler.handleResponse( response );
         return responseString;
      } catch ( HttpResponseException exception ) {
         System.out.println( "Providing StackTrace for refusal, not necessarily a problem (HttpResponseException):" );
         exception.printStackTrace();
      } catch ( ClientProtocolException exception ) {
         System.out.println( "Providing StackTrace for refusal, not necessarily a problem (ClientProtocolException):" );
         exception.printStackTrace();
      } catch ( IOException exception ) {
         System.out.println( "Providing StackTrace for refusal, not necessarily a problem (IOException):" );
         exception.printStackTrace();
      }
      return null;
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isLoggedIn() {
      return connectedClient != null;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public String getLastBuildBuildingState( JenkinsJob jenkinsJob ) {
      if ( !isLoggedIn() ) return null;
      HttpGet get = constructLastBuildBuildingStateRequest( jenkinsLocation, jenkinsJob );
      return executeRequestAndUnpack( get );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public String getLastBuildJobDetails( JenkinsJob jenkinsJob ) {
      if ( !isLoggedIn() ) return null;
      HttpGet get = constructLastBuildJobDetailsRequest( jenkinsLocation, jenkinsJob );
      return executeRequestAndUnpack( get );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public String getJobsList() {
      if ( !isLoggedIn() ) return null;
      HttpGet get = constructJobListRequest( jenkinsLocation );
      return executeRequestAndUnpack( get );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public String getLatestTestResults( JenkinsJob jenkinsJob ) {
      if ( !isLoggedIn() ) return null;
      HttpGet get = constructLastBuildTestResultsRequest( jenkinsLocation, jenkinsJob );
      return executeRequestAndUnpack( get );
   }//End Method
   
   /**
    * Method to prefix the given jenkins location, if needed, with http://.
    * @param jenkinsLocation the location to prefix.
    * @return the location if already prefixed, or with the prefix.
    */
   static String prefixJenkinsLocation( String jenkinsLocation ) {
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
   static String extractAndPrefixJob( JenkinsJob jenkinsJob ) {
      return JOB + jenkinsJob.nameProperty().get();
   }//End Method

   /**
    * Method to construct the base request for establishing a connection.
    * @param jenkinsLocation the location, can be missing prefix.
    * @return the {@link HttpGet} to execute.
    */
   static HttpGet constructBaseRequest( String jenkinsLocation ) {
      return new HttpGet( prefixJenkinsLocation( jenkinsLocation ) + BASE_REQUEST );
   }//End Method

   /**
    * Method to construct the request for getting the last build building state.
    * @param jenkinsLocation the location.
    * @param jenkinsJob the {@link JenkinsJob} in question.
    * @return the {@link HttpGet} to execute.
    */
   static HttpGet constructLastBuildBuildingStateRequest( String jenkinsLocation, JenkinsJob jenkinsJob ) {
      return new HttpGet( jenkinsLocation + extractAndPrefixJob( jenkinsJob ) + LAST_BUILD_BUILDING );
   }//End Method

   /**
    * Method to construct the request for getting all jobs from jenkins.
    * @param jenkinsLocation the location.
    * @return the {@link HttpGet} to execute.
    */
   static HttpGet constructJobListRequest( String jenkinsLocation ) {
      return new HttpGet( jenkinsLocation + JOBS_LIST );
   }//End Method

   /**
    * Method to construct the request for getting the last build details.
    * @param jenkinsLocation the location.
    * @param jenkinsJob the {@link JenkinsJob} in question.
    * @return the {@link HttpGet} to execute.
    */
   static HttpGet constructLastBuildJobDetailsRequest( String jenkinsLocation, JenkinsJob jenkinsJob ) {
      return new HttpGet( jenkinsLocation + extractAndPrefixJob( jenkinsJob ) + LAST_BUILD_DETAILS );
   }//End Method

   /**
    * Method to construct the request for getting the last build test results.
    * @param jenkinsLocation the location.
    * @param jenkinsJob the {@link JenkinsJob} in question.
    * @return the {@link HttpGet} to execute.
    */
   public static HttpGet constructLastBuildTestResultsRequest( String jenkinsLocation, JenkinsJob jenkinsJob ) {
      return new HttpGet( jenkinsLocation + extractAndPrefixJob( jenkinsJob ) + LAST_BUILD_TEST_RESULTS );
   }//End Method

}//End Class
