/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.storage.database;

/**
 * The {@link SystemWideJenkinsDatabaseImpl} is a {@link SystemWideObject} for the {@link JenkinsDatabase}.
 */
public class SystemWideJenkinsDatabaseImpl extends SystemWideObject< JenkinsDatabase > {

   private static final JenkinsDatabase SYSTEM_DATABASE = new JenkinsDatabaseImpl();
   
   /**
    * Constructs a new {@link SystemWideJenkinsDatabaseImpl}.
    */
   public SystemWideJenkinsDatabaseImpl() {
      super( SYSTEM_DATABASE );
   }//End Constructor

}//End Class
