/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.styling;

/**
 * {@link BuildWallStyles} provides a mapping of styles that are read in from
 * a css style sheet.
 */
public enum BuildWallStyles {
   ProgressBarSuccess( "progress-bar-success" ), 
   ProgressBarFailed( "progress-bar-failed" ), 
   ProgressBarUnstable( "progress-bar-unstable" ), 
   ProgressBarAborted( "progress-bar-aborted" ), 
   ProgressBarNotBuilt( "progress-bar-not-built" ), 
   ProgressBarUnknown( "progress-bar-unknown" );
   
   private String label;
   
   /**
    * Constructs a new {@link BuildWallStyles}.
    * @param label the label in the css style sheet.
    */
   private BuildWallStyles( String label ) {
      this.label = label;
   }//End Constructor
   
   /**
    * Getter for the label associated.
    * @return the {@link String} label in the css style sheet.
    */
   String label(){
      return label;
   }//End Method
   
}//End Class
