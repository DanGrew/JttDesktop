/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel.type;

import java.util.function.BiFunction;

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
public enum JobPanelDescriptionProviders {
   
   Simple( ( configuration, job ) -> new SimpleJobPanelDescriptionImpl( configuration, job ) ),
   Default( ( configuration, job ) -> new DefaultJobPanelDescriptionImpl( configuration, job ) ),
   Detailed( ( configuration, job ) -> new DetailedJobPanelDescriptionImpl( configuration, job ) );
   
   private transient BiFunction< BuildWallConfiguration, JenkinsJob, JobPanelDescriptionBaseImpl > provider;
   
   /**
    * Constructs a new {@link JobPanelDescriptionProviders}.
    * @param provider the {@link JobPanelDescriptionProvider} function.
    */
   private JobPanelDescriptionProviders( BiFunction< BuildWallConfiguration, JenkinsJob, JobPanelDescriptionBaseImpl > provider ) {
      this.provider = provider;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   public JobPanelDescriptionBaseImpl constructJobDescriptionPanel( BuildWallConfiguration configuration, JenkinsJob job ) {
      return provider.apply( configuration, job );
   }//End Method

}//End Enum
