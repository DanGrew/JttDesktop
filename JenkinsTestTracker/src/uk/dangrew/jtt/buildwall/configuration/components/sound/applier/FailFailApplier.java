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
 * The {@link FailFailApplier} applies sound for all changes that from failing to failing.
 */
public class FailFailApplier extends BrsChangeListApplier {

   static final List< BuildResultStatusChange > changes = Arrays.asList( 
            new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.ABORTED ),
            new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.FAILURE ),
            new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.NOT_BUILT ),
            new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.UNKNOWN ),
            new BuildResultStatusChange( BuildResultStatus.ABORTED, BuildResultStatus.UNSTABLE ),
            new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.ABORTED ),
            new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.FAILURE ),
            new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.NOT_BUILT ),
            new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.UNKNOWN ),
            new BuildResultStatusChange( BuildResultStatus.FAILURE, BuildResultStatus.UNSTABLE ),
            new BuildResultStatusChange( BuildResultStatus.NOT_BUILT, BuildResultStatus.ABORTED ),
            new BuildResultStatusChange( BuildResultStatus.NOT_BUILT, BuildResultStatus.FAILURE ),
            new BuildResultStatusChange( BuildResultStatus.NOT_BUILT, BuildResultStatus.NOT_BUILT ),
            new BuildResultStatusChange( BuildResultStatus.NOT_BUILT, BuildResultStatus.UNKNOWN ),
            new BuildResultStatusChange( BuildResultStatus.NOT_BUILT, BuildResultStatus.UNSTABLE ),
            new BuildResultStatusChange( BuildResultStatus.UNKNOWN, BuildResultStatus.ABORTED ),
            new BuildResultStatusChange( BuildResultStatus.UNKNOWN, BuildResultStatus.FAILURE ),
            new BuildResultStatusChange( BuildResultStatus.UNKNOWN, BuildResultStatus.NOT_BUILT ),
            new BuildResultStatusChange( BuildResultStatus.UNKNOWN, BuildResultStatus.UNKNOWN ),
            new BuildResultStatusChange( BuildResultStatus.UNKNOWN, BuildResultStatus.UNSTABLE ),
            new BuildResultStatusChange( BuildResultStatus.UNSTABLE, BuildResultStatus.ABORTED ),
            new BuildResultStatusChange( BuildResultStatus.UNSTABLE, BuildResultStatus.FAILURE ),
            new BuildResultStatusChange( BuildResultStatus.UNSTABLE, BuildResultStatus.NOT_BUILT ),
            new BuildResultStatusChange( BuildResultStatus.UNSTABLE, BuildResultStatus.UNKNOWN ),
            new BuildResultStatusChange( BuildResultStatus.UNSTABLE, BuildResultStatus.UNSTABLE )
   );
   
   /**
    * Constructs a new {@link FailFailApplier}.
    * @param configuration the {@link SoundConfiguration} to configure.
    */
   public FailFailApplier( SoundConfiguration configuration ) {
      super( configuration, changes );
   }//End Constructor

}//End Class
