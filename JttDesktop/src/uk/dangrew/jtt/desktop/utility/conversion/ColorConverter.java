/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.utility.conversion;

import javafx.scene.paint.Color;

/**
 * The {@link ColorConverter} is responsible for converter {@link Color}s to different formats
 * and from those formats.
 */
public class ColorConverter {
   
   /**
    * Method to convert a {@link Color} to a hexadecimal representation.
    * @param colour the {@link Color} to convert.
    * @return the {@link String} hex.
    */
   public String colorToHex( Color colour ) {
      if ( colour == null ) {
         return null;
      }
      
      return String.format( "#%02x%02x%02x",
               (int)( colour.getRed() * 255 ),
               (int)( colour.getGreen() * 255 ),
               (int)( colour.getBlue() * 255 ) );
   }//End Method
   
   /**
    * Method to convert the given hexadecimal {@link String} to a {@link Color}.
    * @param hex the {@link String} to convert.
    * @return the {@link Color} converted.
    */
   public Color hexToColor( String hex ) {
      if ( hex == null ) {
         return null;
      }
      
      try {
         return Color.web( hex );
      } catch ( IllegalArgumentException illegalArgument ) {
         return null;
      }
   }//End Method

}//End Class
