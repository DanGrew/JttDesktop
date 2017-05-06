/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.styling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.text.Font;

/**
 * {@link FontFamilies} provides a basic mechanism for identifying available {@link Font}s
 * dependent on the system that will actually render different text styles, rather than just
 * system default.
 */
public class FontFamilies {
   
   static final String SYSTEM_FONT_INDICATOR = "System";
   private static final List< String > USABLE_FAMILIES;
   private static final List< String > UNUSABLE_FAMILIES;
   
   static {
      USABLE_FAMILIES = new ArrayList<>();
      UNUSABLE_FAMILIES = new ArrayList<>();
      
      for ( String family : Font.getFamilies() ) {
         if ( Font.font( family ).getFamily().contains( SYSTEM_FONT_INDICATOR ) ) {
            UNUSABLE_FAMILIES.add( family );
         } else {
            USABLE_FAMILIES.add( family );
         }
      }
   }//End static

   /**
    * Method to get a {@link List} of the {@link Font} family names that can be used to create
    * different {@link Font}s.
    * @return an unmodifiable {@link List}.
    */
   public static List< String > getUsableFontFamilies() {
      return Collections.unmodifiableList( USABLE_FAMILIES );
   }//End Method
   
   /**
    * Method to get a {@link List} of the {@link Font} family names that cannot be used to create
    * different {@link Font}s.
    * @return an unmodifiable {@link List}.
    */
   public static List< String > getUnusableFontFamilies() {
      return Collections.unmodifiableList( UNUSABLE_FAMILIES );
   }//End Method

}//End Class
