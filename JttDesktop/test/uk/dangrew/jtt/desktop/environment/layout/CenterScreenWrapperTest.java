/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.environment.layout;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;



import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import uk.dangrew.kode.javafx.platform.JavaFxThreading;
import uk.dangrew.kode.launch.TestApplication;

/**
 * {@link CenterScreenWrapper} test.
 */
public class CenterScreenWrapperTest {

   private Node initialCenter;
   private Node center;
   private CenterScreenWrapper systemUnderTest;
   
   @Before public void initialiseSystemUnderTest(){
      TestApplication.startPlatform();
      initialCenter = new Rectangle( 10, 10 );
      center = new Rectangle( 10, 10 );
       JavaFxThreading.runAndWait( () -> {
         systemUnderTest = new CenterScreenWrapper( initialCenter );
         new Scene( systemUnderTest );
      } );
   }//End Method
   
   @Ignore
   @Test public void manual() throws InterruptedException {
      TestApplication.launch( () -> new CenterScreenWrapper( new Rectangle( 100, 100 ) ) );
      
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void shouldContainInitialCenterNode(){
      assertThat( systemUnderTest.getChildren(), contains( initialCenter ) );
      assertThat( systemUnderTest.getCenter(), is( initialCenter ) );
   }//End Method
   
   @Test public void shouldSwapCenterRemovingOthers(){
      systemUnderTest.setCenter( center );
      assertThat( systemUnderTest.getChildren(), contains( center ) );
      assertThat( systemUnderTest.getChildren().contains( initialCenter ), is( false ) );
      assertThat( systemUnderTest.getCenter(), is( center ) );
   }//End Method
   
   @Test public void shouldProvideCenterEvenWhenNull(){
      systemUnderTest = new CenterScreenWrapper();
      assertThat( systemUnderTest.getCenter(), is( nullValue() ) );
   }//End Method

}//End Class
