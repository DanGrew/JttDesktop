/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.versioning;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link Versioning} test.
 */
public class VersioningTest {

   private Versioning systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new Versioning();
      systemUnderTest.getVersionNumber();
   }//End Method
   
   @Test public void shouldNotHaveUnknownVersionNumber() {
      String versionNumber = systemUnderTest.getVersionNumber();
      System.out.println( "Found version number: " + versionNumber );
      
      String variable = System.getenv( Versioning.VERSION_NUMBER_ENV_VAR );
      if ( variable != null ) {
         assertThat( versionNumber, is( variable ) );
      } else {
         assertThat( versionNumber, is( Versioning.FAILED_TO_FIND_VERSION ) );
      }
   }//End Method

}//End Class
