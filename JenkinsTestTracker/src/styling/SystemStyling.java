/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package styling;

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
   static SystemStyles get() {
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
    * @param parent the {@link Parent} to apply to.
    */
   public static void applyStyle( Enum< ? > tyle, Parent parent ) {
      if ( instance == null ) throw new IllegalStateException( "No System Stylings present." );
      instance.applyStyle( tyle, parent );
   }//End Method
   
}//End Class
