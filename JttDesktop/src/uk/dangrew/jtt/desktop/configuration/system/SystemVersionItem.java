/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.system;

import uk.dangrew.jtt.desktop.configuration.item.ScrollableConfigurationItem;
import uk.dangrew.jtt.desktop.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.desktop.environment.preferences.PreferenceController;

/**
 * The {@link SystemVersionItem} provides a {@link SimpleConfigurationItem} that provides
 * information about the current version of the software.
 */
public class SystemVersionItem extends ScrollableConfigurationItem {
   
   static final String NAME = "Software Version";
   static final String TITLE = "Versioning Details and Information";

   /**
    * Constructs a new {@link SystemVersionItem}.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    */
   public SystemVersionItem( PreferenceController controller ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, null ), 
               controller, 
               new SystemVersionPanel()
      );
   }//End Constructor

}//End Class
