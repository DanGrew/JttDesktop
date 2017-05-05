/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.tree.item;

import uk.dangrew.jtt.buildwall.configuration.components.JobPolicyPanel;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.configuration.item.ScrollableConfigurationItem;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.environment.preferences.PreferenceController;

/**
 * The {@link JobPolicyTreeItem} provides the configuration items for the {@link uk.dangrew.jtt.model.jobs.JenkinsJob}
 * {@link uk.dangrew.jtt.buildwall.configuration.BuildWallJobPolicy}s for an individual build wall.
 */
public class JobPolicyTreeItem extends ScrollableConfigurationItem {

   static final String NAME = "Job Policies";
   static final String TITLE = "Configuring Build Wall Job Policies";
   static final String DESCRIPTION = 
            "Job policies define when a job should be shown on a build wall. "
            + "The names are fairly self explanatory, for example, OnlyShowFailures "
            + "will display the job only when it fails.";
   
   private final BuildWallConfiguration configuration;
   
   /**
    * Constructs a new {@link JobPolicyTreeItem}.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    * @param configuration the {@link BuildWallConfiguration} associated.
    */
   public JobPolicyTreeItem( PreferenceController controller, BuildWallConfiguration configuration ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, DESCRIPTION ), 
               controller, 
               new JobPolicyPanel( configuration )
      );
      this.configuration = configuration;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isAssociatedWith( Object object ) {
      return this.configuration.equals( object );
   }//End Method
}//End Class
