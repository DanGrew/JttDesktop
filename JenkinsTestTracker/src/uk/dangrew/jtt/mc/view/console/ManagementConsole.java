/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.view.console;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import uk.dangrew.jtt.mc.notifiers.jobs.BuildResultStatusNotifier;
import uk.dangrew.jtt.mc.sides.jobs.JobProgressTree;
import uk.dangrew.jtt.mc.view.tree.NotificationTree;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link ManagementConsole} provides an area of notifications and information for managing 
 * Jenkins and builds.
 */
public class ManagementConsole extends BorderPane {

   /**
    * Constructs a new {@link ManagementConsole}.
    * @param database the {@link JenkinsDatabase}.
    */
   public ManagementConsole( JenkinsDatabase database ) {
      NotificationTree tree = new NotificationTree();
      
      //not tested - to be wrapped with others
      new BuildResultStatusNotifier( database );
      
      SplitPane split = new SplitPane( new JobProgressTree( database ), tree );
      setCenter( split );
   }//End Constructor
   
}//End Class
