/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package graphics;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * {@link PlatformDecouplerImpl} test.
 */
public class PlatformDecouplerImplTest {
   
   @Rule public Timeout timeout = new Timeout( 10000, TimeUnit.MILLISECONDS );

   @Test public void shouldRunRunnableInDueCourse() throws InterruptedException {
      JavaFxInitializer.startPlatform();
      CountDownLatch latch = new CountDownLatch( 1 );
      BooleanProperty result = new SimpleBooleanProperty( false );
      PlatformDecoupler decoupler = new PlatformDecouplerImpl();
      decoupler.run( () -> {
         result.set( true );
         latch.countDown();  
      } );
      latch.await();
      Assert.assertTrue( result.get() );
   }//End Method

}//End Class
