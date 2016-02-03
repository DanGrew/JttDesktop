/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package main;

import java.util.concurrent.CountDownLatch;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.javafx.application.PlatformImpl;

import buildwall.layout.BuildWallDisplayImpl;
import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import styling.BuildWallStyles;
import styling.SystemStyling;

/**
 * {@link BuildWallApplication} test.
 */
public class BuildWallApplicationTest2 {
   
   private static Stage applicationStage;
   
   @BeforeClass public static void initialseApplication() throws InterruptedException {
      CountDownLatch latch = new CountDownLatch( 1 );
      BuildWallApplication.launchedStageProperty.addListener( ( source, old, updated ) -> latch.countDown() );
      
      JavaFxInitializer.startPlatform();
      PlatformImpl.runLater( () -> {
         try {
            new BuildWallApplication().start( new Stage() );
         } catch ( Exception e ) {
            e.printStackTrace();
         }
      } );
      latch.await();
      
      applicationStage = BuildWallApplication.launchedStageProperty.get();
   }//End Method

   @AfterClass public static void shutdown(){
      PlatformImpl.runLater( () -> {
         applicationStage.fireEvent(
            new WindowEvent(
                applicationStage,
                WindowEvent.WINDOW_CLOSE_REQUEST
            )
         );
      } );
   }//End Method
   
   @Test public void shouldHaveInitialisedSystemStylings() {
      //Expect no exception.
      SystemStyling.applyStyle( BuildWallStyles.ProgressBarFailed, new Group() );
   }//End Method
   
   @Test public void shouldHaveInitialisedPlatform() {
      //Expect no exception.
      DecoupledPlatformImpl.runLater( () -> {} );
   }//End Method
   
   @Test public void shouldUseBuildWallDisplay(){
      Assert.assertTrue( applicationStage.getScene().getRoot() instanceof BuildWallDisplayImpl );
   }//End Method
   
   @Test public void shouldShouldConfigureKepBoardShortcut(){
      Assert.assertTrue( applicationStage.getScene().getAccelerators().containsKey( BuildWallApplication.configurationCombination ) );
   }//End Method
   
   @Test public void configurationShortcutShouldTriggerToggle() throws InterruptedException{
      BuildWallDisplayImpl wall = ( BuildWallDisplayImpl )applicationStage.getScene().getRoot();
      Assert.assertNull( wall.getRight() );
      
      Runnable runnable = applicationStage.getScene().getAccelerators().get( BuildWallApplication.configurationCombination );
      Assert.assertNotNull( runnable );
      runOnPlatformImplAndWait( runnable );
      Assert.assertNotNull( wall.getRight() );
      
      runOnPlatformImplAndWait( runnable );
      Assert.assertNull( wall.getRight() );
   }//End Method
   
   /**
    * Method to run the given {@link Runnable} on the {@link PlatformImpl} thread and then return.
    * @param runnable the {@link Runnable} to run.
    */
   private void runOnPlatformImplAndWait( Runnable runnable ) throws InterruptedException {
      CountDownLatch latch = new CountDownLatch( 1 );
      PlatformImpl.runLater( () -> {
         runnable.run();
         latch.countDown();
      } );
      latch.await();
   }//End Method

}//End Class
