/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.configuration.tree.item;

import uk.dangrew.jtt.configuration.item.ScrollableConfigurationItem;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.environment.preferences.PreferenceController;
import uk.dangrew.jtt.mc.configuration.components.ManagementConsoleDescriptionPanel;

/**
 * The {@link ManagementConsoleRootItem} provides the root of a set of configuration items
 * for an individual build wall.
 */
public class ManagementConsoleRootItem extends ScrollableConfigurationItem {
   
   static final String NAME = "Management Console";
   static final String TITLE = "Configuring the Management Console";

   /**
    * Constructs a new {@link ManagementConsoleRootItem}.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    */
   public ManagementConsoleRootItem( PreferenceController controller ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, null ), 
               controller, 
               new ManagementConsoleDescriptionPanel()
      );
   }//End Constructor

}//End Class
