/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.data.json.users;

import org.json.JSONObject;

/**
 * The {@link JsonUserImporter} provides an interface to an importer of user data in the JSON
 * format.
 */
public interface JsonUserImporter {
   
   /**
    * Method to import the users from a {@link JSONObject} into the given {@link JenkinsDatabase}.
    * @param response the response from the {@link ExternalApi}.
    */
   public void importUsers( JSONObject response );//End Method

}//End Interface
