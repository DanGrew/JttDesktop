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
import uk.dangrew.jtt.model.nodes.JenkinsNode;
import uk.dangrew.jtt.model.nodes.JenkinsNodeImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.TestJenkinsDatabaseImpl;

/**
 * {@link JenkinsJobPropertyListener} test.
 */
public class JenkinsJobPropertyListenerTest {

   private JenkinsDatabase databse;
   private JenkinsJob job1;
   private JenkinsJob job2;
   private JenkinsJob job3;
   private JenkinsNode node1;
   private JenkinsNode node2;
   
   private JenkinsJobPropertyListener systemUnderTest;
   private List< Pair< JenkinsJob, Pair< Integer, BuildResultStatus > > > buildResultStatusNotifications;
   private JttChangeListener< JenkinsJob, Pair< Integer, BuildResultStatus > > buildResultListener;
   private List< Pair< JenkinsJob, BuildState > > buildStateNotifications;
   private JttChangeListener< JenkinsJob, BuildState > buildStateListener;
   private List< Pair< JenkinsJob, JenkinsNode > > builtOnNotifications;
   private JttChangeListener< JenkinsJob, JenkinsNode > builtOnListener;
   
   private List< Pair< JenkinsJob, Integer > > integerNotifications;
   private JttChangeListener< JenkinsJob, Integer > testTotalListener;
   private JttChangeListener< JenkinsJob, Integer > testFailuresListener;
   
   private List< Pair< JenkinsJob, Long > > longNotifications;
   private JttChangeListener< JenkinsJob, Long > timestampListener;
   
   @Before public void initialiseSystemUnderTest(){
      databse = new TestJenkinsDatabaseImpl();
      job1 = new JenkinsJobImpl( "first job" );
      job2 = new JenkinsJobImpl( "second job" );
      job3 = new JenkinsJobImpl( "third job" );
      databse.store( job1 );
      databse.store( job2 );
      databse.store( job3 );
      databse.store( node1 = new JenkinsNodeImpl( "Node1" ) );
      databse.store( node2 = new JenkinsNodeImpl( "Node2" ) );
      
      systemUnderTest = new JenkinsJobPropertyListener( databse );
      buildResultStatusNotifications = new ArrayList<>();
      buildResultListener = ( job, old, updated ) -> buildResultStatusNotifications.add( 
               new Pair< JenkinsJob, Pair< Integer, BuildResultStatus > >( job, updated ) 
      );
      buildStateNotifications = new ArrayList<>();
      buildStateListener = ( job, old, updated ) -> buildStateNotifications.add( 
               new Pair< JenkinsJob, BuildState >( job, updated ) 
      );
      builtOnNotifications = new ArrayList<>();
      builtOnListener = ( job, old, updated ) -> builtOnNotifications.add( 
               new Pair< JenkinsJob, JenkinsNode >( job, updated ) 
      );
      integerNotifications = new ArrayList<>();
      testFailuresListener = ( j, o, n ) -> integerNotifications.add(
               new Pair< JenkinsJob, Integer >( j, n ) 
      );
      testTotalListener = ( j, o, n ) -> integerNotifications.add(
               new Pair< JenkinsJob, Integer >( j, n ) 
      );
      longNotifications = new ArrayList<>();
      timestampListener = ( j, o, n ) -> longNotifications.add(
               new Pair< JenkinsJob, Long >( j, n ) 
      );
      
      systemUnderTest.addBuildResultStatusListener( buildResultListener );
      systemUnderTest.addBuildStateListener( buildStateListener );
      systemUnderTest.addTestTotalCountListener( testTotalListener );
      systemUnderTest.addTestFailureCountListener( testFailuresListener );
      systemUnderTest.addBuiltOnListener( builtOnListener );
      systemUnderTest.addTimestampListener( timestampListener );
   }//End Method
   
   @Test public void shouldNotifyLastBuildResultStatusWhenChanged() {
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
      job1.setBuildStatus( BuildResultStatus.SUCCESS );
      job2.setBuildStatus( BuildResultStatus.FAILURE );
      
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
   
   @Test public void shouldNotifyBuiltOnWhenChanged() {
      assertThat( builtOnNotifications.isEmpty(), is( true ) );
      job1.builtOnProperty().set( node1 );
      job2.builtOnProperty().set( node2 );
      
      assertThat( builtOnNotifications, hasSize( 2 ) );
      Pair< JenkinsJob, JenkinsNode > result = builtOnNotifications.remove( 0 );
      assertThat( result.getKey(), is( job1 ) );
      assertThat( result.getValue(), is( node1 ) );
      Pair< JenkinsJob, JenkinsNode > result2 = builtOnNotifications.remove( 0 );
      assertThat( result2.getKey(), is( job2 ) );
      assertThat( result2.getValue(), is( node2 ) );
   }//End Method
   
   @Test public void shouldNotifyTimestampWhenChanged() {
      assertThat( longNotifications.isEmpty(), is( true ) );
      job1.buildTimestampProperty().set( 1000L );
      job2.buildTimestampProperty().set( 6543L );
      
      assertThat( longNotifications, hasSize( 2 ) );
      Pair< JenkinsJob, Long > result = longNotifications.remove( 0 );
      assertThat( result.getKey(), is( job1 ) );
      assertThat( result.getValue(), is( 1000L ) );
      Pair< JenkinsJob, Long > result2 = longNotifications.remove( 0 );
      assertThat( result2.getKey(), is( job2 ) );
      assertThat( result2.getValue(), is( 6543L ) );
   }//End Method
   
}//End Class
