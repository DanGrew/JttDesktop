/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.notifiers.jobs;

import java.util.function.Function;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import uk.dangrew.jtt.mc.resources.ManagementConsoleImages;
import uk.dangrew.jtt.model.jobs.BuildResultStatus;

/**
 * The {@link BuildResultStatusChange} provides the different levels of change.
 */
public enum BuildResultStatusChange {

   Passed( images -> images.constuctPassedImage() ),
   ActionRequired( images -> images.constuctActionImage() ),
   Unchanged( images -> images.constuctEqualImage() );
   
   private static final ManagementConsoleImages images = new ManagementConsoleImages();
   private final Function< ManagementConsoleImages, Image > imageSupplier;
   
   /**
    * Constructs a new {@link BuildResultStatusChange}.
    * @param imageSupplier the {@link Function} supplying the {@link Image}.
    */
   private BuildResultStatusChange( Function< ManagementConsoleImages, Image > imageSupplier ) {
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
