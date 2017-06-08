/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import java.util.function.Function;

import uk.dangrew.jtt.desktop.utility.math.IdentityTranslation;
import uk.dangrew.jtt.desktop.utility.math.NegateTranslation;

/**
 * The {@link SquareBoundary} provides the type of {@link ContentBoundary}s on a square shape.
 */
public enum SquareBoundary {
   
   Left( ContentArea::leftBoundary, new NegateTranslation() ),
   Top( ContentArea::topBoundary, new NegateTranslation() ),
   Right( ContentArea::rightBoundary, new IdentityTranslation() ),
   Bottom( ContentArea::bottomBoundary, new IdentityTranslation() );
   
   private final Function< ContentArea, ContentBoundary > boundaryMapping;
   private final Function< Double, Double > pushTranslation;
   
   /**
    * Constructs a new {@link SquareBoundary}.
    * @param boundaryMapping the {@link Function} for providing the {@link ContentBoundary}.
    * @param pushTranslation the {@link Function} for translating the change for a push operation.
    */
   private SquareBoundary( 
            Function< ContentArea, ContentBoundary > boundaryMapping,
            Function< Double, Double > pushTranslation
   ) {
      this.boundaryMapping = boundaryMapping;
      this.pushTranslation = pushTranslation;
   }//End Constructor
   
   /**
    * Provides the {@link ContentBoundary} for the type of boundary.
    * @param area the {@link ContentArea} to get the {@link ContentBoundary} for.
    * @return the {@link ContentBoundary}.
    */
   public ContentBoundary boundary( ContentArea area ) {
      return boundaryMapping.apply( area );
   }//End Method
   
   /**
    * Method to push the {@link ContentBoundary} on the given {@link ContentArea} by the
    * given amount.
    * @param area the {@link ContentArea} to push.
    * @param change the change to apply.
    */
   public void push( ContentArea area, double change ) {
      boundary( area ).changePosition( pushTranslation.apply( change ) );
   }//End Method
   
   /**
    * Method to pull the {@link ContentBoundary} on the given {@link ContentArea} by the
    * given amount.
    * @param area the {@link ContentArea} to pull.
    * @param change the change to apply.
    */
   public void pull( ContentArea area, double change ) {
      boundary( area ).changePosition( -pushTranslation.apply( change ) );
   }//End Method

}//End Enum
