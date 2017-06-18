/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import java.util.function.BiConsumer;
import java.util.function.Function;

import uk.dangrew.jtt.desktop.utility.math.IdentityTranslation;
import uk.dangrew.jtt.desktop.utility.math.NegateTranslation;

/**
 * The {@link SquareBoundary} provides the type of {@link ContentBoundary}s on a square shape.
 */
public enum SquareBoundary {
   
   Left( 
            ContentArea::leftBoundary, 
            ( b, a ) -> a.setLeftBoundary( b ),
            new NegateTranslation() 
   ),
   Top( 
            ContentArea::topBoundary, 
            ( b, a ) -> a.setTopBoundary( b ),
            new NegateTranslation() 
   ),
   Right( 
            ContentArea::rightBoundary, 
            ( b, a ) -> a.setRightBoundary( b ),
            new IdentityTranslation() 
   ),
   Bottom( 
            ContentArea::bottomBoundary, 
            ( b, a ) -> a.setBottomBoundary( b ),
            new IdentityTranslation() 
   );
   
   private final Function< ContentArea, ContentBoundary > boundaryGetter;
   private final BiConsumer< ContentBoundary, ContentArea > boundarySetter;
   private final Function< Double, Double > pushTranslation;
   
   /**
    * Constructs a new {@link SquareBoundary}.
    * @param boundaryGetter the {@link Function} for providing the {@link ContentBoundary}.
    * @param boundarySetter the {@link Function} for setting the {@link ContentBoundary}.
    * @param pushTranslation the {@link Function} for translating the change for a push operation.
    */
   private SquareBoundary( 
            Function< ContentArea, ContentBoundary > boundaryGetter,
            BiConsumer< ContentBoundary, ContentArea > boundarySetter,
            Function< Double, Double > pushTranslation
   ) {
      this.boundaryGetter = boundaryGetter;
      this.boundarySetter = boundarySetter;
      this.pushTranslation = pushTranslation;
   }//End Constructor
   
   /**
    * Provides the {@link ContentBoundary} for the type of boundary.
    * @param area the {@link ContentArea} to get the {@link ContentBoundary} for.
    * @return the {@link ContentBoundary}.
    */
   public ContentBoundary boundary( ContentArea area ) {
      return boundaryGetter.apply( area );
   }//End Method
   
   /**
    * Method to set the {@link ContentBoundary} on the given {@link ContentArea} for this
    * type of {@link ContentBoundary}.
    * @param boundary the {@link ContentBoundary} to set.
    * @param area the {@link ContentArea} to set on.
    */
   public void set( ContentBoundary boundary, ContentArea area ) {
      boundarySetter.accept( boundary, area );
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
   
   /**
    * Method to provide the opposte {@link SquareBoundary} to this. Values cannot reference eachother
    * and therefore must use a switch.
    * @return the opposite.
    */
   public SquareBoundary opposite(){
      switch ( this ) {
         case Bottom:
            return Top;
         case Left:
            return Right;
         case Right:
            return Left;
         case Top:
            return Bottom;
         default:
            return null;
      }
   }//End Method

}//End Enum
