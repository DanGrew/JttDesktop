/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.item;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import uk.dangrew.kode.javafx.style.JavaFxStyle;

/**
 * The {@link SimpleConfigurationTitle} provides a simple implementation of a title and 
 * description panel.
 */
public class SimpleConfigurationTitle extends VBox {

   static final int SPACING = 10;
   
   private final String title;
   private final String description;
   
   /**
    * Constructs a new {@link SimpleConfigurationTitle}.
    * @param title the {@link String} title.
    * @param description the {@link String} description.
    */
   public SimpleConfigurationTitle( String title, String description ) {
      this( title, description, new JavaFxStyle() );
   }//End Constructor
   
   /**
    * Constructs a new {@link SimpleConfigurationTitle}.
    * @param title the {@link String} title.
    * @param description the {@link String} description.
    * @param styling the {@link JavaFxStyle} to use.
    */
   SimpleConfigurationTitle( String title, String description, JavaFxStyle styling ) {
      if ( title == null ) {
         throw new IllegalArgumentException( "Title string must not be null." );
      }
      
      this.title = title;
      this.description = description;
      
      Label titleLabel = styling.createBoldLabel( title );
      titleLabel.setAlignment( Pos.CENTER_LEFT );
      getChildren().add( titleLabel );
      
      if ( description != null ) {
         Label descriptionLabel = styling.createWrappedTextLabel( description );
         getChildren().add( descriptionLabel );
      }
      
      getChildren().add( new Separator() );
      
      setSpacing( SPACING );
   }//End Constructor

   /**
    * Getter for the title of the panel.
    * @return the {@link String} title.
    */
   public String getTitle() {
      return title;
   }//End Method
   
   /**
    * Getter for the description of the panel.
    * @return the {@link String} description.
    */
   public String getDescription() {
      return description;
   }//End Method
   
}//End Class
