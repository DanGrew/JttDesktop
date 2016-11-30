/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.sound;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

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

}//End Class
