/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package main.selector;

/**
 * {@link Tools} provides the types of tools available to launch.
 */
public enum Tools {

   TestTable( "Tracker Table" ),
   BuildWall( "Build Wall" );
   
   private String displayName;
   
   /**
    * Constructs a new {@link Tools}.
    * @param displayName the display name of the tool.
    */
   private Tools( String displayName ) {
      this.displayName = displayName;
   }//End Constructor
   
   /**
    * Getter for the display name.
    * @return the display name of the tool.
    */
   public String displayName() {
      return displayName;
   }//End Method
}//End Enum
