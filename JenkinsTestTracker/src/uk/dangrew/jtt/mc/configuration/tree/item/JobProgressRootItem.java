/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.configuration.tree.item;

import uk.dangrew.jtt.configuration.item.SimpleConfigurationItem;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.environment.preferences.PreferenceController;
import uk.dangrew.jtt.mc.configuration.components.JobProgressDescriptionPanel;

/**
 * The {@link JobProgressRootItem} provides the root of a set of configuration items
 * for the job progress view.
 */
public class JobProgressRootItem extends SimpleConfigurationItem {
   
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
