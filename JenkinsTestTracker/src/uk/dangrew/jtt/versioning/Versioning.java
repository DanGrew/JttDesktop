/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.versioning;

import java.io.File;

import uk.dangrew.sd.logging.io.BasicStringIO;

/**
 * {@link Versioning} is responsible for providing the correct version number of the software.
 */
public class Versioning {

   static final String FAILED_TO_FIND_VERSION = "WORKSPACE";
   static final String VERSION_NUMBER_ENV_VAR = "VERSION_NUMBER";
   static final String VERSION_FILE_NAME = "../VERSION";

   private final String version;
   
   /**
    * Constructs a new {@link Versioning}.
    */
   public Versioning() {
      BasicStringIO stringIO = new BasicStringIO();
      File versionFile = new File( getClass().getResource( VERSION_FILE_NAME ).getFile() );
      System.out.println( versionFile.getAbsolutePath() );
      version = stringIO.read( versionFile );
   }//End Constructor
   
   /**
    * Method to get the {@link String} version number of the software.
    * @return the version number.
    */
   public String getVersionNumber() {
      return version;
   }//End Method

}//End Class
