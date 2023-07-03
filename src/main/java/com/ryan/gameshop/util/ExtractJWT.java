package com.ryan.gameshop.util;


import com.ryan.gameshop.exception.UserEmailMissingException;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class ExtractJWT {

   private static String payloadJWTExtraction(String token, String property) {
      token.replace("Bearer ", "");
      // "\\." matches a literal dot character.
      String[] splitToken = token.split("\\.");
      Base64.Decoder decoder = Base64.getUrlDecoder();

      String payload = new String(decoder.decode(splitToken[1]));

      String[] entries = payload.split(",");
      Map<String, String> map = new HashMap<>();

      for(String entry: entries) {
         String[] keyValue = entry.split(":");

         if(keyValue[0].equals(property)) {
            int remove = 1;
            if(keyValue[1].endsWith("}")) {
               remove = 2;
            }
            keyValue[1] = keyValue[1].substring(0, keyValue[1].length() - remove);
            keyValue[1] = keyValue[1].substring(1);

            map.put(keyValue[0], keyValue[1]);
         }
      }

      return map.getOrDefault(property, null);
   }

   public static String extractUserEmail(String token) throws UserEmailMissingException{
      String userEmail = ExtractJWT.payloadJWTExtraction(token,"\"sub\"");
      if(userEmail == null) {
         throw new UserEmailMissingException("User Email Does Not Exist.");
      }
      return userEmail;
   }

   public static Boolean isAdmin(String token) throws UserEmailMissingException{
      String isAdmin = ExtractJWT.payloadJWTExtraction(token,"\"userType\"");
      if(isAdmin == null || !isAdmin.equals("admin")) {
         return false;
      }
      return true;
   }
}
