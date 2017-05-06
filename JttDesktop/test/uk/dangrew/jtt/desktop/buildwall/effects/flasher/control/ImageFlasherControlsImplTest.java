/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.flasher.control;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.desktop.buildwall.effects.flasher.control.ImageFlasherControls;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.control.ImageFlasherControlsImpl;

/**
 * {@link ImageFlasherControlsImpl} test.
 */
public class ImageFlasherControlsImplTest {
   
   private ImageFlasherControls systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new ImageFlasherControlsImpl();
   }//End Method

   @Test public void shouldProvideFlashingSwitch() {
      assertThat( systemUnderTest.flashingSwitch().get(), is( false ) );
   }//End Method

}//End Class
