/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.tree.item;

import uk.dangrew.jtt.buildwall.configuration.components.DualPropertiesPanel;
import uk.dangrew.jtt.buildwall.configuration.properties.DualWallConfiguration;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationItem;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.configuration.tree.ConfigurationTreeController;

/**
 * The {@link DualPropertiesTreeItem} provides the configuration items for the properties related
 * specifically to the {@link uk.dangrew.jtt.buildwall.dual.DualBuildWallDisplayImpl}.
 */
public class DualPropertiesTreeItem extends SimpleConfigurationItem {

   static final String NAME = "Dual Properties";
   static final String TITLE = "Configuring the Dual Wall properties";
   static final String DESCRIPTION = 
            "The following properties apply to the dual wall as a whole and control "
            + "how it is displayed.";
   
   private final DualWallConfiguration configuration;
   
   /**
    * Constructs a new {@link DualPropertiesTreeItem}.
    * @param controller the {@link ConfigurationTreeController} for controlling the configuration.
    * @param configuration the {@link DualWallConfiguration} associated.
    */
   public DualPropertiesTreeItem( ConfigurationTreeController controller, DualWallConfiguration configuration ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, DESCRIPTION ),
               controller, 
               new DualPropertiesPanel( configuration ) 
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
