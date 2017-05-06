/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.api.sources;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

/**
 * The {@link ClientHandler} handles the interaction with jenkins using apache http library.
 */
public class ClientHandler implements ResponseHandler< String > {

   static final int DEFAULT_TIMEOUT = 600000;
   private final BasicResponseHandler responseHandler;

   /**
    * Constructs a new {@link ClientHandler}.
    */
   public ClientHandler() {
      this( new BasicResponseHandler() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ClientHandler}.
    * @param handler the {@link BasicResponseHandler}.
    */
   ClientHandler( BasicResponseHandler handler ) {
      responseHandler = handler;
   }//End Constructor
   
   /**
    * Method to construct a reusable {@link HttpClient} for the given jenkins location and credentials.
    * @param jenkinsLocation the location of jenkins.
    * @param user the username.
    * @param password the password.
    * @return the {@link HttpClient}, note no credentials validation is performed.
    */
   public HttpClient constructClient( String jenkinsLocation, String user, String password ) {
      // Create your httpclient
      final HttpParams httpParams = new BasicHttpParams();
      resetTimeout( httpParams );
      DefaultHttpClient client = new DefaultHttpClient(httpParams);

      // Then provide the right credentials
      client.getCredentialsProvider().setCredentials( 
               new AuthScope( AuthScope.ANY_HOST, AuthScope.ANY_PORT ),
               new UsernamePasswordCredentials( user, password ) 
      );

      // Add as the first (because of the zero) request interceptor
      // It will first intercept the request and preemptively initialize the
      // authentication scheme if there is not
      client.addRequestInterceptor( new PreemptiveAuth(), 0 );
      return client;
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override public String handleResponse( HttpResponse response ) throws IOException {
      try {
         return responseHandler.handleResponse( response );
      } finally {
         if ( response.getEntity() != null ) {
            EntityUtils.consume( response.getEntity() );
            response.getEntity().getContent().close();
         }
      }
   }//End Method

   /**
    * Method to adjust the client timeout. This will change
    * {@link HttpConnectionParams#setSoTimeout(HttpParams, int)},
    * {@link HttpConnectionParams#setConnectionTimeout(HttpParams, int)},
    * {@link HttpConnectionParams#setLinger(HttpParams, int)}.
    * @param params the the {@link HttpParams} to adjust for.
    * @param timeout the timeout in milliseconds.
    */
   public void adjustClientTimeout( HttpParams params, int timeout ) {
      HttpConnectionParams.setSoTimeout( params, timeout );
      HttpConnectionParams.setConnectionTimeout( params, timeout );
      HttpConnectionParams.setLinger( params, timeout );
   }//End Method

   /**
    * Method to reset the timeouts to the {@link #DEFAULT_TIMEOUT}.
    * @see #adjustClientTimeout(HttpParams, int).
    * @param params the {@link HttpParams} to reset.
    */
   public void resetTimeout( HttpParams params ) {
      adjustClientTimeout( params, DEFAULT_TIMEOUT );
   }//End Method

   /**
    * Preemptive authentication interceptor.
    * This code was taken from an online example. 
    */
   static class PreemptiveAuth implements HttpRequestInterceptor {

      @Override public void process( HttpRequest request, HttpContext context ) throws HttpException, IOException {
         // Get the AuthState
         AuthState authState = ( AuthState ) context.getAttribute( ClientContext.TARGET_AUTH_STATE );

         // If no auth scheme available yet, try to initialize it preemptively
         if ( authState.getAuthScheme() == null ) {
            AuthScheme authScheme = ( AuthScheme )context.getAttribute( "preemptive-auth" );
            CredentialsProvider credsProvider = ( CredentialsProvider ) context.getAttribute( ClientContext.CREDS_PROVIDER );
            HttpHost targetHost = ( HttpHost ) context.getAttribute( ExecutionContext.HTTP_TARGET_HOST );
            if ( authScheme != null ) {
               Credentials creds = credsProvider.getCredentials( new AuthScope( targetHost.getHostName(), targetHost.getPort() ) );
               if ( creds == null ) {
                  throw new HttpException( "No credentials for preemptive authentication" );
               }
               authState.setAuthScheme( authScheme );
               authState.setCredentials( creds );
            }
         }
      }
   }

}//End Class
