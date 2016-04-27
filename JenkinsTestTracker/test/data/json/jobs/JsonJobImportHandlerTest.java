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
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import api.handling.BuildState;
import api.handling.JenkinsProcessing;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import model.users.JenkinsUser;
import model.users.JenkinsUserImpl;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;
import utility.mockito.DoAnswerNullReturn;

/**
 * {@link JsonJobImportHandler} test.
 */
public class JsonJobImportHandlerTest {

   private static final String USER_NAME = "some user";
   
   private JenkinsDatabase database;
   @Mock private JenkinsProcessing jenkinsProcessing;
   private JenkinsJob job;
   private JenkinsUser user;
   private JsonJobImportHandler systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      database = new JenkinsDatabaseImpl();
      job = new JenkinsJobImpl( "any job" );
      user = new JenkinsUserImpl( USER_NAME );
      systemUnderTest = new JsonJobImportHandler( database, jenkinsProcessing );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullDatabase(){
      systemUnderTest = new JsonJobImportHandler( null, jenkinsProcessing );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullFetcher(){
      systemUnderTest = new JsonJobImportHandler( database, null );
   }//End Method
   
   @Test public void shouldUpdateBuildStateWhenBuilding() {
      job.buildStateProperty().set( BuildState.Built );
      systemUnderTest.handleBuildingState( job, true );
      assertThat( job.buildStateProperty().get(), is( BuildState.Building ) );
   }//End Method
   
   @Test public void shouldUpdateBuildStateAndBuildTimeWhenBuilt() {
      job.buildStateProperty().set( BuildState.Building );
      job.currentBuildTimeProperty().set( 12345 );
      systemUnderTest.handleBuildingState( job, false );
      assertThat( job.buildStateProperty().get(), is( BuildState.Built ) );
      assertThat( job.currentBuildTimeProperty().get(), is( 0L ) );
   }//End Method
   
   @Test public void shouldUpdateExpectedDuration(){
      job.expectedBuildTimeProperty().set( 12345 );
      
      final long expected = 9879076;
      systemUnderTest.handleExpectedDuration( job, expected );
      assertThat( job.expectedBuildTimeProperty().get(), is( expected ) );
   }//End Method

   @Test public void shouldUpdateTimestamp(){
      job.currentBuildTimestampProperty().set( 12345 );
      
      final long expected = 9879076;
      systemUnderTest.handleBuildTimestamp( job, expected );
      assertThat( job.currentBuildTimestampProperty().get(), is( expected ) );
   }//End Method
   
   @Test public void shouldUpdateCurrentBuildNumberWhenBuilding(){
      final int previousBuildNumber = 10;
      job.currentBuildNumberProperty().set( previousBuildNumber );
      job.lastBuildNumberProperty().set( previousBuildNumber );
      
      job.buildStateProperty().set( BuildState.Building );
      
      final int newBuildNumber = 11;
      systemUnderTest.handleBuildNumber( job, newBuildNumber );
      
      assertThat( job.currentBuildNumberProperty().get(), is( newBuildNumber ) );
      assertThat( job.lastBuildNumberProperty().get(), is( previousBuildNumber ) );
   }//End Method
   
   @Test public void shouldUpdateLastBuildNumberWhenBuilt(){
      final int previousBuildNumber = 10;
      job.currentBuildNumberProperty().set( previousBuildNumber );
      job.lastBuildNumberProperty().set( previousBuildNumber );
      
      job.buildStateProperty().set( BuildState.Built );
      
      final int newBuildNumber = 11;
      systemUnderTest.handleBuildNumber( job, newBuildNumber );
      
      assertThat( job.currentBuildNumberProperty().get(), is( newBuildNumber ) );
      assertThat( job.lastBuildNumberProperty().get(), is( newBuildNumber ) );
   }//End Method
   
   @Test public void shouldUpdateBuiltJobDetails(){
      job.lastBuildNumberProperty().set( 10 );
      job.lastBuildStatusProperty().set( BuildResultStatus.UNKNOWN );
      
      systemUnderTest.handleBuiltJobDetails( job, 11, BuildResultStatus.SUCCESS );
      
      assertThat( job.lastBuildNumberProperty().get(), is( 11 ) );
      assertThat( job.lastBuildStatusProperty().get(), is( BuildResultStatus.SUCCESS ) );
   }//End Method
   
   @Test public void startImportingCulpritsShouldClearExistingCulprits(){
      job.culprits().addAll( new JenkinsUserImpl( "a" ), new JenkinsUserImpl( "b" ), new JenkinsUserImpl( "c" ) );
      
      systemUnderTest.startImportingJobCulprits( job );
      assertThat( job.culprits(), is( empty() ) );
   }//End Method
   
   @Test public void shouldIdentifyCulpritFromDatabaseAndApplyToJob(){
      database.store( user );
      
      assertThat( job.culprits(), is( empty() ) );
      systemUnderTest.handleUserCulprit( job, USER_NAME );
      assertThat( job.culprits(), contains( user ) );
   }//End Method
   
   @Test public void shouldNotFindCulpritRequestUpdateFindCulpritAndApplyToJob(){
      doAnswer( new DoAnswerNullReturn( () -> database.store( user ) ) ).when( jenkinsProcessing ).fetchUsers();
      
      assertThat( job.culprits(), is( empty() ) );
      systemUnderTest.handleUserCulprit( job, USER_NAME );
      
      verify( jenkinsProcessing ).fetchUsers();
      assertThat( job.culprits(), contains( user ) );
   }//End Method
   
   @Test public void shouldNotFindCulpritRequestUpdatedFailAgainAndIgnore(){
      doNothing().when( jenkinsProcessing ).fetchUsers();
      
      assertThat( job.culprits(), is( empty() ) );
      systemUnderTest.handleUserCulprit( job, USER_NAME );
      
      verify( jenkinsProcessing ).fetchUsers();
      assertThat( job.culprits(), is( empty() ) );
   }//End Method
   
   @Test public void shouldIgnoreJobWithInvalidName(){
      assertThat( database.jenkinsJobs(), is( empty() ) );
      systemUnderTest.handleJobFound( "   " );
      assertThat( database.jenkinsJobs(), is( empty() ) );
   }//End Method
   
   @Test public void shouldIgnoreJobThatAlreadyExists(){
      assertThat( database.jenkinsJobs(), is( empty() ) );
      database.store( job );
      systemUnderTest.handleJobFound( job.nameProperty().get() );
      assertThat( database.jenkinsJobs(), contains( job ) );
   }//End Method
   
   @Test public void shouldCreateAndStoreNewJob(){
      assertThat( database.jenkinsJobs(), is( empty() ) );
      database.store( job );
      
      final String newJobName = "brand new job";
      systemUnderTest.handleJobFound( newJobName );
      
      assertThat( database.jenkinsJobs(), hasSize( 2 ) );
      assertThat( database.jenkinsJobs().get( 0 ), is( job ) );
      assertThat( database.jenkinsJobs().get( 1 ).nameProperty().get(), is( newJobName ) );
   }//End Method

}//End Class
