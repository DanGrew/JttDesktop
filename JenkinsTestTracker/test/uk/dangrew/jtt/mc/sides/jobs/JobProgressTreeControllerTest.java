/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.jobs;

import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.TestJenkinsDatabaseImpl;

/**
 * {@link JobProgressTreeController} test.
 */
public class JobProgressTreeControllerTest {

   @Mock private BuildResultStatusLayout layout;
   private JenkinsDatabase database;
   private JobProgressTreeController systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      
      database = new TestJenkinsDatabaseImpl();
      database.store( new JenkinsJobImpl( "One Job" ) );
      database.store( new JenkinsJobImpl( "Another Job" ) );
      database.store( new JenkinsJobImpl( "Extra Job" ) );
      
      systemUnderTest = new JobProgressTreeController( layout, database );
   }//End Method
   
   @Test public void shouldAddNewJobToTree() {
      JenkinsJob job = new JenkinsJobImpl( "a new job" );
      database.store( job );
      verify( layout ).add( job );
   }//End Method
   
   @Test public void shouldRemoveJobFromTree() {
      JenkinsJob job = database.jenkinsJobs().get( 0 );
      database.store( job );
      verify( layout ).remove( job );
   }//End Method
   
   @Test public void shouldUpdaeJobInTree() {
      JenkinsJob job = database.jenkinsJobs().get( 0 );
      job.setBuildStatus( BuildResultStatus.SUCCESS );
      verify( layout ).update( job );
   }//End Method

}//End Class
