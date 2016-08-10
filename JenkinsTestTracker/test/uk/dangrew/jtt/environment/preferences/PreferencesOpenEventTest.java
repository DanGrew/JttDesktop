/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.preferences;

import uk.dangrew.jtt.event.structure.AbstractEventManagerTest;
import uk.dangrew.jtt.event.structure.EventManager;

/**
 * {@link PreferencesOpenEvent} test.
 */
public class PreferencesOpenEventTest extends AbstractEventManagerTest< Void, PreferenceBehaviour > {

   /**
    * {@inheritDoc}
    */
   @Override protected EventManager< Void, PreferenceBehaviour > constructSut() {
      return new PreferencesOpenEvent();
   }//End Method
}//End Class
