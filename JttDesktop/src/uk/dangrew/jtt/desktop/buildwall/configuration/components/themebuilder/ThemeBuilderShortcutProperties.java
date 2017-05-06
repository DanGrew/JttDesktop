/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.themebuilder;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * {@link ThemeBuilderShortcutProperties} provide properties that allow shortcuts to be made
 * when configuring a {@link uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme}. For
 * example, a global {@link javafx.scene.control.ColorPicker} whose value can be set on any
 * property.
 */
class ThemeBuilderShortcutProperties {

   private final ObjectProperty< Color > shortcutColor;
   
   /**
    * Constructs a new {@link ThemeBuilderShortcutProperties}.
    */
   public ThemeBuilderShortcutProperties() {
      this.shortcutColor = new SimpleObjectProperty<>( null );
   }//End Constructor
   
   /**
    * Access to the {@link ObjectProperty} for the {@link Color}.
    * @return the {@link ObjectProperty}.
    */
   public ObjectProperty< Color > shortcutColorProperty() {
      return shortcutColor;
   }//End Method

}//End Class
