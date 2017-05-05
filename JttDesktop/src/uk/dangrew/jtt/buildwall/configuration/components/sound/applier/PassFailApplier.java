/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.components.sound.applier;

import java.util.Arrays;
import java.util.List;

import uk.dangrew.jtt.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;

/**
 * The {@link PassFailApplier} applies sound for all changes that from passing to failing.
 */
public class PassFailApplier extends BrsChangeListApplier {

   static final List< BuildResultStatusChange > changes = Arrays.asList( 
            new BuildResultStatusChange( BuildResultStatus.SUCCESS, BuildResultStatus.ABORTED ),
            new BuildResultStatusChange( BuildResultStatus.SUCCESS, BuildResultStatus.FAILURE ),
            new BuildResultStatusChange( BuildResultStatus.SUCCESS, BuildResultStatus.NOT_BUILT ),
            new BuildResultStatusChange( BuildResultStatus.SUCCESS, BuildResultStatus.UNKNOWN ),
            new BuildResultStatusChange( BuildResultStatus.SUCCESS, BuildResultStatus.UNSTABLE )
   );
   
   /**
    * Constructs a new {@link PassFailApplier}.
    * @param configuration the {@link SoundConfiguration} to configure.
    */
   public PassFailApplier( SoundConfiguration configuration ) {
      super( configuration, changes );
   }//End Constructor

}//End Class
