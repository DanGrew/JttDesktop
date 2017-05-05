/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.sources;

import org.junit.Test;

import uk.dangrew.jtt.utility.TestCommon;

public class JobRequestTest {

   @Test public void shouldMapNameToEnum() {
      TestCommon.assertEnumNameWithValueOf( JobRequest.class );
   }//End Method
   
   @Test public void shouldMapToStringToEnum() {
      TestCommon.assertEnumToStringWithValueOf( JobRequest.class );
   }//End Method
   
}//End Class
