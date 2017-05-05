/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.api.sources;

import static org.mockito.Mockito.mock;

import org.junit.Test;

import uk.dangrew.jtt.model.jobs.JenkinsJob;
import uk.dangrew.jtt.utility.TestCommon;

public class BuildRequestTest {

   @Test public void shouldMapNameToEnum() {
      TestCommon.assertEnumNameWithValueOf( BuildRequest.class );
   }//End Method
   
   @Test public void shouldMapToStringToEnum() {
      TestCommon.assertEnumToStringWithValueOf( BuildRequest.class );
   }//End Method
   
   @Test( expected = UnsupportedOperationException.class ) public void shouldNotBeSupportedYet(){
      BuildRequest.HistoricDetails.execute( "", mock( JenkinsJob.class ), 100 );
   }//End Method

}//End Class
