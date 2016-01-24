/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.handling;

import org.junit.Test;

import utility.TestCommon;

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
