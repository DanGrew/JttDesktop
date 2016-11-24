/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.panel.type;

import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.buildwall.panel.description.JobPanelDescriptionBaseImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * The {@link DescriptionCreator} is responsible for defining an interface for creating
 * {@link JobPanelDescriptionBaseImpl}s.
 */
@FunctionalInterface
public interface DescriptionCreator {

   /**
    * Method to create the associated type of {@link JobPanelDescriptionBaseImpl}.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param theme the {@link BuildWallTheme}.
    * @param job the associated {@link JenkinsJob}.
    * @return the created {@link JobPanelDescriptionBaseImpl}.
    */
   public JobPanelDescriptionBaseImpl create( BuildWallConfiguration configuration, BuildWallTheme theme, JenkinsJob job );
   
}//End Interface
