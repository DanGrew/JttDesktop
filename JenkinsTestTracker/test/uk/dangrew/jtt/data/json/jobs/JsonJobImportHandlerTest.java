/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.data.json.jobs;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static uk.dangrew.jtt.data.json.jobs.JsonJobImportHandler.MASTER_ID;
import static uk.dangrew.jtt.data.json.jobs.JsonJobImportHandler.MASTER_NAME;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.util.Pair;
import uk.dangrew.jtt.api.handling.JenkinsProcessing;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.nodes.JenkinsNodeImpl;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.jtt.utility.mockito.DoAnswerNullReturn;

/**
 * {@link JsonJobImportHandler} test.
 */
public class JsonJobImportHandlerTest {

   private static final String USER_NAME = "some user";
   private static final String NODE_NAME = "some node";
   
   private JenkinsDatabase database;
   @Mock private JenkinsProcessing jenkinsProcessing;
   private JenkinsJob job;
   private JenkinsUser user;
   private JenkinsNode node;
   private JenkinsNode masterNode;
   private JsonJobImportHandler systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      database = new JenkinsDatabaseImpl();
      job = new JenkinsJobImpl( "any job" );
      user = new JenkinsUserImpl( USER_NAME );
      node = new JenkinsNodeImpl( NODE_NAME );
      masterNode = new JenkinsNodeImpl( MASTER_NAME );
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
      
      job.buildStateProperty().set( BuildState.Building );
      
      final int newBuildNumber = 11;
      systemUnderTest.handleCurrentBuildNumber( job, newBuildNumber );
      
      assertThat( job.currentBuildNumberProperty().get(), is( newBuildNumber ) );
   }//End Method
   
   @Test public void shouldUpdateLastBuildNumberWhenBuilt(){
      final int previousBuildNumber = 10;
      job.currentBuildNumberProperty().set( previousBuildNumber );
      
      job.buildStateProperty().set( BuildState.Built );
      
      final int newBuildNumber = 11;
      systemUnderTest.handleCurrentBuildNumber( job, newBuildNumber );
      
      assertThat( job.currentBuildNumberProperty().get(), is( newBuildNumber ) );
   }//End Method
   
   @Test public void shouldUpdateBuiltJobDetails(){
      job.lastBuildProperty().set( new Pair<>( 10, BuildResultStatus.UNKNOWN ) );
      
      systemUnderTest.handleBuiltJobDetails( job, 11, BuildResultStatus.SUCCESS );
      
      assertThat( job.lastBuildProperty().get(), is( new Pair<>( 11, BuildResultStatus.SUCCESS ) ) );
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
   
   @Test public void shouldSetBuiltOnEvenIfNull(){
      systemUnderTest.handleBuiltOn( job, null );
      
      assertThat( database.jenkinsNodes(), hasSize( 0 ) );
      assertThat( job.lastBuiltOnProperty().get(), is( nullValue() ) );
   }//End Method
   
   @Test public void shouldCreateBuiltOnIfNotPresent(){
      systemUnderTest.handleBuiltOn( job, NODE_NAME );
      
      assertThat( database.jenkinsNodes(), hasSize( 1 ) );
      assertThat( job.lastBuiltOnProperty().get(), is( database.jenkinsNodes().get( 0 ) ) );
      assertThat( database.jenkinsNodes().get( 0 ).nameProperty().get(), is( NODE_NAME ) );
      assertThat( database.jenkinsNodes().get( 0 ), is( not( node ) ) );
   }//End Method
   
   @Test public void shouldUseBuiltOnNodeIfPresentInDatabase(){
      database.store( node );
      systemUnderTest.handleBuiltOn( job, NODE_NAME );
      
      assertThat( database.jenkinsNodes(), hasSize( 1 ) );
      assertThat( job.lastBuiltOnProperty().get(), is( database.jenkinsNodes().get( 0 ) ) );
      assertThat( database.jenkinsNodes().get( 0 ).nameProperty().get(), is( NODE_NAME ) );
      assertThat( database.jenkinsNodes().get( 0 ), is( node ) );
   }//End Method   
   
   @Test public void shouldInterpretEmptyNameAsMasterAndCreateMasterIfNotPresent(){
      systemUnderTest.handleBuiltOn( job, MASTER_ID );
      
      assertThat( database.jenkinsNodes(), hasSize( 1 ) );
      assertThat( job.lastBuiltOnProperty().get(), is( database.jenkinsNodes().get( 0 ) ) );
      assertThat( database.jenkinsNodes().get( 0 ).nameProperty().get(), is( MASTER_NAME ) );
      assertThat( database.jenkinsNodes().get( 0 ), is( not( masterNode ) ) );
   }//End Method
   
   @Test public void shouldInterpretEmptyNameAsMasterAndUseMasterIfPresent(){
      database.store( masterNode );
      systemUnderTest.handleBuiltOn( job, MASTER_ID );
      
      assertThat( database.jenkinsNodes(), hasSize( 1 ) );
      assertThat( job.lastBuiltOnProperty().get(), is( database.jenkinsNodes().get( 0 ) ) );
      assertThat( database.jenkinsNodes().get( 0 ).nameProperty().get(), is( MASTER_NAME ) );
      assertThat( database.jenkinsNodes().get( 0 ), is( masterNode ) );
   }//End Method

}//End Class
