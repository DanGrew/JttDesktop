/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.resources;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.image.Image;

/**
 * The {@link ManagementConsoleImages} provides access to {@link Image}s that are available.
 */
public class ManagementConsoleImages {
   
   private static final String PASSED_IMAGE = "uk/dangrew/jtt/resources/mc/buildresultstatus/images/passed.png";
   private static final String ACTION_IMAGE = "uk/dangrew/jtt/resources/mc/buildresultstatus/images/action.png";
   private static final String EQUAL_IMAGE = "uk/dangrew/jtt/resources/mc/buildresultstatus/images/unchanged.png";
   private static final String PEOPLE_IMAGE = "uk/dangrew/jtt/resources/mc/buildresultstatus/images/people.png";
   private static final String CLOSE_IMAGE = "uk/dangrew/jtt/resources/mc/buildresultstatus/images/close.png";
   
   private static final Map< String, Image > imageCache = new HashMap<>();
   
   /**
    * Method to construct a new {@link Image} using the given location.
    * @param imageFile the location of the {@link Image}.
    * @return the constructed image.
    */
   private Image constructImage( String imageFile ) {
      if ( imageCache.containsKey( imageFile ) ) {
         return imageCache.get( imageFile );
      }
      Image image = new Image( getClass().getClassLoader().getResourceAsStream( imageFile ) );
      imageCache.put( imageFile, image );
      return image;
   }//End Method 

   /**
    * Method to construct a new passed {@link Image}.
    * @return the {@link Image}.
    */
   public Image constuctPassedImage(){
      return constructImage( PASSED_IMAGE );
   }//End Method

   /**
    * Method to construct a new action {@link Image}.
    * @return the {@link Image}.
    */
   public Image constuctActionImage(){
      return constructImage( ACTION_IMAGE );
   }//End Method
   
   /**
    * Method to construct a new equal {@link Image}.
    * @return the {@link Image}.
    */
   public Image constuctEqualImage(){
      return constructImage( EQUAL_IMAGE );
   }//End Method
   
   /**
    * Method to construct a new people {@link Image}.
    * @return the {@link Image}.
    */
   public Image constuctPeopleImage(){
      return constructImage( PEOPLE_IMAGE );
   }//End Method
   
   /**
    * Method to construct a new close {@link Image}.
    * @return the {@link Image}.
    */
   public Image constuctCloseImage(){
      return constructImage( CLOSE_IMAGE );
   }//End Method

}//End Class
