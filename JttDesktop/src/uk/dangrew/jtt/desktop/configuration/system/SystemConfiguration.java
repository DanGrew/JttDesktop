/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.system;

import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.BuildWallConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.DualWallConfiguration;
import uk.dangrew.jtt.desktop.buildwall.configuration.properties.DualWallConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.jtt.desktop.statistics.configuration.StatisticsConfiguration;

/**
 * The {@link SystemConfiguration} collects the different configuration items
 * which can be provided as and when they are needed.
 */
public class SystemConfiguration {
   
   private final DualWallConfiguration dualConfiguration;
   private final SoundConfiguration soundConfiguration;
   private final BuildWallConfiguration leftConfiguration;
   private final BuildWallConfiguration rightConfiguration;
   private final StatisticsConfiguration statisticsConfiguration;
   
   public SystemConfiguration() {
      this.dualConfiguration = new DualWallConfigurationImpl();
      this.soundConfiguration = new SoundConfiguration();
      this.leftConfiguration = new BuildWallConfigurationImpl();
      this.rightConfiguration = new BuildWallConfigurationImpl();
      this.statisticsConfiguration = new StatisticsConfiguration();
   }//End Constructor

   /**
    * Getter for the {@link DualWallConfiguration}.
    * @return the {@link DualWallConfiguration}.
    */
   public DualWallConfiguration getDualConfiguration() {
      return dualConfiguration;
   }//End Method
   
   /**
    * Getter for the {@link SoundConfiguration}.
    * @return the {@link SoundConfiguration}.
    */
   public SoundConfiguration getSoundConfiguration() {
      return soundConfiguration;
   }//End Method
   
   /**
    * Getter for the left {@link BuildWallConfiguration}.
    * @return the {@link BuildWallConfiguration}.
    */
   public BuildWallConfiguration getLeftConfiguration() {
      return leftConfiguration;
   }//End Method
   
   /**
    * Getter for the right {@link BuildWallConfiguration}.
    * @return the {@link BuildWallConfiguration}.
    */
   public BuildWallConfiguration getRightConfiguration() {
      return rightConfiguration;
   }//End Method

   /**
    * Getter for the {@link StatisticsConfiguration}.
    * @return the {@link StatisticsConfiguration}.
    */
   public StatisticsConfiguration getStatisticsConfiguration() {
      return statisticsConfiguration;
   }//End Method
   
}//End Class
