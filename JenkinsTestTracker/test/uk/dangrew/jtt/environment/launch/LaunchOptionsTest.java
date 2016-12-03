/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.launch;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.buildwall.dual.DualBuildWallContextMenuOpener;
import uk.dangrew.jtt.buildwall.dual.DualBuildWallDisplayImpl;
import uk.dangrew.jtt.configuration.system.SystemConfiguration;
import uk.dangrew.jtt.environment.main.EnvironmentWindow;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.TestPlatformDecouplerImpl;
import uk.dangrew.jtt.mc.view.console.ManagementConsole;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.jtt.styling.SystemStyling;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * {@link LaunchOptions} test.
 */
public class LaunchOptionsTest {
   
   private EnvironmentWindowWithExposedWidth window;
   private SystemConfiguration configuration;
   private JenkinsDatabase database;
   @Mock private DigestViewer digest;
   @Captor private ArgumentCaptor< Node > contentCaptor;
   private LaunchOptions systemUnderTest;
   
   /** Extended {@link EnvironmentWindow} to exposed width and height.*/
   private static class EnvironmentWindowWithExposedWidth extends EnvironmentWindow {
      public EnvironmentWindowWithExposedWidth( SystemConfiguration configuration ) {
         super( configuration );
      }//End Constructor

      @Override protected void setWidth( double value ) {
         super.setWidth( value );
      }//End Method
      
      @Override protected void setHeight( double value ) {
         super.setHeight( value );
      }//End Method
   }//End Class
   
   @Before public void initialiseSystemUnderTest(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new TestPlatformDecouplerImpl() );
      SystemStyling.initialise();
      MockitoAnnotations.initMocks( this );
      configuration = new SystemConfiguration();
      database = new JenkinsDatabaseImpl();
      window = spy( new EnvironmentWindowWithExposedWidth( configuration ) );
      systemUnderTest = new LaunchOptions( window, configuration, database, digest );
   }//End Method

   @Ignore
   @Test public void manual() throws InterruptedException {
      JavaFxInitializer.launchInWindow( () -> systemUnderTest );
      
      Thread.sleep( 100000 );
   }//End Method
   
   @Test public void shouldProvideManagementConsoleButton(){
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.managementConsoleButton() ), is( true ) );
      assertThat( systemUnderTest.managementConsoleButton().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.managementConsoleButton().getText(), is( LaunchOptions.MANAGEMENT_CONSOLE_BUTTON_TEXT ) );
   }//End Method
   
   @Test public void shouldProvideBuildWallButton(){
      assertThat( systemUnderTest.getChildren().contains( systemUnderTest.buildWallButton() ), is( true ) );
      assertThat( systemUnderTest.buildWallButton().getMaxWidth(), is( Double.MAX_VALUE ) );
      assertThat( systemUnderTest.buildWallButton().getText(), is( LaunchOptions.BUILD_WALL_BUTTON_TEXT ) );
   }//End Method
   
   @Test public void shouldUseSpacingForReadability(){
      assertThat( systemUnderTest.getSpacing(), is( LaunchOptions.BUTTON_SPACING ) );
   }//End Method
   
   @Test public void buildWallButtonShouldLaunchBuildWall(){
      systemUnderTest.buildWallButton().getOnAction().handle( new ActionEvent() );
      verify( window ).setContent( contentCaptor.capture() );
      
      Node content = contentCaptor.getValue();
      assertThat( content, instanceOf( BorderPane.class ) );
      
      BorderPane wrapperPane = ( BorderPane ) ( ( BorderPane )content ).getCenter();
      
      DualBuildWallDisplayImpl display = ( DualBuildWallDisplayImpl ) wrapperPane.getCenter();
      assertThat( display.getOnContextMenuRequested(), instanceOf( DualBuildWallContextMenuOpener.class ) );
      DualBuildWallContextMenuOpener opener = ( DualBuildWallContextMenuOpener ) display.getOnContextMenuRequested();
      assertThat( opener.isSystemDigestControllable(), is( true ) );
      
      assertThat( wrapperPane.getTop(), instanceOf( TitledPane.class ) );
      TitledPane titledPane = ( TitledPane )wrapperPane.getTop();
      assertThat( titledPane.getContent(), is( digest ) );
   }//End Method
   
   @Test public void managementConsoleButtonShouldMangementConsole(){
      systemUnderTest.managementConsoleButton().getOnAction().handle( new ActionEvent() );
      verify( window ).setContent( contentCaptor.capture() );
      
      Node content = contentCaptor.getValue();
      assertThat( content, instanceOf( ManagementConsole.class ) );
   }//End Method
   
   @Test public void shouldBindBuildWallToWindow(){
      systemUnderTest.buildWallButton().getOnAction().handle( new ActionEvent() );
      assertDimensionsBound();
   }//End Method
   
   @Test public void shouldBindNotificationsToWindow(){
      systemUnderTest.managementConsoleButton().getOnAction().handle( new ActionEvent() );
      assertDimensionsBound();
   }//End Method
   
   private void assertDimensionsBound(){
      verify( window ).setContent( contentCaptor.capture() );
      
      Node content = contentCaptor.getValue();
      BorderPane wrapper = ( BorderPane ) content;
      
      window.setWidth( 103 );
      assertThat( wrapper.getMinWidth(), is( 103.0 ) );
      assertThat( wrapper.getMaxWidth(), is( 103.0 ) );
      
      window.setHeight( 102 );
      assertThat( wrapper.getMinHeight(), is( 102.0 ) );
      assertThat( wrapper.getMaxHeight(), is( 102.0 ) );
   }//End Method
   
   @Test public void shouldHaveNotificationCenter(){
      assertThat( systemUnderTest.notificationCenter(), is( notNullValue() ) );
   }//End Method
   
   @Test public void shouldBeAssociatedWith(){
      assertThat( systemUnderTest.isAssociatedWith( digest ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( window ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( database ), is( true ) );
      assertThat( systemUnderTest.isAssociatedWith( configuration ), is( true ) );
      
      assertThat( systemUnderTest.isAssociatedWith( mock( DigestViewer.class ) ), is( false ) );
      assertThat( systemUnderTest.isAssociatedWith( mock( EnvironmentWindow.class ) ), is( false ) );
      assertThat( systemUnderTest.isAssociatedWith( new JenkinsDatabaseImpl() ), is( false ) );
      assertThat( systemUnderTest.isAssociatedWith( new SystemConfiguration() ), is( false ) );
   }//End Method
   
   @Test public void shouldHaveSoundPlayerAssociated(){
      systemUnderTest.buildWallButton().getOnAction().handle( new ActionEvent() );
      assertThat( systemUnderTest.soundPlayer(), is( notNullValue() ) );
      assertThat( systemUnderTest.soundPlayer().isAssociatedWith( configuration.getSoundConfiguration() ), is( true ) );
   }//End Method
   
   @Test public void shouldNotHaveSoundPlayerForMc(){
      systemUnderTest.managementConsoleButton().getOnAction().handle( new ActionEvent() );
      assertThat( systemUnderTest.soundPlayer(), is( nullValue() ) );
   }//End Method

}//End Class
