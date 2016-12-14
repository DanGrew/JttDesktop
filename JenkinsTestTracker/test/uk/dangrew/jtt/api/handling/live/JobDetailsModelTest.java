/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling.live;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.ListChangeListener;
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

public class JobDetailsModelTest {

   private static final String ANYTHING = "anything";
   
   private JenkinsJob job;
   private JenkinsNode node;
   private JenkinsUser user;
   private JenkinsDatabase database;
   private JobDetailsModel systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      job = new JenkinsJobImpl( "Job" );
      node = new JenkinsNodeImpl( "Node" );
      user = new JenkinsUserImpl( "User" );
      database = new JenkinsDatabaseImpl();
      systemUnderTest = new JobDetailsModel( database );
   }//End Method

   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullDatabase() {
      new JobDetailsModel( null );
   }//End Method
   
   @Test public void shouldClearPreviousJobDetailsWhenStartingNewJob() {
      database.store( job );
      
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuildingState( ANYTHING, true );
      systemUnderTest.setBuildNumber( ANYTHING, 101 );
      systemUnderTest.setBuiltOn( ANYTHING, "Node" );
      systemUnderTest.setDuration( ANYTHING, 123456L );
      systemUnderTest.setEstimatedDuration( ANYTHING, 234567L );
      systemUnderTest.setFailCount( ANYTHING, 98 );
      systemUnderTest.setResultingState( ANYTHING, BuildResultStatus.UNSTABLE );
      systemUnderTest.setSkipCount( ANYTHING, 87 );
      systemUnderTest.setTimestamp( ANYTHING, 345678L );
      systemUnderTest.setTotalTestCount( ANYTHING, 9876 );
      systemUnderTest.addCulprit( ANYTHING, "Dan" );
      
      systemUnderTest.startJob( ANYTHING );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.finishJob( ANYTHING );
      
      assertThat( job.buildStateProperty().get(), is( BuildState.Built ) );
      assertThat( job.getLastBuildNumber(), is( not( 101 ) ) );
      assertThat( job.currentBuildNumberProperty().get(), is( not( 101 ) ) );
      assertThat( job.lastBuiltOnProperty().get(), is( nullValue() ) );
      assertThat( job.totalBuildTimeProperty().get(), is( JenkinsJob.DEFAULT_TOTAL_BUILD_TIME ) );
      assertThat( job.expectedBuildTimeProperty().get(), is( JenkinsJob.DEFAULT_EXPECTED_BUILD_TIME ) );
      assertThat( job.testFailureCount().get(), is( JenkinsJob.DEFAULT_FAILURE_COUNT ) );
      assertThat( job.testSkipCount().get(), is( JenkinsJob.DEFAULT_SKIP_COUNT ) );
      assertThat( job.testTotalCount().get(), is( JenkinsJob.DEFAULT_TOTAL_TEST_COUNT ) );
      assertThat( job.getLastBuildStatus(), is( JenkinsJob.DEFAULT_LAST_BUILD_STATUS ) );
      assertThat( job.currentBuildTimestampProperty().get(), is( JenkinsJob.DEFAULT_BUILD_TIMESTAMP ) );
      assertThat( job.culprits(), is( empty() ) );
   }//End Method
   
   @Test public void shouldRetrieveExistingJobAndPopulateDetailsWhenFinishingJob() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuildingState( ANYTHING, true );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.buildStateProperty().get(), is( BuildState.Building ) ); 
   }//End Method
   
   @Test public void shouldCreateMissingJobAndPopulateDetailsWhenFinishingJob() {
      String newJob = "job-to-create";
      systemUnderTest.setJobName( ANYTHING, newJob );
      systemUnderTest.setBuildingState( ANYTHING, true );
      systemUnderTest.finishJob( ANYTHING );
      
      assertThat( database.hasJenkinsJob( newJob ), is( true ) );
      assertThat( database.getJenkinsJob( newJob ).buildStateProperty().get(), is( BuildState.Building ) );
   }//End Method
   
   @Test public void shouldCreateNodeIfItDoesntExist() {
      String node = "Node";
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuiltOn( ANYTHING, node );
      systemUnderTest.finishJob( ANYTHING );
      
      assertThat( database.hasJenkinsNode( node ), is( true ) );
      assertThat( job.lastBuiltOnProperty().get(), is( database.getJenkinsNode( node ) ) );
   }//End Method
   
   @Test public void shouldUseNodeIfItDoesExist() {
      database.store( job );
      database.store( node );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuiltOn( ANYTHING, node.nameProperty().get() );
      systemUnderTest.finishJob( ANYTHING );
      
      assertThat( job.lastBuiltOnProperty().get(), is( node ) );
   }//End Method
   
   @Test public void shoulduUseMasterNodeIfNameIsNotFullString(){
      database.store( job );
      node.nameProperty().set( JobDetailsModel.MASTER_NAME );
      database.store( node );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuiltOn( ANYTHING, JobDetailsModel.MASTER_ID );
      systemUnderTest.finishJob( ANYTHING );
      
      assertThat( job.lastBuiltOnProperty().get(), is( node ) );
   }//End Method
   
   @Test public void shouldCreateMasterNodeIfNameIsNotFullString(){
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuiltOn( ANYTHING, JobDetailsModel.MASTER_ID );
      systemUnderTest.finishJob( ANYTHING );
      
      JenkinsNode master = database.getJenkinsNode( JobDetailsModel.MASTER_NAME );
      assertThat( job.lastBuiltOnProperty().get(), is( master ) );
   }//End Method
   
   @Test public void shouldCreateUserIfItDoesntExist() {
      String user = "User";
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.addCulprit( ANYTHING, user );
      systemUnderTest.finishJob( ANYTHING );
      
      assertThat( database.hasJenkinsUser( user ), is( true ) );
      assertThat( job.culprits().get( 0 ), is( database.getJenkinsUser( user ) ) );
   }//End Method
   
   @Test public void shouldUseUserIfItDoesExist() {
      database.store( job );
      database.store( user );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.addCulprit( ANYTHING, user.nameProperty().get() );
      systemUnderTest.finishJob( ANYTHING );
      
      assertThat( job.culprits().get( 0 ), is( user ) );
   }//End Method
   
   @Test public void shouldAssignMultipleCulprits() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.addCulprit( ANYTHING, "first" );
      systemUnderTest.addCulprit( ANYTHING, "second" );
      systemUnderTest.finishJob( ANYTHING );
      
      assertThat( job.culprits().get( 0 ).nameProperty().get(), is( "first" ) );
      assertThat( job.culprits().get( 1 ).nameProperty().get(), is( "second" ) );
   }//End Method
   
   @Test public void shouldIgnoreDetailsProvidedWithoutJobName() {
      systemUnderTest.setBuildingState( ANYTHING, true );
      systemUnderTest.finishJob( ANYTHING );
   }//End Method
   
   @Test public void shouldHoldBuildingStateAndUseWhenPopulating() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuildingState( ANYTHING, true );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.buildStateProperty().get(), is( BuildState.Building ) ); 
   }//End Method
   
   @Test public void shouldResetCurrentBuildTimeWhenBuilt(){
      job.currentBuildTimeProperty().set( 100 );
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuildingState( ANYTHING, false );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.currentBuildTimeProperty().get(), is( 0L ) );
   }//End Method
   
   @Test public void shouldNotResetCurrentBuildTimeWhenBuilding(){
      job.currentBuildTimeProperty().set( 100 );
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuildingState( ANYTHING, true );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.currentBuildTimeProperty().get(), is( 100L ) );
   }//End Method
   
   @Test public void shouldHoldDurationAndUseWhenPopulating() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setDuration( ANYTHING, 12345L );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.totalBuildTimeProperty().get(), is( 12345L ) );
   }//End Method
   
   @Test public void shouldHoldEstimatedDurationAndUseWhenPopulating() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setEstimatedDuration( ANYTHING, 12345L );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.expectedBuildTimeProperty().get(), is( 12345L ) );
   }//End Method
   
   @Test public void shouldHoldFailCountAndUseWhenPopulating() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setFailCount( ANYTHING, 456 );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.testFailureCount().get(), is( 456 ) );
   }//End Method
   
   @Test public void shouldHoldBuildNumberAndUseWhenPopulating() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuildNumber( ANYTHING, 456 );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.getLastBuildNumber(), is( 456 ) );
      assertThat( job.currentBuildNumberProperty().get(), is( 456 ) );
   }//End Method
   
   @Test public void shouldHoldResultingStateAndUseWhenPopulating() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setResultingState( ANYTHING, BuildResultStatus.UNSTABLE );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.getLastBuildStatus(), is( BuildResultStatus.UNSTABLE ) );
   }//End Method
   
   @Test public void shouldHoldSkipCountAndUseWhenPopulating() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setSkipCount( ANYTHING, 456 );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.testSkipCount().get(), is( 456 ) );
   }//End Method
   
   @Test public void shouldHoldTimestampAndUseWhenPopulating() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setTimestamp( ANYTHING, 12345L );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.currentBuildTimestampProperty().get(), is( 12345L ) );
   }//End Method
   
   @Test public void shouldHoldTotalTestCountAndUseWhenPopulating() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setTotalTestCount( ANYTHING, 456 );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.testTotalCount().get(), is( 456 ) );
   }//End Method
   
   @Test public void shouldNotOverwriteValuesWhenNotSet(){
      database.store( job );
      
      job.buildStateProperty().set( BuildState.Building );
      job.setLastBuildNumber( 101 );
      job.currentBuildNumberProperty().set( 101 );
      job.lastBuiltOnProperty().set( node );
      job.totalBuildTimeProperty().set( 12345L );
      job.expectedBuildTimeProperty().set( 12345L );
      job.testFailureCount().set( 456 );
      job.testSkipCount().set( 456 );
      job.testTotalCount().set( 456 );
      job.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      job.currentBuildTimestampProperty().set( 12345L );
      job.culprits().add( user );
      
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.finishJob( ANYTHING );
      
      assertThat( job.buildStateProperty().get(), is( BuildState.Building ) );
      assertThat( job.getLastBuildNumber(), is( 101 ) );
      assertThat( job.currentBuildNumberProperty().get(), is( 101 ) );
      assertThat( job.lastBuiltOnProperty().get(), is( node ) );
      assertThat( job.totalBuildTimeProperty().get(), is( 12345L ) );
      assertThat( job.expectedBuildTimeProperty().get(), is( 12345L ) );
      assertThat( job.testFailureCount().get(), is( 456 ) );
      assertThat( job.testSkipCount().get(), is( 456 ) );
      assertThat( job.testTotalCount().get(), is( 456 ) );
      assertThat( job.getLastBuildStatus(), is( BuildResultStatus.UNSTABLE ) );
      assertThat( job.currentBuildTimestampProperty().get(), is( 12345L ) );
      assertThat( job.culprits(), is( empty() ) );
   }//End Method
   
   @Test public void shouldNotClearCulpritsWhenSame(){
      JenkinsUser user2 = new JenkinsUserImpl( "User2" );
      JenkinsUser user3 = new JenkinsUserImpl( "User3" );
      database.store( job );
      database.store( user );
      database.store( user2 );
      database.store( user3 );
      job.culprits().addAll( user, user2, user3 );
      
      @SuppressWarnings("unchecked") //mocking genericized 
      ListChangeListener< JenkinsUser > userChanges = mock( ListChangeListener.class );
      job.culprits().addListener( userChanges );
      
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.addCulprit( ANYTHING, user.nameProperty().get() );
      systemUnderTest.addCulprit( ANYTHING, user2.nameProperty().get() );
      systemUnderTest.addCulprit( ANYTHING, user3.nameProperty().get() );
      systemUnderTest.finishJob( ANYTHING );
      
      verifyZeroInteractions( userChanges );
   }//End Method

}//End Class
