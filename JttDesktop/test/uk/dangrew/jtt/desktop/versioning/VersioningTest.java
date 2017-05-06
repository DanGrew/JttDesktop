/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.versioning;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.desktop.versioning.Versioning;
import uk.dangrew.jtt.model.utility.friendly.FriendlyClass;

/**
 * {@link Versioning} test.
 */
public class VersioningTest {

   private FriendlyClass< Versioning > friendlyClass;
   private Versioning systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      friendlyClass = spy( new FriendlyClass<>( Versioning.class ) );
      doCallRealMethod().when( friendlyClass ).getResourceAsStream( Versioning.VERSION_FILE_NAME );
   }//End Method
   
   @Test public void shouldNotHaveUnknownVersionNumber() {
      systemUnderTest = new Versioning();
      String versionNumber = systemUnderTest.getVersionNumber();
      System.out.println( "Found version number: " + versionNumber );
      
      String variable = System.getenv( Versioning.VERSION_NUMBER_ENV_VAR );
      if ( variable != null ) {
         assertThat( versionNumber, is( variable ) );
      } else {
         assertThat( versionNumber, is( Versioning.FAILED_TO_FIND_VERSION ) );
      }
   }//End Method
   
   @Test public void shouldFindFileWithinJar(){
      systemUnderTest = new Versioning( friendlyClass );
      verify( friendlyClass ).getResourceAsStream( Versioning.VERSION_FILE_NAME );
   }//End Method

}//End Class
