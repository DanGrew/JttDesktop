/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.friendly.javafx;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Friendly wrapper for the final {@link MediaPlayer}.
 */
public class FriendlyMediaPlayer {
   
   private final MediaPlayer mediaPlayer;
   
   /**
    * Constructs a new {@link FriendlyMediaPlayer}.
    * @param media the {@link Media}.
    */
   public FriendlyMediaPlayer( Media media ) {
      this.mediaPlayer = new MediaPlayer( media );
   }//End Constructor
   
   /**
    * {@link MediaPlayer#play()}
    */
   public void friendly_play(){
      mediaPlayer.play();
   }//End Method
   
   /**
    * {@link MediaPlayer#setOnEndOfMedia(Runnable)}
    */
   public void friendly_setOnEndOfMedia( Runnable runnable ) {
      mediaPlayer.setOnEndOfMedia( runnable );
   }//End Method

}//End Class
