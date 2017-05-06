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

import uk.dangrew.jtt.desktop.api.handling.live.JobBuiltEvent;
import uk.dangrew.jtt.desktop.api.handling.live.JobBuiltIdentifier;
import uk.dangrew.jtt.desktop.api.handling.live.JobBuiltResult;
import uk.dangrew.jtt.model.event.structure.Event;
import uk.dangrew.jtt.model.event.structure.EventSubscription;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

public class JobBuiltIdentifierTest {

   private JenkinsJob job1;
   private JenkinsJob job2;
   
   @Mock private EventSubscription< JobBuiltResult > subscription;
   @Captor private ArgumentCaptor< Event< JobBuiltResult > > eventCaptor;
   
   private JobBuiltIdentifier systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      MockitoAnnotations.initMocks( this );
      JobBuiltEvent events = new JobBuiltEvent();
      events.clearAllSubscriptions();
      events.register( subscription );
      
      job1 = new JenkinsJobImpl( "Job1" );
      job2 = new JenkinsJobImpl( "Job2" );
      systemUnderTest = new JobBuiltIdentifier();
   }//End Method

   @Test public void shouldTakeResultWhenBuildingStartedAndNotifyCurrentWhenBuilt() {
      job1.setBuildNumber( 123 );
      job1.setBuildStatus( BuildResultStatus.FAILURE );
      job1.buildStateProperty().set( BuildState.Building );
      systemUnderTest.recordState( job1 );
      
      job1.setBuildNumber( 124 );
      job1.setBuildStatus( BuildResultStatus.SUCCESS );
      job1.buildStateProperty().set( BuildState.Built );
      
      assertThatNoEventsHaveBeenFired();
      systemUnderTest.identifyStateChanges();
      
      assertThatJobBuiltResultProvidesStatus( job1, 0, 1, BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS );
   }//End Method
   
   @Test public void shouldNotifyForMultipleJobs() {
      job1.setBuildNumber( 123 );
      job1.setBuildStatus( BuildResultStatus.FAILURE );
      job1.buildStateProperty().set( BuildState.Building );
      systemUnderTest.recordState( job1 );
      
      job2.setBuildNumber( 234 );
      job2.setBuildStatus( BuildResultStatus.SUCCESS );
      job2.buildStateProperty().set( BuildState.Building );
      systemUnderTest.recordState( job2 );
      
      job1.setBuildNumber( 124 );
      job1.setBuildStatus( BuildResultStatus.SUCCESS );
      job1.buildStateProperty().set( BuildState.Built );
      
      job2.setBuildNumber( 235 );
      job2.setBuildStatus( BuildResultStatus.SUCCESS );
      job2.buildStateProperty().set( BuildState.Built );
      
      assertThatNoEventsHaveBeenFired();
      systemUnderTest.identifyStateChanges();
      
      assertThatJobBuiltResultProvidesStatus( job1, 0, 2, BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS );
      assertThatJobBuiltResultProvidesStatus( job2, 1, 2, BuildResultStatus.SUCCESS, BuildResultStatus.SUCCESS );
   }//End Method
   
   @Test public void shouldTakeResultWhenBuildingStartedAndNotifyCurrentWhenBuiltEvenWhenSameStatus() {
      job1.setBuildNumber( 123 );
      job1.setBuildStatus( BuildResultStatus.FAILURE );
      job1.buildStateProperty().set( BuildState.Building );
      systemUnderTest.recordState( job1 );
      
      job1.setBuildNumber( 124 );
      job1.setBuildStatus( BuildResultStatus.FAILURE );
      job1.buildStateProperty().set( BuildState.Built );
      
      assertThatNoEventsHaveBeenFired();
      systemUnderTest.identifyStateChanges();
      
      assertThatJobBuiltResultProvidesStatus( job1, 0, 1, BuildResultStatus.FAILURE, BuildResultStatus.FAILURE );
   }//End Method
   
   private void assertThatNoEventsHaveBeenFired(){
      verifyZeroInteractions( subscription );
   }//End Method
   
   /**
    * Method to assert that the relevant notification has been triggered.
    * @param job the {@link JenkinsJob}.
    * @param eventIndex the index of the event in the captors values.
    * @param verifications the number of events expected.
    * @param previous the previous {@link BuildResultStatus}.
    * @param current the current {@link BuildResultStatus}.
    */
   private void assertThatJobBuiltResultProvidesStatus( JenkinsJob job, int eventIndex, int verifications, BuildResultStatus previous, BuildResultStatus current ) {
      verify( subscription, times( verifications ) ).notify( eventCaptor.capture() );
      Event< JobBuiltResult > resultEvent = eventCaptor.getAllValues().get( eventIndex );
      JobBuiltResult result = resultEvent.getValue();
      assertThat( result.getJenkinsJob(), is( job ) );
      assertThat( result.getPreviousStatus(), is( previous ) );
      assertThat( result.getCurrentStatus(), is( current ) );
   }//End Method
   
   @Test public void shouldImmediatelyNotifyIfWasBuildingAndBuildNumberHasChanged() {
      job1.setBuildNumber( 123 );
      job1.setBuildStatus( BuildResultStatus.FAILURE );
      job1.buildStateProperty().set( BuildState.Building );
      systemUnderTest.recordState( job1 );
      
      job1.setBuildNumber( 124 );
      job1.setBuildStatus( BuildResultStatus.SUCCESS );
      job1.buildStateProperty().set( BuildState.Building );
      
      assertThatNoEventsHaveBeenFired();
      systemUnderTest.identifyStateChanges();
      
      assertThatJobBuiltResultProvidesStatus( job1, 0, 1, BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS );
   }//End Method
   
   @Test public void shouldImmediatelyNotifyIfWasBuiltAndBuildNumberHasChanged() {
      job1.setBuildNumber( 123 );
      job1.setBuildStatus( BuildResultStatus.FAILURE );
      job1.buildStateProperty().set( BuildState.Built );
      systemUnderTest.recordState( job1 );
      
      job1.setBuildNumber( 124 );
      job1.setBuildStatus( BuildResultStatus.SUCCESS );
      job1.buildStateProperty().set( BuildState.Built );
      
      assertThatNoEventsHaveBeenFired();
      systemUnderTest.identifyStateChanges();
      
      assertThatJobBuiltResultProvidesStatus( job1, 0, 1, BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS );
   }//End Method
   
   @Test public void shouldNotNotifyIfStillBuildingWithSameBuildNumber() {
      job1.setBuildNumber( 123 );
      job1.setBuildStatus( BuildResultStatus.FAILURE );
      job1.buildStateProperty().set( BuildState.Building );
      systemUnderTest.recordState( job1 );
      
      job1.setBuildNumber( 123 );
      job1.setBuildStatus( BuildResultStatus.FAILURE );
      job1.buildStateProperty().set( BuildState.Building );
      
      assertThatNoEventsHaveBeenFired();
      systemUnderTest.identifyStateChanges();
      assertThatNoEventsHaveBeenFired();
   }//End Method

   @Test public void shouldUpdateStateAfterNotifying() {
      job1.setBuildNumber( 123 );
      job1.setBuildStatus( BuildResultStatus.FAILURE );
      job1.buildStateProperty().set( BuildState.Building );
      systemUnderTest.recordState( job1 );
      
      job1.setBuildNumber( 123 );
      job1.setBuildStatus( BuildResultStatus.SUCCESS );
      job1.buildStateProperty().set( BuildState.Built );
      
      assertThatNoEventsHaveBeenFired();
      systemUnderTest.identifyStateChanges();
      assertThatJobBuiltResultProvidesStatus( job1, 0, 1, BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS );
      systemUnderTest.identifyStateChanges();
      assertThatJobBuiltResultProvidesStatus( job1, 0, 1, BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS );
   }//End Method
   
   @Test public void shouldNotUpdateDetailsWhenRecording(){
      job1.setBuildNumber( 123 );
      job1.setBuildStatus( BuildResultStatus.FAILURE );
      job1.buildStateProperty().set( BuildState.Building );
      systemUnderTest.recordState( job1 );
      
      job1.setBuildNumber( 123 );
      job1.setBuildStatus( BuildResultStatus.SUCCESS );
      job1.buildStateProperty().set( BuildState.Built );
      systemUnderTest.recordState( job1 );
      
      assertThatNoEventsHaveBeenFired();
      systemUnderTest.identifyStateChanges();
      assertThatJobBuiltResultProvidesStatus( job1, 0, 1, BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS );
   }//End Method

}//End Class
