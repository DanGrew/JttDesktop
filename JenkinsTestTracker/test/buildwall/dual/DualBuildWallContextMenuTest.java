/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.dual;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.sun.javafx.application.PlatformImpl;

import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.PlatformDecouplerImpl;
import graphics.TestPlatformDecouplerImpl;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import storage.database.JenkinsDatabaseImpl;
import viewer.basic.DigestViewer;

/**
 * {@link DualBuildWallContextMenu} test.
 */
public class DualBuildWallContextMenuTest {
   
   //Note that these are not object requirements, but useful for the test.
   private static final int CONTROL_LEFT = 0;
   private static final int CONTROL_RIGHT = 1;
   private static final int FIRST_SEPARATOR = 2;
   private static final int CONFIGURE_LEFT = 3;
   private static final int CONFIGURE_RIGHT = 4;
   private static final int HIDE_CONFIG = 5;
   private static final int SECOND_SEPARATOR = 6;
   private static final int CANCEL = 7;
   
   private DualBuildWallDisplayImpl display;
   private DualBuildWallContextMenu systemUnderTest;
   private DualBuildWallContextMenuOpener opener;
   
   @BeforeClass public static void initialisePlatform(){
      PlatformImpl.startup( () -> {} );
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      display = mock( DualBuildWallDisplayImpl.class );
      systemUnderTest = new DualBuildWallContextMenu( display );
   }//End Method

   @Ignore
   @Test public void manualInspection() throws InterruptedException {
      fullLaunch();
      Thread.sleep( 10000000 );
   }//End Method
   
   /**
    * Method to fully launch the {@link DualBuildWallDisplayImpl} and to use the 
    * {@link DualBuildWallContextMenuOpener}.
    */
   private void fullLaunch(){
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
      JavaFxInitializer.launchInWindow( () -> {
         display = new DualBuildWallDisplayImpl( new JenkinsDatabaseImpl() );
         opener = new DualBuildWallContextMenuOpener( display, systemUnderTest );
         display.setOnContextMenuRequested( opener );
         return display; 
      } );
   }//End Method
   
   @Test public void shouldContainMenusInOrder(){
      assertThat( retrieveMenuItem( CONTROL_LEFT ).getText(), is( DualBuildWallContextMenu.HIDE_LEFT ) );
      assertThat( retrieveMenuItem( CONTROL_RIGHT ).getText(), is( DualBuildWallContextMenu.HIDE_RIGHT ) );
      assertThat( retrieveMenuItem( FIRST_SEPARATOR ), instanceOf( SeparatorMenuItem.class ) );
      assertThat( retrieveMenuItem( CONFIGURE_LEFT ).getText(), is( DualBuildWallContextMenu.CONFIGURE_LEFT ) );
      assertThat( retrieveMenuItem( CONFIGURE_RIGHT ).getText(), is( DualBuildWallContextMenu.CONFIGURE_RIGHT ) );
      assertThat( retrieveMenuItem( HIDE_CONFIG ).getText(), is( DualBuildWallContextMenu.HIDE_CONFIGURATION ) );
      assertThat( retrieveMenuItem( SECOND_SEPARATOR ), instanceOf( SeparatorMenuItem.class ) );
      assertThat( retrieveMenuItem( CANCEL ).getText(), is( DualBuildWallContextMenu.CANCEL ) );
      assertThat( systemUnderTest.getItems(), hasSize( 8 ) );
   }//End Method
   
   /**
    * Convenience method to retrieve the appropriate {@link MenuItem} from the {@link DualBuildWallContextMenu}.
    * @param item the item required.
    * @return the {@link MenuItem}.
    */
   private MenuItem retrieveMenuItem( int item ) {
      return systemUnderTest.getItems().get( item );
   }//End Method
   
   @Test public void shouldControlRightWall() {
      MenuItem controRight = retrieveMenuItem( CONTROL_RIGHT );
      
      controRight.getOnAction().handle( new ActionEvent() );
      verify( display ).hideRightWall();
      assertThat( controRight.getText(), is( DualBuildWallContextMenu.SHOW_RIGHT ) );
      
      controRight.getOnAction().handle( new ActionEvent() );
      verify( display ).showRightWall();
      assertThat( controRight.getText(), is( DualBuildWallContextMenu.HIDE_RIGHT ) );
   }//End Method
   
   @Test public void shouldControlLeftWall() {
      MenuItem controLeft = retrieveMenuItem( CONTROL_LEFT );
      
      controLeft.getOnAction().handle( new ActionEvent() );
      verify( display ).hideLeftWall();
      assertThat( controLeft.getText(), is( DualBuildWallContextMenu.SHOW_LEFT ) );
      
      controLeft.getOnAction().handle( new ActionEvent() );
      verify( display ).showLeftWall();
      assertThat( controLeft.getText(), is( DualBuildWallContextMenu.HIDE_LEFT ) );
   }//End Method
   
   @Test public void shouldShowConfiguration() {
      MenuItem leftConfig = retrieveMenuItem( CONFIGURE_LEFT );
      MenuItem rightConfig = retrieveMenuItem( CONFIGURE_RIGHT );
      
      leftConfig.getOnAction().handle( new ActionEvent() );
      verify( display ).showLeftConfiguration();
      assertThat( leftConfig.getText(), is( DualBuildWallContextMenu.CONFIGURE_LEFT ) );
      
      rightConfig.getOnAction().handle( new ActionEvent() );
      verify( display ).showRightConfiguration();
      assertThat( rightConfig.getText(), is( DualBuildWallContextMenu.CONFIGURE_RIGHT ) );
   }//End Method
   
   @Test public void shouldHideConfiguration() {
      MenuItem hideConfig = retrieveMenuItem( HIDE_CONFIG );
      
      hideConfig.getOnAction().handle( new ActionEvent() );
      verify( display ).hideConfiguration();
      assertThat( hideConfig.getText(), is( DualBuildWallContextMenu.HIDE_CONFIGURATION ) );
   }//End Method
   
   @Test public void shouldAutohide() {
      assertThat( systemUnderTest.isAutoHide(), is( true ) );
   }//End Method
   
   @Test public void cancelShouldHideWhenUsingHeavySetup(){
      fullLaunch();
      PlatformImpl.runAndWait( () -> {
         opener.handle( new ContextMenuEvent( null, 0, 0, 0, 0, false, null ) );
      } );
      assertThat( systemUnderTest.friendly_isShowing(), is( true ) );
      PlatformImpl.runAndWait( () -> {
         retrieveMenuItem( CANCEL ).getOnAction().handle( new ActionEvent() );
      } );
      assertThat( systemUnderTest.friendly_isShowing(), is( false ) );
   }//End Method
   
   @Test public void shouldProvideHideDigestOptionIfFoundInParent(){
      assertThat( systemUnderTest.digestControl(), nullValue() );
   }//End Method
   
   @Test public void shouldNotProvideHideDigestOptionIfNotFoundInParent(){
      assertThat( systemUnderTest.digestControl(), nullValue() );
   }//End Method
   
   /**
    * Method to apply the preconditions for the digest control being testable.
    */
   private void digestControlPreconditions(){
      display = new DualBuildWallDisplayImpl( new JenkinsDatabaseImpl() );
      
      BorderPane parent = new BorderPane( display );
      new Scene( parent );
      parent.setTop( new DigestViewer() );
      
      systemUnderTest = new DualBuildWallContextMenu( display );
   }//End Method
   
   @Test public void shouldShowAndHideDigest(){
      digestControlPreconditions();
      assertThat( display.getParent(), notNullValue() );
      BorderPane parent = ( BorderPane ) display.getParent();
      assertThat( parent.getTop(), notNullValue() );
      
      systemUnderTest.digestControl().getOnAction().handle( new ActionEvent() );
      assertThat( parent.getTop(), nullValue() );
      
      systemUnderTest.digestControl().getOnAction().handle( new ActionEvent() );
      assertThat( parent.getTop(), notNullValue() );
   }//End Method

}//End Class
