/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import com.sun.javafx.application.PlatformImpl;

import javafx.beans.value.ChangeListener;
import javafx.scene.layout.Pane;

/**
 * The {@link WallBuilder} is a {@link Pane} for arranging content in the tool.
 */
public class WallBuilder extends Pane {
   
   private ContentBoundary left;
   private ContentBoundary top;
   private ContentBoundary right;
   private ContentBoundary bottom;
   private ContentArea initial;
   
   private final ContentAreaSelector selector;
   
   /**
    * Constructs a new {@link WallBuilder}.
    */
   WallBuilder() {
      this( new ContentAreaSelector() );
   }//End Constructor
      
   /**
    * Constructs a new {@link WallBuilder}.
    * @param selector the {@link ContentAreaSelector}.
    */
   WallBuilder( ContentAreaSelector selector ) {
      this.selector = selector;
      this.selector.setNodes( getChildren() );
      
      this.left = new ContentBoundary( 0 );
      this.top = new ContentBoundary( 0 );
      this.right = new ContentBoundary( 100 );
      this.bottom = new ContentBoundary( 100 );
      
      this.left.setFixed( true );
      this.right.setFixed( true );
      this.top.setFixed( true );
      this.bottom.setFixed( true );
      
      initial = new ContentArea( 
               left, top, right, bottom,
               getWidth(), getHeight() 
      );
      getChildren().add( initial );  
      
      ChangeListener< Number > contentRescaler = ( s, o, n ) -> { 
         getChildren().forEach( c -> ( ( ContentArea )c ).setParentDimensions( getWidth(), getHeight() ) ); 
      }; 
      
      widthProperty().addListener( contentRescaler );
      heightProperty().addListener( contentRescaler );
   }//End Constructor
   
   /**
    * Method to construct the {@link ContentBoundary} between the given {@link ContentBoundary}s.
    * @param splitBetweenHere the split from {@link ContentBoundary}.
    * @param splitBetweenThere the split to {@link ContentBoundary}.
    * @return a new {@link ContentBoundary} between the two given.
    */
   private ContentBoundary constructBoundaryToSplit( 
            ContentBoundary splitBetweenHere, 
            ContentBoundary splitBetweenThere 
   ){
      double splitPosition = splitBetweenThere.positionPercentage() - splitBetweenHere.positionPercentage();
      splitPosition /= 2;
      splitPosition += splitBetweenHere.positionPercentage();
      return new ContentBoundary( splitPosition );
   }//End Method
   
   /**
    * Method to split the current selection vertically, resizing the given to be on top and adding a
    * new {@link ContentArea} for the bottom.
    */
   void splitVertically() {
      ContentArea area = selector.getSelection();
      if ( area == null ) {
         return;
      }
      
      ContentBoundary newBoundary = constructBoundaryToSplit( 
               area.topBoundary(), area.bottomBoundary() 
      );
      
      ContentArea newArea = new ContentArea( 
               area.leftBoundary(), newBoundary, area.rightBoundary(), area.bottomBoundary(), 
               getWidth(), getHeight() 
      );
      addChild( newArea );
      
      area.setBottomBoundary( newBoundary );
   }//End Method

   /**
    * Method to split the current selection horizontally, resizing the given to be on the left and adding a
    * new {@link ContentArea} for the right.
    */
   void splitHorizontally() {
      ContentArea area = selector.getSelection();
      if ( area == null ) {
         return;
      }
      
      ContentBoundary newBoundary = constructBoundaryToSplit( 
               area.leftBoundary(), area.rightBoundary() 
      );
      
      ContentArea newArea = new ContentArea( 
               newBoundary, area.topBoundary(), area.rightBoundary(), area.bottomBoundary(), 
               getWidth(), getHeight() 
      );
      addChild( newArea );
      
      area.setRightBoundary( newBoundary );
   }//End Method
   
   /**
    * Method to add the {@link ContentArea} as a child, since it needs to be on the JavaFx thread.
    * @param area the {@link ContentArea} to add.
    */
   private void addChild( ContentArea area ) {
      PlatformImpl.runAndWait( () -> getChildren().add( area ) );      
   }//End Method
   
   /**
    * Method to push the current selection at the given {@link SquareBoundary} by the given
    * percentage.
    * @param boundaryType the {@link SquareBoundary} to push by.
    * @param percentage the percentage to push by.
    */
   void push( SquareBoundary boundaryType, double percentage ) {
      ContentArea selection = selector.getSelection();
      if ( selection == null ) {
         return;
      }
      
      boundaryType.push( selection, percentage );
   }//End Method
   
   /**
    * Method to pull the current selection at the given {@link SquareBoundary} by the given
    * percentage.
    * @param boundaryType the {@link SquareBoundary} to pull by.
    * @param percentage the percentage to push by.
    */
   void pull( SquareBoundary boundaryType, double percentage ) {
      ContentArea selection = selector.getSelection();
      if ( selection == null ) {
         return;
      }
      
      boundaryType.pull( selection, percentage );
   }//End Method
   
}//End Class
