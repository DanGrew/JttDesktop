/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.notifiers.jobs;

import java.util.function.Function;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uk.dangrew.jtt.desktop.mc.resources.ManagementConsoleImages;

/**
 * The {@link BuildResultStatusHighLevelChange} provides the different levels of change.
 */
public enum BuildResultStatusHighLevelChange {

   Passed( images -> images.constuctPassedImage() ),
   ActionRequired( images -> images.constuctActionImage() ),
   Unchanged( images -> images.constuctEqualImage() );
   
   private static final ManagementConsoleImages images = new ManagementConsoleImages();
   private final Function< ManagementConsoleImages, Image > imageSupplier;
   
   /**
    * Constructs a new {@link BuildResultStatusHighLevelChange}.
    * @param imageSupplier the {@link Function} supplying the {@link Image}.
    */
   private BuildResultStatusHighLevelChange( Function< ManagementConsoleImages, Image > imageSupplier ) {
      this.imageSupplier = imageSupplier;
   }//End Constructor
   
   /**
    * Method to construct a new {@link ImageView} and {@link Image} to be sized by the caller.
    * @return the {@link ImageView} with {@link Image} inside.
    */
   public ImageView constructImage(){
      return new ImageView( imageSupplier.apply( images ) );
   }//End Method

}//End Enum
