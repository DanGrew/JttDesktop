/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.api.handling.live;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.collections.ListChangeListener;
import uk.dangrew.jtt.desktop.api.handling.live.JobDetailsModel;
import uk.dangrew.jtt.desktop.api.handling.live.StateChangeIdentifier;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.nodes.JenkinsNodeImpl;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.model.storage.database.TestJenkinsDatabaseImpl;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;

public class JobDetailsModelTest {

   private static final String ANYTHING = "anything";
   
   private JenkinsJob job;
   private JenkinsNode node;
   private JenkinsUser user;
   @Mock private StateChangeIdentifier statusChanges;
   private JenkinsDatabase database;
   private JobDetailsModel systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      job = new JenkinsJobImpl( "Job" );
      node = new JenkinsNodeImpl( "Node" );
      user = new JenkinsUserImpl( "User" );
      database = new TestJenkinsDatabaseImpl();
      systemUnderTest = new JobDetailsModel( database, statusChanges );
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
      systemUnderTest.setSkipCount( ANYTHING, 87 );
      systemUnderTest.setTimestamp( ANYTHING, 345678L );
      systemUnderTest.setTotalTestCount( ANYTHING, 9876 );
      systemUnderTest.addCulprit( ANYTHING, "Dan" );
      
      systemUnderTest.startLastBuild( ANYTHING );
      systemUnderTest.setResultingState( ANYTHING, BuildResultStatus.UNSTABLE );
      systemUnderTest.startLastCompletedBuild( ANYTHING );
      systemUnderTest.setResultingState( ANYTHING, BuildResultStatus.FAILURE );
      
      systemUnderTest.startJob( ANYTHING );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.finishJob( ANYTHING );
      
      assertThat( job.buildStateProperty().get(), is( BuildState.Built ) );
      assertThat( job.getBuildNumber(), is( not( 101 ) ) );
      assertThat( job.builtOnProperty().get(), is( nullValue() ) );
      assertThat( job.totalBuildTimeProperty().get(), is( JenkinsJob.DEFAULT_TOTAL_BUILD_TIME ) );
      assertThat( job.expectedBuildTimeProperty().get(), is( JenkinsJob.DEFAULT_EXPECTED_BUILD_TIME ) );
      assertThat( job.testFailureCount().get(), is( JenkinsJob.DEFAULT_FAILURE_COUNT ) );
      assertThat( job.testSkipCount().get(), is( JenkinsJob.DEFAULT_SKIP_COUNT ) );
      assertThat( job.testTotalCount().get(), is( JenkinsJob.DEFAULT_TOTAL_TEST_COUNT ) );
      assertThat( job.getBuildStatus(), is( JenkinsJob.DEFAULT_LAST_BUILD_STATUS ) );
      assertThat( job.buildTimestampProperty().get(), is( JenkinsJob.DEFAULT_BUILD_TIMESTAMP ) );
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
      assertThat( job.builtOnProperty().get(), is( database.getJenkinsNode( node ) ) );
   }//End Method
   
   @Test public void shouldUseNodeIfItDoesExist() {
      database.store( job );
      database.store( node );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuiltOn( ANYTHING, node.nameProperty().get() );
      systemUnderTest.finishJob( ANYTHING );
      
      assertThat( job.builtOnProperty().get(), is( node ) );
   }//End Method
   
   @Test public void shoulduUseMasterNodeIfNameIsNotFullString(){
      database.store( job );
      node.nameProperty().set( JobDetailsModel.MASTER_NAME );
      database.store( node );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuiltOn( ANYTHING, JobDetailsModel.MASTER_ID );
      systemUnderTest.finishJob( ANYTHING );
      
      assertThat( job.builtOnProperty().get(), is( node ) );
   }//End Method
   
   @Test public void shouldCreateMasterNodeIfNameIsNotFullString(){
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuiltOn( ANYTHING, JobDetailsModel.MASTER_ID );
      systemUnderTest.finishJob( ANYTHING );
      
      JenkinsNode master = database.getJenkinsNode( JobDetailsModel.MASTER_NAME );
      assertThat( job.builtOnProperty().get(), is( master ) );
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
      assertThat( job.getBuildNumber(), is( 456 ) );
   }//End Method
   
   @Test public void shouldResetCurrentProgressWhenBuildNumberChanges() {
      job.currentBuildTimeProperty().set( 100 );
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuildNumber( ANYTHING, 456 );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.getBuildNumber(), is( 456 ) );
      assertThat( job.currentBuildTimeProperty().get(), is( 0L ) );
   }//End Method
   
   @Test public void shouldHoldCurrentResultAndUseWhenPopulating() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.startLastBuild( ANYTHING );
      systemUnderTest.setResultingState( ANYTHING, BuildResultStatus.UNSTABLE );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.getBuildStatus(), is( BuildResultStatus.UNSTABLE ) );
   }//End Method
   
   @Test public void shouldHoldPreviousResultAndUseWhenPopulating() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.startLastCompletedBuild( ANYTHING );
      systemUnderTest.setResultingState( ANYTHING, BuildResultStatus.UNSTABLE );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.getBuildStatus(), is( BuildResultStatus.UNSTABLE ) );
   }//End Method
   
   @Test public void shouldCurrentResultAndPreviousAndUseCurrentWhenPresent() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.startLastBuild( ANYTHING );
      systemUnderTest.setResultingState( ANYTHING, BuildResultStatus.UNSTABLE );
      systemUnderTest.startLastCompletedBuild( ANYTHING );
      systemUnderTest.setResultingState( ANYTHING, BuildResultStatus.FAILURE );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.getBuildStatus(), is( BuildResultStatus.UNSTABLE ) );
   }//End Method
   
   @Test public void shouldCurrentResultAndPreviousAndUsePreviousWhenCurrentNotPresent() {
      database.store( job );
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.startLastBuild( ANYTHING );
      systemUnderTest.setResultingState( ANYTHING, null );
      systemUnderTest.startLastCompletedBuild( ANYTHING );
      systemUnderTest.setResultingState( ANYTHING, BuildResultStatus.FAILURE );
      systemUnderTest.finishJob( ANYTHING );
      assertThat( job.getBuildStatus(), is( BuildResultStatus.FAILURE ) );
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
      assertThat( job.buildTimestampProperty().get(), is( 12345L ) );
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
      job.setBuildNumber( 101 );
      job.builtOnProperty().set( node );
      job.totalBuildTimeProperty().set( 12345L );
      job.expectedBuildTimeProperty().set( 12345L );
      job.testFailureCount().set( 456 );
      job.testSkipCount().set( 456 );
      job.testTotalCount().set( 456 );
      job.setBuildStatus( BuildResultStatus.UNSTABLE );
      job.buildTimestampProperty().set( 12345L );
      job.culprits().add( user );
      
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.finishJob( ANYTHING );
      
      assertThat( job.buildStateProperty().get(), is( BuildState.Building ) );
      assertThat( job.getBuildNumber(), is( 101 ) );
      assertThat( job.builtOnProperty().get(), is( node ) );
      assertThat( job.totalBuildTimeProperty().get(), is( 12345L ) );
      assertThat( job.expectedBuildTimeProperty().get(), is( 12345L ) );
      assertThat( job.testFailureCount().get(), is( 456 ) );
      assertThat( job.testSkipCount().get(), is( 456 ) );
      assertThat( job.testTotalCount().get(), is( 456 ) );
      assertThat( job.getBuildStatus(), is( BuildResultStatus.UNSTABLE ) );
      assertThat( job.buildTimestampProperty().get(), is( 12345L ) );
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
   
   @Test public void shouldRecordStateChangesBeforeApplyingAnyIdentifyingAfter(){
      database.store( job );
      
      doAnswer( i -> {
         assertThat( job.buildStateProperty().get(), is( BuildState.Built ) );
         assertThat( job.getBuildNumber(), is( not( 101 ) ) );
         assertThat( job.builtOnProperty().get(), is( nullValue() ) );
         assertThat( job.totalBuildTimeProperty().get(), is( JenkinsJob.DEFAULT_TOTAL_BUILD_TIME ) );
         assertThat( job.expectedBuildTimeProperty().get(), is( JenkinsJob.DEFAULT_EXPECTED_BUILD_TIME ) );
         assertThat( job.testFailureCount().get(), is( JenkinsJob.DEFAULT_FAILURE_COUNT ) );
         assertThat( job.testSkipCount().get(), is( JenkinsJob.DEFAULT_SKIP_COUNT ) );
         assertThat( job.testTotalCount().get(), is( JenkinsJob.DEFAULT_TOTAL_TEST_COUNT ) );
         assertThat( job.getBuildStatus(), is( JenkinsJob.DEFAULT_LAST_BUILD_STATUS ) );
         assertThat( job.buildTimestampProperty().get(), is( JenkinsJob.DEFAULT_BUILD_TIMESTAMP ) );
         assertThat( job.culprits(), is( empty() ) );
         return null;
      } ).when( statusChanges ).recordState( job );
      
      doAnswer( i -> {
         assertThat( job.buildStateProperty().get(), is( BuildState.Building ) );
         assertThat( job.getBuildNumber(), is( 101 ) );
         assertThat( job.builtOnProperty().get(), is( notNullValue() ) );
         assertThat( job.totalBuildTimeProperty().get(), is( 123456L ) );
         assertThat( job.expectedBuildTimeProperty().get(), is( 234567L ) );
         assertThat( job.testFailureCount().get(), is( 98 ) );
         assertThat( job.testSkipCount().get(), is( 87 ) );
         assertThat( job.testTotalCount().get(), is( 9876 ) );
         assertThat( job.getBuildStatus(), is( BuildResultStatus.UNSTABLE ) );
         assertThat( job.buildTimestampProperty().get(), is( 345678L ) );
         assertThat( job.culprits(), is( not( empty() ) ) );
         return null;
      } ).when( statusChanges ).identifyStateChanges();
      
      systemUnderTest.setJobName( ANYTHING, job.nameProperty().get() );
      systemUnderTest.setBuildingState( ANYTHING, true );
      systemUnderTest.setBuildNumber( ANYTHING, 101 );
      systemUnderTest.setBuiltOn( ANYTHING, "Node" );
      systemUnderTest.setDuration( ANYTHING, 123456L );
      systemUnderTest.setEstimatedDuration( ANYTHING, 234567L );
      systemUnderTest.setFailCount( ANYTHING, 98 );
      systemUnderTest.setSkipCount( ANYTHING, 87 );
      systemUnderTest.setTimestamp( ANYTHING, 345678L );
      systemUnderTest.setTotalTestCount( ANYTHING, 9876 );
      systemUnderTest.addCulprit( ANYTHING, "Dan" );
      
      systemUnderTest.startLastBuild( ANYTHING );
      systemUnderTest.setResultingState( ANYTHING, BuildResultStatus.UNSTABLE );
      
      systemUnderTest.finishJob( ANYTHING );
      verify( statusChanges ).recordState( job );
      verify( statusChanges ).identifyStateChanges();
   }//End Method

}//End Class
