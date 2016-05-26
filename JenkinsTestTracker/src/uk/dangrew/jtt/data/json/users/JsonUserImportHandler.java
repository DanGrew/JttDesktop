/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.data.json.users;

import uk.dangrew.jtt.model.users.JenkinsUser;
import uk.dangrew.jtt.model.users.JenkinsUserImpl;
import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * {@link JsonUserImportHandler} is responsible for handling the imported data by the {@link JsonUserImporter},
 * translating it to changes in the {@link JenkinsDatabase}.
 */
public class JsonUserImportHandler {
   
   private final JenkinsDatabase database;
   
   /**
    * Constructs a new {@link JsonUserImportHandler}.
    * @param database the {@link JenkinsDatabase}.
    */
   JsonUserImportHandler( JenkinsDatabase database ) {
      this.database = database;
   }//End Constructor
   
   /**
    * Method to handle the finding of a user in the users import. This will create a new {@link JenkinsUser}
    * in the {@link JenkinsDatabase} if one does not already exist.
    * @param userName the name of the user.
    */
   void userFound( String userName ){
      if ( userName == null || userName.trim().length() == 0 ) {
         return;
      }
      
      if ( database.hasJenkinsUser( userName ) ) {
         return;
      }
      
      JenkinsUser jenkinsUser = new JenkinsUserImpl( userName );
      database.store( jenkinsUser );
   }//End Method

}//End Class
