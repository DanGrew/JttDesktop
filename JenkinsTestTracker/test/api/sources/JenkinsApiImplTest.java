/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.sources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;

/**
 * {@link JenkinsApiImpl} test.
 */
public class JenkinsApiImplTest {
   
   private static final String JENKINS_LOCATION = "any-location";
   private static final String USERNAME = "any user";
   private static final String PASSWORD = "any password";
   private static final String EXPECTED_RESPONSE = "anythingNotNull";
   
   private JenkinsApiImpl systemUnderTest;
   @Mock private JenkinsApiDigest digest;
   private ObjectProperty< Object > request;
   @Mock private ClientHandler clientHandler;
   @Mock private HttpClient client;
   private JenkinsJob jenkinsJob;
   
   @Before public void initialiseSystemUnderTest() throws ClientProtocolException, IOException{
      MockitoAnnotations.initMocks( this );
      systemUnderTest = new JenkinsApiImpl( clientHandler, digest );
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
      assertEquals( JenkinsApiImpl.constructBaseRequest( JENKINS_LOCATION ).getURI().toString(), get.getURI().toString() );
      
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
      
      String response = systemUnderTest.getLastBuildBuildingState( jenkinsJob );
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               JenkinsApiImpl.constructLastBuildBuildingStateRequest( 
                        JenkinsApiImpl.prefixJenkinsLocation( JENKINS_LOCATION ), jenkinsJob 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldGetLastBuildJobDetails() {
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.getLastBuildJobDetails( jenkinsJob );
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               JenkinsApiImpl.constructLastBuildJobDetailsRequest(  
                        JenkinsApiImpl.prefixJenkinsLocation( JENKINS_LOCATION ), jenkinsJob 
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
               JenkinsApiImpl.constructJobListRequest(   
                        JenkinsApiImpl.prefixJenkinsLocation( JENKINS_LOCATION ) 
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
               JenkinsApiImpl.constructUserListRequest(   
                        JenkinsApiImpl.prefixJenkinsLocation( JENKINS_LOCATION ) 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldGetLatestTestResultsWrapped() {
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.getLatestTestResultsWrapped( jenkinsJob );
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               JenkinsApiImpl.constructLastBuildTestResultsWrappedRequest( 
                        JenkinsApiImpl.prefixJenkinsLocation( JENKINS_LOCATION ), jenkinsJob 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldGetLatestTestResultsUnwrapped() {
      systemUnderTest.attemptLogin( JENKINS_LOCATION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.getLatestTestResultsUnwrapped( jenkinsJob );
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               JenkinsApiImpl.constructLastBuildTestResultsUnwrappedRequest( 
                        JenkinsApiImpl.prefixJenkinsLocation( JENKINS_LOCATION ), jenkinsJob 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method

   @Test public void shouldNotGetLastBuildBuildingStateWhenNotLoggedIn() {
      systemUnderTest.getLastBuildBuildingState( Mockito.mock( JenkinsJob.class ) );
      Mockito.verifyNoMoreInteractions( clientHandler, client );
   }//End Method
   
   @Test public void shouldNotGetLastBuildJobDetailsWhenNotLoggedIn() {
      systemUnderTest.getLastBuildJobDetails( Mockito.mock( JenkinsJob.class ) );
      Mockito.verifyNoMoreInteractions( clientHandler, client );
   }//End Method

   @Test public void shouldNotGetJobListWhenNotLoggedIn() {
      systemUnderTest.getJobsList();
      Mockito.verifyNoMoreInteractions( clientHandler, client );
   }//End Method
   
   @Test public void shouldNotGetUserListWhenNotLoggedIn() {
      systemUnderTest.getUsersList();
      Mockito.verifyNoMoreInteractions( clientHandler, client );
   }//End Method

   @Test public void shouldNotGetLatestTestResultsWrappedWhenNotLoggedIn() {
      systemUnderTest.getLatestTestResultsWrapped( Mockito.mock( JenkinsJob.class ) );
      Mockito.verifyNoMoreInteractions( clientHandler, client );
   }//End Method
   
   @Test public void shouldNotGetLatestTestResultsUnwrappedWhenNotLoggedIn() {
      systemUnderTest.getLatestTestResultsUnwrapped( Mockito.mock( JenkinsJob.class ) );
      Mockito.verifyNoMoreInteractions( clientHandler, client );
   }//End Method
   
   @Test public void shouldConstructBaseRequestWithHttp(){
      final String jenkinsLocation = "http://some-location";
      final String expectedBaseRequest = jenkinsLocation + JenkinsApiImpl.BASE_REQUEST;
      Assert.assertEquals( expectedBaseRequest, JenkinsApiImpl.constructBaseRequest( jenkinsLocation ).getURI().toString() );
   }//End Method
   
   @Test public void shouldConstructBaseRequestWithoutHttp(){
      final String jenkinsLocation = "some-location";
      final String expectedBaseRequest = JenkinsApiImpl.LOCATION_PREFIX + jenkinsLocation + JenkinsApiImpl.BASE_REQUEST;
      Assert.assertEquals( expectedBaseRequest, JenkinsApiImpl.constructBaseRequest( jenkinsLocation ).getURI().toString() );
   }//End Method
   
   @Test public void shouldConstructLastBuildBuildingStateRequest(){
      final String expectedBaseRequest = "http://some-location/job/SomeJenkinsProject/lastBuild/api/json?tree=building,estimatedDuration,timestamp";
      Assert.assertEquals( 
               expectedBaseRequest, 
               JenkinsApiImpl.constructLastBuildBuildingStateRequest( "http://some-location", jenkinsJob ).getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldConstructJobListRequest(){
      final String expectedRequest = "http://some-location/api/json?tree=jobs[name]&pretty=true";
      Assert.assertEquals( 
               expectedRequest, 
               JenkinsApiImpl.constructJobListRequest( "http://some-location" ).getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldConstructUserListRequest(){
      final String expectedRequest = "http://some-location/api/json?pretty=true&tree=users[user[fullName]]";
      Assert.assertEquals( 
               expectedRequest, 
               JenkinsApiImpl.constructUserListRequest( "http://some-location" ).getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldConstructLastBuildJobDetailsRequest(){
      final String expectedRequest = "http://some-location/job/SomeJenkinsProject/lastCompletedBuild/api/json?tree=number,result";
      Assert.assertEquals( 
               expectedRequest, 
               JenkinsApiImpl.constructLastBuildJobDetailsRequest( "http://some-location", jenkinsJob ).getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldConstructLastBuildTestResultsRequestWrapped(){
      final String expectedRequest = "http://some-location/job/SomeJenkinsProject" + JenkinsApiImpl.LAST_BUILD_TEST_RESULTS_WRAPPED;
      Assert.assertEquals( 
               expectedRequest, 
               JenkinsApiImpl.constructLastBuildTestResultsWrappedRequest( "http://some-location", jenkinsJob ).getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldConstructLastBuildTestResultsRequestUnwrapped(){
      final String expectedRequest = "http://some-location/job/SomeJenkinsProject" + JenkinsApiImpl.LAST_BUILD_TEST_RESULTS_UNWRAPPED;
      Assert.assertEquals( 
               expectedRequest, 
               JenkinsApiImpl.constructLastBuildTestResultsUnwrappedRequest( "http://some-location", jenkinsJob ).getURI().toString() 
      );
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
   
   @Test public void shouldSubstituteSpacesInJenkinsJobs(){
      JenkinsJob jobWithSpaces = new JenkinsJobImpl( "anything with spaces" );
      Assert.assertEquals( "/job/anything%20with%20spaces", JenkinsApiImpl.extractAndPrefixJob( jobWithSpaces ) );
   }//End Method
}//End Class
