package rmafia.phraseditector.helpers.apiHandlers;

import rmafia.phraseditector.helpers.MyFileUtil;

import java.util.HashMap;
import java.util.Map;

public class PurgoMalumAPIHandler implements APIHandler<String, String> {
    private final String BASE_URL = "https://www.purgomalum.com/service/";
    private String query;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    private Map<String, String> params;

    public PurgoMalumAPIHandler(){
        initRequestQuery();
    }

    @Override
    public void initRequestQuery() {
        query = BASE_URL;
        params = new HashMap<String, String>();
    }

    @Override
    public String executeGetRequest() {
        String sParams = "?";
        for(Map.Entry e : params.entrySet()){
            String key = (String)e.getKey();
            String value = (String)e.getValue();
            sParams += (sParams == "?") ? key+"="+value : "&" + key+"="+value;
        }

        System.out.println("executed: " + query+sParams);

        String response = MyFileUtil.fileGetContents(query+sParams).toString();

        return response;
    }

    @Override
    public String addParam(String key, String value) {
        value = value
                .replaceAll(" ", "%20")
                .replaceAll("\\|", "%7C"); //for | (vertical bar)

        params.put(key, value);
        return params.get(key);
    }

    @Override @Deprecated
    public Map<String, String> addParams(HashMap<String, String> kayValuePair) {
        return null;
    }

    @Override
    public String appendToQuery(String q) {
        query += q;
        return query;
    }
}
