/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.updating;

import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallJobPolicy;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.model.jobs.JenkinsJobImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.TestJenkinsDatabaseImpl;

/**
 * {@link JobPolicyUpdater} test.
 */
public class JobPolicyUpdaterTest {

   private JenkinsDatabase database;
   private BuildWallConfiguration configuration;
   
   @Before public void initialiseSystemUnderTest(){
      database = new TestJenkinsDatabaseImpl();
      database.store( new JenkinsJobImpl( "already-stored" ) );
      
      configuration = new BuildWallConfigurationImpl();
      new JobPolicyUpdater( database, configuration );
   }//End Method
   
   @Test public void shouldAutomaticallyShowNewJobs() {
      assertThat( configuration.jobPolicies().size(), is( 1 ) );
      
      JenkinsJob job = new JenkinsJobImpl( "anything" );
      database.store( job );
      
      assertThat( configuration.jobPolicies(), hasKey( job ) );
      assertThat( configuration.jobPolicies().get( job ), is( BuildWallJobPolicy.AlwaysShow ) );
   }//End Method
   
   @Test public void shouldShowNewJobsForItemsPresentInTheDatabase(){
      assertThat( configuration.jobPolicies().containsKey( database.jenkinsJobs().get( 0 ) ), is( true ) );
   }//End Method
   
   @Test public void shouldNotInitiallySetIfAlreadyPresent(){
      configuration.jobPolicies().clear();
      
      JenkinsJob job = new JenkinsJobImpl( "one to add" );
      configuration.jobPolicies().put( job, BuildWallJobPolicy.NeverShow );
      database.store( job );
      
      new JobPolicyUpdater( database, configuration );
      
      assertThat( configuration.jobPolicies(), hasKey( job ) );
      assertThat( configuration.jobPolicies().get( job ), is( BuildWallJobPolicy.NeverShow ) );
   }//End Method
   
   @Test public void shouldNotSetWhenAddedIfAlreadyPresent(){
      assertThat( configuration.jobPolicies().size(), is( 1 ) );
      
      JenkinsJob job = new JenkinsJobImpl( "one to add" );
      configuration.jobPolicies().put( job, BuildWallJobPolicy.NeverShow );
      
      database.store( job );
      
      assertThat( configuration.jobPolicies(), hasKey( job ) );
      assertThat( configuration.jobPolicies().get( job ), is( BuildWallJobPolicy.NeverShow ) );
   }//End Method

}//End Class
