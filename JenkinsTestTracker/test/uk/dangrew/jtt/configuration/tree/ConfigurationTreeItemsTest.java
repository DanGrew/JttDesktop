/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.tree;

import org.junit.Test;

import uk.dangrew.jtt.utility.TestCommon;

/**
 * {@link ConfigurationTreeItems} test.
 */
public class ConfigurationTreeItemsTest {

   @Test public void shouldMatchEnumNameToValueOf() {
      TestCommon.assertEnumNameWithValueOf( ConfigurationTreeItems.class );
   }//End Method
   
   @Test public void shouldMatchEnumToStringWithValueOf() {
      TestCommon.assertEnumToStringWithValueOf( ConfigurationTreeItems.class );
   }//End Method

}//End Class
