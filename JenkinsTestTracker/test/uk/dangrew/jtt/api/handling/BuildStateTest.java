/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.handling;

import org.junit.Test;

import uk.dangrew.jtt.model.jobs.BuildState;
import uk.dangrew.jtt.utility.TestCommon;

/**
 * {@link BuildState} test.
 */
public class BuildStateTest {

   @Test public void shouldMapValueOfToString() {
      TestCommon.assertEnumToStringWithValueOf( BuildState.class );
   }//End Method
   
   @Test public void shouldMapValueOfName() {
      TestCommon.assertEnumNameWithValueOf( BuildState.class );
   }//End Method

}//End Class
