/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.configuration.properties;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.utility.TestCommon;

/**
 * {@link DualConfigurationImpl} test.
 */
public class DualConfigurationImplTest {

   private DualConfiguration systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new DualConfigurationImpl();
   }//End Method
   
   @Test public void shouldProvideDividerPositionProperty() {
      assertThat( systemUnderTest.dividerPositionProperty(), is( notNullValue() ) );
      assertThat( systemUnderTest.dividerPositionProperty().get(), is( closeTo( DualConfigurationImpl.DEFAULT_DIVIDER_POSITION, TestCommon.precision() ) ) );
   }//End Method
   
   @Test public void shouldProvideDividerOrientationProperty() {
      assertThat( systemUnderTest.dividerOrientationProperty(), is( notNullValue() ) );
      assertThat( systemUnderTest.dividerOrientationProperty().get(), is( DualConfigurationImpl.DEFAULT_DIVIDER_ORIENTATION ) );
   }//End Method

}//End Class
