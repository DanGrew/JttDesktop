/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.configuration.tree.item;

import uk.dangrew.jtt.desktop.configuration.item.ScrollableConfigurationItem;
import uk.dangrew.jtt.desktop.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.desktop.environment.preferences.PreferenceController;
import uk.dangrew.jtt.desktop.mc.configuration.components.UserAssignmentsDescriptionPanel;

/**
 * The {@link UserAssignmentsRootItem} provides the root of a set of configuration items
 * for the user assignments system.
 */
public class UserAssignmentsRootItem extends ScrollableConfigurationItem {
   
   static final String NAME = "User Assignments";
   static final String TITLE = "Configuring the User Assignment System";

   /**
    * Constructs a new {@link UserAssignmentsRootItem}.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    */
   public UserAssignmentsRootItem( PreferenceController controller ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, null ), 
               controller, 
               new UserAssignmentsDescriptionPanel()
      );
   }//End Constructor

}//End Class
