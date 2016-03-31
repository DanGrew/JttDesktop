/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package data.json.users;

import api.sources.ExternalApi;
import storage.database.JenkinsDatabase;

/**
 * The {@link JsonUserImporter} provides an interface to an importer of user data in the JSON
 * format.
 */
public interface JsonUserImporter {
   
   /**
    * Method to import the users from a json {@link String} into the given {@link JenkinsDatabase}.
    * @param response the response from the {@link ExternalApi}.
    */
   public void importUsers( String response );//End Method

}//End Interface
