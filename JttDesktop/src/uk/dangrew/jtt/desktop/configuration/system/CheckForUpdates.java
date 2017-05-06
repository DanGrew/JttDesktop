/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.configuration.system;

import uk.dangrew.jtt.desktop.environment.releases.JttReleasesAvailableTask;
import uk.dangrew.jtt.desktop.main.JenkinsTestTracker;
import uk.dangrew.jupa.update.launch.BasicSystemHandover;
import uk.dangrew.jupa.update.view.panel.ReleaseSummaryWindowController;

/**
 * {@link CheckForUpdates} combines the {@link ReleaseSummaryWindowController} with the {@link JttReleasesAvailableTask}.
 */
class CheckForUpdates {

   private final JttReleasesAvailableTask task;
   
   /**
    * Constructs a new {@link CheckForUpdates}.
    */
   CheckForUpdates() {
      ReleaseSummaryWindowController controller = new ReleaseSummaryWindowController( new BasicSystemHandover( JenkinsTestTracker.class ) );
      this.task = new JttReleasesAvailableTask( controller::releasesAreNowAvailable );
   }//End Constructor
   
   /**
    * Method to check for updates. This will block the current thread and popup the {@link uk.dangrew.jupa.update.view.panel.ReleaseSummaryPanel}.
    */
   void checkForUpdates(){
      task.run();
   }//End Method
   
}//End Class
