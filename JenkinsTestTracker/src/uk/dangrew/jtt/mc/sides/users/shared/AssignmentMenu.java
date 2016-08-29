/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.mc.sides.users.shared;

import java.util.function.Consumer;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;
import uk.dangrew.jtt.utility.observable.FunctionListChangeListenerImpl;

/**
 * The {@link AssignmentMenu} provides a common {@link Menu} for selecting a {@link JenkinsUser}
 * from the {@link JenkinsUser}s provided in the {@link JenkinsDatabase}.
 */
public class AssignmentMenu extends Menu {
   
   static final String ASSIGN = "Assign";
   
   private final JenkinsDatabase database;
   private final Consumer< JenkinsUser > notificationHandler;
   
   /**
    * Constructs a new {@link AssignmentMenu}.
    * @param database the {@link JenkinsDatabase} for monitoring {@link JenkinsUser}s.
    * @param noticationHandler the {@link Consumer} to handle the selection of a {@link JenkinsUser}.
    */
   public AssignmentMenu( 
            JenkinsDatabase database, 
            Consumer< JenkinsUser > noticationHandler 
   ) {
      super( ASSIGN );
      if ( database == null || noticationHandler == null ) {
         throw new IllegalArgumentException( "Must not supply null arguments." );
      }
      
      this.database = database;
      this.notificationHandler = noticationHandler;
      
      resetMenus();
      database.jenkinsUsers().addListener( new FunctionListChangeListenerImpl<>( 
               change -> resetMenus(), 
               change -> resetMenus() 
      ) );
   }//End Constructor
   
   /**
    * Method to reset the {@link Menu}s, clearing out the old and replacing with new.
    */
   private void resetMenus(){
      getItems().clear();
      
      database.jenkinsUsers().forEach( user -> {
         MenuItem menu = new MenuItem( user.nameProperty().get() );
         menu.setOnAction( event -> notificationHandler.accept( user ) );
         getItems().add( menu );
      } );
   }//End Method

}//End Class
