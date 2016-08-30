/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility.system;

/**
 * {@link OperatingSystem} provides information about this system to handle special features.
 */
public class OperatingSystem {
   
   private static final String OPERATING_SYSTEM = System.getProperty( "os.name" ).toLowerCase();
   
   /**
    * Method to determine if this system is a Mac.
    * @return true if this system is a Mac.
    */
   public boolean isMac(){
      return OPERATING_SYSTEM.indexOf( "mac" ) >= 0;
   }//End Method

}//End Class
