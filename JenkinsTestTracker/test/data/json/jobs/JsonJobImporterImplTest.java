/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.jobs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import api.handling.BuildState;
import api.sources.ExternalApi;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;
import utility.TestCommon;

/**
 * {@link JsonJobImporterImpl} test.
 */
public class JsonJobImporterImplTest {
   
   private JenkinsDatabase database;
   private JenkinsJob jenkinsJob;
   private JsonJobImporter systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      jenkinsJob = new JenkinsJobImpl( "anyName" );
      database = new JenkinsDatabaseImpl();
      systemUnderTest = new JsonJobImporterImpl( database );
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
   
   @Test public void shouldParseJobsList(){
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list.json" );
      Assert.assertNotNull( response );
      
      assertJobsImported( response, new ArrayList<>() );
   }//End Method
   
   @Test public void shouldIgnoreEmptyJobsList(){
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list-empty-jobs.json" );
      Assert.assertNotNull( response );
      
      JenkinsDatabase database = new JenkinsDatabaseImpl();
      systemUnderTest.importJobs( response );
      Assert.assertTrue( database.hasNoJenkinsJobs() );
      Assert.assertTrue( database.jenkinsJobs().isEmpty() );
   }//End Method
   
   @Test public void shouldIgnoreInvalidJobNameInJobList(){
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list-invalid-name-value.json" );
      Assert.assertNotNull( response );
      
      assertJobsImported( response, Arrays.asList( 0 ) );
   }//End Method
   
   @Test public void shouldIgnoreMissingJobsInJobList(){
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list-missing-jobs.json" );
      Assert.assertNotNull( response );
      
      JenkinsDatabase database = new JenkinsDatabaseImpl();
      systemUnderTest.importJobs( response );
      Assert.assertTrue( database.hasNoJenkinsJobs() );
      Assert.assertTrue( database.jenkinsJobs().isEmpty() );
   }//End Method
   
   @Test public void shouldIgnoreMissingNameKeyInJobList(){
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list-missing-name-key.json" );
      Assert.assertNotNull( response );
      
      assertJobsImported( response, Arrays.asList( 2 ) );
   }//End Method
   
   @Test public void shouldIgnoreMissingNameValueInJobList(){
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list-missing-name-value.json" );
      Assert.assertNotNull( response );
      
      assertJobsImported( response, Arrays.asList( 5 ) );
   }//End Method
   
   @Test public void shouldOnlyImportNewJobs(){
      database.store( new JenkinsJobImpl( "ClassicStuff" ) );
      database.store( new JenkinsJobImpl( "CommonProject" ) );
      database.store( new JenkinsJobImpl( "JenkinsTestTracker" ) );
      
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list.json" );
      Assert.assertNotNull( response );
      
      assertJobsImported( response, new ArrayList<>() );
   }//End Method
   
   /**
    * Method to assert that the expected {@link JenkinsJob}s have been imported.
    * @param response the response from the {@link ExternalApi}.
    * @param missingJobs the job number to exclude.
    */
   private void assertJobsImported( String response, List< Integer > missingJobs ) {
      List< String > expected = new ArrayList<>();
      if ( !missingJobs.contains( 0 ) ) {
         expected.add( "ClassicStuff" );
      }
      if ( !missingJobs.contains( 1 ) ) {
         expected.add( "CommonProject" );
      }
      if ( !missingJobs.contains( 2 ) ) {
         expected.add( "JenkinsTestTracker" );
      } 
      if ( !missingJobs.contains( 3 ) ) {
         expected.add( "MySpecialProject" );
      }
      if ( !missingJobs.contains( 4 ) ) {
         expected.add( "Silly Project" );
      }
      if ( !missingJobs.contains( 5 ) ) {
         expected.add( "SomeOtherTypeOfProject" );
      }
      if ( !missingJobs.contains( 6 ) ) {
         expected.add( "Zebra!" );
      }
      
      systemUnderTest.importJobs( response );
      Assert.assertEquals( 7 - missingJobs.size(), database.jenkinsJobs().size() );
      for ( int i = 0; i < 7 - missingJobs.size(); i ++ ) {
         Assert.assertEquals( expected.get( i ), database.jenkinsJobs().get( i ).nameProperty().get() );
      }
   }//End Method
   
   @Test public void shouldIgnoreNullDatabaseInJobList(){
      JenkinsDatabase database = Mockito.mock( JenkinsDatabase.class );
      systemUnderTest.importJobs( null );
      Mockito.verifyNoMoreInteractions( database );
   }//End Method
   
   @Test public void shouldIgnoreNullResponseInJobList(){
      systemUnderTest.importJobs( "anything" );
   }//End Method
}//End Class
