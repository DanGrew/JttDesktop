/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.buildwall.dual;

import javafx.scene.Node;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Pair;
import viewer.basic.DigestViewer;

/**
 * This provides a mechanism for identifying the system digest
 * if present in parent, allowing it to be controlled from the child.
 */
class WrappedSystemDigest {

   private final BorderPane parentWrapper;
   private final Node extractedDigest;
   
   /**
    * Constructs a new {@link WrappedSystemDigest}.
    * @param parent the {@link BorderPane} display to check for system digest.
    */
   WrappedSystemDigest( Node parent ) {
      Pair< BorderPane, Node > extraction = extract( parent );
      this.parentWrapper = extraction.getKey();
      this.extractedDigest = extraction.getValue();
   }//End Constructor
   
   /**
    * Method to extract the components needed from the parent if possible.
    * @param parent the {@link BorderPane} parent to extract from.
    * @return the {@link Pair} of {@link BorderPane} parent to the system digest {@link Node}.
    * Note that this can and does return null if the digest cannot be found.
    */
   private Pair< BorderPane, Node > extract( Node parent ){
      if ( parent == null || !( parent instanceof BorderPane ) ) {
         return new Pair<>( null, null );
      }
      
      BorderPane extractedParent = ( BorderPane ) parent;
      Node top = extractedParent.getTop();
      if ( top == null ) {
         return new Pair<>( null, null );
      }
      
      if ( top instanceof DigestViewer ) {
         return new Pair<>( extractedParent, top );
      }
      
      if ( !( top instanceof TitledPane ) ) {
         return new Pair<>( null, null );
      }
      
      TitledPane extractedPane = ( TitledPane ) top;
      Node content = extractedPane.getContent();
      if ( content == null || !( content instanceof DigestViewer ) ) {
         return new Pair<>( null, null );
      }
      
      return new Pair<>( extractedParent, extractedPane );
   }//End Method
   
   /**
    * Method to determine if the system digest has been found.
    * @return true if digest found.
    */
   boolean isSystemDigestAvailable(){
      return extractedDigest != null;
   }//End Method

   /**
    * Method to remove the digest from the parent.
    */
   void removeDigest() {
      parentWrapper.setTop( null );
   }//End Method

   /**
    * Method to insert the digest back into the parent.
    */
   void insertDigest() {
      parentWrapper.setTop( extractedDigest );
   }//End Method
   
}//End Class
