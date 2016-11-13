/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.styling;

import javafx.scene.Parent;

/**
 * {@link SystemStyling} provides a singleton for {@link SystemStyles}.
 */
public class SystemStyling {

   private static SystemStyles instance = null; 
   
   /**
    * Getter for the singleton.
    * @return the {@link SystemStyles}.
    */
   public static SystemStyles get() {
      return instance;
   }//End Method

   /**
    * Setter for the {@link SystemStyles}, used for testing.
    * @param styles the {@link SystemStyles}.
    */
   public static void set( SystemStyles styles ) {
      instance = styles;
   }//End Method

   /**
    * Convenience method to initialise a default {@link SystemStyles}.
    */
   public static void initialise() {
      instance = new SystemStyles();
   }//End Method
   
   /**
    * Method to apply the style for the given.
    * @param tyle the style to use.
    * @param theme the {@link BuildWallThemes} to get the style from.
    * @param parent the {@link Parent} to apply to.
    */
   public static void applyStyle( Enum< ? > tyle, BuildWallThemes theme, Parent parent ) {
      if ( instance == null ) {
         throw new IllegalStateException( "No System Stylings present." );
      }
      instance.applyStyle( tyle, theme, parent );
   }//End Method
   
}//End Class
