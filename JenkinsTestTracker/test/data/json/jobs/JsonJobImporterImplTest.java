/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.jobs;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import api.handling.BuildState;
import api.handling.JenkinsFetcher;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import model.users.JenkinsUser;
import model.users.JenkinsUserImpl;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;
import utility.TestCommon;

/**
 * {@link JsonJobImporterImpl} test.
 */
public class JsonJobImporterImplTest {
   
   @Mock private JenkinsFetcher fetcher;
   private JenkinsDatabase database;
   private JenkinsJob jenkinsJob;
   private JenkinsUser lucille;
   private JenkinsUser negan;
   private JenkinsUser aaron;
   private JsonJobImporter systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      
      jenkinsJob = new JenkinsJobImpl( "anyName" );
      lucille = new JenkinsUserImpl( "Lucille" );
      negan = new JenkinsUserImpl( "Negan" );
      aaron = new JenkinsUserImpl( "Aaron" );
      
      database = new JenkinsDatabaseImpl();
      database.store( lucille );
      database.store( negan );
      database.store( aaron );
      systemUnderTest = new JsonJobImporterImpl( database, fetcher );
   }//End Method

   @Test public void shouldParseBuildingStateMissingExpectedCompletion() {
      String response = TestCommon.readFileIntoString( getClass(), "building-state-missing-expected-completion.json" );
      systemUnderTest.updateBuildState( jenkinsJob, response );
      Assert.assertEquals( BuildState.Building, jenkinsJob.buildStateProperty().get() );
      Assert.assertEquals( JenkinsJob.DEFAULT_EXPECTED_BUILD_TIME, jenkinsJob.expectedBuildTimeProperty().get() );
   }//End Method
   
   @Test public void shouldParseBuildingStateInvalidExpectedCompletion() {
      String response = TestCommon.readFileIntoString( getClass(), "building-state-invalid-expected-completion.json" );
      systemUnderTest.updateBuildState( jenkinsJob, response );
      Assert.assertEquals( BuildState.Building, jenkinsJob.buildStateProperty().get() );
      Assert.assertEquals( JenkinsJob.DEFAULT_EXPECTED_BUILD_TIME, jenkinsJob.expectedBuildTimeProperty().get() );
   }//End Method
   
   @Test public void shouldParseMissingTimestamp(){
      String response = TestCommon.readFileIntoString( getClass(), "building-state-missing-timestamp.json" );
      systemUnderTest.updateBuildState( jenkinsJob, response );
      Assert.assertEquals( BuildState.Building, jenkinsJob.buildStateProperty().get() );
      Assert.assertEquals( JenkinsJob.DEFAULT_BUILD_TIMESTAMP, jenkinsJob.lastBuildTimestampProperty().get() );
   }//End Method
   
   @Test public void shouldParseInvalidTimestamp(){
      String response = TestCommon.readFileIntoString( getClass(), "building-state-invalid-timestamp.json" );
      systemUnderTest.updateBuildState( jenkinsJob, response );
      Assert.assertEquals( BuildState.Building, jenkinsJob.buildStateProperty().get() );
      Assert.assertEquals( JenkinsJob.DEFAULT_BUILD_TIMESTAMP, jenkinsJob.lastBuildTimestampProperty().get() );
   }//End Method
   
   @Test public void shouldParseBuildingState() {
      String response = TestCommon.readFileIntoString( getClass(), "building-state.json" );
      systemUnderTest.updateBuildState( jenkinsJob, response );
      Assert.assertEquals( BuildState.Building, jenkinsJob.buildStateProperty().get() );
      Assert.assertEquals( 100000, jenkinsJob.expectedBuildTimeProperty().get() );
      Assert.assertEquals( 123456, jenkinsJob.lastBuildTimestampProperty().get() );
   }//End Method
   
   @Test public void shouldParseBuiltState() {
      String response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
      systemUnderTest.updateBuildState( jenkinsJob, response );
      Assert.assertEquals( BuildState.Built, jenkinsJob.buildStateProperty().get() );
   }//End Method
   
   @Test public void shouldResetProgressWhenParseBuiltState() {
      jenkinsJob.currentBuildTimeProperty().set( 1000 );
      jenkinsJob.buildStateProperty().set( BuildState.Building );
      Assert.assertEquals( 1000, jenkinsJob.currentBuildTimeProperty().get() );
      
      BooleanProperty builtStateHasChanged = new SimpleBooleanProperty( false );
      jenkinsJob.buildStateProperty().addListener( ( source, old, updated ) -> 
         builtStateHasChanged.set( true ) 
      );
      jenkinsJob.currentBuildTimeProperty().addListener( ( source, old, updated ) -> 
         Assert.assertFalse( builtStateHasChanged.get() ) 
      );
      
      String response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
      systemUnderTest.updateBuildState( jenkinsJob, response );
      
      Assert.assertEquals( 0, jenkinsJob.currentBuildTimeProperty().get() );
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
      systemUnderTest.updateBuildState( jenkinsJob, response );
   }//End Method
   
   @Test public void shouldIgnoreInvalidBuiltStateValue() {
      String response = TestCommon.readFileIntoString( getClass(), "invalid-value.json" );
      systemUnderTest.updateBuildState( jenkinsJob, response );
   }//End Method
   
   @Test public void shouldParseJobDetails() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details.json" );
      assertDefaultJobDetailsImported( response );
      
      assertThat( jenkinsJob.culprits().isEmpty(), is( false ) );
      assertThat( jenkinsJob.culprits(), contains( lucille, negan, aaron ) );
   }//End Method
   
   @Test public void shouldParseJobDetailsAndNotDuplicateCulprits() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details.json" );
      assertDefaultJobDetailsImported( response );
      systemUnderTest.updateJobDetails( jenkinsJob, response );
      
      assertThat( jenkinsJob.culprits().isEmpty(), is( false ) );
      assertThat( jenkinsJob.culprits(), contains( lucille, negan, aaron ) );
   }//End Method
   
   @Test public void shouldIgnoreJobDetailsMissingNumber() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-missing-number.json" );
      assertJobUnchanged( response );
   }//End Method
   
   @Test public void shouldIgnoreJobDetailsMissingResult() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-missing-result.json" );
      assertJobUnchanged( response );
   }//End Method
   
   @Test public void shouldIgnoreJobDetailsInvalidNumber() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-invalid-number.json" );
      assertJobUnchanged( response );
   }//End Method
   
   @Test public void shouldIgnoreJobDetailsInvalidResult() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-invalid-results.json" );
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
      
      assertJobsImported( response, new ArrayList<>() );
   }//End Method
   
   @Test public void shouldIgnoreEmptyJobsList(){
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list-empty-jobs.json" );
      
      JenkinsDatabase database = new JenkinsDatabaseImpl();
      systemUnderTest.importJobs( response );
      Assert.assertTrue( database.hasNoJenkinsJobs() );
      Assert.assertTrue( database.jenkinsJobs().isEmpty() );
   }//End Method
   
   @Test public void shouldIgnoreInvalidJobNameInJobList(){
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list-invalid-name-value.json" );
      
      assertJobsImported( response, Arrays.asList( 0 ) );
   }//End Method
   
   @Test public void shouldIgnoreMissingJobsInJobList(){
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list-missing-jobs.json" );
      
      JenkinsDatabase database = new JenkinsDatabaseImpl();
      systemUnderTest.importJobs( response );
      Assert.assertTrue( database.hasNoJenkinsJobs() );
      Assert.assertTrue( database.jenkinsJobs().isEmpty() );
   }//End Method
   
   @Test public void shouldIgnoreMissingNameKeyInJobList(){
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list-missing-name-key.json" );
      
      assertJobsImported( response, Arrays.asList( 2 ) );
   }//End Method
   
   @Test public void shouldIgnoreMissingNameValueInJobList(){
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list-missing-name-value.json" );
      
      assertJobsImported( response, Arrays.asList( 5 ) );
   }//End Method
   
   @Test public void shouldOnlyImportNewJobs(){
      database.store( new JenkinsJobImpl( "ClassicStuff" ) );
      database.store( new JenkinsJobImpl( "CommonProject" ) );
      database.store( new JenkinsJobImpl( "JenkinsTestTracker" ) );
      
      String response = TestCommon.readFileIntoString( getClass(), "jobs-list.json" );
      
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
      systemUnderTest = new JsonJobImporterImpl( null, fetcher );
      systemUnderTest.importJobs( "anything" );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldRejectNullFetcher(){
      systemUnderTest = new JsonJobImporterImpl( database, null );
   }//End Method
   
   @Test public void shouldIgnoreNullResponseInJobList(){
      systemUnderTest.importJobs( null );
   }//End Method
   
   @Test public void shouldIgnoreMissingButValidData(){
      systemUnderTest.importJobs( "{ }" );
   }//End Method
   
   @Test public void shouldParseEmptyCulpritsInJobDetails() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-empty-culprits.json" );
      assertDefaultJobDetailsImported( response );

      assertThat( jenkinsJob.culprits().isEmpty(), is( true ) );
   }//End Method
   
   @Test public void shouldParseMissingCulpritFullNameInJobDetails() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-missing-culprit-full-name.json" );
      assertDefaultJobDetailsImported( response );

      assertThat( jenkinsJob.culprits().isEmpty(), is( false ) );
      assertThat( jenkinsJob.culprits(), contains( negan, aaron ) );
   }//End Method
   
   @Test public void shouldParseMissingCulpritNameInJobDetails() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-missing-culprit-name.json" );
      assertDefaultJobDetailsImported( response );

      assertThat( jenkinsJob.culprits().isEmpty(), is( false ) );
      assertThat( jenkinsJob.culprits(), contains( negan, aaron ) );
   }//End Method
   
   @Test public void shouldParseMissingCulpritsInJobDetails() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-missing-culprits.json" );
      assertDefaultJobDetailsImported( response );

      assertThat( jenkinsJob.culprits().isEmpty(), is( true ) );
   }//End Method
   
   @Test public void shouldParseCulpritsButIgnoredIncorrectFormatInJobDetails() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-culprit-name-format.json" );
      assertDefaultJobDetailsImported( response );
      
      assertThat( jenkinsJob.culprits().isEmpty(), is( false ) );
      assertThat( jenkinsJob.culprits(), contains( negan, aaron ) );
   }//End Method

   @Test public void shouldParseCulpritsAndRequestMissingUsersIgnoringIfNotResolvedInJobDetails() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-user-missing-from-database.json" );
      assertDefaultJobDetailsImported( response );

      assertThat( jenkinsJob.culprits().isEmpty(), is( false ) );
      assertThat( jenkinsJob.culprits(), contains( lucille, negan, aaron ) );
   }//End Method
   
   @Test public void shouldParseCulpritsAndRequestMissingUsersIdentifyingIfResolvedInJobDetails() {
      String response = TestCommon.readFileIntoString( getClass(), "job-details-user-missing-from-database.json" );
      
      JenkinsUser glenn = new JenkinsUserImpl( "Not going to be Glenn!" );
      Mockito.doAnswer( invocation -> {
         database.store( glenn );
         return null;
      } ).when( fetcher ).fetchUsers();
      assertDefaultJobDetailsImported( response );

      assertThat( jenkinsJob.culprits().isEmpty(), is( false ) );
      assertThat( jenkinsJob.culprits(), contains( lucille, negan, aaron, glenn ) );
   }//End Method
   
   /**
    * Method to assert that the default job details are imported when specifically looking at
    * culprits parsing.
    * @param response the response from the api.
    */
   private void assertDefaultJobDetailsImported( String response ) {
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      systemUnderTest.updateJobDetails( jenkinsJob, response );
      Assert.assertEquals( 22, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.SUCCESS, jenkinsJob.lastBuildStatusProperty().get() );
   }//End Method
   
}//End Class
