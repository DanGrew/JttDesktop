/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database.events;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;

/**
 * {@link GlobalPropertyListenerImpl} test.
 */
public class GlobalPropertyListenerImplTest {

   private ObservableList< JenkinsJob > databaseJobs;
   private JenkinsJob job1;
   private JenkinsJob job2;
   private JenkinsJob job3;
   
   private GlobalPropertyListenerImpl< JenkinsJob, BuildResultStatus > systemUnderTest;
   
   private List< Pair< JenkinsJob, BuildResultStatus > > buildResultStatusNotifications;
   private JttChangeListener< JenkinsJob, BuildResultStatus > buildResultListener;
   
   @Before public void initialiseSystemUnderTest(){
      databaseJobs = FXCollections.observableArrayList();
      job1 = new JenkinsJobImpl( "first job" );
      job2 = new JenkinsJobImpl( "second job" );
      job3 = new JenkinsJobImpl( "third job" );
      databaseJobs.addAll( job1, job2, job3 );
      
      buildResultStatusNotifications = new ArrayList<>();
      buildResultListener = ( job, old, updated ) -> buildResultStatusNotifications.add( new Pair< JenkinsJob, BuildResultStatus >( job, updated ) );
      
      systemUnderTest = new GlobalPropertyListenerImpl<>( 
               databaseJobs,
               job -> { return job.lastBuildStatusProperty(); } 
      );
      systemUnderTest.addListener( buildResultListener );
   }//End Method
   
   @Test public void shouldDetectChangeInItemsAlreadyPresent() {
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
      job1.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      
      assertNextResult( job1, BuildResultStatus.SUCCESS );
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
   }//End Method
   
   @Test public void shouldNotNotifyLastBuildResultStatusWhenSameValueSet() {
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
      job1.lastBuildStatusProperty().set( job1.lastBuildStatusProperty().get() );
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
   }//End Method
   
   @Test public void shouldDetectChangeInNewItemsAdded() {
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
      
      JenkinsJob addedJob = new JenkinsJobImpl( "another job" );
      databaseJobs.add( addedJob );
      addedJob.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      
      assertNextResult( addedJob, BuildResultStatus.SUCCESS );
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
   }//End Method
   
   @Test public void shouldIgnoreChangeInItemsRemoved() {
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
      
      databaseJobs.remove( job1 );
      job1.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
   }//End Method
   
   @Test public void shouldIgnoreSameObjectAddedMultipleTimes() {
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
      
      databaseJobs.add( job1 );
      databaseJobs.add( job1 );
      databaseJobs.add( job1 );
      job1.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      
      assertNextResult( job1, BuildResultStatus.SUCCESS );
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
   }//End Method
   
   @Test public void shouldNotAllowSameListenerTwiceAndIgnore() {
      systemUnderTest.addListener( buildResultListener );
      shouldDetectChangeInItemsAlreadyPresent();
   }//End Method
   
   @Test public void shouldAllowListenersToBeRemoved() {
      systemUnderTest.removeListener( buildResultListener );
      
      job1.lastBuildStatusProperty().set( BuildResultStatus.SUCCESS );
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
   }//End Method
   
   private void assertNextResult( JenkinsJob job, BuildResultStatus resultStatus ) {
      assertThat( buildResultStatusNotifications.isEmpty(), is( false ) );
      Pair< JenkinsJob, BuildResultStatus > result = buildResultStatusNotifications.remove( 0 );
      assertThat( result.getKey(), is( job ) );
      assertThat( result.getValue(), is( resultStatus ) );
   }//End Method

}//End Class
