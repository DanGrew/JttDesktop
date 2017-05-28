/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.api;

import uk.dangrew.jtt.desktop.configuration.item.ScrollableConfigurationItem;
import uk.dangrew.jtt.desktop.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.desktop.environment.preferences.PreferenceController;

/**
 * The {@link ApiConfigurationItem} provides a {@link ApiConfigurationPanel} that provides
 * information about the connections to jenkins instances.
 */
public class ApiConfigurationItem extends ScrollableConfigurationItem {
   
   static final String NAME = "Jenkins Connections";
   static final String TITLE = "Api Connection Management";
   static final String DESCRIPTION = "Here you can manage connections to Jenkins servers. You "
            + "can add multiple connections to combine information and builds into a single "
            + "display.";

   /**
    * Constructs a new {@link ApiConfigurationItem}.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    */
   public ApiConfigurationItem( PreferenceController controller ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, DESCRIPTION ), 
               controller, 
               new ApiConfigurationPanel()
      );
   }//End Constructor

}//End Class
