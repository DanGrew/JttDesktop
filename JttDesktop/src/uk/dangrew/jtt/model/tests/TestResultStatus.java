/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.model.tests;

import javafx.scene.paint.Color;

/**
 * Enum definnig the states that a {@link TestCase} can be in.
 */
public enum TestResultStatus {

   PASSED( Color.GREEN.brighter() ),
   FIXED( Color.GREEN.brighter() ),
   FAILED( Color.ORANGE ),
   SKIPPED( Color.AQUA ),
   REGRESSION( Color.RED ),
   UNKNOWN( Color.GRAY );
   
   private Color colour;
   
   /**
    * Constructs a new {@link TestResultStatus}.
    * @param colour the {@link Color} associated.
    */
   private TestResultStatus( Color colour ) {
      this.colour = colour;
   }//End Constructor
   
   /**
    * Method to get the associated {@link Color}.
    * @return the {@link Color}.
    */
   public Color colour(){
      return colour;
   }//End Method
}//End Enum
