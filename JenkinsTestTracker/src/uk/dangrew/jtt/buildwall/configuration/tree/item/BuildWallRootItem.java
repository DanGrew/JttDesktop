/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.tree.item;

import uk.dangrew.jtt.buildwall.configuration.components.BuildWallDescriptionPanel;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationItem;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.environment.preferences.PreferenceController;

/**
 * The {@link BuildWallRootItem} provides the root of a set of configuration items
 * for an individual build wall.
 */
public class BuildWallRootItem extends SimpleConfigurationItem {
   
   static final String NAME_SUFFIX = " Build Wall";
   static final String TITLE = "Configuring Build Walls";

   /**
    * Constructs a new {@link BuildWallRootItem}.
    * @param identifier the identifier for this set of configuration.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    */
   public BuildWallRootItem( String identifier, PreferenceController controller ) {
      super( 
               identifier + NAME_SUFFIX, 
               new SimpleConfigurationTitle( TITLE, null ), 
               controller, 
               new BuildWallDescriptionPanel() 
      );
   }//End Constructor

}//End Class
