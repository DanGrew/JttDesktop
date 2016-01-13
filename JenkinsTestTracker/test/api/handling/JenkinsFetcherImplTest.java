/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.handling;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import api.sources.ExternalApi;
import data.json.jobs.JenkinsJobImpl;
import model.jobs.BuildResultStatus;
import model.jobs.JenkinsJob;
import storage.database.JenkinsDatabase;
import utility.TestCommon;

/**
 * {@link JenkinsFetcherImpl} test.
 */
public class JenkinsFetcherImplTest {

   private JenkinsJob jenkinsJob;
   private JenkinsDatabase database;
   private ExternalApi externalApi;
   private Fetcher systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      externalApi = Mockito.mock( ExternalApi.class );
      database = Mockito.mock( JenkinsDatabase.class );
      jenkinsJob = new JenkinsJobImpl( "JenkinsJob" );
      database.store( jenkinsJob );
      systemUnderTest = new JenkinsFetcherImpl( externalApi );
   }//End Method

   @Test public void shouldUpdateJobBuilding() {
      jenkinsJob.buildStateProperty().set( BuildState.Built );
      Assert.assertEquals( BuildState.Built, jenkinsJob.buildStateProperty().get() );
      
      String response = TestCommon.readFileIntoString( getClass(), "building-state.json" );
      Mockito.when( externalApi.getLastBuildBuildingState( jenkinsJob ) ).thenReturn( response );
      systemUnderTest.updateBuildState( jenkinsJob );
      Assert.assertEquals( BuildState.Building, jenkinsJob.buildStateProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateJobBuilt() {
      jenkinsJob.buildStateProperty().set( BuildState.Building );
      Assert.assertEquals( BuildState.Building, jenkinsJob.buildStateProperty().get() );
      
      String response = TestCommon.readFileIntoString( getClass(), "built-state.json" );
      Mockito.when( externalApi.getLastBuildBuildingState( jenkinsJob ) ).thenReturn( response );
      systemUnderTest.updateBuildState( jenkinsJob );
      Assert.assertEquals( BuildState.Built, jenkinsJob.buildStateProperty().get() );
   }//End Method
   
   @Test public void shouldUpdateJobDetailsWhenBuilt() {
      String buildingResponse = TestCommon.readFileIntoString( getClass(), "built-state.json" );
      Mockito.when( externalApi.getLastBuildBuildingState( jenkinsJob ) ).thenReturn( buildingResponse );
      
      String response = TestCommon.readFileIntoString( getClass(), "job-details.json" );
      Mockito.when( externalApi.getLastBuildJobDetails( jenkinsJob ) ).thenReturn( response );
      
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      systemUnderTest.updateJobDetails( jenkinsJob );
      Assert.assertEquals( 22, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.SUCCESS, jenkinsJob.lastBuildStatusProperty().get() );
   }//End Method
   
   @Test public void shouldNotUpdateJobDetailsWhenBuilding() {
      String buildingResponse = TestCommon.readFileIntoString( getClass(), "building-state.json" );
      Mockito.when( externalApi.getLastBuildBuildingState( jenkinsJob ) ).thenReturn( buildingResponse );

      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      systemUnderTest.updateJobDetails( jenkinsJob );
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      
      Mockito.verify( externalApi, Mockito.times( 0 ) ).getLastBuildJobDetails( Mockito.any() );
   }//End Method
   
   @Test public void shouldNotUpdateJobDetailsWhenNullJob() {
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      systemUnderTest.updateJobDetails( null );
      Assert.assertEquals( 0, jenkinsJob.lastBuildNumberProperty().get() );
      Assert.assertEquals( BuildResultStatus.FAILURE, jenkinsJob.lastBuildStatusProperty().get() );
      
      Mockito.verify( externalApi, Mockito.times( 0 ) ).getLastBuildJobDetails( Mockito.any() );
      Mockito.verify( externalApi, Mockito.times( 0 ) ).getLastBuildBuildingState( Mockito.any() );
   }//End Method
   
}//End Class
