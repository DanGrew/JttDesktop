/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.configuration.components.sound;

import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundConfiguration;

/**
 * The {@link SoundConfigurationApplier} provides in interface for something that takes a file path
 * and applies it as {@link uk.dangrew.jtt.buildwall.effects.sound.SoundConfiguration}.
 */
public interface SoundConfigurationApplier {

   /**
    * Method to configure the path as a sound.
    * @param fileName the path.
    */
   public void configure( String fileName );

   /**
    * Method to determine whether the given {@link SoundConfiguration} is associated.
    * @param configuration the {@link SoundConfiguration} in question.
    * @return true if identical.
    */
   public boolean isAssociatedWith( SoundConfiguration configuration );
   
}//End Interface
