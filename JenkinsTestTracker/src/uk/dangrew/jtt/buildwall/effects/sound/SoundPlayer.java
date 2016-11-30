/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.sound;

import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.friendly.javafx.FriendlyMediaPlayer;

/**
 * The {@link SoundPlayer} is responsible for playing sounds in response to {@link SoundTriggerEvent}s.
 */
public class SoundPlayer {
   
   private final SoundConfiguration configuration;
   private final StringMediaConverter converter;
   
   /**
    * Constructs a new {@link SoundPlayer}.
    * @param configuration the {@link SoundConfiguration}.
    */
   public SoundPlayer( SoundConfiguration configuration ) {
      this( new StringMediaConverter(), configuration );
   }//End Constructor
   
   /**
    * Constructs a new {@link SoundPlayer}.
    * @param converter the {@link StringMediaConverter}.
    * @param configuration the {@link SoundConfiguration}.
    */
   SoundPlayer( StringMediaConverter converter, SoundConfiguration configuration ) {
      if( configuration == null ) {
         throw new IllegalArgumentException( "Must not supply null configuration." );
      }
      this.converter = converter;
      this.configuration = configuration;
      
      new SoundTriggerEvent().register( this::playSoundFromConfiguration );
   }//End Constructor
   
   /**
    * Method to play the sound associated with the {@link BuildResultStatusChange} in the 
    * {@link SoundConfiguration}.
    * @param event the {@link Event} with the {@link BuildResultStatusChange}.
    */
   private void playSoundFromConfiguration( Event< BuildResultStatusChange > event ){
      FriendlyMediaPlayer player = converter.apply( configuration.statusChangeSounds().get( event.getValue() ) );
      if ( player != null ) {
         player.friendly_play();
      }
   }//End Method

}//End Class
