/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.sound;

import javafx.collections.ObservableMap;
import uk.dangrew.sd.utility.synchronization.SynchronizedObservableMap;

/**
 * Configuration object for sounds.
 */
public class SoundConfiguration {

   private final ObservableMap< BuildResultStatusChange, String > statusChangeSoundsMap;
   
   /**
    * Constructs a new {@link SoundConfiguration}.
    */
   public SoundConfiguration() {
      this.statusChangeSoundsMap = new SynchronizedObservableMap<>();
   }//End Constructor
   
   /**
    * Access to the {@link ObservableMap} for the {@link BuildResultStatusChange} sounds.
    * @return the {@link ObservableMap}.
    */
   public ObservableMap< BuildResultStatusChange, String > statusChangeSounds() {
      return statusChangeSoundsMap;
   }//End Method

}//End Class
