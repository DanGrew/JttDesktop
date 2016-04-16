/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.effects.flasher;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import buildwall.effects.flasher.configuration.ImageFlasherConfiguration;
import buildwall.effects.flasher.configuration.ImageFlasherConfigurationImpl;
import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.TestPlatformDecouplerImpl;

/**
 * {@link ImageFlasherRunnable} test.
 */
public class ImageFlasherRunnableTest {
   
   private ImageFlasherConfiguration configuration;
   @Mock private ImageFlasher imageFlasher;
   private ImageFlasherRunnable systemUnderTest;
   
   private int countedInstructions = 0;
   private long lastRecordedSystemTime = 0;
   private CountDownLatch latch;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      configuration = new ImageFlasherConfigurationImpl();
      systemUnderTest = new ImageFlasherRunnable( imageFlasher, configuration );
   }//End Method
   
   @Test public void shouldFlashConfiguredNumberOfTimes() throws InterruptedException {
      final int customNumber = 100;
      CountDownLatch latch = new CountDownLatch( customNumber * 2 );
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl( () -> latch.countDown() ) );
      
      assertThat( configuration.numberOfFlashesProperty().get(), not( customNumber ) );
      configuration.numberOfFlashesProperty().set( customNumber );
      
      configuration.flashOnProperty().set( 1 );
      configuration.flashOffProperty().set( 1 );
      
      systemUnderTest.run();
      
      latch.await();
      
      verify( imageFlasher, times( customNumber ) ).flashOn();
      verify( imageFlasher, times( customNumber ) ).flashOff();
   }//End Method
   
   @Test public void eachFlashShouldBeOnAndOffForConfiguredPeriod() throws InterruptedException {
      final int customNumber = 10;
      latch = new CountDownLatch( customNumber * 2 );
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl( this::assertTimeBetweenInstructions ) );
      
      configuration.numberOfFlashesProperty().set( customNumber );
      configuration.flashOnProperty().set( 50 );
      configuration.flashOffProperty().set( 20 );
      
      systemUnderTest.run();
      
      latch.await();
      
      assertThat( countedInstructions, is( customNumber * 2 ) );
   }//End Method

   /**
    * Method to assert that time between instruction is correct.
    */
   private void assertTimeBetweenInstructions() {
      countedInstructions++;
      
      long currentSystemTime = System.currentTimeMillis();
      long timeTaken = currentSystemTime - lastRecordedSystemTime;
      if ( countedInstructions % 2 == 0 ) {
         assertThat( timeTaken, greaterThanOrEqualTo( ( long )configuration.flashOnProperty().get() ) );
         System.out.println( "proving on " + currentSystemTime );
      } else {
         assertThat( timeTaken, greaterThanOrEqualTo( ( long )configuration.flashOffProperty().get() ) );
         System.out.println( "proving of " + currentSystemTime );
      }
      
      lastRecordedSystemTime = currentSystemTime;
      
      latch.countDown();
   }//End Method
   
}//End Class
