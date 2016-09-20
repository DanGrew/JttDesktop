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

import javafx.util.Pair;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;

/**
 * {@link JenkinsJobPropertyListener} test.
 */
public class JenkinsJobPropertyListenerImplTest {

   private JenkinsDatabase databse;
   private JenkinsJob job1;
   private JenkinsJob job2;
   private JenkinsJob job3;
   
   private JenkinsJobPropertyListener systemUnderTest;
   private List< Pair< JenkinsJob, Pair< Integer, BuildResultStatus > > > buildResultStatusNotifications;
   private JttChangeListener< JenkinsJob, Pair< Integer, BuildResultStatus > > buildResultListener;
   
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
      buildResultListener = ( job, old, updated ) -> buildResultStatusNotifications.add( new Pair< JenkinsJob, Pair< Integer, BuildResultStatus > >( job, updated ) );
      systemUnderTest.addBuildResultStatusListener( buildResultListener );
   }//End Method
   
   @Test public void shouldNotifyLastBuildResultStatusWhenChanged() {
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
      job1.setLastBuildStatus( BuildResultStatus.SUCCESS );
      
      assertThat( buildResultStatusNotifications.isEmpty(), is( false ) );
      Pair< JenkinsJob, Pair< Integer, BuildResultStatus > > result = buildResultStatusNotifications.remove( 0 );
      assertThat( result.getKey(), is( job1 ) );
      assertThat( result.getValue().getValue(), is( BuildResultStatus.SUCCESS ) );
      assertThat( buildResultStatusNotifications.isEmpty(), is( true ) );
   }//End Method
   
}//End Class
