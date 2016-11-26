/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.css;

/**
 * {@link CssOnlyProperties} provides common properties in css format that seem only to be
 * accessible via css.
 */
public class CssOnlyProperties {

   static final String BACKGROUND_COLOUR_FX_PROPERTY = "-fx-background-color: ";
   static final String FX_PROPERTY_SUFFIX = ";";
   static final String TRANSPARENT = "transparent";
   
   /**
    * Provides the background property in css format.
    * @return the value.
    */
   public String backgroundProperty(){
      return BACKGROUND_COLOUR_FX_PROPERTY;
   }//End Method
   
   /**
    * Provides the property suffic in css format.
    * @return the value.
    */
   public String propertySuffix(){
      return FX_PROPERTY_SUFFIX;
   }//End Method
   
   /**
    * Provides the transparent key word in css format.
    * @return the value.
    */
   public String transparent(){
      return TRANSPARENT;
   }//End Method
   
}//End Class
