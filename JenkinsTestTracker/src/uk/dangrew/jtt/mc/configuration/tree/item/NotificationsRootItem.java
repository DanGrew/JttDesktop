/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.configuration.tree.item;

import uk.dangrew.jtt.configuration.item.ScrollableConfigurationItem;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.environment.preferences.PreferenceController;
import uk.dangrew.jtt.mc.configuration.components.NotificationsDescriptionPanel;

/**
 * The {@link NotificationsRootItem} provides the root of a set of configuration items
 * for the notifications system.
 */
public class NotificationsRootItem extends ScrollableConfigurationItem {
   
   static final String NAME = "Notifications";
   static final String TITLE = "Configuring the Notification System";

   /**
    * Constructs a new {@link NotificationsRootItem}.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    */
   public NotificationsRootItem( PreferenceController controller ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, null ), 
               controller, 
               new NotificationsDescriptionPanel()
      );
   }//End Constructor

}//End Class
