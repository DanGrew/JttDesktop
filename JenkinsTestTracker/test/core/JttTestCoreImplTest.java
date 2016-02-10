/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import api.handling.BuildState;
import api.sources.ExternalApi;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;

/**
 * {@link JttTestCoreImpl} test.
 */
public class JttTestCoreImplTest {

   private JttTestCoreImpl systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new JttTestCoreImpl( Mockito.mock( ExternalApi.class ) );
   }//End Method
   
   @Test public void shouldNotHaveTimersAssociated() {
      Assert.assertFalse( systemUnderTest.getBuildProgressor().hasTimer() );
      Assert.assertFalse( systemUnderTest.getJobUpdater().hasTimer() );
   }//End Method
   
   @Test public void shouldSetCurrentTime(){
      systemUnderTest.setSimulatedTime( 10000l );
      Mockito.verifyZeroInteractions( systemUnderTest.getClock() );
      
      JenkinsJob job = new JenkinsJobImpl( "anything" );
      job.buildStateProperty().set( BuildState.Building );
      systemUnderTest.getJenkinsDatabase().store( job );
      
      systemUnderTest.getBuildProgressor().poll();
      Mockito.verify( systemUnderTest.getClock() ).millis();
   }//End Method

}//End Class
