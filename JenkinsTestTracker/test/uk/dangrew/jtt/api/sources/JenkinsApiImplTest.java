/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.sources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

/**
 * {@link JenkinsApiImpl} test.
 */
public class JenkinsApiImplTest {
   
   private static final String JENKINS_LOCATION = "any-location";
   private static final String USERNAME = "any user";
   private static final String PASSWORD = "any password";
   private static final String EXPECTED_RESPONSE = "anythingNotNull";
   
   @Mock private JenkinsApiDigest digest;
   private ObjectProperty< Object > request;
   @Mock private ClientHandler clientHandler;
   @Mock private HttpClient client;
   private JenkinsJob jenkinsJob;
   
   @Spy private JenkinsApiRequests requests;
   private JenkinsApiImpl systemUnderTest;
   
   @Before public void initialiseSystemUnderTest() throws ClientProtocolException, IOException{
      MockitoAnnotations.initMocks( this );
      requests = new JenkinsApiRequests();
      systemUnderTest = new JenkinsApiImpl( clientHandler, digest, requests );
      jenkinsJob = new JenkinsJobImpl( "SomeJenkinsProject" );
      
      request = new SimpleObjectProperty< Object >();
      when( client.execute( Mockito.< HttpGet >any(), Mockito.< BasicHttpContext >any() ) ).thenAnswer( 
               invocation -> {
                  request.set( invocation.getArguments()[ 0 ] );
                  return Mockito.mock( HttpResponse.class );
               } 
      );
      
      when( clientHandler.constructClient( JENKINS_LOCATION, USERNAME, PASSWORD ) ).thenReturn( client );
      when( clientHandler.handleResponse( Mockito.any() ) ).thenReturn( EXPECTED_RESPONSE );
      
      verify( digest ).attachSource( systemUnderTest );
   }//End Method

   @Test public void shouldAttemptLogin() throws ClientProtocolException, IOException {
      HttpClient actualClient = systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      assertEquals( client, actualClient );
      
      verify( clientHandler ).constructClient( JENKINS_LOCATION, USERNAME, PASSWORD );
      assertNotNull( request.get() );
      assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      assertEquals( requests.constructBaseRequest( JENKINS_LOCATION ).getURI().toString(), get.getURI().toString() );
      
      verify( digest ).executingLoginRequest();
      verify( digest ).connectionSuccess();
   }//End Method
   
   @Test public void shouldNotAttemptLoginWhenClientIsNull() throws ClientProtocolException, IOException {
      Mockito.when( clientHandler.constructClient( JENKINS_LOCATION, USERNAME, PASSWORD ) ).thenReturn( null );
      Assert.assertNull( systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD ) );
      
      Mockito.verify( clientHandler ).constructClient( JENKINS_LOCATION, USERNAME, PASSWORD );
      Mockito.verifyNoMoreInteractions( client, clientHandler );
      
      verify( digest, times( 0 ) ).executingLoginRequest();
   }//End Method
   
   @Test public void shouldResetClientConnectionAndHandleNullResponse() throws ClientProtocolException, IOException{
      Mockito.when( clientHandler.handleResponse( Mockito.any() ) ).thenReturn( null );
      Assert.assertNull( systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD ) );
      Assert.assertFalse( systemUnderTest.isLoggedIn() );
      
      verify( digest ).executingLoginRequest();
      verify( digest ).connectionFailed();
   }//End Method
   
   @Test public void shouldNotBeLoggedInUntilAttemptedAndSucceeded() throws ClientProtocolException, IOException{
      Assert.assertFalse( systemUnderTest.isLoggedIn() );
      shouldAttemptLogin();
      Assert.assertTrue( systemUnderTest.isLoggedIn() );
   }//End Method
   
   @Test public void shouldGetLastBuildBuildingState() throws ClientProtocolException, IOException {
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.executeRequest( JobRequest.LastBuildBuildingStateRequest, jenkinsJob );
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               requests.constructLastBuildBuildingStateRequest( 
                        requests.prefixJenkinsLocation( JENKINS_LOCATION ), jenkinsJob 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldGetLastBuildJobDetails() {
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.executeRequest( JobRequest.LastBuildJobDetailsRequest, jenkinsJob );
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               requests.constructLastBuildJobDetailsRequest(  
                        requests.prefixJenkinsLocation( JENKINS_LOCATION ), jenkinsJob 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method

   @Test public void shouldGetJobList() {
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.getJobsList();
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               requests.constructJobListRequest(   
                        requests.prefixJenkinsLocation( JENKINS_LOCATION ) 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method

   @Test public void shouldGetUserList() {
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.getUsersList();
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               requests.constructUserListRequest(   
                        requests.prefixJenkinsLocation( JENKINS_LOCATION ) 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldExecuteJobDetailsRequest() throws ClientProtocolException, IOException {
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.executeRequest( JenkinsBaseRequest.CurrentJobDetailsRequest );
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               requests.constructCurrentJobDetailsRequest( requests.prefixJenkinsLocation( JENKINS_LOCATION ) 
      ).getURI().toString(), get.getURI().toString() );
   }//End Method
   
   @Test public void shouldGetLatestTestResultsWrapped() {
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.executeRequest( JobRequest.LastBuildTestResultsWrappedRequest, jenkinsJob );
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               requests.constructLastBuildTestResultsWrappedRequest( 
                        requests.prefixJenkinsLocation( JENKINS_LOCATION ), jenkinsJob 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldGetLatestTestResultsUnwrapped() {
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.executeRequest( JobRequest.LastBuildTestResultsUnwrappedRequest, jenkinsJob );
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               requests.constructLastBuildTestResultsUnwrappedRequest( 
                        requests.prefixJenkinsLocation( JENKINS_LOCATION ), jenkinsJob 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method
   
   @Test( expected = UnsupportedOperationException.class ) public void shouldNotSupportHistoricDetails() throws ClientProtocolException, IOException {
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      systemUnderTest.executeRequest( BuildRequest.HistoricDetails, jenkinsJob, 100 );
   }//End Method

   @Test public void shouldNotGetJobListWhenNotLoggedIn() {
      systemUnderTest.getJobsList();
      Mockito.verifyNoMoreInteractions( clientHandler, client );
   }//End Method
   
   @Test public void shouldNotGetUserListWhenNotLoggedIn() {
      systemUnderTest.getUsersList();
      Mockito.verifyNoMoreInteractions( clientHandler, client );
   }//End Method

   @SuppressWarnings("unchecked") //Fail fast, manually verified. 
   @Test public void executeShouldHandleClientProtocolException() throws ClientProtocolException, IOException{
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      verify( digest, times( 1 ) ).handlingResponse();
      verify( digest, times( 1 ) ).responseReady();
      
      when( client.execute( Mockito.< HttpGet >any(), Mockito.< HttpContext >any() ) ).thenThrow( ClientProtocolException.class );
      assertNull( systemUnderTest.executeRequestAndUnpack( Mockito.mock( HttpGet.class ) ) );
      
      verify( digest, times( 1 ) ).handlingResponse();
      verify( digest, times( 1 ) ).responseReady();
   }//End Method
   
   @SuppressWarnings("unchecked") //Fail fast, manually verified.
   @Test public void executeShouldHandleIOException() throws ClientProtocolException, IOException{
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      verify( digest, times( 1 ) ).handlingResponse();
      verify( digest, times( 1 ) ).responseReady();
      
      Mockito.when( client.execute( Mockito.< HttpGet >any(), Mockito.< HttpContext >any() ) ).thenThrow( IOException.class );
      Assert.assertNull( systemUnderTest.executeRequestAndUnpack( Mockito.mock( HttpGet.class ) ) );
      
      verify( digest, times( 1 ) ).handlingResponse();
      verify( digest, times( 1 ) ).responseReady();
   }//End Method

   @SuppressWarnings("unchecked") //Fail fast, manually verified.
   @Test public void executeShouldHandleHttpResponseException() throws ClientProtocolException, IOException{
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      verify( digest, times( 1 ) ).handlingResponse();
      verify( digest, times( 1 ) ).responseReady();
      
      Mockito.when( clientHandler.handleResponse( Mockito.any() ) ).thenThrow( HttpResponseException.class );
      Assert.assertNull( systemUnderTest.executeRequestAndUnpack( Mockito.mock( HttpGet.class ) ) );
      
      verify( digest, times( 2 ) ).handlingResponse();
      verify( digest, times( 1 ) ).responseReady();
   }//End Method
   
   @Test public void executeShouldDigetsFully() throws ClientProtocolException, IOException{
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      verify( digest, times( 1 ) ).handlingResponse();
      verify( digest, times( 1 ) ).responseReady();
      
      assertEquals( systemUnderTest.executeRequestAndUnpack( Mockito.mock( HttpGet.class ) ), EXPECTED_RESPONSE );
      
      verify( digest, times( 2 ) ).handlingResponse();
      verify( digest, times( 2 ) ).responseReady();
   }//End Method
   
   @Test public void shouldApplyLoginTimeoutForLoginPeriodOnly() throws IOException{
      HttpParams params = mock( HttpParams.class );
      when( client.getParams() ).thenReturn( params );
      
      shouldAttemptLogin();
      InOrder order = inOrder( clientHandler );
      order.verify( clientHandler ).adjustClientTimeout( params, JenkinsApiImpl.LOGIN_TIMEOUT );
      order.verify( clientHandler ).handleResponse( Mockito.any() );
      order.verify( clientHandler ).resetTimeout( params );
   }//End Method
   
   @Test public void shouldNotExecuteRequestWhenNotLoggedIn() {
      systemUnderTest.executeRequest( JobRequest.LastBuildBuildingStateRequest, Mockito.mock( JenkinsJob.class ) );
      Mockito.verifyNoMoreInteractions( clientHandler, client );
   }//End Method
   
   @Test public void shouldNotExecuteBaseRequestWhenNotLoggedIn() {
      systemUnderTest.executeRequest( JenkinsBaseRequest.CurrentJobDetailsRequest );
      Mockito.verifyNoMoreInteractions( clientHandler, client );
   }//End Method
   
}//End Class
