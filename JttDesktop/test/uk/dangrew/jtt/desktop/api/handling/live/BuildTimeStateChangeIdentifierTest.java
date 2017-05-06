/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.api.handling.live;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.desktop.api.handling.live.BuildTimeStateChangeIdentifier;
import uk.dangrew.jtt.desktop.api.handling.live.JobBuiltEvent;
import uk.dangrew.jtt.desktop.api.handling.live.JobBuiltResult;
import uk.dangrew.jtt.desktop.api.handling.live.StateChangeIdentifier;
import uk.dangrew.jtt.model.event.structure.Event;
import uk.dangrew.jtt.model.event.structure.EventSubscription;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

public class BuildTimeStateChangeIdentifierTest {

   private JenkinsJob job;
   @Mock private EventSubscription< JobBuiltResult > subscription;
   
   @Captor private ArgumentCaptor< Event< JobBuiltResult > > eventCaptor;
   private StateChangeIdentifier systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      
      JobBuiltEvent events = new JobBuiltEvent();
      events.clearAllSubscriptions();
      events.register( subscription );
      
      job = new JenkinsJobImpl( "Job" );
      systemUnderTest = new BuildTimeStateChangeIdentifier();
   }//End Method

   @Test public void shouldNotifyJobBuiltEventWhenLastBuildTimeChanges() {
      job.totalBuildTimeProperty().set( 100L );
      job.setBuildStatus( BuildResultStatus.ABORTED );
      systemUnderTest.recordState( job );
      job.totalBuildTimeProperty().set( 101L );
      job.setBuildStatus( BuildResultStatus.SUCCESS );
      systemUnderTest.identifyStateChanges();
      
      assertThatJobBuiltResultProvidesStatus( BuildResultStatus.ABORTED, BuildResultStatus.SUCCESS );
   }//End Method
   
   @Test public void shouldNotifyJobBuiltEventWhenLastBuildTimeChangesAndResultIsIdentical() {
      job.totalBuildTimeProperty().set( 100L );
      job.setBuildStatus( BuildResultStatus.ABORTED );
      systemUnderTest.recordState( job );
      job.totalBuildTimeProperty().set( 101L );
      job.setBuildStatus( BuildResultStatus.ABORTED );
      systemUnderTest.identifyStateChanges();
      
      assertThatJobBuiltResultProvidesStatus( BuildResultStatus.ABORTED, BuildResultStatus.ABORTED );
   }//End Method
      
   private void assertThatJobBuiltResultProvidesStatus( BuildResultStatus previous, BuildResultStatus current ) {
      verify( subscription, times( 1 ) ).notify( eventCaptor.capture() );
      Event< JobBuiltResult > resultEvent = eventCaptor.getValue();
      JobBuiltResult result = resultEvent.getValue();
      assertThat( result.getJenkinsJob(), is( job ) );
      assertThat( result.getPreviousStatus(), is( previous ) );
      assertThat( result.getCurrentStatus(), is( current ) );
   }//End Method
   
   @Test public void shouldNotNotifyJobBuiltEventWhenLastBuildTimeIsIdentical() {
      job.totalBuildTimeProperty().set( 100L );
      systemUnderTest.recordState( job );
      job.totalBuildTimeProperty().set( 100L );
      systemUnderTest.identifyStateChanges();
      
      verifyZeroInteractions( subscription );
   }//End Method
   
   @Test public void shouldIgnoreJobWhenBuildingWhenBuildTimeIsSetToZero() {
      job.totalBuildTimeProperty().set( 100L );
      systemUnderTest.recordState( job );
      job.totalBuildTimeProperty().set( BuildTimeStateChangeIdentifier.BUILD_TIME_WHEN_BUILDING );
      systemUnderTest.identifyStateChanges();
      
      verifyZeroInteractions( subscription );
   }//End Method
   
   @Test( expected = IllegalStateException.class ) public void shouldNotAcceptUpdateWhenNothingRecorded(){
      systemUnderTest.identifyStateChanges();
   }//End Method
   
   @Test public void shouldHandleResultUpdatingBeforeJobCompleting(){
      job.buildStateProperty().set( BuildState.Building );
      job.setBuildStatus( BuildResultStatus.FAILURE );
      systemUnderTest.recordState( job );
      
      job.setBuildStatus( BuildResultStatus.SUCCESS );
      systemUnderTest.identifyStateChanges();
      assertThatJobBuiltResultProvidesStatus( BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS );
      
      systemUnderTest.recordState( job );
      job.totalBuildTimeProperty().set( 100L );
      systemUnderTest.identifyStateChanges();
      assertThatJobBuiltResultProvidesStatus( BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS );
   }//End Method
   
   @Test public void shouldNotifyOnlyOnceWhenResultAndTimeChangedInSameUpdate(){
      job.buildStateProperty().set( BuildState.Building );
      job.setBuildStatus( BuildResultStatus.FAILURE );
      systemUnderTest.recordState( job );
      
      job.setBuildStatus( BuildResultStatus.SUCCESS );
      job.totalBuildTimeProperty().set( 100L );
      systemUnderTest.identifyStateChanges();
      assertThatJobBuiltResultProvidesStatus( BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS );
   }//End Method

}//End Class
