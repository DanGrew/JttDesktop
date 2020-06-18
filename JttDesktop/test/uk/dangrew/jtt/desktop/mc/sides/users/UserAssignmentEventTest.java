/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.users;

import uk.dangrew.kode.event.structure.AbstractEventManagerTest;
import uk.dangrew.kode.event.structure.EventManager;

/**
 * {@link UserAssignmentEvent} test.
 */
public class UserAssignmentEventTest extends AbstractEventManagerTest< UserAssignment > {

   /**
    * {@inheritDoc}
    */
   @Override protected EventManager< UserAssignment > constructSut() {
      return new UserAssignmentEvent();
   }//End Method

}//End Class
