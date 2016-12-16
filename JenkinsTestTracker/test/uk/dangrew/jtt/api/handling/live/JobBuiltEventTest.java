/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling.live;

import uk.dangrew.jtt.event.structure.AbstractEventManagerTest;
import uk.dangrew.jtt.event.structure.EventManager;

public class JobBuiltEventTest extends AbstractEventManagerTest< JobBuiltResult > {

   /**
    * {@inheritDoc}
    */
   @Override protected EventManager< JobBuiltResult > constructSut() {
      return new JobBuiltEvent();
   }//End Method

}//End Class
