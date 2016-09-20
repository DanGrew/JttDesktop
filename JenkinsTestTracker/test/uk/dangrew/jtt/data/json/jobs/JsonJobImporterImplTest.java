/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.data.json.jobs;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.dangrew.jtt.data.json.jobs.JsonJobImporterImpl.BUILDING_KEY;
import static uk.dangrew.jtt.data.json.jobs.JsonJobImporterImpl.CULPRITS_KEY;
import static uk.dangrew.jtt.data.json.jobs.JsonJobImporterImpl.ESTIMATED_DURATION_KEY;
import static uk.dangrew.jtt.data.json.jobs.JsonJobImporterImpl.FULL_NAME_KEY;
import static uk.dangrew.jtt.data.json.jobs.JsonJobImporterImpl.JOBS_KEY;
import static uk.dangrew.jtt.data.json.jobs.JsonJobImporterImpl.NAME_KEY;
import static uk.dangrew.jtt.data.json.jobs.JsonJobImporterImpl.NODE_KEY;
import static uk.dangrew.jtt.data.json.jobs.JsonJobImporterImpl.NUMBER_KEY;
import static uk.dangrew.jtt.data.json.jobs.JsonJobImporterImpl.RESULT_KEY;
import static uk.dangrew.jtt.data.json.jobs.JsonJobImporterImpl.TIMESTAMP_KEY;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.nodes.JenkinsNodeImpl;

/**
 * {@link JsonJobImporterImpl} test.
 */
public class JsonJobImporterImplTest {
   
   private static final int IMPORTED_BUILD_NUMBER = 45;
   private static final long IMPORTED_TIMESTAMP = 12345L;
   private static final long IMPORTED_ESTIMATED_DURATION = 1000450L;
   private static final BuildResultStatus IMPORTED_RESULT = BuildResultStatus.SUCCESS;
   private static final String IMPORTED_NODE = "ImportedNode";
   
   private static final int INITIAL_BUILD_NUMBER = 44;
   private static final long INITIAL_TIMESTAMP = 12345L;
   private static final long INITIAL_ESTIMATED_DURATION = 1000320L;
   private static final String INITIAL_NODE = "InitialNode";
   
   private static final int BUILD_NUMBER_PROVIDED = 46;
   private static final long TIMESTAMP_PROVIDED = 902L;
   private static final long ESTIMATED_DURATION_PROVIDED = 8726L;
   private static final String PROVIDED_NODE = "ProvidedNode";
   
   private static final String FIRST_CULPRIT = "firstCulprit";
   private static final String SECOND_CULPRIT = "secondCulprit";
   private static final String THIRD_CULPRIT = "thirdCulprit";
   
   private static final String FIRST_JOB = "firstJob";
   private static final String SECOND_JOB = "secondJob";
   private static final String THIRD_JOB = "thirdJob";
   
   @Mock private JsonJobImportHandler handler;
   private JenkinsJob jenkinsJob;
   
   @Mock private JSONException exception;
   
   @Mock private JSONObject buildingStateResponse;
   
   @Mock private JSONObject jobDetailsResponse;
   @Mock private JSONArray culpritsArray;
   @Mock private JSONObject firstCulprit;
   @Mock private JSONObject secondCulprit;
   @Mock private JSONObject thirdCulprit;
   
   @Mock private JSONObject jobsListResponse;
   @Mock private JSONArray jobsList;
   @Mock private JSONObject firstJob;
   @Mock private JSONObject secondJob;
   @Mock private JSONObject thirdJob;
   
   private JsonJobImporter systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      
      jenkinsJob = new JenkinsJobImpl( "anyName" );
      jenkinsJob.expectedBuildTimeProperty().set( INITIAL_ESTIMATED_DURATION );
      jenkinsJob.currentBuildTimestampProperty().set( INITIAL_TIMESTAMP );
      jenkinsJob.currentBuildNumberProperty().set( INITIAL_BUILD_NUMBER );
      jenkinsJob.lastBuiltOnProperty().set( new JenkinsNodeImpl( INITIAL_NODE ) );
      
      systemUnderTest = new JsonJobImporterImpl( handler );
      
      constructBuildingStateJsonResponseThatCanBeDynamicallyUpdated();
      constructJobDetailsJsonResponseThatCanByDynamicallyUpdated();
      constructJobListJsonResponseThatCanByDynamicallyUpdated();
   }//End Method
   
   /**
    * Method to construct a building state response that can be dynamically updated for different test
    * cases, breaking parts of the data structure in order to handle errors in test cases.
    */
   private void constructBuildingStateJsonResponseThatCanBeDynamicallyUpdated(){
      when( buildingStateResponse.has( BUILDING_KEY ) ).thenReturn( true );
      when( buildingStateResponse.getBoolean( BUILDING_KEY ) ).thenReturn( true );
      when( buildingStateResponse.optBoolean( BUILDING_KEY ) ).thenReturn( true );
      
      when( buildingStateResponse.has( ESTIMATED_DURATION_KEY ) ).thenReturn( true );
      when( buildingStateResponse.optLong( ESTIMATED_DURATION_KEY, jenkinsJob.expectedBuildTimeProperty().get() ) ).thenReturn( IMPORTED_ESTIMATED_DURATION );
      
      when( buildingStateResponse.has( TIMESTAMP_KEY ) ).thenReturn( true );
      when( buildingStateResponse.optLong( TIMESTAMP_KEY, jenkinsJob.currentBuildTimestampProperty().get() ) ).thenReturn( IMPORTED_TIMESTAMP );
      
      when( buildingStateResponse.has( NUMBER_KEY ) ).thenReturn( true );
      when( buildingStateResponse.optInt( NUMBER_KEY, jenkinsJob.currentBuildNumberProperty().get() ) ).thenReturn( IMPORTED_BUILD_NUMBER );
      
      when( buildingStateResponse.has( NODE_KEY ) ).thenReturn( true );
      when( buildingStateResponse.optString( NODE_KEY, jenkinsJob.lastBuiltOnProperty().get().nameProperty().get() ) ).thenReturn( IMPORTED_NODE );
   }//End Method
   
   /**
    * Method to verify that the building state data has been handled correctly.
    * @param building expected value for building, null implies not expected to be handled.
    * @param duration expected value for expected duration, null implies not expected to be handled.
    * @param timestamp expected value for timestamp, null implies not expected to be handled.
    * @param number expected value for build number, null implies not expected to be handled.
    * @param builtOn expected value for the name of the node last built on, null implies not expected to be handled.
    */
   private void verifyBuildingStateImportDataIsHandled( Boolean building, Long duration, Long timestamp, Integer number, String builtOn ) {
      if ( building == null ) {
         verify( handler, never() ).handleBuildingState( Mockito.any(), Mockito.anyBoolean() );
      } else {
         verify( handler, times( 1 ) ).handleBuildingState( jenkinsJob, building );
      }
      
      if ( duration == null ) {
         verify( handler, never() ).handleExpectedDuration( Mockito.any(), Mockito.anyLong() );
      } else {
         verify( handler, times( 1 ) ).handleExpectedDuration( jenkinsJob, duration );
      }

      if ( timestamp == null ) {
         verify( handler, never() ).handleBuildTimestamp( Mockito.any(), Mockito.anyLong() );
      } else {
         verify( handler, times( 1 ) ).handleBuildTimestamp( jenkinsJob, timestamp );
      }
      
      if ( number == null ) {
         verify( handler, never() ).handleCurrentBuildNumber( Mockito.any(), Mockito.anyInt() );
      } else {
         verify( handler, times( 1 ) ).handleCurrentBuildNumber( jenkinsJob, number );
      }
      
      if ( builtOn == null ) {
         verify( handler, never() ).handleBuiltOn( Mockito.any(), Mockito.anyString() );
      } else {
         verify( handler, times( 1 ) ).handleBuiltOn( jenkinsJob, builtOn );
      }
   }//End Method
   
   @Test public void shouldIgnoreBuildStateIfJobIsNull(){
      systemUnderTest.updateBuildState( null, buildingStateResponse );
      verifyBuildingStateImportDataIsHandled( null, null, null, null, null );
   }//End Method
   
   @Test public void shouldIgnoreBuildStateIfResponseIsNull(){
      systemUnderTest.updateBuildState( jenkinsJob, null );
      verifyBuildingStateImportDataIsHandled( null, null, null, null, null );
   }//End Method
   
   @Test public void shouldIgnoreResponseIfHasNoBuildingState(){
      when( buildingStateResponse.has( BUILDING_KEY ) ).thenReturn( false );
      systemUnderTest.updateBuildState( jenkinsJob, buildingStateResponse );
      verifyBuildingStateImportDataIsHandled( null, null, null, null, null );
   }//End Method
   
   @Test public void shouldIgnoreResponseIfThoughtToHaveBuildingStateButNotPresent(){
      when( buildingStateResponse.has( BUILDING_KEY ) ).thenReturn( true );
      when( buildingStateResponse.getBoolean( BUILDING_KEY ) ).thenThrow( exception );
      when( buildingStateResponse.optBoolean( BUILDING_KEY ) ).thenReturn( false );
      
      systemUnderTest.updateBuildState( jenkinsJob, buildingStateResponse );
      verifyBuildingStateImportDataIsHandled( null, null, null, null, null );
   }//End Method
   
   @Test public void shouldNotImportDurationOnlyIfNotPresent(){
      when( buildingStateResponse.has( ESTIMATED_DURATION_KEY ) ).thenReturn( false );
      
      systemUnderTest.updateBuildState( jenkinsJob, buildingStateResponse );
      verifyBuildingStateImportDataIsHandled( true, null, IMPORTED_TIMESTAMP, IMPORTED_BUILD_NUMBER, IMPORTED_NODE );
   }//End Method
   
   @Test public void shouldImportDurationUsingDefaultInJob(){
      when( buildingStateResponse.has( ESTIMATED_DURATION_KEY ) ).thenReturn( true );
      when( buildingStateResponse.optLong( ESTIMATED_DURATION_KEY, INITIAL_ESTIMATED_DURATION ) ).thenReturn( ESTIMATED_DURATION_PROVIDED );
      
      systemUnderTest.updateBuildState( jenkinsJob, buildingStateResponse );
      verifyBuildingStateImportDataIsHandled( true, ESTIMATED_DURATION_PROVIDED, IMPORTED_TIMESTAMP, IMPORTED_BUILD_NUMBER, IMPORTED_NODE );
   }//End Method
   
   @Test public void shouldNotImportTimestampOnlyIfNotPresent(){
      when( buildingStateResponse.has( TIMESTAMP_KEY ) ).thenReturn( false );
      
      systemUnderTest.updateBuildState( jenkinsJob, buildingStateResponse );
      verifyBuildingStateImportDataIsHandled( true, IMPORTED_ESTIMATED_DURATION, null, IMPORTED_BUILD_NUMBER, IMPORTED_NODE );
   }//End Method
   
   @Test public void shouldImportTimestampUsingDefaultInJob(){
      when( buildingStateResponse.has( TIMESTAMP_KEY ) ).thenReturn( true );
      when( buildingStateResponse.optLong( TIMESTAMP_KEY, INITIAL_TIMESTAMP ) ).thenReturn( TIMESTAMP_PROVIDED );
      
      systemUnderTest.updateBuildState( jenkinsJob, buildingStateResponse );
      verifyBuildingStateImportDataIsHandled( true, IMPORTED_ESTIMATED_DURATION, TIMESTAMP_PROVIDED, IMPORTED_BUILD_NUMBER, IMPORTED_NODE );
   }//End Method
   
   @Test public void shouldNotImportBuildNumberOnlyIfNotPresent(){
      when( buildingStateResponse.has( NUMBER_KEY ) ).thenReturn( false );
      
      systemUnderTest.updateBuildState( jenkinsJob, buildingStateResponse );
      verifyBuildingStateImportDataIsHandled( true, IMPORTED_ESTIMATED_DURATION, IMPORTED_TIMESTAMP, null, IMPORTED_NODE );
   }//End Method
   
   @Test public void shouldImportBuildNumberUsingDefaultInJob(){
      when( buildingStateResponse.has( NUMBER_KEY ) ).thenReturn( true );
      when( buildingStateResponse.optInt( NUMBER_KEY, INITIAL_BUILD_NUMBER ) ).thenReturn( BUILD_NUMBER_PROVIDED );
      
      systemUnderTest.updateBuildState( jenkinsJob, buildingStateResponse );
      verifyBuildingStateImportDataIsHandled( true, IMPORTED_ESTIMATED_DURATION, IMPORTED_TIMESTAMP, BUILD_NUMBER_PROVIDED, IMPORTED_NODE );
   }//End Method
   
   @Test public void shouldNotImportBuiltOnOnlyIfNotPresent(){
      when( buildingStateResponse.has( NODE_KEY ) ).thenReturn( false );
      
      systemUnderTest.updateBuildState( jenkinsJob, buildingStateResponse );
      verifyBuildingStateImportDataIsHandled( true, IMPORTED_ESTIMATED_DURATION, IMPORTED_TIMESTAMP, IMPORTED_BUILD_NUMBER, null );
   }//End Method
   
   @Test public void shouldImportBuiltOnUsingDefault(){
      when( buildingStateResponse.has( NODE_KEY ) ).thenReturn( true );
      when( buildingStateResponse.optString( NODE_KEY, INITIAL_NODE ) ).thenReturn( PROVIDED_NODE );
      
      systemUnderTest.updateBuildState( jenkinsJob, buildingStateResponse );
      verifyBuildingStateImportDataIsHandled( true, IMPORTED_ESTIMATED_DURATION, IMPORTED_TIMESTAMP, IMPORTED_BUILD_NUMBER, PROVIDED_NODE );
   }//End Method
   
   @Test public void shouldImportBuiltOnUsingDefaultEvenIfNoNodeInJob(){
      jenkinsJob.lastBuiltOnProperty().set( null );
      when( buildingStateResponse.has( NODE_KEY ) ).thenReturn( true );
      when( buildingStateResponse.optString( NODE_KEY, null ) ).thenReturn( PROVIDED_NODE );
      
      systemUnderTest.updateBuildState( jenkinsJob, buildingStateResponse );
      verifyBuildingStateImportDataIsHandled( true, IMPORTED_ESTIMATED_DURATION, IMPORTED_TIMESTAMP, IMPORTED_BUILD_NUMBER, PROVIDED_NODE );
   }//End Method
   
   /**
    * Method to construct a job details response that can be dynamically updated for different test
    * cases, breaking parts of the data structure in order to handle errors in test cases.
    */
   private void constructJobDetailsJsonResponseThatCanByDynamicallyUpdated(){
      when( jobDetailsResponse.has( NUMBER_KEY ) ).thenReturn( true );
      when( jobDetailsResponse.getInt( NUMBER_KEY ) ).thenReturn( IMPORTED_BUILD_NUMBER );
      when( jobDetailsResponse.optInt( NUMBER_KEY, INITIAL_BUILD_NUMBER ) ).thenReturn( IMPORTED_BUILD_NUMBER );
      
      when( jobDetailsResponse.has( RESULT_KEY ) ).thenReturn( true );
      when( jobDetailsResponse.getEnum( BuildResultStatus.class, RESULT_KEY ) ).thenReturn( IMPORTED_RESULT );
      when( jobDetailsResponse.optEnum( BuildResultStatus.class, RESULT_KEY ) ).thenReturn( IMPORTED_RESULT );
      
      when( jobDetailsResponse.has( CULPRITS_KEY ) ).thenReturn( true );
      when( jobDetailsResponse.getJSONArray( CULPRITS_KEY ) ).thenReturn( culpritsArray );
      when( jobDetailsResponse.optJSONArray( CULPRITS_KEY ) ).thenReturn( culpritsArray );
      
      when( culpritsArray.length() ).thenReturn( 3 );
      when( culpritsArray.getJSONObject( 0 ) ).thenReturn( firstCulprit );
      when( culpritsArray.getJSONObject( 1 ) ).thenReturn( secondCulprit );
      when( culpritsArray.getJSONObject( 2 ) ).thenReturn( thirdCulprit );
      
      when( firstCulprit.has( FULL_NAME_KEY ) ).thenReturn( true );
      when( firstCulprit.getString( FULL_NAME_KEY ) ).thenReturn( FIRST_CULPRIT );
      when( firstCulprit.optString( FULL_NAME_KEY ) ).thenReturn( FIRST_CULPRIT );
      
      when( secondCulprit.has( FULL_NAME_KEY ) ).thenReturn( true );
      when( secondCulprit.getString( FULL_NAME_KEY ) ).thenReturn( SECOND_CULPRIT );
      when( secondCulprit.optString( FULL_NAME_KEY ) ).thenReturn( SECOND_CULPRIT );
      
      when( thirdCulprit.has( FULL_NAME_KEY ) ).thenReturn( true );
      when( thirdCulprit.getString( FULL_NAME_KEY ) ).thenReturn( THIRD_CULPRIT );
      when( thirdCulprit.optString( FULL_NAME_KEY ) ).thenReturn( THIRD_CULPRIT );
   }//End Method
   
   /**
    * Method to verify that the job details have been handled correctly.
    */
   private void verifyJobDetailsDataIsImported(){
      verify( handler ).handleBuiltJobDetails( jenkinsJob, IMPORTED_BUILD_NUMBER, IMPORTED_RESULT );
   }//End Method
   
   /**
    * Method to verify that the job details and not been handled.
    */
   private void verifyJobDetailsDataIsNotImported(){
      verify( handler, never() ).handleBuiltJobDetails( Mockito.any(), Mockito.anyInt(), Mockito.any() );
   }//End Method
   
   /**
    * Method to verify that the expected culprits are imported and handled.
    * @param startImportingCulprits whether the start of the culprits import is expected.
    * @param userNames the user names expected to be imported.
    */
   private void verifyCulpritsAreImported( boolean startImportingCulprits, String... userNames ){
      if ( startImportingCulprits ){
         verify( handler ).startImportingJobCulprits( jenkinsJob );
      } else {
         verify( handler, never() ).startImportingJobCulprits( jenkinsJob );
      }
      verify( handler, times( userNames.length ) ).handleUserCulprit( Mockito.any(), Mockito.anyString() );
      
      for ( String user : userNames ) {
         verify( handler ).handleUserCulprit( jenkinsJob, user );
      }
   }//End Method
   
   @Test public void shouldIgnoreJobDetailsIfJobIsNull(){
      systemUnderTest.updateJobDetails( null, jobDetailsResponse );
      verifyJobDetailsDataIsNotImported();
   }//End Method
   
   @Test public void shouldIgnoreJobDetailsIfResponseIsNull(){
      systemUnderTest.updateJobDetails( jenkinsJob, null );
      verifyJobDetailsDataIsNotImported();
   }//End Method
   
   @Test public void shouldImportJobDetails(){
      systemUnderTest.updateJobDetails( jenkinsJob, jobDetailsResponse );
      verifyJobDetailsDataIsImported();
      verifyCulpritsAreImported( true, FIRST_CULPRIT, SECOND_CULPRIT, THIRD_CULPRIT );
   }//End Method
   
   @Test public void shouldNotImportJobDetailsIfNoNumberPresent(){
      when( jobDetailsResponse.has( NUMBER_KEY ) ).thenReturn( false );
      
      systemUnderTest.updateJobDetails( jenkinsJob, jobDetailsResponse );
      verifyJobDetailsDataIsNotImported();
      verifyCulpritsAreImported( false );
   }//End Method
   
   @Test public void shouldNotImportJobDetailsIfNoResultPresent(){
      when( jobDetailsResponse.has( RESULT_KEY ) ).thenReturn( false );
      
      systemUnderTest.updateJobDetails( jenkinsJob, jobDetailsResponse );
      verifyJobDetailsDataIsNotImported();
      verifyCulpritsAreImported( false );
   }//End Method
   
   @Test public void shouldImportJobDetailsIfNumberThoughtButNotPresentByUsingCurrentBuildNumber(){
      when( jobDetailsResponse.has( NUMBER_KEY ) ).thenReturn( true );
      when( jobDetailsResponse.getInt( NUMBER_KEY ) ).thenThrow( exception );
      when( jobDetailsResponse.optInt( NUMBER_KEY, INITIAL_BUILD_NUMBER ) ).thenReturn( IMPORTED_BUILD_NUMBER );
      
      systemUnderTest.updateJobDetails( jenkinsJob, jobDetailsResponse );
      verifyJobDetailsDataIsImported();
      verifyCulpritsAreImported( true, FIRST_CULPRIT, SECOND_CULPRIT, THIRD_CULPRIT );
   }//End Method
   
   @Test public void shouldNotImportJobDetailsIfResultThoughtButNotPresent(){
      when( jobDetailsResponse.has( RESULT_KEY ) ).thenReturn( true );
      when( jobDetailsResponse.getEnum( BuildResultStatus.class, RESULT_KEY ) ).thenThrow( exception );
      when( jobDetailsResponse.optEnum( BuildResultStatus.class, RESULT_KEY ) ).thenReturn( null );
      
      systemUnderTest.updateJobDetails( jenkinsJob, jobDetailsResponse );
      verifyJobDetailsDataIsNotImported();
      verifyCulpritsAreImported( false );
   }//End Method
   
   @Test public void shouldNotImportCulpritsIfMissingKey(){
      when( jobDetailsResponse.has( CULPRITS_KEY ) ).thenReturn( false );
      
      systemUnderTest.updateJobDetails( jenkinsJob, jobDetailsResponse );
      verifyJobDetailsDataIsImported();
      verifyCulpritsAreImported( true );
   }//End Method
   
   @Test public void shouldNotImportCulpritsIfArrayNull(){
      when( jobDetailsResponse.has( CULPRITS_KEY ) ).thenReturn( true );
      when( jobDetailsResponse.getJSONArray( CULPRITS_KEY ) ).thenThrow( exception );
      when( jobDetailsResponse.optJSONArray( CULPRITS_KEY ) ).thenReturn( null );
      
      systemUnderTest.updateJobDetails( jenkinsJob, jobDetailsResponse );
      verifyJobDetailsDataIsImported();
      verifyCulpritsAreImported( true );
   }//End Method
   
   @Test public void shouldNotImportSecondCulpritWithMissingFullName(){
      when( secondCulprit.has( FULL_NAME_KEY ) ).thenReturn( false );
      
      systemUnderTest.updateJobDetails( jenkinsJob, jobDetailsResponse );
      verifyJobDetailsDataIsImported();
      verifyCulpritsAreImported( true, FIRST_CULPRIT, THIRD_CULPRIT );
   }//End Method
   
   @Test public void shouldNotImportSecondCulpritWithInvalidFullName(){
      when( secondCulprit.has( FULL_NAME_KEY ) ).thenReturn( true );
      when( secondCulprit.getString( FULL_NAME_KEY ) ).thenThrow( exception );
      when( secondCulprit.optString( FULL_NAME_KEY ) ).thenReturn( null );
      
      systemUnderTest.updateJobDetails( jenkinsJob, jobDetailsResponse );
      verifyJobDetailsDataIsImported();
      verifyCulpritsAreImported( true, FIRST_CULPRIT, THIRD_CULPRIT );
   }//End Method
   
   @Test public void shouldNotImportSecondCulpritThatIsMissing(){
      when( culpritsArray.getJSONObject( 1 ) ).thenReturn( null );
      
      systemUnderTest.updateJobDetails( jenkinsJob, jobDetailsResponse );
      verifyJobDetailsDataIsImported();
      verifyCulpritsAreImported( true, FIRST_CULPRIT, THIRD_CULPRIT );
   }//End Method
   
   /**
    * Method to construct a job list response that can be dynamically updated for different test
    * cases, breaking parts of the data structure in order to handle errors in test cases.
    */
   private void constructJobListJsonResponseThatCanByDynamicallyUpdated(){
      when( jobsListResponse.has( JOBS_KEY ) ).thenReturn( true );
      when( jobsListResponse.getJSONArray( JOBS_KEY ) ).thenReturn( jobsList );
      when( jobsListResponse.optJSONArray( JOBS_KEY ) ).thenReturn( jobsList );
      
      when( jobsList.length() ).thenReturn( 3 );
      when( jobsList.getJSONObject( 0 ) ).thenReturn( firstJob );
      when( jobsList.getJSONObject( 1 ) ).thenReturn( secondJob );
      when( jobsList.getJSONObject( 2 ) ).thenReturn( thirdJob );
      when( jobsList.optJSONObject( 0 ) ).thenReturn( firstJob );
      when( jobsList.optJSONObject( 1 ) ).thenReturn( secondJob );
      when( jobsList.optJSONObject( 2 ) ).thenReturn( thirdJob );
      
      when( firstJob.has( NAME_KEY ) ).thenReturn( true );
      when( firstJob.getString( NAME_KEY ) ).thenReturn( FIRST_JOB );
      when( firstJob.optString( NAME_KEY ) ).thenReturn( FIRST_JOB );
      
      when( secondJob.has( NAME_KEY ) ).thenReturn( true );
      when( secondJob.getString( NAME_KEY ) ).thenReturn( SECOND_JOB );
      when( secondJob.optString( NAME_KEY ) ).thenReturn( SECOND_JOB );
      
      when( thirdJob.has( NAME_KEY ) ).thenReturn( true );
      when( thirdJob.getString( NAME_KEY ) ).thenReturn( THIRD_JOB  );
      when( thirdJob.optString( NAME_KEY ) ).thenReturn( THIRD_JOB  );
   }//End Method
   
   /**
    * Method to verify that the given jobs have been imported.
    * @param jobNames the name of jobs expected to be imported.
    */
   private void verifyJobsAreImported( String... jobNames ) {
      for ( String jobName : jobNames ) {
         verify( handler ).handleJobFound( jobName );
      }
   }//End Method
   
   @Test public void shouldIgnoreJobsIfJobIsNull(){
      systemUnderTest.importJobs( null );
      verifyJobsAreImported();
   }//End Method
   
   @Test public void shouldImportJobs(){
      systemUnderTest.importJobs( jobsListResponse );
      verifyJobsAreImported( FIRST_JOB, SECOND_JOB, THIRD_JOB );
   }//End Method
   
   @Test public void shouldNotImportJobsWhenJobsNotPresent(){
      when( jobsListResponse.has( JOBS_KEY ) ).thenReturn( false );
      
      systemUnderTest.importJobs( jobsListResponse );
      verifyJobsAreImported();
   }//End Method
   
   @Test public void shouldNotImportJobsWhenJobsThoughtPresent(){
      when( jobsListResponse.has( JOBS_KEY ) ).thenReturn( true );
      when( jobsListResponse.getJSONArray( JOBS_KEY ) ).thenThrow( exception );
      when( jobsListResponse.optJSONArray( JOBS_KEY ) ).thenReturn( null );
      
      systemUnderTest.importJobs( jobsListResponse );
      verifyJobsAreImported();
   }//End Method
   
   @Test public void shouldNotImportSecondJobsWhenNotPresent(){
      when( jobsList.getJSONObject( 1 ) ).thenThrow( exception );
      when( jobsList.optJSONObject( 1 ) ).thenReturn( null );
      
      systemUnderTest.importJobs( jobsListResponse );
      verifyJobsAreImported();
   }//End Method
   
   @Test public void shouldNotImportSecondJobsWhenJobNameNotPresent(){
      when( secondJob.has( NAME_KEY ) ).thenReturn( false );
      
      systemUnderTest.importJobs( jobsListResponse );
      verifyJobsAreImported( FIRST_JOB, THIRD_JOB );
   }//End Method
   
   @Test public void shouldNotImportSecondJobsWhenJobNameThoughtPresent(){
      when( secondJob.has( NAME_KEY ) ).thenReturn( true );
      when( secondJob.getString( NAME_KEY ) ).thenThrow( exception );
      when( secondJob.optString( NAME_KEY ) ).thenReturn( null );
      
      systemUnderTest.importJobs( jobsListResponse );
      verifyJobsAreImported( FIRST_JOB, THIRD_JOB );
   }//End Method

}//End Class
