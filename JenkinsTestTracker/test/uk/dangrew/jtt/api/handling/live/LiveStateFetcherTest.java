/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling.live;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import uk.dangrew.jtt.api.handling.JenkinsFetcher;
import uk.dangrew.jtt.api.sources.ExternalApi;
import uk.dangrew.jtt.api.sources.JenkinsBaseRequest;
import uk.dangrew.jtt.data.json.conversion.ApiResponseToJsonConverter;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;

@RunWith( JUnitParamsRunner.class )
public class LiveStateFetcherTest {
   
   private static final String API_RESPONSE = "The Api Response with lots of data.";
   
   private JenkinsJob job1;
   private JenkinsJob job2;
   private JenkinsJob job3;
   
   @Mock private ExternalApi api;
   @Mock private ApiResponseToJsonConverter converter;
   @Mock private JobDetailsParser parser;
   @Mock private JenkinsFetcher fetcher;
   private JenkinsDatabase database;
   private LiveStateFetcher systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      database = new JenkinsDatabaseImpl();
      job1 = new JenkinsJobImpl( "Job1" );
      job2 = new JenkinsJobImpl( "Job2" );
      job3 = new JenkinsJobImpl( "Job3" );
      database.store( job1 );
      database.store( job2 );
      database.store( job3 );
      
      systemUnderTest = new LiveStateFetcher( database, api, converter, parser, fetcher );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullDatabase(){
      new LiveStateFetcher( null, api );
   }//End Method
   
   @Test( expected = IllegalArgumentException.class ) public void shouldNotAcceptNullApi(){
      new LiveStateFetcher( database, null );
   }//End Method

   @Test public void shouldExecuteLastCompletedJobRequestAndParseIntoDatabase() {
      when( api.executeRequest( JenkinsBaseRequest.LastCompleteJobDetailsRequest ) ).thenReturn( API_RESPONSE );
      JSONObject converted = new JSONObject();
      when( converter.convert( API_RESPONSE ) ).thenReturn( converted );
      
      systemUnderTest.loadLastCompletedBuild();
      verify( parser ).parse( converted );
   }//End Method
   
   @Test public void shouldExecuteLastBuildJobRequestAndParseIntoDatabase() {
      when( api.executeRequest( JenkinsBaseRequest.CurrentJobDetailsRequest ) ).thenReturn( API_RESPONSE );
      JSONObject converted = new JSONObject();
      when( converter.convert( API_RESPONSE ) ).thenReturn( converted );
      
      systemUnderTest.updateBuildState();
      verify( parser ).parse( converted );
   }//End Method
   
   @Test public void shouldNotUpdateTestsForJobWhenUpdatingBuildStateIfStateHasntChanged(){
      systemUnderTest.updateBuildState();
      verifyZeroInteractions( fetcher );
   }//End Method
   
   @Test public void shouldNotUpdateTestsForJobWhenLoadingCompletedBuildsIfStateHasntChanged(){
      systemUnderTest.loadLastCompletedBuild();
      verifyZeroInteractions( fetcher );
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldNotUpdateTestsForJobWhenUpdatingBuildStateIfStateIsNotUnstable( BuildResultStatus status ){
      job1.setLastBuildStatus( status );
      job2.setLastBuildStatus( status );
      job3.setLastBuildStatus( status );
      
      systemUnderTest.updateBuildState();
      if ( status == BuildResultStatus.UNSTABLE ) {
         verify( fetcher ).updateTestResults( job1 );
         verify( fetcher ).updateTestResults( job2 );
         verify( fetcher ).updateTestResults( job3 );
      } else {
         verifyZeroInteractions( fetcher );
      }
   }//End Method
   
   @Parameters( source = BuildResultStatus.class )
   @Test public void shouldNotUpdateTestsForJobWhenLoadingCompletedBuildsIfStateIsNotUnstable( BuildResultStatus status ){
      job1.setLastBuildStatus( status );
      job2.setLastBuildStatus( status );
      job3.setLastBuildStatus( status );
      
      systemUnderTest.loadLastCompletedBuild();
      if ( status == BuildResultStatus.UNSTABLE ) {
         verify( fetcher ).updateTestResults( job1 );
         verify( fetcher ).updateTestResults( job2 );
         verify( fetcher ).updateTestResults( job3 );
      } else {
         verifyZeroInteractions( fetcher );
      }
   }//End Method
   
   @Test public void shouldUpdateTestsForJobWhenUpdatingBuildState(){
      job1.setLastBuildStatus( BuildResultStatus.SUCCESS );
      job2.setLastBuildStatus( BuildResultStatus.SUCCESS );
      job3.setLastBuildStatus( BuildResultStatus.SUCCESS );
      
      systemUnderTest.updateBuildState();
      job2.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      systemUnderTest.updateBuildState();
      
      verify( fetcher ).updateTestResults( job2 );
      verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldUpdateTestsForJobWhenLoadingCompletedBuilds(){
      job1.setLastBuildStatus( BuildResultStatus.SUCCESS );
      job2.setLastBuildStatus( BuildResultStatus.SUCCESS );
      job3.setLastBuildStatus( BuildResultStatus.SUCCESS );
      
      systemUnderTest.loadLastCompletedBuild();
      job2.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      systemUnderTest.loadLastCompletedBuild();
      
      verify( fetcher ).updateTestResults( job2 );
      verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldNotUpdateTestsForJobWhenUpdatingBuildStateIfBuilding(){
      job1.buildStateProperty().set( BuildState.Building );
      job1.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      systemUnderTest.updateBuildState();
      
      verifyZeroInteractions( fetcher );
   }//End Method
   
   @Test public void shouldNotUpdateTestsForJobWhenLoadingCompletedBuildsIfBuilding(){
      job1.buildStateProperty().set( BuildState.Building );
      job1.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      systemUnderTest.loadLastCompletedBuild();
      
      verifyZeroInteractions( fetcher );
   }//End Method
   
   @Test public void shouldNotUpdateTestsAgainForJobWhenUpdatingBuildState(){
      job1.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      
      systemUnderTest.updateBuildState();
      job1.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      systemUnderTest.updateBuildState();
      
      verify( fetcher, times( 1 ) ).updateTestResults( job1 );
      verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldNotUpdateTestsAgainForJobWhenLoadingCompletedBuilds(){
      job1.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      
      systemUnderTest.loadLastCompletedBuild();
      job1.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      systemUnderTest.loadLastCompletedBuild();
      
      verify( fetcher, times( 1 ) ).updateTestResults( job1 );
      verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldUpdateTestsAgainForJobWhenUpdatingBuildStateIfBuildNumberHasChanged(){
      job1.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      
      systemUnderTest.updateBuildState();
      job1.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      job1.setLastBuildNumber( 23 );
      systemUnderTest.updateBuildState();
      job1.setLastBuildNumber( new Integer( 23 ) );
      systemUnderTest.loadLastCompletedBuild();
      
      verify( fetcher, times( 2 ) ).updateTestResults( job1 );
      verifyNoMoreInteractions( fetcher );
   }//End Method
   
   @Test public void shouldUpdateTestsAgainForJobWhenLoadingCompletedBuildsIfBuildNumberHasChanged(){
      job1.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      
      systemUnderTest.loadLastCompletedBuild();
      job1.setLastBuildStatus( BuildResultStatus.UNSTABLE );
      job1.setLastBuildNumber( 23 );
      systemUnderTest.loadLastCompletedBuild();
      job1.setLastBuildNumber( new Integer( 23 ) );
      systemUnderTest.loadLastCompletedBuild();
      
      verify( fetcher, times( 2 ) ).updateTestResults( job1 );
      verifyNoMoreInteractions( fetcher );
   }//End Method

}//End Class
