/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.tree.item;

import uk.dangrew.jtt.buildwall.configuration.components.ColoursPanel;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationItem;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.configuration.tree.ConfigurationTreeController;

/**
 * The {@link ColoursTreeItem} provides the configuration items for the colour related
 * properties for an individual build wall.
 */
public class ColoursTreeItem extends SimpleConfigurationItem {

   static final String NAME = "Colours";
   static final String TITLE = "Configuring Build Wall Colours";
   static final String DESCRIPTION = 
                     "Colours can be configured use standard colour pickers. Notice that they do support"
                     + "transparency for differetn effects.";
   
   private final BuildWallConfiguration configuration;
   
   /**
    * Constructs a new {@link ColoursTreeItem}.
    * @param controller the {@link ConfigurationTreeController} for controlling the configuration.
    * @param configuration the {@link BuildWallConfiguration} associated.
    */
   public ColoursTreeItem( ConfigurationTreeController controller, BuildWallConfiguration configuration ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, DESCRIPTION ), 
               controller, 
               new ColoursPanel( configuration ) 
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
