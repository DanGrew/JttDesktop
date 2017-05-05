/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.effects.flasher.configuration;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import uk.dangrew.jtt.buildwall.effects.flasher.configuration.ImageFlasherConfiguration;
import uk.dangrew.jtt.buildwall.effects.flasher.configuration.ImageFlasherConfigurationImpl;

/**
 * {@link ImageFlasherConfiguration} test.
 */
public class ImageFlasherConfigurationTest {

   private ImageFlasherConfiguration systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      systemUnderTest = new ImageFlasherConfigurationImpl();
   }//End Method
   
   @Test public void shouldProvideFlashOnPeriod() {
      assertThat( systemUnderTest.flashOnProperty().get(), is( ImageFlasherConfigurationImpl.DEFAULT_FLASH_ON ) );
   }//End Method
   
   @Test public void shouldProvideFlashOffPeriod() {
      assertThat( systemUnderTest.flashOffProperty().get(), is( ImageFlasherConfigurationImpl.DEFAULT_FLASH_OFF ) );
   }//End Method
   
   @Test public void shouldProvideNumberOfFlashes() {
      assertThat( systemUnderTest.numberOfFlashesProperty().get(), is( ImageFlasherConfigurationImpl.DEFAULT_NUMBER_OF_FLASHES ) );
   }//End Method
   
   @Test public void shouldProvideTransparency() {
      assertThat( systemUnderTest.transparencyProperty().get(), is( ImageFlasherConfigurationImpl.DEFAULT_TRANSPARENCY ) );
   }//End Method
   
   @Test public void shouldProvideImageProperty(){
      assertThat( systemUnderTest.imageProperty().get(), is( nullValue() ) );  
   }//End Method
   
}//End Class
