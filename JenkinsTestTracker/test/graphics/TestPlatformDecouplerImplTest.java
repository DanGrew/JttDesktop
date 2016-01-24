/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package graphics;

import org.junit.Assert;
import org.junit.Test;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * {@link TestPlatformDecouplerImpl} test.
 */
public class TestPlatformDecouplerImplTest {

   @Test public void shouldRunRunnableInDueCourse() throws InterruptedException {
      BooleanProperty result = new SimpleBooleanProperty( false );
      PlatformDecoupler decoupler = new TestPlatformDecouplerImpl();
      decoupler.run( () -> {
         result.set( true );
      } );
      Assert.assertTrue( result.get() );
   }//End Method

}//End Class
