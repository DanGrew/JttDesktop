/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.configuration.updating;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.configuration.BuildWallJobPolicy;
import model.jobs.JenkinsJob;
import storage.database.JenkinsDatabase;
import utility.observable.FunctionListChangeListenerImpl;

/**
 * The {@link JobPolicyUpdater} is responsible for updating the {@link BuildWallConfiguration}
 * as {@link JenkinsJob}s are created, or found in the {@link JenkinsDatabase}.
 */
public class JobPolicyUpdater {

   /**
    * Constructs a new {@link JobPolicyUpdater}.
    * @param database the {@link JenkinsDatabase} to get {@link JenkinsJob}s from.
    * @param configuration the {@link BuildWallConfiguration} to keep up to date.
    */
   public JobPolicyUpdater( JenkinsDatabase database, BuildWallConfiguration configuration ) {
      database.jenkinsJobs().forEach(  
               job -> putInitialPolicy( configuration, job )
      );
      database.jenkinsJobs().addListener( new FunctionListChangeListenerImpl<>( 
               job -> putInitialPolicy( configuration, job ),
               job -> { /* Do nothing on remove. */ } 
      ) );
   }//End Constructor
   
   /**
    * Method to put the initial {@link BuildWallJobPolicy} into the {@link BuildWallConfiguration}.
    * @param configuration the {@link BuildWallConfiguration} to configure.
    * @param job the {@link JenkinsJob} to configure.
    */
   private void putInitialPolicy( BuildWallConfiguration configuration, JenkinsJob job ) {
      configuration.jobPolicies().put( job, BuildWallJobPolicy.AlwaysShow );
   }//End Method

}//End Class
