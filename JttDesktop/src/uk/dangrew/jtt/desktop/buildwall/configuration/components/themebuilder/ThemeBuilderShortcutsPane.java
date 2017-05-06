/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.themebuilder;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.desktop.buildwall.configuration.style.JavaFxStyle;
import uk.dangrew.jtt.desktop.javafx.registrations.ChangeListenerBindingImpl;
import uk.dangrew.jtt.desktop.javafx.registrations.RegistrationManager;

/**
 * {@link ThemeBuilderShortcutsPane} provides options for shortcuts while configuring the 
 * a {@link uk.dangrew.jtt.buildwall.configuration.theme.BuildWallTheme}.
 */
class ThemeBuilderShortcutsPane extends GridPane {

   static final String LABEL_TEXT = "Choose Colour";
   
   private final ThemeBuilderShortcutProperties shortcuts;
   private final Label label;
   private final ColorPicker picker;
   private final RegistrationManager registrations;
   
   /**
    * Constructs a new {@link ThemeBuilderShortcutsPane}.
    * @param shortcuts the {@link ThemeBuilderShortcutProperties}.
    */
   public ThemeBuilderShortcutsPane( ThemeBuilderShortcutProperties shortcuts ) {
      this( new JavaFxStyle(), shortcuts );
   }//End Constructor
   
   /**
    * Constructs a new {@link ThemeBuilderShortcutsPane}.
    * @param styling the {@link JavaFxStyle}.
    * @param shortcuts the {@link ThemeBuilderShortcutProperties}.
    */
   ThemeBuilderShortcutsPane( JavaFxStyle styling, ThemeBuilderShortcutProperties shortcuts ){
      this.shortcuts = shortcuts;
      this.registrations = new RegistrationManager();
      styling.configureHalfWidthConstraints( this );
      
      label = styling.createBoldLabel( LABEL_TEXT );
      add( label, 0, 0 );
      
      picker = new ColorPicker();
      styling.configureColorPicker( picker, ( Color )null );
      add( picker, 1, 0 );
      
      this.registrations.apply( 
               new ChangeListenerBindingImpl< Color >( picker.valueProperty(), shortcuts.shortcutColorProperty() 
      ) );
      
      styling.applyBasicPadding( this );
   }//End Constructor
   
   /**
    * Method to detach from system by removing all registrations.
    */
   void detachFromSystem(){
      registrations.shutdown();
   }//End Method

   /**
    * Method to determine whether this is associated with the given.
    * @param shortcuts the {@link ThemeBuilderShortcutProperties} in question.
    * @return true if identical.
    */
   boolean isAssociatedWith( ThemeBuilderShortcutProperties shortcuts ) {
      return this.shortcuts == shortcuts;
   }//End Method
   
   Label label(){
      return label;
   }//End Method
   
   ColorPicker picker(){
      return picker;
   }//End Method
   
}//End Class
