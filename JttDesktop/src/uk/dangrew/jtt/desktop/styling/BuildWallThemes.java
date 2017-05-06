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
 * {@link BuildWallThemes} provides a selection of defined themes for the 
 * {@link uk.dangrew.jtt.buildwall.panel.JobProgressImpl}.
 */
public enum BuildWallThemes {
   
   Standard( "/uk/dangrew/jtt/desktop/buildwall/style/job-progress-standard.css" ),
   Frozen( "/uk/dangrew/jtt/desktop/buildwall/style/job-progress-frozen.css" );
   
   private String sheet;
   
   /**
    * Constructs a new {@link BuildWallThemes}.
    * @param sheet the sheet associated.
    */
   private BuildWallThemes( String sheet ) {
      this.sheet = sheet;
   }//End Constructor

   /**
    * Getter for the sheet associated with the theme.
    * @return the path to the sheet.
    */
   public String sheet() {
      return sheet;
   }//End Method

}//End Class
