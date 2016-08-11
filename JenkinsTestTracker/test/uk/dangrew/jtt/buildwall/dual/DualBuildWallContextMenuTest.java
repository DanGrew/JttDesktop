/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.dual;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.sun.javafx.application.PlatformImpl;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.buildwall.configuration.persistence.buildwall.BuildWallConfigurationSessions;
import uk.dangrew.jtt.buildwall.configuration.persistence.dualwall.DualWallConfigurationSessions;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.preferences.PreferenceBehaviour;
import uk.dangrew.jtt.environment.preferences.PreferencesOpenEvent;
import uk.dangrew.jtt.environment.preferences.WindowPolicy;
import uk.dangrew.jtt.event.structure.EventAssertions;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.jtt.styling.SystemStyling;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * {@link DualBuildWallContextMenu} test.
 */
public class DualBuildWallContextMenuTest {
   
   //Note that these are not object requirements, but useful for the test.
   private static final int CONTROL_LEFT = 0;
   private static final int CONTROL_RIGHT = 1;
   private static final int FIRST_SEPARATOR = 2;
   private static final int CONFIGURE_IMAGE_FLASHER = 3;
   private static final int HIDE_CONFIG = 4;
   private static final int SECOND_SEPARATOR = 5;
   private static final int CONFIG_WINDOW = 6;
   private static final int THIRD_SEPARATOR = 7;
   private static final int CANCEL = 8;
   
   @Mock private DualBuildWallDisplayImpl display;
   @Mock private BorderPane buildWallPane;
   private DualBuildWallContextMenu systemUnderTest;
   private DualBuildWallContextMenuOpener opener;
   
   @BeforeClass public static void initialisePlatform(){
      PlatformImpl.startup( () -> {} );
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      SystemStyling.initialise();
   }//End Method
   
   @Before public void initialiseSystemUnderTest(){
      MockitoAnnotations.initMocks( this );
      when( display.buildWallPane() ).thenReturn( buildWallPane );
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
         display = new DualBuildWallDisplayImpl( new JenkinsDatabaseImpl(), new SystemConfiguration() );
         opener = new DualBuildWallContextMenuOpener( display, systemUnderTest );
         display.setOnContextMenuRequested( opener );
         return display; 
      } );
   }//End Method
   
   @Test public void shouldContainMenusInOrder(){
      assertThat( retrieveMenuItem( CONTROL_LEFT ).getText(), is( DualBuildWallContextMenu.HIDE_LEFT ) );
      assertThat( retrieveMenuItem( CONTROL_RIGHT ).getText(), is( DualBuildWallContextMenu.HIDE_RIGHT ) );
      assertThat( retrieveMenuItem( FIRST_SEPARATOR ), instanceOf( SeparatorMenuItem.class ) );
      assertThat( retrieveMenuItem( CONFIGURE_IMAGE_FLASHER ).getText(), is( DualBuildWallContextMenu.CONFIGURE_IMAGE_FLASHER ) );
      assertThat( retrieveMenuItem( HIDE_CONFIG ).getText(), is( DualBuildWallContextMenu.HIDE_CONFIGURATION ) );
      assertThat( retrieveMenuItem( SECOND_SEPARATOR ), instanceOf( SeparatorMenuItem.class ) );
      assertThat( retrieveMenuItem( CONFIG_WINDOW ).getText(), is( DualBuildWallContextMenu.PREFERENCES ) );
      assertThat( retrieveMenuItem( THIRD_SEPARATOR ), instanceOf( SeparatorMenuItem.class ) );
      assertThat( retrieveMenuItem( CANCEL ).getText(), is( DualBuildWallContextMenu.CANCEL ) );
      assertThat( systemUnderTest.getItems(), hasSize( 9 ) );
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
   
   @Test public void shouldControlConfigWindow() {
      MenuItem controWindow = retrieveMenuItem( CONFIG_WINDOW );
      
      EventAssertions.assertEventRaised( 
               () -> new PreferencesOpenEvent(), 
               () -> controWindow.getOnAction().handle( new ActionEvent() ), 
               null, 
               new PreferenceBehaviour( WindowPolicy.Open, null )
      );
      
      EventAssertions.assertEventRaised( 
               () -> new PreferencesOpenEvent(), 
               () -> controWindow.getOnAction().handle( new ActionEvent() ), 
               null, 
               new PreferenceBehaviour( WindowPolicy.Open, null )
      );
   }//End Method
   
   @Test public void shouldShowImageFlasherConfiguration() {
      MenuItem config = retrieveMenuItem( CONFIGURE_IMAGE_FLASHER );
      
      config.getOnAction().handle( new ActionEvent() );
      verify( display ).showImageFlasherConfiguration();
      assertThat( config.getText(), is( DualBuildWallContextMenu.CONFIGURE_IMAGE_FLASHER ) );
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
      SystemConfiguration systemConfiguration = new SystemConfiguration();
      
      display = new DualBuildWallDisplayImpl( 
               new JenkinsDatabaseImpl(),
               systemConfiguration
      );
      
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
   
   @Test public void shouldShowDigestAsControllable(){
      digestControlPreconditions();
      assertThat( systemUnderTest.isSystemDigestControllable(), is( true ) );
   }//End Method
   
   @Test public void shouldNotShowDigestAsControllable(){
      assertThat( systemUnderTest.isSystemDigestControllable(), is( false ) );
   }//End Method
   
   @Test public void shouldUpdateTextBasedOnRightWallState(){
      MenuItem controRight = retrieveMenuItem( CONTROL_RIGHT );
      assertThat( controRight.getText(), is( DualBuildWallContextMenu.HIDE_RIGHT ) );
      
      when( display.isRightWallShowing() ).thenReturn( false );
      systemUnderTest.resetMenuOptions();
      assertThat( controRight.getText(), is( DualBuildWallContextMenu.SHOW_RIGHT ) );
      
      when( display.isRightWallShowing() ).thenReturn( true );
      systemUnderTest.resetMenuOptions();
      assertThat( controRight.getText(), is( DualBuildWallContextMenu.HIDE_RIGHT ) );
   }//End Method
   
   @Test public void shouldUpdateTextBasedOnLeftWallState(){
      MenuItem controLeft = retrieveMenuItem( CONTROL_LEFT );
      assertThat( controLeft.getText(), is( DualBuildWallContextMenu.HIDE_LEFT ) );
      
      when( display.isLeftWallShowing() ).thenReturn( false );
      systemUnderTest.resetMenuOptions();
      assertThat( controLeft.getText(), is( DualBuildWallContextMenu.SHOW_LEFT ) );
      
      when( display.isLeftWallShowing() ).thenReturn( true );
      systemUnderTest.resetMenuOptions();
      assertThat( controLeft.getText(), is( DualBuildWallContextMenu.HIDE_LEFT ) );
   }//End Method

}//End Class
