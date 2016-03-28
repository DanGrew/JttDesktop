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
import buildwall.panel.description.JobPanelDescriptionBaseImpl;
import model.jobs.JenkinsJob;

/**
 * The {@link JobPanelDescriptionProvider} is an interface for an object that
 * can provide a {@link JobPanelDescriptionBaseImpl}.
 */
@FunctionalInterface
public interface JobPanelDescriptionProvider {

   /**
    * Method to construct a new {@link JobPanelDescriptionBaseImpl} for the type associated.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param job the {@link JenkinsJob} the panel is for.
    * @return the constructed {@link JobPanelDescriptionBaseImpl}.
    */
   public JobPanelDescriptionBaseImpl constructJobDescriptionPanel( 
            BuildWallConfiguration configuration, 
            JenkinsJob job 
   );

}//End Interface
