/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.utility;

/**
 * {@link TestableFonts} provides information for testing {@link javafx.scene.text.Font}s.
 */
public class TestableFonts {

   private static final String COMMON_FONT = "Arial";
   
   /**
    * Method to get the family name of a common {@link javafx.scene.text.Font} across platforms.
    * @return the {@link String} family.
    */
   public static String commonFont(){
      return COMMON_FONT;
   }//End Method
}//End Class
