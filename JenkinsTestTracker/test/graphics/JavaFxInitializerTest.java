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
import org.junit.Ignore;
import org.junit.Test;

import com.sun.javafx.application.PlatformImpl;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * {@link JavaFxInitializer} test.
 */
public class JavaFxInitializerTest {

   /**
    * Proves {@link JavaFxInitializer} should have launched and recorded that fact.
    */
   @Ignore //JavaFx initialises once for entire test suite, so first assertion is rarely false.
   @Test public void shouldHaveLaunched() throws InterruptedException {
      Assert.assertFalse( JavaFxInitializer.hasLaunched() );
      JavaFxInitializer.threadedLaunchWithDefaultScene();
      Assert.assertTrue( JavaFxInitializer.hasLaunched() );
   }//End Method
   
   /**
    * Proves {@link JavaFxInitializer} should have launched only when a {@link Scene} has been shown.
    */
   @Ignore //JavaFx initialises once for entire test suite, so first assertion is rarely false.
   @Test public void shouldHaveLaunchedOnlyWhenSceneAttached() {
      Assert.assertFalse( JavaFxInitializer.hasLaunched() );
      JavaFxInitializer.content = new BorderPane();
      Assert.assertFalse( JavaFxInitializer.hasLaunched() );
   }//End Method
   
   /**
    * Proves that the {@link JavaFxInitializer} should not launch again, if already launched. 
    */
   @Ignore
   @Test public void shouldNotRelaunch() {
      JavaFxInitializer.threadedLaunchWithDefaultScene();
      Node firstCenter = JavaFxInitializer.content.getCenter();
      JavaFxInitializer.threadedLaunchWithDefaultScene();
      Node secondCenter = JavaFxInitializer.content.getCenter();
      Assert.assertEquals( firstCenter, secondCenter );
   }//End Method
   
   /**
    * Proves that when already launched the center of the {@link Application} can be swapped.
    * @throws InterruptedException 
    */
   @Ignore
   @Test public void shouldSwapCenter() {
      JavaFxInitializer.startPlatform();
      Node first = new Label( "anything" );
      JavaFxInitializer.threadedLaunch( () -> { return first; } );
      Assert.assertTrue( JavaFxInitializer.hasLaunched() );
      Assert.assertEquals( first, JavaFxInitializer.content.getCenter() );
      Node second = new Label( "something else" );
      JavaFxInitializer.threadedLaunch( () -> { return second; } );
      Assert.assertTrue( JavaFxInitializer.hasLaunched() );
      Assert.assertEquals( second, JavaFxInitializer.content.getCenter() );
   }//End Method
   
   /**
    * Prove that starting the platform allows runnables to be scheduled.
    */
   @Ignore
   @Test public void platformShouldAcceptRunnables(){
      JavaFxInitializer.startPlatform();
      PlatformImpl.runLater( () -> {} );
   }//End Method

}//End Class
