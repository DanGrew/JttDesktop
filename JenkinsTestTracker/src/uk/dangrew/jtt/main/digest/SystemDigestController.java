/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.main.digest;

import viewer.basic.DigestViewer;

/**
 * The {@link SystemDigestController} is responsible for the digest elements of the
 * {@link uk.dangrew.jtt.main.JenkinsTestTracker}.
 */
public class SystemDigestController {
   
   private final DigestViewer digestViewer;
   
   /**
    * Constructs a new {@link SystemDigestController} for the {@link uk.dangrew.jtt.main.JenkinsTestTracker}.
    */
   public SystemDigestController() {
      this.digestViewer = new DigestViewer( 600, 200 );
   }//End Constructor

   /**
    * Method to get the {@link DigestViewer} associated with the {@link uk.dangrew.jtt.main.JenkinsTestTracker}.
    * @return the {@link DigestViewer}.
    */
   public DigestViewer getDigestViewer() {
      return digestViewer;
   }//End Method
   
}//End Class
