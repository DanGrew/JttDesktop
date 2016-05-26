/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.data.json.users;

import org.json.JSONArray;
import org.json.JSONObject;

import uk.dangrew.jtt.storage.database.JenkinsDatabase;

/**
 * The {@link JsonUserImporterImpl} provides a method of converting json responses for user data 
 * from an {@link ExternalApi} into {@link JenkinsJob} updates.
 */
public class JsonUserImporterImpl implements JsonUserImporter {
   
   static final String USERS_KEY = "users";
   static final String USER_KEY = "user";
   static final String FULL_NAME_KEY = "fullName";

   private final JsonUserImportHandler handler;
   
   /**
    * Constructs a new {@link JsonUserImporterImpl}.
    * @param database the {@link JenkinsDatabase} to import to.
    */
   public JsonUserImporterImpl( JenkinsDatabase database ) {
      this( new JsonUserImportHandler( database ) );
   }//End Constructor
   
   /**
    * Constructs a new {@link JsonUserImporterImpl}.
    * @param handler the {@link JsonUserImportHandler} to handle import data.
    */
   JsonUserImporterImpl( JsonUserImportHandler handler ) {
      this.handler = handler;
   }//End Constructor

   /**
    * {@inheritDoc}
    */
   @Override public void importUsers( JSONObject response ) {
      if ( response == null ) return;
      if ( !response.has( USERS_KEY ) ) return;
      
      JSONArray jobs = response.optJSONArray( USERS_KEY );
      if ( jobs == null ) return;
      
      for ( int i = 0; i < jobs.length(); i++ ) {
         JSONObject user = jobs.optJSONObject( i );
         if ( user == null ) continue;
         if ( !user.has( USER_KEY ) ) continue;
         
         JSONObject job = user.optJSONObject( USER_KEY );
         if ( job == null ) continue;
         if ( !job.has( FULL_NAME_KEY ) ) continue;
         
         String name = job.optString( FULL_NAME_KEY );
         handler.userFound( name );
      }
   }//End Method

}//End Class
