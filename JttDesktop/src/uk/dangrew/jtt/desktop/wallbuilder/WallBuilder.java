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
   
   private ContentArea initial;
   private final ContentAreaSelector selector;
   private final ContentAreaIntersections intersections;
   
   /**
    * Constructs a new {@link WallBuilder}.
    */
   WallBuilder() {
      this( new ContentAreaSelector(), new ContentAreaIntersections() );
   }//End Constructor
      
   /**
    * Constructs a new {@link WallBuilder}.
    * @param selector the {@link ContentAreaSelector}.
    * @param intersections the {@link ContentAreaIntersections}.
    */
   WallBuilder( ContentAreaSelector selector, ContentAreaIntersections intersections ) {
      this.selector = selector;
      this.selector.setNodes( getChildren() );
      this.intersections = intersections;
      this.intersections.setNodes( getChildren() );
      
      initial = new ContentArea( 
               getWidth(), getHeight(), 
               0, 0, 
               100, 100 
      );
      getChildren().add( initial );  
      
      ChangeListener< Number > contentRescaler = ( s, o, n ) -> { 
         getChildren().forEach( c -> ( ( ContentArea )c ).setParentDimensions( getWidth(), getHeight() ) ); 
      }; 
      
      widthProperty().addListener( contentRescaler );
      heightProperty().addListener( contentRescaler );
   }//End Constructor
   
   /**
    * Method to verify that the {@link ContentArea} is present in the {@link Pane}.
    * @param area the {@link ContentArea} in question.
    */
   private void verifyPresence( ContentArea area ) {
      if ( !getChildren().contains( area ) ) {
         throw new IllegalArgumentException( "ContentArea not present in WallBuilder." );
      }
   }//End Method

   /**
    * Method to split the given {@link ContentArea} vertically, resizing the given to be on top and adding a
    * new {@link ContentArea} for the bottom.
    * @param area the given {@link ContentArea} to split.
    */
   void splitVertically( ContentArea area ) {
      verifyPresence( area );
      
      double percentageSplit = area.percentageHeight() / 2;
      area.changeHeightPercentageBy( -percentageSplit );
      
      ContentArea newArea = new ContentArea( 
               getWidth(), getHeight(), 
               area.xPositionPercentage(), area.yPositionPercentage() + percentageSplit, 
               area.percentageWidth(), area.percentageHeight() 
      );
      
      addChild( newArea );
   }//End Method

   /**
    * Method to split the given {@link ContentArea} horizontally, resizing the given to be on the left and adding a
    * new {@link ContentArea} for the right.
    * @param area the given {@link ContentArea} to split.
    */
   void splitHorizontally( ContentArea area ) {
      verifyPresence( area );
      
      double percentageSplit = area.percentageWidth() / 2;
      area.changeWidthPercentageBy( -percentageSplit );
      
      ContentArea newArea = new ContentArea( 
               getWidth(), getHeight(), 
               area.xPositionPercentage() + percentageSplit, area.yPositionPercentage(), 
               area.percentageWidth(), area.percentageHeight() 
      );
      
      addChild( newArea );
   }//End Method
   
   /**
    * Method to add the {@link ContentArea} as a child, since it needs to be on the JavaFx thread.
    * @param area the {@link ContentArea} to add.
    */
   private void addChild( ContentArea area ) {
      PlatformImpl.runAndWait( () -> getChildren().add( area ) );      
   }//End Method
   
   /**
    * Method to stretch the current selection to the left by the given percentage of the width.
    * @param percentage the percentage to change by.
    */
   void stretchLeft( double percentage ) {
      ContentArea selection = selector.getSelection();
      if ( selection == null ) {
         return;
      }
      
      selection.changeXPositionPercentageBy( -percentage );
      intersections.checkTranslationXIntersection( selection );
   }//End Method
   
   /**
    * Method to stretch the current selection to the right by the given percentage of the width.
    * @param percentage the percentage to change by.
    */
   void stretchRight( double percentage ) {
      ContentArea selection = selector.getSelection();
      if ( selection == null ) {
         return;
      }
      
      selection.changeWidthPercentageBy( percentage );
      intersections.checkWidthIntersection( selection );
   }//End Method
   
   /**
    * Method to stretch the current selection up by the given percentage of the height.
    * @param percentage the percentage to change by.
    */
   void stretchUp( double percentage ) {
      ContentArea selection = selector.getSelection();
      if ( selection == null ) {
         return;
      }
      
      selection.changeYPositionPercentageBy( -percentage );
      intersections.checkTranslationYIntersection( selection );
   }//End Method
   
   /**
    * Method to stretch the current selection down by the given percentage of the height.
    * @param percentage the percentage to change by.
    */
   void stretchDown( double percentage ) {
      ContentArea selection = selector.getSelection();
      if ( selection == null ) {
         return;
      }
      
      selection.changeHeightPercentageBy( percentage );
      intersections.checkHeightIntersection( selection );
   }//End Method

   ContentAreaSelector selectionController() {
      return selector;
   }//End Method

}//End Class
