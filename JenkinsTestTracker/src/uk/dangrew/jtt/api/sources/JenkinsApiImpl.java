/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.sources;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link JenkinsApiImpl} is responsible for connecting to Jenkins and logging in.
 */
public class JenkinsApiImpl implements ExternalApi {

   static final int LOGIN_TIMEOUT = 5000;
   
   private final JenkinsApiRequests requests;
   private final ClientHandler clientHandler;
   private final JenkinsApiDigest digest;
   
   private String jenkinsLocation;
   private HttpClient connectedClient;
   private HttpContext getContext;
   
   /**
    * Constructs a new {@link JenkinsApiImpl}.
    * @param clientHandler the {@link ClientHandler} used to handle interactions with jenkins.
    */
   public JenkinsApiImpl( ClientHandler clientHandler ) {
      this( clientHandler, new JenkinsApiDigest(), new JenkinsApiRequests() );
   }//End Constructor

   /**
    * Constructs a new {@link JenkinsApiImpl}.
    * @param clientHandler the {@link ClientHandler} used to handle interactions with jenkins.
    * @param digest the {@link JenkinsApiDigest} to use.
    * @param requests the {@link JenkinsApiRequests}.
    */
   JenkinsApiImpl( ClientHandler clientHandler, JenkinsApiDigest digest, JenkinsApiRequests requests ) {
      this.clientHandler = clientHandler;
      this.requests = requests;
      this.digest = digest;
      this.digest.attachSource( this );
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
      if ( connectedClient == null ) {
         return null;
      }
      
      this.jenkinsLocation = requests.prefixJenkinsLocation( jenkinsLocation );
      
      HttpGet get = requests.constructBaseRequest( jenkinsLocation );
      digest.executingLoginRequest();
      
      clientHandler.adjustClientTimeout( connectedClient.getParams(), LOGIN_TIMEOUT );
      String responseString = executeRequestAndUnpack( get );
      clientHandler.resetTimeout( connectedClient.getParams() );
      
      if ( responseString == null ) {
         digest.connectionFailed();
         connectedClient = null;
         return null;
      } else {
         digest.connectionSuccess();
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
         digest.handlingResponse();
         String responseString = clientHandler.handleResponse( response );
         digest.responseReady();
         return responseString;
      } catch ( HttpResponseException exception ) {
//         System.out.println( "Providing StackTrace for refusal, not necessarily a problem (HttpResponseException):" );
         if ( getRequest.getURI() != null ) System.out.println( "Attempted: " + getRequest.getURI().toString() );
//         exception.printStackTrace();
         digest.connectionException( exception );
      } catch ( ClientProtocolException exception ) {
//         System.out.println( "Providing StackTrace for refusal, not necessarily a problem (ClientProtocolException):" );
         if ( getRequest.getURI() != null ) System.out.println( "Attempted: " + getRequest.getURI().toString() );
         exception.printStackTrace();
         digest.connectionException( exception );
      } catch ( IOException exception ) {
//         System.out.println( "Providing StackTrace for refusal, not necessarily a problem (IOException):" );
         if ( getRequest.getURI() != null ) System.out.println( "Attempted: " + getRequest.getURI().toString() );
//         exception.printStackTrace();
         digest.connectionException( exception );
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
   @Override public String executeRequest( JobRequest request, JenkinsJob job ) {
      if ( !isLoggedIn() ) {
         return null;
      }
      
      HttpGet get = request.execute( jenkinsLocation, job );
      return executeRequestAndUnpack( get );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public String executeRequest( BuildRequest request, JenkinsJob job, int buildNumber ) {
      if ( !isLoggedIn() ) {
         return null;
      }
      
      HttpGet get = request.execute( jenkinsLocation, job, buildNumber );
      return executeRequestAndUnpack( get );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public String getJobsList() {
      if ( !isLoggedIn() ) {
         return null;
      }
      
      HttpGet get = requests.constructJobListRequest( jenkinsLocation );
      return executeRequestAndUnpack( get );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public String getUsersList() {
      if ( !isLoggedIn() ) {
         return null;
      }
      
      HttpGet get = requests.constructUserListRequest( jenkinsLocation );
      return executeRequestAndUnpack( get );
   }//End Method

}//End Class
