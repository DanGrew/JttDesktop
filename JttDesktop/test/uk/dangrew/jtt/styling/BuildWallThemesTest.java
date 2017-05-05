/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.styling;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import uk.dangrew.jtt.utility.TestCommon;

public class BuildWallThemesTest {

   @Test public void shouldHaveValueOfForName() {
      TestCommon.assertEnumNameWithValueOf( BuildWallThemes.class );
   }//End Method
   
   @Test public void shouldHaveValueOfForToString() {
      TestCommon.assertEnumToStringWithValueOf( BuildWallThemes.class );
   }//End Method
   
   @Test public void shouldHaveLabelsAssociated(){
      for ( BuildWallThemes style : BuildWallThemes.values() ) {
         assertThat( style.sheet(), notNullValue() );
      }
   }//End Method

}//End Class
