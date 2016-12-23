/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.statistics.configuration;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.sd.utility.synchronization.SynchronizedObservableList;

/**
 * The {@link StatisticsConfiguration} provides the configuration items for customising the
 * {@link uk.dangrew.jtt.statistics.panel.StatisticPanel}s.
 */
public class StatisticsConfiguration {

   static final Color DEFAULT_STATISTIC_TEXT = Color.WHITE;
   static final Color DEFAULT_STATISTIC_BACKGROUND = Color.CORNFLOWERBLUE;
   
   private final ObjectProperty< Color > background;
   private final ObjectProperty< Color > text;
   private final ObservableList< JenkinsJob > excludedJobs;
   
   /**
    * Constructs a new {@link StatisticsConfiguration}.
    */
   public StatisticsConfiguration() {
      this.background = new SimpleObjectProperty<>( DEFAULT_STATISTIC_BACKGROUND );
      this.text = new SimpleObjectProperty<>( DEFAULT_STATISTIC_TEXT );
      this.excludedJobs = new SynchronizedObservableList<>();
   }//End Constructor
   
   /**
    * Access to the {@link Color} of the background, of the statistic.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Color > statisticBackgroundProperty() {
      return background;
   }//End Method

   /**
    * Access to the {@link Color} of the text, of the statistic.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Color > statisticTextProperty() {
      return text;
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
