/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.jobs;

/**
 * Enum defining the build result of a {@link JenkinsJob}.
 */
public enum BuildResultStatus {

   ABORTED( "Aborted" ),
   SUCCESS( "Success" ),
   FAILURE( "Failure" ),
   NOT_BUILT( "Not Built" ),
   UNSTABLE( "Unstable" ),
   UNKNOWN( "Unknown" );
   
   private final String displayName;
   
   /**
    * Constructs a new {@link BuildResultStatus}.
    * @param displayName the display version.
    */
   private BuildResultStatus( String displayName ) {
      this.displayName = displayName;
   }//End Constructor
   
   /**
    * Access to the readable display version of the {@link BuildResultStatus}.
    * @return the display name.
    */
   public String displayName(){
      return displayName;
   }//End Method
   
}//End Enum
