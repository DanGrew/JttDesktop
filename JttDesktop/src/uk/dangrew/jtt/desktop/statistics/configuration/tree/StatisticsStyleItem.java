/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.configuration.tree;

import uk.dangrew.jtt.desktop.configuration.item.ScrollableConfigurationItem;
import uk.dangrew.jtt.desktop.configuration.item.SimpleConfigurationTitle;
import uk.dangrew.jtt.desktop.environment.preferences.PreferenceController;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;
import uk.dangrew.jtt.desktop.statistics.configuration.components.StatisticsStylePanel;

/**
 * The {@link StatisticsStyleItem} provides the configuration for the appearance of the statistics panels.
 */
public class StatisticsStyleItem extends ScrollableConfigurationItem {
   
   static final String NAME = "Style";
   static final String TITLE = "Configuring The Style Of Statisitcs.";
   static final String DESCRIPTION = "The following configuration allows the presentation "
            + "of the statistics panels to be changed.";

   private final StatisticsConfiguration configuration;
   
   /**
    * Constructs a new {@link StatisticsStyleItem}.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    */
   public StatisticsStyleItem( 
            PreferenceController controller, 
            StatisticsConfiguration configuration 
   ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, DESCRIPTION ), 
               controller, 
               new StatisticsStylePanel( configuration )
      );
      this.configuration = configuration;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isAssociatedWith( Object object ) {
      return object == configuration;
   }//End Method
   
}//End Class
