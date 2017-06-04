/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.wallbuilder;

import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import uk.dangrew.jtt.model.utility.observable.FunctionListChangeListenerImpl;

/**
 * {@link ContentAreaSelector} is responsible for managing the selection of {@link ContentArea}s
 * in the {@link WallBuilder}.
 */
class ContentAreaSelector {
   
   /**
    * {@link ClickHandler} provides an {@link EventHandler} associated with a {@link ContentArea}.
    */
   class ClickHandler implements EventHandler< MouseEvent > {

      private final ContentArea source;
      
      /**
       * Constructs a new {@link ClickHandler}.
       * @param source the {@link ContentArea} associated.
       */
      public ClickHandler( ContentArea source ) {
         this.source = source;
      }//End Constructor
      
      /**
       * {@inheritDoc}
       */
      @Override public void handle( MouseEvent event ) {
         contentSelected( source );
      }//End Method
      
   }//End Class
   
   private final ContentAreaColours colours;
   private ContentArea selection;
   
   /**
    * Constructs a new {@link ContentAreaSelector}.
    * @param areasthe {@link Node}s in the scene that can be selected.
    */
   ContentAreaSelector() {
      this( new ContentAreaColours() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ContentAreaSelector}.
    * @param colours the {@link ContentAreaColours} for highlighting.
    */
   ContentAreaSelector( ContentAreaColours colours ) {
      this.colours = colours;
   }//End Constructor

   /**
    * Method to set the {@link Node}s being observed.
    * @param areas the {@link Node}s in the scene that can be selected.
    */
   void setNodes( ObservableList< Node > areas ) {
      areas.forEach( this::contentAdded );
      areas.addListener( new FunctionListChangeListenerImpl<>( 
               this::contentAdded, this::contentRemoved 
      ) );
   }//End Method
   
   /**
    * Method to extract the {@link ContentArea} from a {@link Node}.
    * @param node the {@link Node} that could be a {@link ContentArea}.
    * @return {@link Optional} of {@link ContentArea}.
    */
   private Optional< ContentArea > extractContentArea( Node node ) {
      if ( !( node instanceof ContentArea ) ) {
         return Optional.empty();
      }
      
      ContentArea area = ( ContentArea )node;
      return Optional.of( area );
   }//End Method
   
   /**
    * Method to handle the selection of a {@link Node}.
    * @param node the {@link Node} selected.
    */
   private void contentSelected( Node node ) {
      Optional< ContentArea > area = extractContentArea( node );
      if ( !area.isPresent() ) {
         return;
      }
      
      if ( selection != null ) {
         colours.applyUnselectedColours( selection );
      }
      
      if ( selection == area.get() ) {
         selection = null;
      } else {
         selection = area.get();
         selection.toFront();
         colours.applySelectedColours( selection );
      }
   }//End Method

   /**
    * Method to handle the adding of a {@link Node}.
    * @param node the {@link Node} added.
    */
   private void contentAdded( Node node ){
      Optional< ContentArea > area = extractContentArea( node );
      if ( !area.isPresent() ) {
         return;
      }
      
      colours.applyUnselectedColours( area.get() );
      area.get().setOnMouseClicked( new ClickHandler( area.get() ) );
   }//End Method
   
   /**
    * Method to handle the removal of a {@link Node}.
    * @param node the {@link Node} removed.
    */
   private void contentRemoved( Node node ){
      Optional< ContentArea > area = extractContentArea( node );
      if ( !area.isPresent() ) {
         return;
      }
      area.get().setOnMouseClicked( null );
   }//End Method
   
   /**
    * Getter for the current selection.
    * @return the {@link ContentArea}.
    */
   public ContentArea getSelection() {
      return selection;
   }//End Method

}//End Class
