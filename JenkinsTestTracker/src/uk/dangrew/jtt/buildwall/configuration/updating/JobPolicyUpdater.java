/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.updating;

import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.BuildWallJobPolicy;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.utility.observable.FunctionListChangeListenerImpl;

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
