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
import static org.hamcrest.Matchers.lessThan;
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

import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.TestPlatformDecouplerImpl;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * {@link ImageFlasherRunnable} test.
 */
public class ImageFlasherRunnableTest {
   
   private ImageFlasherProperties properties;
   @Mock private ImageFlasher imageFlasher;
   private ImageFlasherRunnable systemUnderTest;
   
   private IntegerProperty counter;
   private int stoppingPoint;
   private int countedInstructions = 0;
   private long lastRecordedSystemTime = 0;
   private CountDownLatch latch;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      MockitoAnnotations.initMocks( this );
      properties = new ImageFlasherPropertiesImpl();
      systemUnderTest = new ImageFlasherRunnable( imageFlasher, properties );
   }//End Method
   
   @Test public void shouldFlashConfiguredNumberOfTimes() throws InterruptedException {
      final int customNumber = 100;
      CountDownLatch latch = new CountDownLatch( customNumber * 2 );
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl( () -> latch.countDown() ) );
      
      assertThat( properties.numberOfFlashesProperty().get(), not( customNumber ) );
      properties.numberOfFlashesProperty().set( customNumber );
      
      properties.flashOnProperty().set( 1 );
      properties.flashOffProperty().set( 1 );
      
      properties.flashingSwitch().set( true );
      
      latch.await();
      
      verify( imageFlasher, times( customNumber ) ).flashOn();
      verify( imageFlasher, times( customNumber ) ).flashOff();
      
      assertThat( properties.flashingSwitch().get(), is( false ) );
   }//End Method
   
   @Test public void eachFlashShouldBeOnAndOffForConfiguredPeriod() throws InterruptedException {
      final int numberOfFlashes = 10;
      latch = new CountDownLatch( numberOfFlashes * 2 );
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl( this::assertTimeBetweenInstructions ) );
      
      properties.numberOfFlashesProperty().set( numberOfFlashes );
      properties.flashOnProperty().set( 50 );
      properties.flashOffProperty().set( 20 );
      
      properties.flashingSwitch().set( true );
      
      latch.await();
      
      assertThat( countedInstructions, is( numberOfFlashes * 2 ) );
   }//End Method

   /**
    * Method to assert that time between instruction is correct.
    */
   private void assertTimeBetweenInstructions() {
      long currentSystemTime = System.currentTimeMillis();
      long timeTaken = currentSystemTime - lastRecordedSystemTime;
      if ( countedInstructions % 2 == 0 ) {
         System.out.println( "proving time leading up to flash on " + currentSystemTime );
         assertThat( timeTaken, greaterThanOrEqualTo( ( long )properties.flashOffProperty().get() ) );
      } else {
         System.out.println( "proving time leading up to flash off " + currentSystemTime );
         assertThat( timeTaken, greaterThanOrEqualTo( ( long )properties.flashOnProperty().get() ) );
      }
      
      countedInstructions++;
      lastRecordedSystemTime = currentSystemTime;
      
      latch.countDown();
   }//End Method
   
   @Test public void shouldBreakWhenControlIsSwitchOff() throws InterruptedException{
      final int numberOfFlashes = 1000;
      latch = new CountDownLatch( 1 );
      stoppingPoint = 344;
      counter = new SimpleIntegerProperty( 0 );
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl( this::interruptAtStoppingPoint ) );
      
      properties.numberOfFlashesProperty().set( numberOfFlashes );
      properties.flashOnProperty().set( 1 );
      properties.flashOffProperty().set( 1 );
      
      properties.flashingSwitch().set( true );
      latch.await();
      
      Thread.sleep( numberOfFlashes );
      assertThat( counter.get(), is( stoppingPoint ) );
   }//End Method
   
   @Test public void shouldDisposeOfFirstThreadBeforeStartingANew(){
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl( () -> countedInstructions++ ) );
      
      final int numberOfFlashes = 1000;
      properties.numberOfFlashesProperty().set( numberOfFlashes );
      properties.flashOnProperty().set( 1 );
      properties.flashOffProperty().set( 1 );
      
      properties.flashingSwitch().set( true );
      
      for ( int i = 0; i < 1000; i++ ) {
         properties.flashingSwitch().set( false );
         properties.flashingSwitch().set( true );
      }
      
      assertThat( countedInstructions, lessThan( 2 * numberOfFlashes ) );
   }//End Method
   
   @Test public void shouldAlwaysFlashOffIfInterruptedAtFlashOn() throws InterruptedException{
      final int numberOfFlashes = 10;
      latch = new CountDownLatch( 1 );
      stoppingPoint = 5;
      counter = new SimpleIntegerProperty( 0 );
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl( this::interruptAtStoppingPoint ) );
      
      properties.numberOfFlashesProperty().set( numberOfFlashes );
      properties.flashOnProperty().set( 1 );
      properties.flashOffProperty().set( 1 );
      
      properties.flashingSwitch().set( true );
      latch.await();
      
      Thread.sleep( numberOfFlashes );
      
      assertThat( counter.get(), is( stoppingPoint + 1 ) );
      verify( imageFlasher, times( 3 ) ).flashOn();
      verify( imageFlasher, times( 3 ) ).flashOff();
   }//End Method
   
   /**
    * Method to interrupt the thread at the stopping point.
    */
   private void interruptAtStoppingPoint(){
      counter.set( counter.get() + 1 );
      if ( counter.get() == stoppingPoint ) {
         properties.flashingSwitch().set( false );
         latch.countDown();
      }
   }//End Method
}//End Class
