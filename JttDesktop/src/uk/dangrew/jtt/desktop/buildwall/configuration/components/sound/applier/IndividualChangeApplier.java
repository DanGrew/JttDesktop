/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.sound.applier;

import java.util.Arrays;

import uk.dangrew.jtt.connection.api.handling.live.BuildResultStatusChange;
import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;

/**
 * The {@link IndividualChangeApplier} is a {@link BrsChangeListApplier} for a single {@link BuildResultStatusChange}.
 */
public class IndividualChangeApplier extends BrsChangeListApplier {

   /**
    * Constructs a new {@link IndividualChangeApplier}.
    * @param configuration the {@link SoundConfiguration} to configure.
    * @param change the {@link BuildResultStatusChange} to configure.
    */
   public IndividualChangeApplier( SoundConfiguration configuration, BuildResultStatusChange change ) {
      super( configuration, Arrays.asList( change ) );
   }//End Constructor
   
   /**
    * Constructs a new {@link IndividualChangeApplier}.
    * @param configuration the {@link SoundConfiguration} to configure.
    * @param previous the previous {@link BuildResultStatus}.
    * @param current the current {@link BuildResultStatus}.
    */
   public IndividualChangeApplier( SoundConfiguration configuration, BuildResultStatus previous, BuildResultStatus current ) {
      this( configuration, new BuildResultStatusChange( previous, current ) );
   }//End Constructor

}//End Class
