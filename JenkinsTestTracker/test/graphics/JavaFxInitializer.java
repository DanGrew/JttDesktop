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
import java.util.function.Supplier;

import org.junit.Assert;

import com.sun.javafx.application.PlatformImpl;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * {@link JavaFxInitializer} is responsible for launching JavaFx using the correct
 * practices, using {@link Application}, as opposed to JFXPanel.
 */
public class JavaFxInitializer extends Application {
   
   /** {@link BorderPane} at center of default {@link Scene}.**/
   static BorderPane content;

   /**
    * {@inheritDoc}
    */
   @Override public void start(Stage stage) throws Exception {
      Scene scene = new Scene( content );
      stage.setScene( scene );
      stage.show();
   }//End Method
      
   /**
    * Private method to launch the {@link Application}, checking that it has not already
    * been launched.
    */
   private static void launch( Supplier< Node > runnable ){
      if ( !hasLaunched() ) {
         CountDownLatch launchLatch = new CountDownLatch( 1 );
         content = new BorderPane();
         /* Run on separate thread because this will not return while the scene is open.*/
         PlatformImpl.runLater( () -> {
            try {
               new JavaFxInitializer().start( new Stage() );
            } catch ( Exception exception ) {
               exception.printStackTrace();
            }
         } );
         
         //Get feedback as soon as the center is set, continue current thread.
         content.sceneProperty().addListener( ( source, old, updated ) -> {
            launchLatch.countDown();
         } );
         
         try {
            launchLatch.await();
         } catch ( InterruptedException e ) {
            Assert.fail( "CountDownLatch interrupted when launching." );
         }
         Assert.assertTrue( hasLaunched() );
      }
      
      CountDownLatch contentLatch = new CountDownLatch( 1 );
      //Must synchronise call through to launch with the run later so that the center is set.
      PlatformImpl.runLater( () -> {
         content.setCenter( runnable.get() );
         contentLatch.countDown();
      } );
      
      try {
         contentLatch.await();
      } catch ( InterruptedException e ) {
         Assert.fail( "CountDownLatch interrupted when applying content." );
      }
   }//End Method
   
   /**
    * Method to determine whether the {@link JavaFxInitializer} has initialised.
    * @return true if already launched, false otherwise.
    */
   public static boolean hasLaunched(){
      if ( content == null ) {
         return false;
      }
      return content.getScene() != null;
   }//End Method
   
   /**
    * Method to initialise JavaFx with the given {@link Supplier} of a {@link Node} to display.
    * This is mainly used for manual tests where a test item can be supplied. If nothing is supplied
    * we get a quick boot and a shutdown of JavaFx.
    * @param runnable the {@link Supplier} for the {@link Node} to display.
    */
   public static void threadedLaunch( Supplier< Node > runnable ){
      startPlatform();
      launch( runnable );
   }//End Method
   
   /**
    * Method to initialise JavaFx using a default {@link Scene} containing a message. The expectation is that
    * this {@link Scene} will be present for the entirety of the testing. This is expected to be used for 
    * running with popups or dialog type items.
    */
   public static void threadedLaunchWithDefaultScene(){
      if ( !hasLaunched() ) {
         threadedLaunch( () -> { return new Label( "Testing, should stay open!" ); } );
      }
   }//End Method
   
   /**
    * Method to initialise JavaFx using a default {@link Scene} containing a message. The expectation is that
    * this {@link Scene} will be present for the entirety of the testing. This is expected to be used for 
    * running with popups or dialog type items.
    */
   public static void startPlatform(){
      //Should be safe to call if already launched.
      PlatformImpl.startup( () -> {} );
   }//End Method
}//End Class
