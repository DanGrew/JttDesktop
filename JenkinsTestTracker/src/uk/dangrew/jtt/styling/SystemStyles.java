/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.styling;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Parent;

/**
 * {@link SystemStyles} provides a method of holding styles and types within them,
 * and applying them to components.
 */
public class SystemStyles {
   
   private Map< Enum< ? >, String > sheets;
   private Map< Enum< ? >, String > styles;
   
   /**
    * Constructs a new {@link SystemStyles}, finding the styles needed for the system.
    */
   SystemStyles() {
      sheets = new HashMap<>();
      styles = new HashMap<>();
      
      for ( BuildWallStyles style : BuildWallStyles.values() ) {
         sheets.put( style, BuildWallStyles.SHEET );
         styles.put( style, style.label() );
      }
   }//End Class

   /**
    * Method to apply the given style to the given {@link Parent}.
    * @param style the {@link Enum} style to apply.
    * @param parent the {@link Parent} to apply to.
    */
   public void applyStyle( Enum< ? > style, Parent parent ) {
      if ( hasSheet( style ) && hasLabel( style ) ) {
         parent.getStylesheets().clear();
         parent.getStylesheets().add( getSheet( style ) );
         parent.getStyleClass().clear();
         parent.getStyleClass().add( getLabel( style ) );
      }
   }//End Method

   /**
    * Method to determine whether the given {@link Enum} has a style sheet.
    * @param type the {@link Enum}.
    * @return true if found.
    */
   boolean hasSheet( Enum< ? > type ) {
      return sheets.containsKey( type );
   }//End Method

   /**
    * Method to determine whether the given {@link Enum} has a style label.
    * @param type the {@link Enum}.
    * @return true if found.
    */
   boolean hasLabel( Enum< ? > type ) {
      return styles.containsKey( type );
   }//End Method

   /**
    * Method to get the given {@link Enum}s style sheet.
    * @param type the {@link Enum}.
    * @return the {@link String} sheet.
    */
   String getSheet( Enum< ? > progressbarpassed ) {
      return sheets.get( progressbarpassed );
   }//End Method

   /**
    * Method to get the given {@link Enum}s style label.
    * @param type the {@link Enum}.
    * @return the {@link String} label.
    */
   String getLabel( Enum< ? > progressbarpassed ) {
      return styles.get( progressbarpassed );
   }//End Method
}//End Class

