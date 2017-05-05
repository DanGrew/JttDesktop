/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.data.json.conversion;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The {@link ApiResponseToJsonConverter} is responsible for converting {@link String} responses
 * from the {@link uk.dangrew.jtt.api.sources.JenkinsApiImpl} into a {@link JSONObject}.
 */
public class ApiResponseToJsonConverter {

   private ApiResponseToJsonConverterDigest digest;
   
   /**
    * Constructs a new {@link ApiResponseToJsonConverter}.
    */
   public ApiResponseToJsonConverter() {
      this( new ApiResponseToJsonConverterDigest() );
   }//End Constructor
   
   /**
    * Constructs a new {@link ApiResponseToJsonConverter}.
    * @param digest the {@link ApiResponseToJsonConverterDigest}.
    */
   ApiResponseToJsonConverter( ApiResponseToJsonConverterDigest digest ) {
      this.digest = digest;
      this.digest.attachSource( this );
   }//End Constructor

   /**
    * Method to convert the {@link String} input into a {@link JSONObject}. The input is expected 
    * to be full and valid and will be reported if not.
    * @param input the input to convert.
    * @return the converted {@link JSONObject}, or null if cannot convert.
    */
   public JSONObject convert( String input ) {
      if ( input == null ) {
         digest.reportNullApiResponse();
         return null;
      }
      
      try {
         return new JSONObject( input );
      } catch ( JSONException exception ) {
         digest.reportInvalidApiResponse();
         return null;
      }
   }//End Method

   /**
    * Method to convert the {@link String} input into a {@link JSONObject}. The input is optional 
    * and will not be reported if null, however an invalid input will be reported.
    * @param input the input to convert.
    * @return the converted {@link JSONObject}, or null if cannot convert.
    */
   public JSONObject optionalConvert( String input ) {
      if ( input == null ) {
         return null;
      }
      
      try {
         return new JSONObject( input );
      } catch ( JSONException exception ) {
         return null;
      }
   }//End Method
   
}//End Class
