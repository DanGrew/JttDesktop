/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.sound;

import java.util.HashSet;
import java.util.Set;

import uk.dangrew.jtt.event.structure.Event;
import uk.dangrew.jtt.friendly.javafx.FriendlyMediaPlayer;

/**
 * The {@link SoundPlayer} is responsible for playing sounds in response to {@link SoundTriggerEvent}s.
 */
public class SoundPlayer {
   
   private final Set< FriendlyMediaPlayer > playersPlaying;
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
      this.playersPlaying = new HashSet<>();
      
      new SoundTriggerEvent().register( this::playSoundFromConfiguration );
   }//End Constructor
   
   /**
    * Method to play the sound associated with the {@link BuildResultStatusChange} in the 
    * {@link SoundConfiguration}.
    * @param event the {@link Event} with the {@link BuildResultStatusChange}.
    */
   private void playSoundFromConfiguration( Event< BuildResultStatusChange > event ){
      FriendlyMediaPlayer player = converter.convert( configuration.statusChangeSounds().get( event.getValue() ) );
      if ( player != null ) {
         playersPlaying.add( player );
         player.friendly_setOnEndOfMedia( () -> playersPlaying.remove( player ) );
         player.friendly_play();
      }
   }//End Method

   /**
    * Method to determine whether the given is associated.
    * @param configuration the {@link SoundConfiguration} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( SoundConfiguration configuration ) {
      return this.configuration == configuration;
   }//End Method
   
   /**
    * Method to determine whether the {@link SoundPlayer} is waiting for the given {@link FriendlyMediaPlayer}
    * to finish.
    * @param player the {@link FriendlyMediaPlayer} in quesiton.
    * @return true if still waiting to finish.
    */
   boolean isWaitingFor( FriendlyMediaPlayer player ) {
      return playersPlaying.contains( player );
   }//End Method

}//End Class
