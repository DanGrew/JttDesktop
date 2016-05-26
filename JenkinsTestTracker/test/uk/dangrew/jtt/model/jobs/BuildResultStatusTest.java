/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.jobs;

import org.junit.Test;

import uk.dangrew.jtt.model.jobs.BuildResultStatus;
import uk.dangrew.jtt.utility.TestCommon;

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
