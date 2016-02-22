/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration.updating;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallConfigurationImpl;
import buildwall.configuration.BuildWallJobPolicy;
import model.jobs.JenkinsJob;
import model.jobs.JenkinsJobImpl;
import storage.database.JenkinsDatabase;
import storage.database.JenkinsDatabaseImpl;

/**
 * {@link JobPolicyUpdater} test.
 */
public class JobPolicyUpdaterTest {

   private JenkinsDatabase database;
   private BuildWallConfiguration configuration;
   
   @Before public void initialiseSystemUnderTest(){
      database = new JenkinsDatabaseImpl();
      database.store( new JenkinsJobImpl( "already-stored" ) );
      
      configuration = new BuildWallConfigurationImpl();
      new JobPolicyUpdater( database, configuration );
   }//End Method
   
   @Test public void shouldAutomaticallyShowNewJobs() {
      assertThat( configuration.jobPolicies().size(), is( 1 ) );
      
      JenkinsJob job = new JenkinsJobImpl( "anything" );
      database.store( job );
      
      assertThat( configuration.jobPolicies().containsKey( job ), is( true ) );
      assertThat( configuration.jobPolicies().get( job ), is( BuildWallJobPolicy.AlwaysShow ) );
   }//End Method
   
   @Test public void shouldShowNewJobsForItemsPresentInTheDatabase(){
      assertThat( configuration.jobPolicies().containsKey( database.jenkinsJobs().get( 0 ) ), is( true ) );
   }//End Method

}//End Class
