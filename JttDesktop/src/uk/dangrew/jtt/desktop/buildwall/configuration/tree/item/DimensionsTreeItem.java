/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.tree.item;

import uk.dangrew.jtt.desktop.buildwall.configuration.components.DimensionsPanel;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.configuration.item.ScrollableConfigurationItem;
import uk.dangrew.jtt.desktop.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.desktop.environment.preferences.PreferenceController;

/**
 * The {@link DimensionsTreeItem} provides the configuration items for the dimension related
 * properties for an individual build wall.
 */
public class DimensionsTreeItem extends ScrollableConfigurationItem {

   static final String NAME = "Dimensions";
   static final String TITLE = "Configuring Build Wall Dimensions";
   static final String DESCRIPTION = 
            "The following properties control the dimensions of the build wall "
            + "and how it is presented.";
   
   private final BuildWallConfiguration configuration;
   
   /**
    * Constructs a new {@link DimensionsTreeItem}.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    * @param configuration the {@link BuildWallConfiguration} associated.
    */
   public DimensionsTreeItem( PreferenceController controller, BuildWallConfiguration configuration ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, DESCRIPTION ),
               controller, 
               new DimensionsPanel( configuration )
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
