/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.jobs;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import api.handling.BuildState;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import utility.TestCommon;

/**
 * {@link JsonJobImporter} test.
 */
public class JsonJobImporterTest {
   
   private JenkinsJob jenkinsJob;
   private JsonJobImporter systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      jenkinsJob = new JenkinsJobImpl( "anyName" );
      systemUnderTest = new JsonJobImporter();
   }//End Method

   @Test public void shouldParseBuildingState() {
      String response = TestCommon.readFileIntoString( getClass(), "building-state.json" );
      Assert.assertNotNull( response );
      systemUnderTest.updateBuildState( jenkinsJob, response );
      Assert.assertEquals( BuildState.Building, jenkinsJob.buildStateProperty().get() );
   }//End Method
   
   @Test public void shouldParseBuiltState() {
      String response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
      Assert.assertNotNull( response );
      systemUnderTest.updateBuildState( jenkinsJob, response );
      Assert.assertEquals( BuildState.Built, jenkinsJob.buildStateProperty().get() );
   }//End Method
   
   @Test public void shouldIgnoreNullBuildInput() {
      systemUnderTest.updateBuildState( jenkinsJob, null );
   }//End Method
   
   @Test public void shouldIgnoreInvalidBuildInput() {
      systemUnderTest.updateBuildState( jenkinsJob, "anything" );
   }//End Method
   
   @Test public void shouldIgnoreNullJenkinsJobBuildState() {
      systemUnderTest.updateBuildState( null, "anything" );
   }//End Method

   @Test public void shouldIgnoreInvalidBuiltStateKey() {
      String response = TestCommon.readFileIntoString( getClass(), "invalid-key.json" );
      Assert.assertNotNull( response );
      systemUnderTest.updateBuildState( jenkinsJob, response );
   }//End Method
   
   @Test public void shouldIgnoreInvalidBuiltStateValue() {
      String response = TestCommon.readFileIntoString( getClass(), "invalid-value.json" );
      Assert.assertNotNull( response );
      systemUnderTest.updateBuildState( jenkinsJob, response );
   }//End Method
   
   @Test public void shouldParseJobDetails() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details.json" );
      Assert.assertNotNull( response );
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      systemUnderTest.updateJobDetails( jenkinsJob, response );
      Assert.assertEquals( 22, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.SUCCESS, jenkinsJob.lastBuildStatusProperty().get() );
   }//End Method
   
   @Test public void shouldIgnoreJobDetailsMissingNumber() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-missing-number.json" );
      Assert.assertNotNull( response );
      assertJobUnchanged( response );
   }//End Method
   
   @Test public void shouldIgnoreJobDetailsMissingResult() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-missing-result.json" );
      Assert.assertNotNull( response );
      assertJobUnchanged( response );
   }//End Method
   
   @Test public void shouldIgnoreJobDetailsInvalidNumber() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-invalid-number.json" );
      Assert.assertNotNull( response );
      assertJobUnchanged( response );
   }//End Method
   
   @Test public void shouldIgnoreJobDetailsInvalidResult() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-invalid-results.json" );
      Assert.assertNotNull( response );
      assertJobUnchanged( response );
   }//End Method
   
   @Test public void shouldIgnoreNullJobDetailsInput() {
      systemUnderTest.updateJobDetails( jenkinsJob, null );
      assertJobUnchanged( null );
   }//End Method
   
   @Test public void shouldIgnoreInvalidJobDetailsInput() {
      systemUnderTest.updateJobDetails( jenkinsJob, "anything" );
      assertJobUnchanged( "anything" );
   }//End Method
   
   @Test public void shouldIgnoreNullJobDetailsJob() {
      systemUnderTest.updateJobDetails( null, "anything" );
      assertJobUnchanged( "anything" );
   }//End Method
   
   /**
    * Method to assert that the {@link JenkinsJob} has not been changed when the response has been executed.
    * @param response the {@link String} json response.
    */
   private void assertJobUnchanged( String response ){
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      systemUnderTest.updateJobDetails( jenkinsJob, response );
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
   }//End Method
}//End Class
