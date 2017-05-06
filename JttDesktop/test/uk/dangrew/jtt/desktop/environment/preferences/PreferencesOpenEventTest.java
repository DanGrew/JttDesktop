/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.environment.preferences;

import uk.dangrew.jtt.desktop.environment.preferences.PreferenceBehaviour;
import uk.dangrew.jtt.desktop.environment.preferences.PreferencesOpenEvent;
import uk.dangrew.jtt.model.event.structure.AbstractEventManagerTest;
import uk.dangrew.jtt.model.event.structure.EventManager;

/**
 * {@link PreferencesOpenEvent} test.
 */
public class PreferencesOpenEventTest extends AbstractEventManagerTest< PreferenceBehaviour > {

   /**
    * {@inheritDoc}
    */
   @Override protected EventManager< PreferenceBehaviour > constructSut() {
      return new PreferencesOpenEvent();
   }//End Method
}//End Class
