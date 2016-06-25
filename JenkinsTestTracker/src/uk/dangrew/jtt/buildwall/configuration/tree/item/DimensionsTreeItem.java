/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.tree.item;

import uk.dangrew.jtt.buildwall.configuration.components.DimensionsPanel;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationItem;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.configuration.tree.ConfigurationTreeController;

/**
 * The {@link DimensionsTreeItem} provides the configuration items for the dimension related
 * properties for an individual build wall.
 */
public class DimensionsTreeItem extends SimpleConfigurationItem {

   static final String NAME = "Dimensions";
   static final String TITLE = "Configuring Build Wall Dimensions";
   static final String DESCRIPTION = 
            "The following properties control the dimensions of the build wall "
            + "and how it is presented.";
   
   private final BuildWallConfiguration configuration;
   
   /**
    * Constructs a new {@link DimensionsTreeItem}.
    * @param controller the {@link ConfigurationTreeController} for controlling the configuration.
    * @param configuration the {@link BuildWallConfiguration} associated.
    */
   public DimensionsTreeItem( ConfigurationTreeController controller, BuildWallConfiguration configuration ) {
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
