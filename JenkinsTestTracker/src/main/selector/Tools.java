/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package main.selector;

import java.util.function.Function;

import buildwall.dual.DualBuildWallDisplayImpl;
import buildwall.layout.BuildWallDisplayImpl;
import javafx.scene.Scene;
import shortcuts.keyboard.KeyBoardShortcuts;
import storage.database.JenkinsDatabase;
import view.table.TestTableView;

/**
 * {@link Tools} provides the types of tools available to launch.
 */
public enum Tools {

   TestTable( 
         database -> { 
            TestTableView table = new TestTableView( database );
            return new Scene( table );
         }
   ),
   BuildWall(
         database -> { 
            BuildWallDisplayImpl buildWall = new BuildWallDisplayImpl( database );
            Scene scene = new Scene( buildWall );
            KeyBoardShortcuts.cmdShiftO( scene, () -> buildWall.toggleConfiguration() );
            return scene;
         }
   ), 
   DualBuildWall(
         database -> {
            DualBuildWallDisplayImpl dualWall = new DualBuildWallDisplayImpl( database );
            return new Scene( dualWall );
         }
   );
   
   private Function< JenkinsDatabase, Scene > toolConstructor;
   
   /**
    * Constructs a new {@link Tools}.
    * @param toolConstructor the {@link Function} for constructing the tool.
    */
   private Tools( Function< JenkinsDatabase, Scene > toolConstructor ) {
      this.toolConstructor = toolConstructor;
   }//End Constructor
   
   /**
    * Method to construct the tool given the {@link JenkinsDatabase}.
    * @param database the {@link JenkinsDatabase} to construct with.
    * @return the {@link Scene} the tool is placed in.
    */
   public Scene construct( JenkinsDatabase database ){
      return toolConstructor.apply( database );
   }//End Method
   
}//End Enum
