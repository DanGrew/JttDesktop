/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.tree.item;

import uk.dangrew.jtt.buildwall.configuration.components.FontsPanel;
import uk.dangrew.jtt.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationItem;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.configuration.tree.ConfigurationTreeController;

/**
 * The {@link FontsTreeItem} provides the configuration items for the {@link javafx.scene.text.Font} related
 * properties for an individual build wall.
 */
public class FontsTreeItem extends SimpleConfigurationItem {

   static final String NAME = "Fonts";
   static final String TITLE = "Configuring Build Wall Fonts";
   static final String DESCRIPTION = 
            "Font can be configured by family and by size and effect different elements "
            + "of the build wall, sometimes in groups of text items.";
   
   private final BuildWallConfiguration configuration;
   
   /**
    * Constructs a new {@link FontsTreeItem}.
    * @param controller the {@link ConfigurationTreeController} for controlling the configuration.
    * @param configuration the {@link BuildWallConfiguration} associated.
    */
   public FontsTreeItem( ConfigurationTreeController controller, BuildWallConfiguration configuration ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, DESCRIPTION ), 
               controller, 
               new FontsPanel( configuration ) 
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
