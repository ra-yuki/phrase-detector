package rmafia.phraseditector.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
* [Dependency Requirement]
* == external libraries ==
* 1. jackson (json parser)
*   1-1. gradle
*     compile('com.fasterxml.jackson.core:jackson-core:2.9.6')
*     compile('com.fasterxml.jackson.core:jackson-annotations:2.9.6')
*     compile('com.fasterxml.jackson.core:jackson-databind:2.9.6')
*
**/
public class JsonParser {
    public static JsonNode string2JsonNode(String subject){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(subject);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
