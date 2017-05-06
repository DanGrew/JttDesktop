/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.panel.type;

import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallTheme;
import uk.dangrew.jtt.desktop.buildwall.panel.description.DefaultJobPanelDescriptionImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.description.DetailedJobPanelDescriptionImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.description.JobPanelDescriptionBaseImpl;
import uk.dangrew.jtt.desktop.buildwall.panel.description.SimpleJobPanelDescriptionImpl;
import uk.dangrew.jtt.model.jobs.JenkinsJob;

/**
 * {@link JobPanelDescriptionProviders} defines the {@link JobPanelDescriptionProvider}s usable in the
 * system.
 */
public enum JobPanelDescriptionProviders {
   
   Simple( ( configuration, theme, job ) -> new SimpleJobPanelDescriptionImpl( configuration, theme, job ) ),
   Default( ( configuration, theme, job ) -> new DefaultJobPanelDescriptionImpl( configuration, theme, job ) ),
   Detailed( ( configuration, theme, job ) -> new DetailedJobPanelDescriptionImpl( configuration, theme, job ) );
   
   private transient DescriptionCreator provider;
   
   /**
    * Constructs a new {@link JobPanelDescriptionProviders}.
    * @param provider the {@link DescriptionCreator} function.
    */
   private JobPanelDescriptionProviders( DescriptionCreator provider ) {
      this.provider = provider;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   public JobPanelDescriptionBaseImpl constructJobDescriptionPanel( BuildWallConfiguration configuration, BuildWallTheme theme, JenkinsJob job ) {
      return provider.create( configuration, theme, job );
   }//End Method

}//End Enum
