/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database.events;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import javafx.util.Pair;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;

/**
 * {@link JenkinsJobPropertyListener} test.
 */
public class JenkinsJobPropertyListenerTest {

   private JenkinsDatabase databse;
   private JenkinsJob job1;
   private JenkinsJob job2;
   private JenkinsJob job3;
   
   private JenkinsJobPropertyListener systemUnderTest;
   private List< Pair< JenkinsJob, Pair< Integer, BuildResultStatus > > > buildResultStatusNotifications;
   private JttChangeListener< JenkinsJob, Pair< Integer, BuildResultStatus > > buildResultListener;
   private List< Pair< JenkinsJob, BuildState > > buildStateNotifications;
   private JttChangeListener< JenkinsJob, BuildState > buildStateListener;
   
   private List< Pair< JenkinsJob, Integer > > integerNotifications;
   private JttChangeListener< JenkinsJob, Integer > testTotalListener;
   private JttChangeListener< JenkinsJob, Integer > testFailuresListener;
   
   @Before public void initialiseSystemUnderTest(){
      databse = new JenkinsDatabaseImpl();
      job1 = new JenkinsJobImpl( "first job" );
      job2 = new JenkinsJobImpl( "second job" );
      job3 = new JenkinsJobImpl( "third job" );
      databse.store( job1 );
      databse.store( job2 );
      databse.store( job3 );
      
      systemUnderTest = new JenkinsJobPropertyListener( databse );
      buildResultStatusNotifications = new ArrayList<>();
      buildResultListener = ( job, old, updated ) -> buildResultStatusNotifications.add( 
               new Pair< JenkinsJob, Pair< Integer, BuildResultStatus > >( job, updated ) 
      );
      buildStateNotifications = new ArrayList<>();
      buildStateListener = ( job, old, updated ) -> buildStateNotifications.add( 
               new Pair< JenkinsJob, BuildState >( job, updated ) 
      );
      integerNotifications = new ArrayList<>();
      testFailuresListener = ( j, o, n ) -> integerNotifications.add(
               new Pair< JenkinsJob, Integer >( j, n ) 
      );
      testTotalListener = ( j, o, n ) -> integerNotifications.add(
               new Pair< JenkinsJob, Integer >( j, n ) 
      );
      
      systemUnderTest.addBuildResultStatusListener( buildResultListener );
      systemUnderTest.addBuildStateListener( buildStateListener );
      systemUnderTest.addTestTotalCountListener( testTotalListener );
      systemUnderTest.addTestFailureCountListener( testFailuresListener );
   }//End Method
   
   @Test public void shouldNotifyLastBuildResultStatusWhenChanged() {
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
      job1.setLastBuildStatus( BuildResultStatus.SUCCESS );
      job2.setLastBuildStatus( BuildResultStatus.FAILURE );
      
      assertThat( buildResultStatusNotifications, hasSize( 2 ) );
      Pair< JenkinsJob, Pair< Integer, BuildResultStatus > > result = buildResultStatusNotifications.remove( 0 );
      assertThat( result.getKey(), is( job1 ) );
      assertThat( result.getValue().getValue(), is( BuildResultStatus.SUCCESS ) );
      
      Pair< JenkinsJob, Pair< Integer, BuildResultStatus > > result2 = buildResultStatusNotifications.remove( 0 );
      assertThat( result2.getKey(), is( job2 ) );
      assertThat( result2.getValue().getValue(), is( BuildResultStatus.FAILURE ) );
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
   }//End Method
   
   @Test public void shouldNotifyBuildingStateWhenChanged() {
      assertThat( buildStateNotifications.isEmpty(), is( true ) );
      job1.buildStateProperty().set( BuildState.Building );
      job2.buildStateProperty().set( BuildState.Building );
      
      assertThat( buildStateNotifications, hasSize( 2 ) );
      Pair< JenkinsJob, BuildState > result = buildStateNotifications.remove( 0 );
      assertThat( result.getKey(), is( job1 ) );
      assertThat( result.getValue(), is( BuildState.Building ) );
      Pair< JenkinsJob, BuildState > result2 = buildStateNotifications.remove( 0 );
      assertThat( result2.getKey(), is( job2 ) );
      assertThat( result2.getValue(), is( BuildState.Building ) );
   }//End Method
   
   @Test public void shouldNotifyTestTotalWhenChanged() {
      assertThat( integerNotifications.isEmpty(), is( true ) );
      job1.testTotalCount().set( 100 );
      job2.testTotalCount().set( 765 );
      
      assertThat( integerNotifications, hasSize( 2 ) );
      Pair< JenkinsJob, Integer > result = integerNotifications.remove( 0 );
      assertThat( result.getKey(), is( job1 ) );
      assertThat( result.getValue(), is( 100 ) );
      Pair< JenkinsJob, Integer > result2 = integerNotifications.remove( 0 );
      assertThat( result2.getKey(), is( job2 ) );
      assertThat( result2.getValue(), is( 765 ) );
   }//End Method
   
   @Test public void shouldNotifyTestFailuresWhenChanged() {
      assertThat( integerNotifications.isEmpty(), is( true ) );
      job1.testFailureCount().set( 100 );
      job2.testFailureCount().set( 765 );
      
      assertThat( integerNotifications, hasSize( 2 ) );
      Pair< JenkinsJob, Integer > result = integerNotifications.remove( 0 );
      assertThat( result.getKey(), is( job1 ) );
      assertThat( result.getValue(), is( 100 ) );
      Pair< JenkinsJob, Integer > result2 = integerNotifications.remove( 0 );
      assertThat( result2.getKey(), is( job2 ) );
      assertThat( result2.getValue(), is( 765 ) );
   }//End Method
   
}//End Class
