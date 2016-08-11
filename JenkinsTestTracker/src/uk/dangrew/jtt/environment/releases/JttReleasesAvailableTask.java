/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.environment.releases;

import uk.dangrew.jtt.versioning.Versioning;
import uk.dangrew.jupa.update.download.ReleaseAvailableTask;
import uk.dangrew.jupa.update.download.ReleasesDownloader;
import uk.dangrew.jupa.update.model.ReleaseAvailabilityObserver;

/**
 * {@link uk.dangrew.jtt.main.JenkinsTestTracker} specific {@link ReleaseAvailableTask} to connect
 * to the correct location and use the correct version number.
 */
public class JttReleasesAvailableTask extends ReleaseAvailableTask {

   static final String RELEASES_LOCATION = "https://raw.githubusercontent.com/DanGrew/JenkinsTestTracker/master/RELEASES";
   
   public JttReleasesAvailableTask( ReleaseAvailabilityObserver observer ) {
      super( new Versioning().getVersionNumber(), new ReleasesDownloader( RELEASES_LOCATION ), observer );
   }//End Constructor

}//End Class
