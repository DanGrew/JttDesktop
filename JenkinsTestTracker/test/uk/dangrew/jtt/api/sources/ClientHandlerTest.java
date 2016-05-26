/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.sources;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import uk.dangrew.jtt.api.sources.ClientHandler;
import uk.dangrew.jtt.api.sources.ClientHandler.PreemptiveAuth;

/**
 * {@link ClientHandler} test.
 */
public class ClientHandlerTest {

   private BasicResponseHandler handler;
   private ClientHandler systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      handler = Mockito.mock( BasicResponseHandler.class );
      systemUnderTest = new ClientHandler( handler );
   }//End Method
   
   @Test public void shouldProduceClient() {
      Assert.assertNotNull( systemUnderTest.constructClient( "anything", "anything", "anything" ) );
   }//End Method
   
   @Test public void shouldConfigureAuthentication() {
      final String location = "anywhere";
      final String username = "username";
      final String password = "password";
      HttpClient client = systemUnderTest.constructClient( location, username, password );
      
      Assert.assertTrue( client instanceof DefaultHttpClient );
      DefaultHttpClient defaultClient = ( DefaultHttpClient )client;
      Credentials credentials = defaultClient.getCredentialsProvider().getCredentials( 
               new AuthScope( AuthScope.ANY_HOST, AuthScope.ANY_PORT )
      );
      Assert.assertNotNull( credentials );
      
      Assert.assertEquals( username, credentials.getUserPrincipal().getName() );
      Assert.assertEquals( password, credentials.getPassword() );
   }//End Method
   
   @Test public void shouldUserPreemptiveAuthentication(){
      HttpClient client = systemUnderTest.constructClient( "anything", "anything", "anything" );
      Assert.assertTrue( client instanceof DefaultHttpClient );
      DefaultHttpClient defaultClient = ( DefaultHttpClient )client;
      Assert.assertTrue( defaultClient.getRequestInterceptor( 0 ) instanceof PreemptiveAuth );
   }//End Method
   
   @Test public void shouldHandleResponse() throws ClientProtocolException, IOException{
      HttpResponse response = Mockito.mock( HttpResponse.class );
      systemUnderTest.handleResponse( response );
      Mockito.verify( handler ).handleResponse( response );
   }//End Method
   
   @Test public void shouldCloseResponse() throws IllegalStateException, IOException{
      HttpEntity entity = Mockito.mock( HttpEntity.class );
      InputStream stream = Mockito.mock( InputStream.class );
      Mockito.when( entity.getContent() ).thenReturn( stream );
      
      HttpResponse response = Mockito.mock( HttpResponse.class );
      Mockito.when( response.getEntity() ).thenReturn( entity );
      
      systemUnderTest.handleResponse( response );
      Mockito.verify( stream ).close();
   }//End Method
   
   @Test public void shouldHandleResponseWithNoContent() throws IllegalStateException, IOException{
      HttpResponse response = Mockito.mock( HttpResponse.class );
      systemUnderTest.handleResponse( response );
   }//End Method
   
   @Test public void adjustClientTimeoutShouldChangeTheTimeout(){
      HttpClient client = systemUnderTest.constructClient( "anything", "else", "specific" ); 
      
      assertThat( HttpConnectionParams.getSoTimeout( client.getParams() ), is( ClientHandler.DEFAULT_TIMEOUT ) );
      assertThat( HttpConnectionParams.getConnectionTimeout( client.getParams() ), is( ClientHandler.DEFAULT_TIMEOUT ) );
      assertThat( HttpConnectionParams.getLinger( client.getParams() ), is( ClientHandler.DEFAULT_TIMEOUT ) );
      final int timeout = 123847;
      systemUnderTest.adjustClientTimeout( client.getParams(), timeout );
      assertThat( HttpConnectionParams.getSoTimeout( client.getParams() ), is( timeout ) );
      assertThat( HttpConnectionParams.getConnectionTimeout( client.getParams() ), is( timeout ) );
      assertThat( HttpConnectionParams.getLinger( client.getParams() ), is( timeout ) );
      
      systemUnderTest.resetTimeout( client.getParams() );
      assertThat( HttpConnectionParams.getSoTimeout( client.getParams() ), is( ClientHandler.DEFAULT_TIMEOUT ) );
      assertThat( HttpConnectionParams.getConnectionTimeout( client.getParams() ), is( ClientHandler.DEFAULT_TIMEOUT ) );
      assertThat( HttpConnectionParams.getLinger( client.getParams() ), is( ClientHandler.DEFAULT_TIMEOUT ) );  
   }//End Method
   
}//End Class
