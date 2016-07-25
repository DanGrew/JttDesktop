/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.releases;

import java.util.Timer;
import java.util.function.Supplier;

import uk.dangrew.jtt.main.JenkinsTestTracker;
import uk.dangrew.jtt.versioning.Versioning;
import uk.dangrew.jupa.update.download.NotificationScheduler;
import uk.dangrew.jupa.update.download.ReleaseAvailableTask;
import uk.dangrew.jupa.update.download.ReleasesDownloader;
import uk.dangrew.jupa.update.launch.BasicSystemHandover;
import uk.dangrew.jupa.update.view.panel.ReleaseNotificationPanel;
import uk.dangrew.jupa.update.view.panel.ReleaseNotificationTimeout;

/**
 * The {@link ReleasesWrapper} provides a {@link ReleaseNotificationPanel} that captures the releases configuration
 * required to periodically check for new updates.
 */
public class ReleasesWrapper extends ReleaseNotificationPanel {
   
   static final long NOTIFICATION_PERIOD = 60L * 60L * 1000L;
   static final long NOTIFICATION_TIMEOUT = 60L * 1000L;
   static final String RELEASES_LOCATION = "https://raw.githubusercontent.com/DanGrew/JenkinsTestTracker/master/RELEASES";

   private final NotificationScheduler scheduler;
   private final ReleaseAvailableTask task;
   private final ReleasesDownloader downloader;
   
   /**
    * Constructs a new {@link ReleasesWrapper}.
    */
   public ReleasesWrapper() {
      this( new ReleasesDownloader( RELEASES_LOCATION ), new Versioning(), () -> new ReleaseNotificationTimeout() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ReleasesWrapper}.
    * @param downloader the {@link ReleasesDownloader} to download with.
    * @param versioning the {@link Versioning} to use for this softwares version.
    * @param timeoutSupplier the {@link ReleaseNotificationTimeout} supplier for hiding the notification.
    */
   ReleasesWrapper( ReleasesDownloader downloader, Versioning versioning, Supplier< ReleaseNotificationTimeout > timeoutSupplier ) {
      super( 
               new BasicSystemHandover( JenkinsTestTracker.class ) 
      );
      this.downloader = new ReleasesDownloader( RELEASES_LOCATION );
      this.task = new ReleaseAvailableTask( versioning.getVersionNumber(), downloader, this );
      this.scheduler = new NotificationScheduler( task, NOTIFICATION_PERIOD );
      
      this.showingProperty().addListener( ( source, old, updated ) -> {
         if ( updated ) {
            timeoutSupplier.get().schedule( new Timer(), NOTIFICATION_TIMEOUT, this );
         }
      } );
   }//End Constructor
   
   ReleasesDownloader downloader() {
      return downloader;
   }//End Method
   
   NotificationScheduler scheduler() {
      return scheduler;
   }//End Method
   
   ReleaseAvailableTask task() {
      return task;
   }//End Method

}//End Class
