/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.api.sources;

import org.junit.Test;

import uk.dangrew.jtt.desktop.api.sources.JenkinsBaseRequest;
import uk.dangrew.jtt.model.utility.TestCommon;

public class JenkinsBaseRequestTest {

   @Test public void shouldMapNameToEnum() {
      TestCommon.assertEnumNameWithValueOf( JenkinsBaseRequest.class );
   }//End Method
   
   @Test public void shouldMapToStringToEnum() {
      TestCommon.assertEnumToStringWithValueOf( JenkinsBaseRequest.class );
   }//End Method
   
}//End Class
