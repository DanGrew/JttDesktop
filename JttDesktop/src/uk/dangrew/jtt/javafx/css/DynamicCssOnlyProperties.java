/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.css;

import javafx.scene.Node;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.paint.Color;
import uk.dangrew.jtt.styling.BuildWallStyles;
import uk.dangrew.jtt.styling.BuildWallThemes;
import uk.dangrew.jtt.styling.SystemStyles;
import uk.dangrew.jtt.styling.SystemStyling;
import uk.dangrew.jtt.utility.conversion.ColorConverter;

/**
 * {@link DynamicProgressBarProperties} provides a method of dynamically configuring properties
 * on the {@link ProgressBar} that can only be set by style sheets. This is potentially a fragile
 * mechanism however it seems the only way to achieve dynamic configuration.
 */
public class DynamicCssOnlyProperties {

   static final String TRACK_LOOKUP = ".track";
   static final String BAR_LOOKUP = ".bar";
   
   private final ColorConverter colorConverter;
   private final SystemStyles styling;
   private final CssOnlyProperties css; 
   
   /**
    * Constructs a new {@link DynamicProgressBarProperties}.
    */
   public DynamicCssOnlyProperties() {
      this( SystemStyling.get(), new ColorConverter(), new CssOnlyProperties() );
   }//End Constructor
   
   /**
    * Constructs a new {@link DynamicProgressBarProperties}.
    * @param styling the {@link SystemStyles} for built in properties.
    * @param colorConverter the {@link ColorConverter} for converter to hex.
    * @param css the {@link CssOnlyProperties}.
    */
   DynamicCssOnlyProperties( SystemStyles styling, ColorConverter colorConverter, CssOnlyProperties css ) {
      this.styling = styling;
      this.colorConverter = colorConverter;
      this.css = css;
   }//End Constructor

   /**
    * Method to apply the {@link BuildWallThemes#Standard} {@link BuildWallStyles} to the {@link ProgressBar}.
    * @param style the {@link BuildWallStyles} to apply.
    * @param progressBar the {@link ProgressBar} to apply to.
    */
   public void applyStandardColourFor( BuildWallStyles style, ProgressBar progressBar ) {
      styling.applyStyle( style, BuildWallThemes.Standard, progressBar );
   }//End Method

   /**
    * Method to apply the {@link BuildWallThemes#Frozen} {@link BuildWallStyles} to the {@link ProgressBar}.
    * @param style the {@link BuildWallStyles} to apply.
    * @param progressBar the {@link ProgressBar} to apply to.
    */
   public void applyFrozenColourFor( BuildWallStyles style, ProgressBar progressBar ) {
      styling.applyStyle( style, BuildWallThemes.Frozen, progressBar );
   }//End Method
   
   /**
    * Method to apply custom {@link Color}s to the {@link ProgressBar}. If {@link Node} lookup fails
    * the application of the {@link Color} is silently abandoned.
    * @param barColour the bar {@link Color}.
    * @param trackColour the track {@link Color}.
    * @param progressBar the {@link ProgressBar} to apply to.
    */
   public void applyCustomColours( Color barColour, Color trackColour, ProgressBar progressBar ) {
      Node track;
      Node bar;
      try {
         track = lookup( TRACK_LOOKUP, progressBar );
         bar = lookup( BAR_LOOKUP, progressBar );
      } catch ( ClassNotFoundException exception ) {
         //issue raised to add to digest.
         return;
      }
      track.setStyle( formatBackgroundColourProperty( colorConverter.colorToHex( trackColour ) ) );
      bar.setStyle( formatBackgroundColourProperty( colorConverter.colorToHex( barColour ) ) );
   }//End Method
   
   /**
    * Method to remove the {@link ScrollPane} background by making it transparent.
    * @param scrollPane the {@link ScrollPane} to configure.
    */
   public void removeScrollPaneBorder( ScrollPane scrollPane ) {
      scrollPane.setStyle( formatBackgroundColourProperty( css.transparent() ) );
   }//End Method
   
   /**
    * Method to change the {@link ScrollPane} background.
    * @param scrollPane the {@link ScrollPane} to configure.
    * @param colour the {@link Color} to change to.
    */
   public void applyBackgroundColour( ScrollPane scrollPane, Color colour ) {
      scrollPane.setStyle( formatBackgroundColourProperty( colorConverter.colorToHex( colour ) ) );
   }//End Method
   
   /**
    * Method to look up the node identifier in the given {@link Node}.
    * @param nodeIdentifier the id of the {@link Node}.
    * @param lookInside the {@link Node} to look in.
    * @return the {@link Node} found.
    * @throws ClassNotFoundException if the {@link Node} id cannot be found.
    */
   private Node lookup( String nodeIdentifier, Node lookInside ) throws ClassNotFoundException {
      Node node = lookInside.lookup( nodeIdentifier );
      if ( node == null ) {
         throw new ClassNotFoundException( "Cannot find " + nodeIdentifier + " inside " + lookInside.toString() );
      }
      
      return node;
   }//End Method
   
   /**
    * Method to format the background {@link Color} using the css style property.
    * @param colour the {@link Color} to apply.
    * @return the {@link String} property in css style.
    */
   String formatBackgroundColourProperty( String colour ) {
      return css.backgroundProperty() + colour + css.propertySuffix();
   }//End Method

}//End Constructor
