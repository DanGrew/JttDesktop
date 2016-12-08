/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.sources;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

public class JenkinsApiRequestsTest {

   private JenkinsJob jenkinsJob;
   private JenkinsApiRequests systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      jenkinsJob = new JenkinsJobImpl( "SomeJenkinsProject" );
      systemUnderTest = new JenkinsApiRequests();
   }//End Method

   @Test public void shouldConstructBaseRequestWithHttp(){
      final String jenkinsLocation = "http://some-location";
      final String expectedBaseRequest = jenkinsLocation + JenkinsApiRequests.BASE_REQUEST;
      Assert.assertEquals( expectedBaseRequest, systemUnderTest.constructBaseRequest( jenkinsLocation ).getURI().toString() );
   }//End Method
   
   @Test public void shouldConstructBaseRequestWithoutHttp(){
      final String jenkinsLocation = "some-location";
      final String expectedBaseRequest = JenkinsApiRequests.LOCATION_PREFIX + jenkinsLocation + JenkinsApiRequests.BASE_REQUEST;
      Assert.assertEquals( expectedBaseRequest, systemUnderTest.constructBaseRequest( jenkinsLocation ).getURI().toString() );
   }//End Method
   
   @Test public void shouldConstructLastBuildBuildingStateRequest(){
      final String expectedBaseRequest = "http://some-location/job/SomeJenkinsProject/lastBuild/api/json?tree=building,estimatedDuration,timestamp,number,builtOn";
      Assert.assertEquals( 
               expectedBaseRequest, 
               systemUnderTest.constructLastBuildBuildingStateRequest( "http://some-location", jenkinsJob ).getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldConstructJobListRequest(){
      final String expectedRequest = "http://some-location/api/json?tree=jobs[name]&pretty=true";
      Assert.assertEquals( 
               expectedRequest, 
               systemUnderTest.constructJobListRequest( "http://some-location" ).getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldConstructUserListRequest(){
      final String expectedRequest = "http://some-location/asynchPeople/api/json?pretty=true&tree=users[user[fullName]]";
      Assert.assertEquals( 
               expectedRequest, 
               systemUnderTest.constructUserListRequest( "http://some-location" ).getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldConstructLastBuildJobDetailsRequest(){
      final String expectedRequest = "http://some-location/job/SomeJenkinsProject/lastCompletedBuild/api/json?tree=number,result,culprits[fullName]";
      Assert.assertEquals( 
               expectedRequest, 
               systemUnderTest.constructLastBuildJobDetailsRequest( "http://some-location", jenkinsJob ).getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldConstructLastBuildTestResultsRequestWrapped(){
      final String expectedRequest = "http://some-location/job/SomeJenkinsProject" + JenkinsApiRequests.LAST_BUILD_TEST_RESULTS_WRAPPED;
      Assert.assertEquals( 
               expectedRequest, 
               systemUnderTest.constructLastBuildTestResultsWrappedRequest( "http://some-location", jenkinsJob ).getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldConstructLastBuildTestResultsRequestUnwrapped(){
      final String expectedRequest = "http://some-location/job/SomeJenkinsProject" + JenkinsApiRequests.LAST_BUILD_TEST_RESULTS_UNWRAPPED;
      Assert.assertEquals( 
               expectedRequest, 
               systemUnderTest.constructLastBuildTestResultsUnwrappedRequest( "http://some-location", jenkinsJob ).getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldConstructListOfBuildsRequest(){
      final String expectedRequest = "http://some-location/job/SomeJenkinsProject/api/json?tree=builds[number]";
      Assert.assertEquals( 
               expectedRequest, 
               systemUnderTest.constructListOfBuildsRequest( "http://some-location", jenkinsJob ).getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldConstructJobDetailsRequest(){
      final String expectedRequest = "http://some-location" + JenkinsApiRequests.JOB_DETAILS;
      Assert.assertEquals( 
               expectedRequest, 
               systemUnderTest.constructJobDetailsRequest( "http://some-location" ).getURI().toString() 
      );
   }//End Method
   
   @Test public void shouldSubstituteSpacesInJenkinsJobs(){
      JenkinsJob jobWithSpaces = new JenkinsJobImpl( "anything with spaces" );
      Assert.assertEquals( "/job/anything%20with%20spaces", systemUnderTest.extractAndPrefixJob( jobWithSpaces ) );
   }//End Method

}//End Class
