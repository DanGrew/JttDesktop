/*
 * ----------------------------------------
 *          Jenkins Test Tracker
 * ----------------------------------------
 *          Produced by Dan Grew
 *                 2016
 * ----------------------------------------
 */
package api.sources;

import core.category.Categories;
import core.message.Messages;
import core.source.SourceImpl;
import digest.object.ObjectDigestImpl;

/**
 * The {@link JenkinsApiDigest} provides an {@link ObjectDigestImpl} specifically for the {@link JenkinsApiImpl}.
 */
public class JenkinsApiDigest extends ObjectDigestImpl {

   static final String JENKINS_API_CONNECTION = "Jenkins Api Connection";
   static final String EXECUTING_BASE_LOGIN_REQUEST = "Executing base login request...";
   static final String CLIENT_FAILED_TO_CONNECT = "Client failed to connect.";
   static final String CLIENT_CONNECTED = "Client connected.";
   static final String HANDLING_RESPONSE_FOR_REQUEST = "Handling response for request.";
   static final String RESPONSE_READY = "Response ready.";

   /**
    * Method to attach the {@link JenkinsApiImpl} as the source.
    * @param source the {@link JenkinsApiImpl}.
    */
   void attachSource( JenkinsApiImpl source ) {
      super.attachSource( new SourceImpl( this, JENKINS_API_CONNECTION ) );
   }//End Method

   /**
    * Method to handle the execution of the login request.
    */
   void executingLoginRequest() {
      log( Categories.processingSequence(), Messages.simpleMessage( EXECUTING_BASE_LOGIN_REQUEST ) );
   }//End Method

   /**
    * Method to handle connection failure.
    */
   void connectionFailed() {
      log( Categories.error(), Messages.simpleMessage( CLIENT_FAILED_TO_CONNECT ) );
   }//End Method

   /**
    * Method to handle connection success.
    */
   void connectionSuccess() {
      log( Categories.status(), Messages.simpleMessage( CLIENT_CONNECTED ) );
   }//End Method

   /**
    * Method to handle an {@link Exception} raised during the connection.
    * @param exception the {@link Exception} to use in the {@link Message}.
    */
   void connectionException( Exception exception ) {
      log( Categories.error(), Messages.simpleMessage( exception.getMessage() ) );
   }//End Method

   /**
    * Method to handle the handling of the response.
    */
   void handlingResponse() {
      log( Categories.processingSequence(), Messages.simpleMessage( HANDLING_RESPONSE_FOR_REQUEST ) );
   }//End Method

   /**
    * Method to handle the response being ready.
    */
   void responseReady() {
      log( Categories.processingSequence(), Messages.simpleMessage( RESPONSE_READY ) );
   }//End Method
}//End Class
