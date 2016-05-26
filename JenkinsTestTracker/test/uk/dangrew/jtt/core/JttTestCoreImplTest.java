/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.core;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import uk.dangrew.jtt.api.handling.BuildState;
import uk.dangrew.jtt.api.sources.ExternalApi;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

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
