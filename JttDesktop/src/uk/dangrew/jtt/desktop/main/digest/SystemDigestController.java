/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.main.digest;

import java.time.LocalDateTime;

import uk.dangrew.jtt.desktop.main.JenkinsTestTracker;
import uk.dangrew.jtt.desktop.utility.time.DefaultTimestampProvider;
import uk.dangrew.jtt.desktop.utility.time.TimestampProvider;
import uk.dangrew.sd.logging.location.JarLoggingProtocol;
import uk.dangrew.sd.logging.logger.DigestFileLogger;
import uk.dangrew.sd.table.presentation.DigestTableRowLimit;
import uk.dangrew.sd.utility.threading.ThreadedWrapper;
import uk.dangrew.sd.viewer.basic.DigestViewer;

/**
 * The {@link SystemDigestController} is responsible for the digest elements of the
 * {@link JenkinsTestTracker}.
 */
public class SystemDigestController {
   
   static final long FILE_SIZE_LIMIT = 104857600L;
   static final String LOG_FILE_SUFFIX = "-log.txt";
   static final String FOLDER_NAME = "uk.dangrew.jtt.logs";
   
   private final DigestViewer digestViewer;
   private final JarLoggingProtocol protocol;
   
   /**
    * Constructs a new {@link SystemDigestController} for the {@link JenkinsTestTracker}.
    */
   public SystemDigestController() {
      this( new DefaultTimestampProvider(), new ThreadedWrapper(), new DigestFileLogger(), new DigestViewer( 600, 200 ) );
   }//End Constructor
   
   /**
    * Constructs a new {@link SystemDigestController}.
    * @param timestampProvider the {@link TimestampProvider}.
    * @param threadWrapper the {@link ThreadedWrapper} for running the logging on another {@link Thread}.
    * @param logger the {@link DigestFileLogger} for processing the logging.
    * @param digestViewer the {@link DigestViewer} to show.
    */
   SystemDigestController( TimestampProvider timestampProvider, ThreadedWrapper threadWrapper, DigestFileLogger logger, DigestViewer digestViewer ) {
      this.digestViewer = digestViewer;
      this.digestViewer.setTableRowLimit( DigestTableRowLimit.OneThousand );
      this.protocol = new JarLoggingProtocol( FOLDER_NAME, makeFilePrefix( timestampProvider.get() ) + LOG_FILE_SUFFIX, JenkinsTestTracker.class );
      this.protocol.setFileSizeLimit( FILE_SIZE_LIMIT );
      logger.setFileLocation( protocol );
      threadWrapper.wrap( logger, -1 );
   }//End Constructor

   /**
    * Method to get the {@link DigestViewer} associated with the {@link JenkinsTestTracker}.
    * @return the {@link DigestViewer}.
    */
   public DigestViewer getDigestViewer() {
      return digestViewer;
   }//End Method
   
   /**
    * Provides the location of the logging.
    * @return {@link uk.dangrew.sd.logging.location.LoggingLocationProtocol#getLocation()}.
    */
   public String getLoggingLocation(){
      return protocol.getLocation();
   }//End Method

   /**
    * Method to make a prefix for the file to log to given the {@link LocalDateTime} for now.
    * @param localDateTime the {@link LocalDateTime} to make for.
    * @return the {@link String} prefix.
    */
   String makeFilePrefix( LocalDateTime localDateTime ) {
      return localDateTime.toString().replace( ":", "-" ).replace( "T", "at" );
   }//End Method
   
}//End Class
