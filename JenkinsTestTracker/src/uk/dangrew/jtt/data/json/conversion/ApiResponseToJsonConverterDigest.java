/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package uk.dangrew.jtt.data.json.conversion;

import core.category.Categories;
import core.message.Messages;
import core.source.SourceImpl;
import digest.object.ObjectDigestImpl;

/**
 * The {@link ApiResponseToJsonConverterDigest} is responsible for providing system digest messages
 * based on the input it converts.
 */
public class ApiResponseToJsonConverterDigest extends ObjectDigestImpl {
   
   static final String NAME = "Api Response Converter";
   static final String NULL_RESPONSE = "Null response from Jenkins Api";
   static final String INVALID_RESPONSE = "Invalid response from Jenkins Api";

   /**
    * Method to attach the given {@link ApiResponseToJsonConverter} as {@link uk.dangrew.jtt.core.source.Source} to
    * this {@link ObjectDigestImpl}.
    * @param converter the {@link ApiResponseToJsonConverter}.
    */
   void attachSource( ApiResponseToJsonConverter converter ) {
      super.attachSource( new SourceImpl( converter, NAME ) );
   }//End Method

   /**
    * Method to report to the system digest when a null response has been attempted to convert.
    */
   void reportNullApiResponse() {
      log( Categories.error(), Messages.simpleMessage( NULL_RESPONSE ) );
   }//End Method

   /**
    * Method to report to the system digest when an invalid response has been attempted to convert.
    */
   void reportInvalidApiResponse() {
      log( Categories.error(), Messages.simpleMessage( INVALID_RESPONSE ) );
   }//End Method

}//End Class
