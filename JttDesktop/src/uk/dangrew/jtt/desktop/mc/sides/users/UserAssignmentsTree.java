/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.desktop.mc.sides.users;

import javafx.scene.control.SelectionMode;
import uk.dangrew.jtt.desktop.friendly.javafx.FriendlyMenuOpener;
import uk.dangrew.jtt.desktop.javafx.tree.structure.Tree;
import uk.dangrew.jtt.model.storage.database.JenkinsDatabase;

/**
 * The {@link UserAssignmentsTree} is responsible for holding {@link UserAssignment}s.
 */
public class UserAssignmentsTree extends Tree< 
      UserAssignmentsTreeItem, 
      UserAssignment, 
      UserAssignmentsTreeLayout, 
      UserAssignmentsTreeController 
> {

   private final JenkinsDatabase database;
   private final UserAssignmentsTreeContextMenu menu;
   
   /**
    * Constructs a new {@link UserAssignmentsTree}.
    * @param database the {@link JenkinsDatabase}.
    */
   public UserAssignmentsTree( JenkinsDatabase database ) {
      super();
      this.database = database;
      super.initialise();
      
      getSelectionModel().setSelectionMode( SelectionMode.MULTIPLE );
      menu = new UserAssignmentsTreeContextMenu( this );
      new FriendlyMenuOpener( this, menu );
   }//End Constructor
   
   /**
    * {@inheritDoc}
    */
   @Override protected void insertColumns() {
      insertColumn( item -> item.firstColumnProperty() );
      insertColumn( item -> item.secondColumnProperty() );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected UserAssignmentsTreeLayout constructLayout() {
      return new UserAssignmentsTreeLayout( this );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected UserAssignmentsTreeController constructController() {
      return new UserAssignmentsTreeController( getLayoutManager(), database );
   }//End Method

   /**
    * {@inheritDoc}
    */
   @Override protected void performInitialLayout() {
      getLayoutManager().reconstructBranches( database.jenkinsUsers() );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected UserAssignmentsTreeLayout getLayoutManager() {
      return super.getLayoutManager();
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected UserAssignmentsTreeController getController() {
      return super.getController();
   }//End Method
   
   UserAssignmentsTreeContextMenu menu(){
      return menu;
   }//End Method

}//End Class
