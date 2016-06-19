/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.main.selector;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.buildwall.dual.DualBuildWallContextMenuOpener;
import uk.dangrew.jtt.buildwall.dual.DualBuildWallDisplayImpl;
import uk.dangrew.jtt.buildwall.layout.BuildWallDisplayImpl;
import uk.dangrew.jtt.graphics.DecoupledPlatformImpl;
import uk.dangrew.jtt.graphics.JavaFxInitializer;
import uk.dangrew.jtt.graphics.PlatformDecouplerImpl;
import uk.dangrew.jtt.shortcuts.keyboard.KeyBoardShortcuts;
import uk.dangrew.jtt.storage.database.JenkinsDatabaseImpl;
import uk.dangrew.jtt.utility.TestCommon;
import uk.dangrew.jtt.view.table.TestTableView;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * {@link Tools} test.
 */
public class ToolsTest {
   
   @Mock private DigestViewer digestViewer;
   
   @BeforeClass public static void initialisePlatform(){
      JavaFxInitializer.startPlatform();
      DecoupledPlatformImpl.setInstance( new PlatformDecouplerImpl() );
   }//End Method
   
   @Before public void initialiseEnvironment(){
      MockitoAnnotations.initMocks( this );
   }//End Method
   
   @Test public void shouldValueOfWithName() {
      TestCommon.assertEnumNameWithValueOf( Tools.class );
   }//End Method
   
   @Test public void shouldValueOfWithToString() {
      TestCommon.assertEnumToStringWithValueOf( Tools.class );
   }//End Method
   
   @Test public void shouldConstructTestTableTool() {
      Scene scene = Tools.TestTable.construct( new JenkinsDatabaseImpl(), digestViewer );
      assertThat( scene, notNullValue() );
      
      assertThat( scene.getRoot(), notNullValue() );
      assertThat( scene.getRoot(), instanceOf( TestTableView.class ) );
   }//End Method
   
   @Test public void shouldConstructBuildWallTool() {
      Scene scene = Tools.BuildWall.construct( new JenkinsDatabaseImpl(), digestViewer );
      assertThat( scene, notNullValue() );
      
      assertThat( scene.getRoot(), notNullValue() );
      assertThat( scene.getRoot(), instanceOf( BuildWallDisplayImpl.class ) );
      
      BuildWallDisplayImpl display = ( BuildWallDisplayImpl ) scene.getRoot();
      assertThat( display.hasConfigurationTurnedOn(), is( false ) );
      
      assertThat( KeyBoardShortcuts.getCmdShiftO( scene ), notNullValue() );
      Runnable runnable = KeyBoardShortcuts.getCmdShiftO( scene );
      assertThat( runnable, notNullValue() );
      
      runnable.run();
      assertThat( display.hasConfigurationTurnedOn(), is( true ) );
      
      runnable.run();
      assertThat( display.hasConfigurationTurnedOn(), is( false ) );
   }//End Method
   
   @Test public void shouldConstructDualBuildWallTool() {
      Scene scene = Tools.DualBuildWall.construct( new JenkinsDatabaseImpl(), digestViewer );
      assertThat( scene, notNullValue() );
      
      assertThat( scene.getRoot(), notNullValue() );
      assertThat( scene.getRoot(), instanceOf( BorderPane.class ) );
      
      BorderPane wrapper = ( BorderPane ) scene.getRoot();
      
      DualBuildWallDisplayImpl display = ( DualBuildWallDisplayImpl ) wrapper.getCenter();
      assertThat( display.getOnContextMenuRequested(), instanceOf( DualBuildWallContextMenuOpener.class ) );
      DualBuildWallContextMenuOpener opener = ( DualBuildWallContextMenuOpener ) display.getOnContextMenuRequested();
      assertThat( opener.isSystemDigestControllable(), is( true ) );
      
      assertThat( wrapper.getTop(), instanceOf( TitledPane.class ) );
      TitledPane titledPane = ( TitledPane )wrapper.getTop();
      assertThat( titledPane.getContent(), is( digestViewer ) );
      
      assertThat( scene.getWidth(), is( 500.0 ) );
      assertThat( scene.getHeight(), is( 500.0 ) );
   }//End Method

}//End Class
