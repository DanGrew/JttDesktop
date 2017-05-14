/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.styling;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import uk.dangrew.jtt.model.utility.TestCommon;

/**
 * {@link BuildWallStylesTest}.
 */
public class BuildWallStylesTest {

   @Test public void shouldHaveValueOfForName() {
      TestCommon.assertEnumNameWithValueOf( BuildWallStyles.class );
   }//End Method
   
   @Test public void shouldHaveValueOfForToString() {
      TestCommon.assertEnumToStringWithValueOf( BuildWallStyles.class );
   }//End Method
   
   @Test public void shouldHaveLabelsAssociated(){
      for ( BuildWallStyles style : BuildWallStyles.values() ) {
         assertThat( style.label(), notNullValue() );
      }
   }//End Method

}//End Class
