/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel.type;

import uk.dangrew.jtt.buildwall.configuration.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.panel.description.DefaultJobPanelDescriptionImpl;
import uk.dangrew.jtt.buildwall.panel.description.DetailedJobPanelDescriptionImpl;
import uk.dangrew.jtt.buildwall.panel.description.JobPanelDescriptionBaseImpl;
import uk.dangrew.jtt.buildwall.panel.description.SimpleJobPanelDescriptionImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

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
