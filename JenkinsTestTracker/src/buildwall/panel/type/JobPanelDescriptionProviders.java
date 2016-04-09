/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel.type;

import buildwall.configuration.BuildWallConfiguration;
import buildwall.panel.description.DefaultJobPanelDescriptionImpl;
import buildwall.panel.description.DetailedJobPanelDescriptionImpl;
import buildwall.panel.description.JobPanelDescriptionBaseImpl;
import buildwall.panel.description.SimpleJobPanelDescriptionImpl;
import model.jobs.JenkinsJob;

/**
 * {@link JobPanelDescriptionProviders} defines the {@link JobPanelDescriptionProvider}s usable in the
 * system.
 */
public enum JobPanelDescriptionProviders implements JobPanelDescriptionProvider {
   
   Simple( ( configuration, job ) -> new SimpleJobPanelDescriptionImpl( configuration, job ) ),
   Default( ( configuration, job ) -> new DefaultJobPanelDescriptionImpl( configuration, job ) ),
   Detailed( ( configuration, job ) -> new DetailedJobPanelDescriptionImpl( configuration, job ) );
   
   private transient JobPanelDescriptionProvider provider;
   
   /**
    * Constructs a new {@link JobPanelDescriptionProviders}.
    * @param provider the {@link JobPanelDescriptionProvider} function.
    */
   private JobPanelDescriptionProviders( JobPanelDescriptionProvider provider ) {
      this.provider = provider;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public JobPanelDescriptionBaseImpl constructJobDescriptionPanel( BuildWallConfiguration configuration, JenkinsJob job ) {
      return provider.constructJobDescriptionPanel( configuration, job );
   }//End Method

}//End Enum
