/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package main.selector;

import java.util.function.BiFunction;

import buildwall.dual.DualBuildWallDisplayImpl;
import buildwall.layout.BuildWallDisplayImpl;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import shortcuts.keyboard.KeyBoardShortcuts;
import storage.database.JenkinsDatabase;
import view.table.TestTableView;
import viewer.basic.DigestViewer;

/**
 * {@link Tools} provides the types of tools available to launch.
 */
public enum Tools {

   TestTable( 
         ( database, digest ) -> { 
            TestTableView table = new TestTableView( database );
            return new Scene( table );
         }
   ),
   BuildWall(
         ( database, digest ) -> { 
            BuildWallDisplayImpl buildWall = new BuildWallDisplayImpl( database );
            Scene scene = new Scene( buildWall );
            KeyBoardShortcuts.cmdShiftO( scene, () -> buildWall.toggleConfiguration() );
            return scene;
         }
   ), 
   DualBuildWall(
         ( database, digest ) -> {
            DualBuildWallDisplayImpl dualWall = new DualBuildWallDisplayImpl( database );
            BorderPane digestWrapper = new BorderPane( dualWall );
            digestWrapper.setTop( new TitledPane( "System Digest", digest ) );
            dualWall.initialiseContextMenu();
            return new Scene( digestWrapper, 500, 500 );
         }
   );
   
   private transient BiFunction< JenkinsDatabase, DigestViewer, Scene > toolConstructor;
   
   /**
    * Constructs a new {@link Tools}.
    * @param toolConstructor the {@link BiFunction} for constructing the tool.
    */
   private Tools( BiFunction< JenkinsDatabase, DigestViewer, Scene > toolConstructor ) {
      this.toolConstructor = toolConstructor;
   }//End Constructor
   
   /**
    * Method to construct the tool given the {@link JenkinsDatabase}.
    * @param database the {@link JenkinsDatabase} to construct with.
    * @param digest the {@link DigestViewer}.
    * @return the {@link Scene} the tool is placed in.
    */
   public Scene construct( JenkinsDatabase database, DigestViewer digest ){
      return toolConstructor.apply( database, digest );
   }//End Method
   
}//End Enum
