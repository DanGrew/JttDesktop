/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.configuration.system;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link SystemConfiguration} test.
 */
public class SystemConfigurationTest {

   private SystemConfiguration systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new SystemConfiguration();
   }//End Method
   
   @Test public void shouldProvideConfigurations() {
      assertThat( systemUnderTest.getDualConfiguration(), is( notNullValue() ) );
      assertThat( systemUnderTest.getLeftConfiguration(), is( notNullValue() ) );
      assertThat( systemUnderTest.getRightConfiguration(), is( notNullValue() ) );
      assertThat( systemUnderTest.getSoundConfiguration(), is( notNullValue() ) );
   }//End Method

}//End Class
