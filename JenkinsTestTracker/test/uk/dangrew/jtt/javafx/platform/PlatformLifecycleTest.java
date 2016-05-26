/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.javafx.platform;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import uk.dangrew.jtt.javafx.platform.PlatformLifecycle;
import uk.dangrew.jtt.javafx.platform.PlatformLifecycleImpl;

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
