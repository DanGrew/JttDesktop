/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.tree.item;

import uk.dangrew.jtt.desktop.buildwall.configuration.components.themebuilder.ThemeBuilderPanel;
import uk.dangrew.jtt.desktop.buildwall.configuration.theme.BuildWallThemeImpl;
import uk.dangrew.jtt.desktop.configuration.item.SimpleConfigurationItem;
import uk.dangrew.jtt.desktop.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.desktop.environment.preferences.PreferenceController;

/**
 * The {@link ThemesTreeItem} provides the configuration items for the {@link ThemeBuilderPanel}
 * to configure {@link uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme}s.
 */
public class ThemesTreeItem extends SimpleConfigurationItem {

   static final String NAME = "Themes";
   static final String TITLE = "Configuring Build Wall Themes";
   static final String DESCRIPTION = 
                     "Themes provide colour configurations for a build wall that customise the progress "
                     + "and text colour.";
   
   /**
    * Constructs a new {@link ThemesTreeItem}.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    */
   public ThemesTreeItem( PreferenceController controller ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, DESCRIPTION ), 
               controller, 
               new ThemeBuilderPanel( new BuildWallThemeImpl( "Theme (Not in use)" ) )
      );
   }//End Constructor
   
}//End Class
