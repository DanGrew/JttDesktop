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
import uk.dangrew.jtt.desktop.statistics.configuration.components.StatisticsExclusionsPanel;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

/**
 * The {@link StatisticsExclusionsItem} provides the root of a set of configuration items
 * for the statistics.
 */
public class StatisticsExclusionsItem extends ScrollableConfigurationItem {
   
   static final String NAME = "Exclusions";
   static final String TITLE = "Configuring Jenkins Jobs Used For Statisitcs.";
   static final String DESCRIPTION = "The following configuration allows jobs to be turned on "
            + "and off in the statistics. Here, anything included (ticked) will be used in the metric "
            + "calculations and anything exlcuded (unticked) will not be counted.";

   private final StatisticsConfiguration configuration;
   private final JenkinsDatabase database;
   
   /**
    * Constructs a new {@link StatisticsExclusionsItem}.
    * @param controller the {@link PreferenceController} for controlling the configuration.
    */
   public StatisticsExclusionsItem( 
            PreferenceController controller, 
            JenkinsDatabase database, 
            StatisticsConfiguration configuration 
   ) {
      super( 
               NAME, 
               new SimpleConfigurationTitle( TITLE, DESCRIPTION ), 
               controller, 
               new StatisticsExclusionsPanel( database, configuration )
      );
      this.configuration = configuration;
      this.database = database;
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isAssociatedWith( Object object ) {
      return object == database || object == configuration;
   }//End Method
   
}//End Class
