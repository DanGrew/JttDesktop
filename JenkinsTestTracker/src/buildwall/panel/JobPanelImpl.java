/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package buildwall.panel;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import model.jobs.JenkinsJob;

/**
 * The {@link JobPanelImpl} provides a single {@link Pane} for a individual {@link JenkinsJob}
 * that can be displayed on a build wall.
 */
public class JobPanelImpl extends StackPane {

   /**
    * Constructs a new {@link JobPanelImpl}.
    * @param configuration the {@link BuildWallConfiguration}.
    * @param job the {@link JenkinsJob}.
    */
   public JobPanelImpl( BuildWallConfiguration configuration, JenkinsJob job ) {
      getChildren().add( new JobProgressImpl( job ) );
      getChildren().add( new JobPanelDescriptionImpl( configuration, job ) );
   }//End Method

}//End Class
