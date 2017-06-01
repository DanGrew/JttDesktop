/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.sound;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import uk.dangrew.jtt.desktop.friendly.javafx.FriendlyMediaPlayer;

/**
 * The {@link StringMediaConverter} is responsible for taking a {@link String} file name
 * and turning it into a {@link javafx.scene.media.MediaPlayer}.
 */
public class StringMediaConverter {

   /**
    * Method to convert the given media file into a {@link FriendlyMediaPlayer}.
    * @param mediaFile the path to the media.
    * @return the {@link FriendlyMediaPlayer} or null if invalid.
    */
   public FriendlyMediaPlayer convert( String mediaFile ) {
      Media media = extractMedia( mediaFile );
      if ( media == null ) {
         return null;
      }
      
      try {
         return new FriendlyMediaPlayer( media );
      } catch ( MediaException exception ) {
         //should use digest, not tested for ease
         return null;
      }
   }//End Method
   
   /**
    * Method to safely extract the {@link Media}, handling any failures appropriately.
    * @param mediaFile the media file path.
    * @return the {@link Media}, or null.
    */
   private Media extractMedia( String mediaFile ) {
      if ( mediaFile == null ) {
         return null;
      }
      try {
         return new Media( new File( mediaFile ).toURI().toString() );
      } catch ( MediaException exception ){
         //should use digest
         return null;
      }
   }//End Method
   
   /**
    * Method to determine whether the given media file is valid media.
    * @param mediaFile the media file in question.
    * @return true if valid.
    */
   public boolean isValidMedia( String mediaFile ) {
      return extractMedia( mediaFile ) != null;
   }//End Method
   
}//End Class
