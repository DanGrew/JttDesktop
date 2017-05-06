/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.configuration.tree.item;

import uk.dangrew.jtt.desktop.configuration.item.ScrollableConfigurationItem;
import uk.dangrew.jtt.desktop.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.desktop.environment.preferences.PreferenceController;
import uk.dangrew.jtt.desktop.mc.configuration.components.JobProgressDescriptionPanel;

/**
 * The {@link JobProgressRootItem} provides the root of a set of configuration items
 * for the job progress view.
 */
public class JobProgressRootItem extends ScrollableConfigurationItem {
   
   static final String NAME = "Job Progress View";
   static final String TITLE = "Configuring the Job Progress View";

   /**
    * Constructs a new {@link JobProgressRootItem}.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    */
   public JobProgressRootItem( PreferenceController controller ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, null ), 
               controller, 
               new JobProgressDescriptionPanel()
      );
   }//End Constructor

}//End Class
