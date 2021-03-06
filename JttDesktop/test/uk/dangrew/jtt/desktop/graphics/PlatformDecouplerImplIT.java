/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.graphics;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import uk.dangrew.kode.launch.TestApplication;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * {@link PlatformDecouplerImpl} test.
 */
public class PlatformDecouplerImplIT {
   
   @Rule public Timeout timeout = new Timeout( 50000, TimeUnit.MILLISECONDS );

   @Test public void shouldRunRunnableInDueCourse() throws InterruptedException {
      TestApplication.startPlatform();
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
