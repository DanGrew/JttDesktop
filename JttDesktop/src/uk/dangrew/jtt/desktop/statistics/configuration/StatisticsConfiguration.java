/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.statistics.configuration;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.kode.synchronization.SynchronizedObservableList;

/**
 * The {@link StatisticsConfiguration} provides the configuration items for customising the
 * {@link uk.dangrew.jtt.desktop.statistics.panel.StatisticPanel}s.
 */
public class StatisticsConfiguration {

   static final Color DEFAULT_STATISTIC_TEXT_COLOUR = Color.WHITE;
   static final Color DEFAULT_STATISTIC_BACKGROUND_COLOUR = Color.CORNFLOWERBLUE;
   static final Font DEFAULT_STATISTIC_VALUE_TEXT_FONT = new Font( 30 );
   static final Font DEFAULT_STATISTIC_DESCRIPTION_TEXT_FONT = new Font( 13 );
   
   private final ObjectProperty< Color > backgroundColour;
   private final ObjectProperty< Color > textColour;
   private final ObjectProperty< Font > valueTextFont;
   private final ObjectProperty< Font > descriptionTextFont;
   private final ObservableList< JenkinsJob > excludedJobs;
   
   /**
    * Constructs a new {@link StatisticsConfiguration}.
    */
   public StatisticsConfiguration() {
      this.backgroundColour = new SimpleObjectProperty<>( DEFAULT_STATISTIC_BACKGROUND_COLOUR );
      this.textColour = new SimpleObjectProperty<>( DEFAULT_STATISTIC_TEXT_COLOUR );
      this.valueTextFont = new SimpleObjectProperty<>( DEFAULT_STATISTIC_VALUE_TEXT_FONT );
      this.descriptionTextFont = new SimpleObjectProperty<>( DEFAULT_STATISTIC_DESCRIPTION_TEXT_FONT );
      this.excludedJobs = new SynchronizedObservableList<>();
   }//End Constructor
   
   /**
    * Access to the {@link Color} of the background, of the statistic.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Color > statisticBackgroundColourProperty() {
      return backgroundColour;
   }//End Method

   /**
    * Access to the {@link Color} of the text, of the statistic.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Color > statisticTextColourProperty() {
      return textColour;
   }//End Method
   
   /**
    * Access to the {@link Font} of the value text, of the statistic.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Font > statisticValueTextFontProperty() {
      return valueTextFont;
   }//End Method
   
   /**
    * Access to the {@link Font} of the description text, of the statistic.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Font > statisticDescriptionTextFontProperty() {
      return descriptionTextFont;
   }//End Method

   /**
    * Access to the {@link ObservableList} of {@link JenkinsJob}s that should not be included in 
    * the statistics.
    * @return the {@link ObservableList}.
    */
   public ObservableList< JenkinsJob > excludedJobs() {
      return excludedJobs;
   }//End Method

}//End Class
