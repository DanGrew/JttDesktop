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

import uk.dangrew.jtt.desktop.buildwall.effects.sound.BuildResultStatusChange;
import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundConfiguration;

public class PassPassApplierTest extends BrsChangeListApplierTest {
   
   /**
    * {@inheritDoc}
    */
   @Override protected void populateChanges( List< BuildResultStatusChange > changes ) {
      changes.addAll( PassPassApplier.changes );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected BrsChangeListApplier constructSut( SoundConfiguration configuration, List< BuildResultStatusChange > changes ) {
      return new PassPassApplier( configuration );
   }//End Method

}//End Class
