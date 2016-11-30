/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.sound;

import java.io.File;
import java.util.function.Function;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import uk.dangrew.jtt.friendly.javafx.FriendlyMediaPlayer;

/**
 * The {@link StringMediaConverter} is responsible for taking a {@link String} file name
 * and turning it into a {@link javafx.scene.media.MediaPlayer}.
 */
public class StringMediaConverter implements Function< String, FriendlyMediaPlayer >{

   /**
    * {@inheritDoc}
    */
   @Override public FriendlyMediaPlayer apply( String mediaFile ) {
      Media media = extractMedia( mediaFile );
      if ( media == null ) {
         return null;
      }
      
      return new FriendlyMediaPlayer( media );
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

}//End Class
