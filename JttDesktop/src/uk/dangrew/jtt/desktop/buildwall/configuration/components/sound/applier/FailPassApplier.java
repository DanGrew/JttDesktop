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
import java.util.List;

import uk.dangrew.jtt.desktop.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;

/**
 * The {@link FailPassApplier} applies sound for all changes that from failing to passing.
 */
public class FailPassApplier extends BrsChangeListApplier {

   static final List< BuildResultStatusChange > changes = Arrays.asList( 
            new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.SUCCESS ),
            new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.SUCCESS ),
            new BuildResultStatusChange( BuildResultStatus.NOT_BUILT, BuildResultStatus.SUCCESS ),
            new BuildResultStatusChange( BuildResultStatus.UNKNOWN, BuildResultStatus.SUCCESS ),
            new BuildResultStatusChange( BuildResultStatus.UNSTABLE, BuildResultStatus.SUCCESS )
   );
   
   /**
    * Constructs a new {@link FailPassApplier}.
    * @param configuration the {@link SoundConfiguration} to configure.
    */
   public FailPassApplier( SoundConfiguration configuration ) {
      super( configuration, changes );
   }//End Constructor

}//End Class
