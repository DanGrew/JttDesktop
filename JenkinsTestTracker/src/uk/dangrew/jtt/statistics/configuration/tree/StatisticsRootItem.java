/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.configuration.tree;

import uk.dangrew.jtt.configuration.item.ScrollableConfigurationItem;
import uk.dangrew.jtt.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.environment.preferences.PreferenceController;
import uk.dangrew.jtt.statistics.configuration.components.StatisticsDescriptionPanel;

/**
 * The {@link StatisticsRootItem} provides the root of a set of configuration items
 * for the statistics.
 */
public class StatisticsRootItem extends ScrollableConfigurationItem {
   
   static final String NAME = "Statistics";
   static final String TITLE = "Configuring Statistics";

   /**
    * Constructs a new {@link StatisticsRootItem}.
    * @param identifier the identifier for this set of configuration.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    */
   public StatisticsRootItem( String identifier, PreferenceController controller ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, null ), 
               controller, 
               new StatisticsDescriptionPanel()
      );
   }//End Constructor

}//End Class
