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

public class JenkinsApiJobRequestTest {

   @Test public void shouldMapNameToEnum() {
      TestCommon.assertEnumNameWithValueOf( JenkinsApiJobRequest.class );
   }//End Method
   
   @Test public void shouldMapToStringToEnum() {
      TestCommon.assertEnumToStringWithValueOf( JenkinsApiJobRequest.class );
   }//End Method
   
}//End Class
