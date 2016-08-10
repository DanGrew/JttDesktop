/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.system;

import uk.dangrew.jtt.configuration.item.SimpleConfigurationItem;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.environment.preferences.PreferenceController;

/**
 * The {@link SystemVersionItem} provides a {@link SimpleConfigurationItem} that provides
 * information about the current version of the software.
 */
public class SystemVersionItem extends SimpleConfigurationItem {
   
   static final String NAME = "Software Version";
   static final String TITLE = "Versioning details and information";

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
