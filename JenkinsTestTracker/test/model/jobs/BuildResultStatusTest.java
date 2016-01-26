/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package model.jobs;

import org.junit.Test;

import utility.TestCommon;

/**
 * {@link BuildResultStatus} test.
 */
public class BuildResultStatusTest {

   @Test public void shouldValueOfToString() {
      TestCommon.assertEnumToStringWithValueOf( BuildResultStatus.class );
   }//End Method
   
   @Test public void shouldValueOfName() {
      TestCommon.assertEnumNameWithValueOf( BuildResultStatus.class );
   }//End Method

}//End Class
