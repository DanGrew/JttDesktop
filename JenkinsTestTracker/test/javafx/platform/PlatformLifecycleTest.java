/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package javafx.platform;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

/**
 * {@link PlatformLifecycle} test. Superficial test to prove interactions.
 */
public class PlatformLifecycleTest {

   @Test public void shouldUseInstanceForShutdown() {
      PlatformLifecycleImpl lifecycle = mock( PlatformLifecycleImpl.class );
      PlatformLifecycle.setInstance( lifecycle );
      
      PlatformLifecycle.shutdown();
      verify( lifecycle ).shutdownPlatform();
   }//End Method
   
}//End Class
