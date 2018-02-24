package search;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import java.net.*;
import java.util.*;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import java.util.List;

public class BingImageSearch implements ImageSearch {

  private final static String host = "https://api.cognitive.microsoft.com";
  private final static String path = "/bing/v7.0/images/search";
  private final static int imageIndex = 0;

  private final String subscriptionKey;
  private final String colour;
  private final String searchTerm;

  public BingImageSearch(String subscriptionKey, String colour,
      String searchTerm) {
    this.subscriptionKey = subscriptionKey;
    this.colour = colour;
    this.searchTerm = searchTerm;
  }

  @Override
  public String getImageUrl() {
    SearchResults result = null;
    try {
      result = searchImages(searchTerm);
    } catch (Exception e) {
      e.printStackTrace();
    }
    JsonParser parser = new JsonParser();
    JsonObject json = parser.parse(result.jsonResponse).getAsJsonObject();
    JsonArray images = json.getAsJsonArray("value");
    String imageUrl = images.get(imageIndex).getAsJsonObject()
        .getAsJsonPrimitive
        ("contentUrl").getAsString();

    return imageUrl;
  }

  public SearchResults searchImages (String searchQuery) throws Exception {
    // construct URL of search request (endpoint + query string)
    URL url = new URL(host + path + "?&q=" + URLEncoder.encode(searchQuery,
        "UTF-8") + "&color=" + colour);
    HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
    connection.setRequestProperty("Ocp-Apim-Subscription-Key", subscriptionKey);

    // receive JSON body
    InputStream stream = connection.getInputStream();
    String response = new Scanner(stream).useDelimiter("\\A").next();

    // construct result object for return
    SearchResults results = new SearchResults(new HashMap<String, String>(), response);

    // extract Bing-related HTTP headers
    Map<String, List<String>> headers = connection.getHeaderFields();
    for (String header : headers.keySet()) {
      if (header == null) continue;      // may have null key
      if (header.startsWith("BingAPIs-") || header.startsWith("X-MSEdge-")) {
        results.relevantHeaders.put(header, headers.get(header).get(0));
      }
    }

    stream.close();
    return results;
  }

  // Container class for search results encapsulates relevant headers and JSON data
  private class SearchResults {
    HashMap<String, String> relevantHeaders;
    String jsonResponse;
    SearchResults(HashMap<String, String> headers, String json) {
      relevantHeaders = headers;
      jsonResponse = json;
    }
  }
}
