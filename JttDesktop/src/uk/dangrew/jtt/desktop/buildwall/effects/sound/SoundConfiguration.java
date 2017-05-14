/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.sound;

import java.util.LinkedHashMap;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import uk.dangrew.jtt.connection.api.handling.live.BuildResultStatusChange;
import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.sd.utility.synchronization.SynchronizedObservableList;
import uk.dangrew.sd.utility.synchronization.SynchronizedObservableMap;

/**
 * Configuration object for sounds.
 */
public class SoundConfiguration {

   private final ObservableMap< BuildResultStatusChange, String > statusChangeSoundsMap;
   private final ObservableList< JenkinsJob > excludedJobs;
   
   /**
    * Constructs a new {@link SoundConfiguration}.
    */
   public SoundConfiguration() {
      this.statusChangeSoundsMap = new SynchronizedObservableMap<>( new LinkedHashMap<>() );
      this.excludedJobs = new SynchronizedObservableList<>();
   }//End Constructor
   
   /**
    * Access to the {@link ObservableMap} for the {@link BuildResultStatusChange} sounds.
    * @return the {@link ObservableMap}.
    */
   public ObservableMap< BuildResultStatusChange, String > statusChangeSounds() {
      return statusChangeSoundsMap;
   }//End Method

   /**
    * Access to the {@link ObservableList} of {@link JenkinsJob}s excluded from sound effects.
    * @return the {@link ObservableList}.
    */
   public ObservableList< JenkinsJob > excludedJobs() {
      return excludedJobs;
   }//End Method

}//End Class
