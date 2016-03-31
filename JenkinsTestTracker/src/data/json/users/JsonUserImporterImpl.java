/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.users;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import api.sources.ExternalApi;
import model.jobs.JenkinsJob;
import model.users.JenkinsUser;
import model.users.JenkinsUserImpl;
import storage.database.JenkinsDatabase;

/**
 * The {@link JsonUserImporterImpl} provides a method of converting json responses for user data 
 * from an {@link ExternalApi} into {@link JenkinsJob} updates.
 */
public class JsonUserImporterImpl implements JsonUserImporter {
   
   private static final String USERS_KEY = "users";
   private static final String USER_KEY = "user";
   private static final String FULL_NAME_KEY = "fullName";

   private JenkinsDatabase database;
   
   /**
    * Constructs a new {@link JsonUserImporterImpl}.
    * @param database the {@link JenkinsDatabase} to import to.
    */
   public JsonUserImporterImpl( JenkinsDatabase database ) {
      this.database = database;
   }//End Constructor

   /**
    * Method to import the users from a json {@link String} into the given {@link JenkinsDatabase}.
    * @param response the response from the {@link ExternalApi}.
    */
   @Override public void importUsers( String response ) {
      if ( response == null || database == null ) return;
      try {
         JSONObject object = new JSONObject( response );
         
         if ( !object.has( USERS_KEY ) ) return;
         
         JSONArray jobs = object.getJSONArray( USERS_KEY );
         for ( int i = 0; i < jobs.length(); i++ ) {
            try { //Protective wrapper for parse in constructor.
               JSONObject user = jobs.getJSONObject( i );
               if ( !user.has( USER_KEY ) ) continue;
               
               JSONObject job = user.getJSONObject( USER_KEY );
               if ( !job.has( FULL_NAME_KEY ) ) continue;
               
               String name = job.getString( FULL_NAME_KEY );
               if ( name.trim().length() == 0 ) continue;
               
               if ( database.hasJenkinsUser( name ) ) {
                  continue;
               }
               
               JenkinsUser jenkinsUser = new JenkinsUserImpl( name );
               database.store( jenkinsUser );
            } catch ( JSONException exception ) {
               System.out.println( exception.getMessage() );
               continue;
            }
         }
      } catch ( JSONException exception ) {
         System.out.println( exception.getMessage() );
         return;
      }
   }//End Method

}//End Class
