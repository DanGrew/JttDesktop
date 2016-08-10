/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.tree.item;

import uk.dangrew.jtt.buildwall.configuration.components.DualBuildWallDescriptionPanel;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationItem;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.environment.preferences.PreferenceController;

/**
 * The {@link DualBuildWallRootItem} provides the root of a set of configuration items
 * the {@link uk.dangrew.jtt.buildwall.dual.DualBuildWallDisplayImpl}.
 */
public class DualBuildWallRootItem extends SimpleConfigurationItem {
   
   static final String NAME = "Dual Build Wall";
   static final String TITLE = "Configuring the Dual Build Wall";

   /**
    * Constructs a new {@link DualBuildWallRootItem}.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    */
   public DualBuildWallRootItem( PreferenceController controller ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, null ), 
               controller, 
               new DualBuildWallDescriptionPanel() 
      );
   }//End Constructor

}//End Class
