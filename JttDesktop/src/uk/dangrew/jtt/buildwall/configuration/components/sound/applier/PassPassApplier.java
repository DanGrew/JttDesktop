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
 * The {@link PassPassApplier} applies sound for all changes that from passing to passing.
 */
public class PassPassApplier extends BrsChangeListApplier {

   static final List< BuildResultStatusChange > changes = Arrays.asList( 
            new BuildResultStatusChange( BuildResultStatus.SUCCESS, BuildResultStatus.SUCCESS )
   );
   
   /**
    * Constructs a new {@link PassPassApplier}.
    * @param configuration the {@link SoundConfiguration} to configure.
    */
   public PassPassApplier( SoundConfiguration configuration ) {
      super( configuration, changes );
   }//End Constructor

}//End Class
