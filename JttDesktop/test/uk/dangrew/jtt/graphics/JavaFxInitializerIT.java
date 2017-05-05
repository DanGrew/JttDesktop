/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.graphics;
import org.junit.Assert;
import org.junit.Test;

import com.sun.javafx.application.PlatformImpl;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * {@link JavaFxInitializer} test.
 */
public class JavaFxInitializerIT {
   
   @Test public void shouldLaunchAndShutdown(){
      JavaFxInitializer.launchInWindow( () -> { return new BorderPane(); } );
      
      Assert.assertNotNull( JavaFxInitializer.stage );
      Assert.assertNotNull( JavaFxInitializer.content );
   }//End Method
   
   @Test public void shouldSpamLaunch() {
      for ( int i = 0; i < 20; i++ ) {
         JavaFxInitializer.launchInWindow( () -> { return new BorderPane(); } );
      }
   }//End Method
   
   @Test public void shouldShutdownIfAlreadyLaunched(){
      JavaFxInitializer.launchInWindow( () -> { return new BorderPane(); } );
      Stage firstStage = JavaFxInitializer.stage;
      
      BooleanProperty shutdown = new SimpleBooleanProperty( false );
      firstStage.addEventHandler( WindowEvent.ANY, ( event ) -> shutdown.set( true ) );
      
      JavaFxInitializer.launchInWindow( () -> { return new BorderPane(); } );
      Assert.assertNotNull( JavaFxInitializer.stage );
      Assert.assertNotNull( JavaFxInitializer.content );
      Assert.assertTrue( shutdown.get() );
   }//End Method
   
   /**
    * Prove that starting the platform allows runnables to be scheduled.
    */
   @Test public void platformShouldAcceptRunnables(){
      JavaFxInitializer.startPlatform();
      PlatformImpl.runLater( () -> {} );
   }//End Method
   
}//End Class
