/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package graphics;

import org.junit.Test;
import org.mockito.Mockito;

/**
 * {@link DecoupledPlatformImpl} test.
 */
public class DecoupledPlatformImplTest {

   @Test public void shouldInvokeSetInstance() {
      PlatformDecoupler decoupler = Mockito.mock( PlatformDecoupler.class );
      DecoupledPlatformImpl.setInstance( decoupler );
      Runnable runnable = () -> {};
      DecoupledPlatformImpl.runLater( runnable );
      Mockito.verify( decoupler ).run( runnable );
      Mockito.verifyNoMoreInteractions( decoupler );
   }//End Method
   
   @Test public void shouldChangeDecouplers(){
      shouldInvokeSetInstance();
      shouldInvokeSetInstance();
   }//End Method
   
   @Test( expected = NullPointerException.class ) public void shouldNullPointerWithoutSet() {
      DecoupledPlatformImpl.runLater( () -> {} );
   }//End Method

}//End Class
