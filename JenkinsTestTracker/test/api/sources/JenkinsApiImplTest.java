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
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;

/**
 * {@link JenkinsApiImpl} test.
 */
public class JenkinsApiImplTest {
   
   private static final String JENKINS_LOACTION = "any-location";
   private static final String USERNAME = "any user";
   private static final String PASSWORD = "any password";
   private static final String EXPECTED_RESPONSE = "anythingNotNull";
   
   private JenkinsApiImpl systemUnderTest;
   private ObjectProperty< Object > request;
   private ClientHandler clientHandler;
   private HttpClient client;
   private JenkinsJob jenkinsJob;
   
   @Before public void initialiseSystemUnderTest() throws ClientProtocolException, IOException{
      client = Mockito.mock( HttpClient.class );
      clientHandler = Mockito.mock( ClientHandler.class );
      systemUnderTest = new JenkinsApiImpl( clientHandler );
      jenkinsJob = new JenkinsJobImpl( "SomeJenkinsProject" );
      
      request = new SimpleObjectProperty< Object >();
      Mockito.when( client.execute( Mockito.< HttpGet >any(), Mockito.< BasicHttpContext >any() ) ).thenAnswer( 
               invocation -> {
                  request.set( invocation.getArguments()[ 0 ] );
                  return Mockito.mock( HttpResponse.class );
               } 
      );
      
      Mockito.when( clientHandler.constructClient( JENKINS_LOACTION, USERNAME, PASSWORD ) ).thenReturn( client );
      Mockito.when( clientHandler.handleResponse( Mockito.any() ) ).thenReturn( EXPECTED_RESPONSE );
   }//End Method

   @Test public void shouldAttemptLogin() throws ClientProtocolException, IOException {
      HttpClient actualClient = systemUnderTest.attemptLogin( JENKINS_LOACTION, USERNAME, PASSWORD );
      Assert.assertEquals( client, actualClient );
      
      Mockito.verify( clientHandler ).constructClient( JENKINS_LOACTION, USERNAME, PASSWORD );
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( JenkinsApiImpl.constructBaseRequest( JENKINS_LOACTION ).getURI().toString(), get.getURI().toString() );
   }//End Method
   
   @Test public void shouldNotAttemptLoginWhenClientIsNull() throws ClientProtocolException, IOException {
      Mockito.when( clientHandler.constructClient( JENKINS_LOACTION, USERNAME, PASSWORD ) ).thenReturn( null );
      Assert.assertNull( systemUnderTest.attemptLogin( JENKINS_LOACTION, USERNAME, PASSWORD ) );
      
      Mockito.verify( clientHandler ).constructClient( JENKINS_LOACTION, USERNAME, PASSWORD );
      Mockito.verifyNoMoreInteractions( client, clientHandler );
   }//End Method
   
   @Test public void shouldResetClientConnectionAndHandleNullResponse() throws ClientProtocolException, IOException{
      Mockito.when( clientHandler.handleResponse( Mockito.any() ) ).thenReturn( null );
      Assert.assertNull( systemUnderTest.attemptLogin( JENKINS_LOACTION, USERNAME, PASSWORD ) );
      Assert.assertFalse( systemUnderTest.isLoggedIn() );
   }//End Method
   
   @Test public void shouldNotBeLoggedInUntilAttemptedAndSucceeded() throws ClientProtocolException, IOException{
      Assert.assertFalse( systemUnderTest.isLoggedIn() );
      shouldAttemptLogin();
      Assert.assertTrue( systemUnderTest.isLoggedIn() );
   }//End Method
   
   @Test public void shouldGetLastBuildBuildingState() throws ClientProtocolException, IOException {
      systemUnderTest.attemptLogin( JENKINS_LOACTION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.getLastBuildBuildingState( jenkinsJob );
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               JenkinsApiImpl.constructLastBuildBuildingStateRequest( 
                        JenkinsApiImpl.prefixJenkinsLocation( JENKINS_LOACTION ), jenkinsJob 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldGetLastBuildJobDetails() {
      systemUnderTest.attemptLogin( JENKINS_LOACTION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.getLastBuildJobDetails( jenkinsJob );
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               JenkinsApiImpl.constructLastBuildJobDetailsRequest(  
                        JenkinsApiImpl.prefixJenkinsLocation( JENKINS_LOACTION ), jenkinsJob 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method

   @Test public void shouldGetJobList() {
      systemUnderTest.attemptLogin( JENKINS_LOACTION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.getJobsList();
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               JenkinsApiImpl.constructJobListRequest(   
                        JenkinsApiImpl.prefixJenkinsLocation( JENKINS_LOACTION ) 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method

   @Test public void shouldGetLatestTestResultsWrapped() {
      systemUnderTest.attemptLogin( JENKINS_LOACTION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.getLatestTestResultsWrapped( jenkinsJob );
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               JenkinsApiImpl.constructLastBuildTestResultsWrappedRequest( 
                        JenkinsApiImpl.prefixJenkinsLocation( JENKINS_LOACTION ), jenkinsJob 
               ).getURI().toString(), 
               get.getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldGetLatestTestResultsUnwrapped() {
      systemUnderTest.attemptLogin( JENKINS_LOACTION, USERNAME, PASSWORD );
      
      String response = systemUnderTest.getLatestTestResultsUnwrapped( jenkinsJob );
      Assert.assertEquals( EXPECTED_RESPONSE, response );
      
      Assert.assertNotNull( request.get() );
      Assert.assertTrue( request.get() instanceof HttpGet );
      HttpGet get = ( HttpGet )request.get();
      Assert.assertEquals( 
               JenkinsApiImpl.constructLastBuildTestResultsUnwrappedRequest( 
                        JenkinsApiImpl.prefixJenkinsLocation( JENKINS_LOACTION ), jenkinsJob 
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
      final String expectedBaseRequest = "http://some-location/job/SomeJenkinsProject/lastBuild/api/json?tree=building";
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
      systemUnderTest.attemptLogin( JENKINS_LOACTION, USERNAME, PASSWORD );
      Mockito.when( client.execute( Mockito.< HttpGet >any(), Mockito.< HttpContext >any() ) ).thenThrow( ClientProtocolException.class );
      Assert.assertNull( systemUnderTest.executeRequestAndUnpack( Mockito.mock( HttpGet.class ) ) );
   }//End Method
   
   @SuppressWarnings("unchecked") //Fail fast, manually verified.
   @Test public void executeShouldHandleIOException() throws ClientProtocolException, IOException{
      systemUnderTest.attemptLogin( JENKINS_LOACTION, USERNAME, PASSWORD );
      Mockito.when( client.execute( Mockito.< HttpGet >any(), Mockito.< HttpContext >any() ) ).thenThrow( IOException.class );
      Assert.assertNull( systemUnderTest.executeRequestAndUnpack( Mockito.mock( HttpGet.class ) ) );
   }//End Method

   @SuppressWarnings("unchecked") //Fail fast, manually verified.
   @Test public void executeShouldHandleHttpResponseException() throws ClientProtocolException, IOException{
      systemUnderTest.attemptLogin( JENKINS_LOACTION, USERNAME, PASSWORD );
      Mockito.when( clientHandler.handleResponse( Mockito.any() ) ).thenThrow( HttpResponseException.class );
      Assert.assertNull( systemUnderTest.executeRequestAndUnpack( Mockito.mock( HttpGet.class ) ) );
   }//End Method
   
   @Test public void shouldSubstituteSpacesInJenkinsJobs(){
      JenkinsJob jobWithSpaces = new JenkinsJobImpl( "anything with spaces" );
      Assert.assertEquals( "/job/anything%20with%20spaces", JenkinsApiImpl.extractAndPrefixJob( jobWithSpaces ) );
   }//End Method
}//End Class
