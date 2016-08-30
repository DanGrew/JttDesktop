/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.system;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link OperatingSystem} test.
 */
public class OperatingSystemTest {

   private OperatingSystem systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new OperatingSystem();
   }//End Method
   
   @Test public void shouldBeMacIfIsMac() {
      if ( System.getProperty( "os.name" ).contains( "Mac" ) ) {
         assertThat( systemUnderTest.isMac(), is( true ) );
      } else {
         assertThat( systemUnderTest.isMac(), is( false ) );
      }
   }//End Method

}//End Class
