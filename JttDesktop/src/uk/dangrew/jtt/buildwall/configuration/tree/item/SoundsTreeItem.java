/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.tree.item;

import uk.dangrew.jtt.buildwall.configuration.components.sound.SoundConfigurationPanel;
import uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.configuration.item.ScrollableConfigurationItem;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.environment.preferences.PreferenceController;

/**
 * The {@link SoundsTreeItem} provides the configuration items for the {@link SoundConfigurationPanel}
 * to configure {@link SoundConfiguration}.
 */
public class SoundsTreeItem extends ScrollableConfigurationItem {

   static final String NAME = "Sounds";
   static final String TITLE = "Configuring Build Wall Sounds";
   static final String DESCRIPTION = 
                     "Sounds can be played in response to build status changes such as when a build fails "
                     + "or passes. Here, 'pass' is a successful build, 'fail' is anything else.";
   
   /**
    * Constructs a new {@link SoundsTreeItem}.
    * @param configuration the {@link SoundConfiguration} to configure.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    */
   public SoundsTreeItem( SoundConfiguration configuration, PreferenceController controller ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, DESCRIPTION ), 
               controller, 
               new SoundConfigurationPanel( configuration )
      );
   }//End Constructor
   
}//End Class
