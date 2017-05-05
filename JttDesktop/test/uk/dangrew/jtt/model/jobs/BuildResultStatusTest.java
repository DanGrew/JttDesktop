/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.jobs;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

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
   
   @Test public void shouldSupplyDisplayName(){
      for ( BuildResultStatus status : BuildResultStatus.values() ) {
         assertThat( status.displayName(), is( notNullValue() ) );
      }
   }//End Method

}//End Class
