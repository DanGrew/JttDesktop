/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package model.tests;

import org.junit.Test;

import utility.TestCommon;

/**
 * {@link TestResultStatus} test.
 */
public class TestResultStatusTest {

   @Test public void shouldValueOfToString() {
      TestCommon.assertEnumToStringWithValueOf( TestResultStatus.class );
   }//End Method
   
   @Test public void shouldValueOfName() {
      TestCommon.assertEnumNameWithValueOf( TestResultStatus.class );
   }//End Method

}//End Class
