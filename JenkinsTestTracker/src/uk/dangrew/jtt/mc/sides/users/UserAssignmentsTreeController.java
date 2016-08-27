/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.users;

import uk.dangrew.jtt.javafx.tree.structure.TreeController;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.utility.observable.FunctionListChangeListenerImpl;

/**
 * The {@link UserAssignmentsTreeController} is responsible for controlling actions performed on
 * the {@link UserAssignmentsTree}.
 */
public class UserAssignmentsTreeController extends TreeController< UserAssignmentsTreeItem, UserAssignment, UserAssignmentsTreeLayout > {

   /**
    * Constructs a new {@link UserAssignmentsTreeController}.
    * @param layout the {@link UserAssignmentsTreeLayout}.
    * @param database the {@link JenkinsDatabase} to monitor.
    */
   public UserAssignmentsTreeController( UserAssignmentsTreeLayout layout, JenkinsDatabase database ) {
      super( layout );
      
      database.jenkinsUsers().addListener( new FunctionListChangeListenerImpl<>( 
               this::add, 
               this::remove
      ) );
      database.jenkinsUserProperties().addNamePropertyListener( ( source, old, updated ) -> update( source ) );
      
      new UserAssignmentEvent().register( event -> add( event.getValue() ) );
   }//End Constructor
   
   /**
    * Method to add a {@link JenkinsUser} to the {@link UserAssignmentsTree}.
    * @param user the {@link JenkinsUser} to add.
    */
   void add( JenkinsUser user ) {
      getLayoutManager().addBranch( user );
   }//End Method
   
   /**
    * Method to remove a {@link JenkinsUser} from the {@link UserAssignmentsTree}.
    * @param user the {@link JenkinsUser} to remove.
    */
   void remove( JenkinsUser user ) {
      getLayoutManager().removeBranch( user );
   }//End Method
   
   /**
    * Method to update a {@link JenkinsUser} in the {@link UserAssignmentsTree}.
    * @param user the {@link JenkinsUser} to update.
    */
   void update( JenkinsUser user ) {
      getLayoutManager().updateBranch( user );
   }//End Method
   
   /**
    * {@inheritDoc}
    */
   @Override protected UserAssignmentsTreeLayout getLayoutManager() {
      return super.getLayoutManager();
   }//End Method

}//End Class
