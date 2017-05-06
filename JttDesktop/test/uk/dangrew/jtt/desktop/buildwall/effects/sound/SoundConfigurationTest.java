/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.sound;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.desktop.buildwall.effects.sound.SoundConfiguration;
import uk.dangrew.sd.utility.synchronization.SynchronizedObservableList;
import uk.dangrew.sd.utility.synchronization.SynchronizedObservableMap;

public class SoundConfigurationTest {

   private SoundConfiguration systemUnderTest;

   @Before public void initialiseSystemUnderTest() {
      systemUnderTest = new SoundConfiguration();
   }//End Method

   @Test public void shouldProvideBuildResultStatusChangeSoundMap() {
      assertThat( systemUnderTest.statusChangeSounds(), is( notNullValue() ) );
      assertThat( systemUnderTest.statusChangeSounds(), is( instanceOf( SynchronizedObservableMap.class ) ) );
   }//End Method
   
   @Test public void shouldProvideExcludedJobs() {
      assertThat( systemUnderTest.excludedJobs(), is( notNullValue() ) );
      assertThat( systemUnderTest.excludedJobs(), is( instanceOf( SynchronizedObservableList.class ) ) );
   }//End Method

}//End Class
