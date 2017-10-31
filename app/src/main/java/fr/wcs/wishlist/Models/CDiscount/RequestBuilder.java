package fr.wcs.wishlist.Models.CDiscount;

import org.json.JSONObject;

/**
 * Created by Philippe-Adrien on 31/10/2017.
 */

public class RequestBuilder {
    public enum Sort{
        RELEVANCE("RELEVANCE"), MINPRICE("MINPRICE"), MAXPRICE("MAXPRICE"), RATING("RATING");

        private final String name;

        private Sort(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    private String keyWord;
    private Sort sort;
    private int itemPerPage;
    private int page;

    public RequestBuilder(String keyWord, Sort sort, int itemPerPage, int page) {
        this.keyWord = keyWord;
        this.sort = sort;
        this.itemPerPage = itemPerPage;
    }

    public String build(){
        String jsonRequest = "";
        try {
            jsonRequest = new JSONObject()
                .put("ApiKey", "b2f6b2dd-b767-4db9-a5cf-046efab420ae")
                .put("SearchRequest", new JSONObject()
                    .put("Keyword", keyWord)
                    .put("SortBy", sort.toString())
                    .put("Pagination", new JSONObject()
                        .put("ItemsPerPage", itemPerPage))
                        .put("PageNumber", page))
                    .put("Filters", new JSONObject()
                        .put("Pice", new JSONObject()
                            .put("Min", 0)
                            .put("Max", 0))
                        .put("Navigation", "")
                        .put("IncludeMarkerPlace", false)
                    .put("Condition", null))
                .toString();
        }
        catch (Exception e) {}

        return jsonRequest;
    }

    /*
    "ApiKey": "b2f6b2dd-b767-4db9-a5cf-046efab420ae",
            "SearchRequest": {
                "Keyword": "tablette",
                "SortBy": "relevance",
                "Pagination": {
                    "ItemsPerPage": 1,
                    "PageNumber": 0
                 },
                "Filters": {
                    "Price": {
                        "Min": 0,
                        "Max": 0
                     },
                "Navigation": "",
                "IncludeMarketPlace": false,
                "Condition": null
        }
    }
    */
}
