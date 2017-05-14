/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.sound.applier;

import java.util.List;

import uk.dangrew.jtt.connection.api.handling.live.BuildResultStatusChange;
import uk.dangrew.jtt.desktop.buildwall.configuration.components.sound.SoundConfigurationApplier;
import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundConfiguration;

/**
 * The {@link BrsChangeListApplier} applies sound for all {@link BuildResultStatusChange}s associated.
 */
public class BrsChangeListApplier implements SoundConfigurationApplier {

   private final SoundConfiguration configuration;
   private final List< BuildResultStatusChange > changes;
   
   /**
    * Constructs a new {@link BrsChangeListApplier}.
    * @param configuration the {@link SoundConfiguration} to configure.
    * @param changes the {@link List} of {@link BuildResultStatusChange}s to set.
    */
   public BrsChangeListApplier( SoundConfiguration configuration, List< BuildResultStatusChange > changes ) {
      if ( configuration == null || changes == null ) {
         throw new IllegalArgumentException( "Must supply non null parameters." );
      }
      this.configuration = configuration;
      this.changes = changes;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void configure( String fileName ) {
      changes.forEach( c -> configuration.statusChangeSounds().put( c, fileName ) );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override public boolean isAssociatedWith( SoundConfiguration configuration ) {
      return this.configuration == configuration;
   }//End Method

}//End Class
