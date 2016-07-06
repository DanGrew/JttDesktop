/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.versioning;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import uk.dangrew.jtt.utility.friendly.FriendlyClass;

/**
 * {@link Versioning} is responsible for providing the correct version number of the software.
 */
public class Versioning {

   static final String FAILED_TO_FIND_VERSION = "WORKSPACE";
   static final String VERSION_NUMBER_ENV_VAR = "VERSION_NUMBER";
   static final String VERSION_FILE_NAME = "VERSION";

   private String version;
   
   /**
    * Constructs a new {@link Versioning}.
    */
   public Versioning() {
      this( new FriendlyClass<>( Versioning.class ) );
   }//End Constructor
   
   /**
    * Constructs a new {@link Versioning}.
    * @param classSource the {@link FriendlyClass} for accessing the file data.
    */
   Versioning( FriendlyClass< Versioning > classSource ) {
      InputStream input = classSource.getResourceAsStream( VERSION_FILE_NAME );
      try ( BufferedReader reader = new BufferedReader( new InputStreamReader( input ) ) ) {
         version = reader.readLine();
         if ( version == null || version.trim().length() == 0 ) {
            throw new IllegalStateException( "Invalid version number." );
         }
      } catch ( IOException e ) {
         throw new IllegalStateException( "Cannot find version number file." );
      }
   }//End Constructor
   
   /**
    * Method to get the {@link String} version number of the software.
    * @return the version number.
    */
   public String getVersionNumber() {
      return version;
   }//End Method

}//End Class
