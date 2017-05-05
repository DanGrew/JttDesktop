/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.friendly;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link FriendlyClass} test.
 */
public class FriendlyClassTest {

   private static final String TEST_FILE = "any-file.txt";
   private static final String TEST_DATA = "FriendClassTest input unique to this test";
   
   private Class< ? extends FriendlyClassTest > thisClass;
   private FriendlyClass< FriendlyClassTest > systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      thisClass = getClass();
      systemUnderTest = new FriendlyClass< FriendlyClassTest >( thisClass );
   }//End Method
   
   @Test public void shouldCallThroughToResourceStream() throws IOException {
      InputStream friendlyStream = systemUnderTest.getResourceAsStream( TEST_FILE );
      assertThat( friendlyStream, is( notNullValue() ) );
      InputStream actualStream = getClass().getResourceAsStream( TEST_FILE );
      
      String friendlyData = null;
      String actualData = null;
      
      try ( BufferedReader friendlyReader = new BufferedReader( new InputStreamReader( friendlyStream ) ) ) {
         friendlyData = friendlyReader.readLine();
      }
      
      try ( BufferedReader actualReader = new BufferedReader( new InputStreamReader( actualStream ) ) ) {
         actualData = actualReader.readLine();
      }
      
      assertThat( friendlyData, is( notNullValue() ) );
      assertThat( actualData, is( notNullValue() ) );
      assertThat( friendlyData, is( actualData ) );
      assertThat( friendlyData, is( TEST_DATA ) );
   }//End Method

}//End Class
