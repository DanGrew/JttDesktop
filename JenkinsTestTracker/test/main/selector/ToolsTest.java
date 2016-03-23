/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package main.selector;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import buildwall.dual.DualBuildWallDisplayImpl;
import buildwall.layout.BuildWallDisplayImpl;
import graphics.DecoupledPlatformImpl;
import graphics.JavaFxInitializer;
import graphics.PlatformDecouplerImpl;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import shortcuts.keyboard.KeyBoardShortcuts;
import storage.database.JenkinsDatabaseImpl;
import utility.TestCommon;
import view.table.TestTableView;
import viewer.basic.DigestViewer;

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
      assertThat( scene.getRoot(), instanceOf( DualBuildWallDisplayImpl.class ) );
      
      DualBuildWallDisplayImpl display = ( DualBuildWallDisplayImpl ) scene.getRoot();
      assertThat( display.getRight(), nullValue() );
      
      assertThat( display.getTop(), instanceOf( TitledPane.class ) );
      TitledPane titledPane = ( TitledPane )display.getTop();
      assertThat( titledPane.getContent(), is( digestViewer ) );
   }//End Method

}//End Class
