/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package main.selector;

import org.junit.Assert;
import org.junit.Test;

import utility.TestCommon;

/**
 * {@link Tools} test.
 */
public class ToolsTest {

   @Test public void shouldValueOfWithName() {
      TestCommon.assertEnumNameWithValueOf( Tools.class );
   }//End Method
   
   @Test public void shouldValueOfWithToString() {
      TestCommon.assertEnumToStringWithValueOf( Tools.class );
   }//End Method
   
   @Test public void shouldHaveDisplayNames(){
      for ( Tools tool : Tools.values() ) {
         Assert.assertNotNull( tool.displayName() );
         Assert.assertFalse( tool.displayName().trim().isEmpty() );
      }
   }//End Method

}//End Class
