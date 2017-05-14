/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.buildwall.effects.flasher;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.desktop.buildwall.effects.flasher.configuration.ImageFlasherConfiguration;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.configuration.ImageFlasherConfigurationImpl;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.control.ImageFlasherControls;
import uk.dangrew.jtt.desktop.buildwall.effects.flasher.control.ImageFlasherControlsImpl;

/**
 * {@link ImageFlasherPropertiesImpl} test.
 */
public class ImageFlasherPropertiesImplTest {
   
   private ImageFlasherConfiguration configuration;
   private ImageFlasherControls controls;
   private ImageFlasherProperties systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      configuration = new ImageFlasherConfigurationImpl();
      controls = new ImageFlasherControlsImpl();
      systemUnderTest = new ImageFlasherPropertiesImpl( configuration, controls );
   }//End Method

   @Test public void shouldRedirectNumberOfFlashes() {
      assertThat( systemUnderTest.numberOfFlashesProperty(), is( configuration.numberOfFlashesProperty() ) );
   }//End Method
   
   @Test public void shouldRedirectFlashOn() {
      assertThat( systemUnderTest.flashOnProperty(), is( configuration.flashOnProperty() ) );
   }//End Method
   
   @Test public void shouldRedirectFlashOff() {
      assertThat( systemUnderTest.flashOffProperty(), is( configuration.flashOffProperty() ) );  
   }//End Method
   
   @Test public void shouldRedirectTransparency() {
      assertThat( systemUnderTest.transparencyProperty(), is( configuration.transparencyProperty() ) );
   }//End Method
   
   @Test public void shouldRedirectImage() {
      assertThat( systemUnderTest.imageProperty(), is( configuration.imageProperty() ) );
   }//End Method
   
   @Test public void shouldRedirectFlashingSwitch() {
      assertThat( systemUnderTest.flashingSwitch(), is( controls.flashingSwitch() ) );
   }//End Method

}//End Class
