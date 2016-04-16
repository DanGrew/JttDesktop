/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.effects.flasher;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.PlatformDecouplerImpl;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utility.TestCommon;

/**
 * {@link ImageFlasherImpl} test.
 */
public class ImageFlasherImplTest {
   
   private static final Image ALERT_IMAGE = new Image( ImageFlasherImplTest.class.getResourceAsStream( "alert-image.png" ) );
   private static final Image ALTERNATE_ALERT_IMAGE = new Image( ImageFlasherImplTest.class.getResourceAsStream( "alert-image.png" ) );
   private ImageFlasherConfiguration configuration;
   private ImageFlasherRunnable runnable;
   private ImageFlasherImpl systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      configuration = new ImageFlasherConfigurationImpl();
      configuration.imageProperty().set( ALERT_IMAGE );
      
      systemUnderTest = new ImageFlasherImpl( configuration );
   }//End Method
   
   @Ignore
   @Test public void manualInspection() throws InterruptedException{
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      
      JavaFxInitializer.launchInWindow( () -> {
         
         ImageFlasherImpl imageFlasher = new ImageFlasherImpl( configuration );
         runnable = imageFlasher.flasher();
         return imageFlasher;
      } );
      
      runnable.run();
      
      Thread.sleep( 2000000 );
   }//End Method

   @Test public void shouldProvideImageViewWithImage() {
      systemUnderTest.flashOn();
      
      assertThat( systemUnderTest.getCenter(), instanceOf( ImageView.class ) );
      
      ImageView imageView = ( ImageView )systemUnderTest.getCenter();
      assertThat( imageView.getImage(), is( ALERT_IMAGE ) );
      assertThat( imageView, is( systemUnderTest.imageView() ) );
   }//End Method
   
   @Test public void shouldFlashOnAndOff(){
      systemUnderTest.flashOn();
      
      ImageView imageView = ( ImageView )systemUnderTest.getCenter();
      assertThat( imageView, is( notNullValue() ) );
      
      systemUnderTest.flashOff();
      assertThat( systemUnderTest.getCenter(), is( nullValue() ) );
      
      systemUnderTest.flashOn();
      assertThat( systemUnderTest.getCenter(), is( imageView ) );
   }//End Method
   
   @Test public void shouldUseConfiguredTransparency(){
      ImageView imageView = systemUnderTest.imageView();
      assertThat( imageView.getOpacity(), closeTo( configuration.transparencyProperty().get(), TestCommon.precision() ) );
      
      configuration.transparencyProperty().set( 0.1 );
      assertThat( imageView.getOpacity(), closeTo( configuration.transparencyProperty().get(), TestCommon.precision() ) );
   }//End Method
   
   @Test public void shouldDetachTransparencyFromSystem() {
      ImageView imageView = systemUnderTest.imageView();
      assertThat( imageView.getOpacity(), closeTo( configuration.transparencyProperty().get(), TestCommon.precision() ) );
      
      systemUnderTest.detachFromSystem();
      
      final double original = configuration.transparencyProperty().get();
      configuration.transparencyProperty().set( 0.1 );
      assertThat( imageView.getOpacity(), closeTo( original, TestCommon.precision() ) );
   }//End Method
   
   @Test public void shouldUseConfiguredImage(){
      ImageView imageView = systemUnderTest.imageView();
      assertThat( imageView.getImage(), is( configuration.imageProperty().get() ) );
      
      configuration.imageProperty().set( null );
      assertThat( imageView.getImage(), is( nullValue() ) );
      
      configuration.imageProperty().set( ALTERNATE_ALERT_IMAGE );
      assertThat( imageView.getImage(), is( ALTERNATE_ALERT_IMAGE ) );
   }//End Method
   
   @Test public void shouldDetachImageFromSystem() {
      ImageView imageView = systemUnderTest.imageView();
      assertThat( imageView.getImage(), is( configuration.imageProperty().get() ) );
      
      systemUnderTest.detachFromSystem();
      
      final Image original = configuration.imageProperty().get();
      configuration.imageProperty().set( null );
      assertThat( imageView.getImage(), is( original ) );
      
      configuration.imageProperty().set( ALTERNATE_ALERT_IMAGE );
      assertThat( imageView.getImage(), is( original ) );
   }//End Method
   
}//End Class
