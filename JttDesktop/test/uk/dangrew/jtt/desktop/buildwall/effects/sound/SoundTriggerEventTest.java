/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.sound;

import uk.dangrew.jtt.model.event.structure.AbstractEventManagerTest;
import uk.dangrew.jtt.model.event.structure.EventManager;

public class SoundTriggerEventTest extends AbstractEventManagerTest< BuildResultStatusChange > {

   /**
    * {@inheritDoc}
    */
   @Override protected EventManager< BuildResultStatusChange > constructSut() {
      return new SoundTriggerEvent();
   }//End Method

}//End Class
