/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package utility;

import java.io.File;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.Assert;

/**
 * Common test values and properties used.
 */
public final class TestCommon {
   
   private static final double PRECISION = 0.001;
   
   /**
    * Gets the precision to use for double comparisons.
    * @return the error to compare to.
    */
   public static final double precision(){
      return PRECISION;
   }//End Method
   
   /**
    * Method to read a text file into a {@link String}.
    * @param locationClass the {@link Class} in the package to load the {@link File} from.
    * @param filename the name of the {@link File}.
    * @return the {@link String} containing all text from the {@link File}.
    */
   public static final String readFileIntoString( Class< ? > locationClass, String filename ) {
      InputStream stream = locationClass.getResourceAsStream( filename );
      if ( stream == null ) return null;
      
      Scanner scanner = new Scanner( stream );
      String content = scanner.useDelimiter( "//Z" ).next();
      scanner.close();
      return content;
   }//End Method
   
   /**
    * Method to assert that all values of the enum map using {@link Enum#valueOf(Class, String)} to {@link Enum#name()}.
    * @param enumClass the {@link Enum} {@link Class} to prove.
    */
   public static < E extends Enum< E > > void assertEnumNameWithValueOf( Class< E > enumClass ) {
      for ( Enum< E > value : enumClass.getEnumConstants() ) {
         Assert.assertEquals( value, Enum.valueOf( enumClass, value.name() ) );
      }
   }//End Method
   
   /**
    * Method to assert that all values of the enum map using {@link Enum#valueOf(Class, String)} to {@link Enum#toString()}.
    * @param enumClass the {@link Enum} {@link Class} to prove.
    */
   public static < E extends Enum< E > > void assertEnumToStringWithValueOf( Class< E > enumClass ) {
      for ( Enum< E > value : enumClass.getEnumConstants() ) {
         Assert.assertEquals( value, Enum.valueOf( enumClass, value.toString() ) );
      }
   }//End Method

}//End Class
