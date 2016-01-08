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

}//End Class
