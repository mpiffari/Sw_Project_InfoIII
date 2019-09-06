package com.bc.bookcrossing.src.Controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.bc.bookcrossing.src.View.Fragment.ISBNScanFragment;
import com.bc.bookcrossing.src.UnitTest.Book;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public class FetchBook extends AsyncTask<String,Void,String> {

    // Class name for Log tag
    private static final String LOG_TAG = FetchBook.class.getSimpleName();
    private static Book sendBook;
    private String queryString;

    @Override
    protected String doInBackground(String... params) {

        // Get the search string
        queryString = params[0];


        // Set up variables for the try block that need to be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String bookJSONString = null;

        // Attempt to query the Books API.
        try {
            /*
            // Base URI for the Books API.
            final String BOOK_BASE_URL =  "https://www.googleapis.com/books/v1/volumes?";

            final String QUERY_PARAM = "q"; // Parameter for the search string.
            final String MAX_RESULTS = "maxResults"; // Parameter that limits search results.
            final String PRINT_TYPE = "printType"; // Parameter to filter by print type.

            // Build up your query URI, limiting results to 10 items and printed books.
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(MAX_RESULTS, "10")
                    .appendQueryParameter(PRINT_TYPE, "books")
                    .build();
            */

            URL requestURL = new URL("https://www.googleapis.com/books/v1/volumes?q=" + queryString + ":isbn&key=AIzaSyAi6ffwa0bpwcqM4t5vn1at47kqY_s9kIc");

            // Open the network connection.
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Read the response string into a StringBuilder.
            StringBuilder builder = new StringBuilder();

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // but it does make debugging a *lot* easier if you print out the completed buffer for debugging.
                builder.append(line + "\n");
            }

            if (builder.length() == 0) {
                // Stream was empty.  No point in parsing.
                // return null;
                return null;
            }
            bookJSONString = builder.toString();

            // Catch errors.
        } catch (IOException e) {
            e.printStackTrace();

            // Close the connections.
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

        // Return the raw response.
        return bookJSONString;
    }

    /**
     * Handles the results on the UI thread. Gets the information from
     * the JSON and updates the Views.
     *
     * @param s Result from the doInBackground method containing the raw JSON response,
     *          or null if it failed.
     */



    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        sendBook(s);
    }


    private Book sendBook(String s) {
        try {
            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(s);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

            String title = null;
            String authors = "";
            String yearOfPubblication = null;
            String type = "";

            // Look for results in the items array, exiting when both the title and author
            // are found or when all items have been checked.
            if (itemsArray.length() > 0 || (authors == "" && title == null && yearOfPubblication == null && type == "")) {
                // Get the current item information.
                JSONObject book = itemsArray.getJSONObject(0);
                JSONObject volumeInfo = book.getJSONObject("volumeInfo");

                // Try to get the author and title from the current item,
                // catch if either field is empty and move on.
                try {
                    title = volumeInfo.getString("title");

                    for (int i = 0; i < volumeInfo.getJSONArray("authors").length(); i++) {
                        authors += volumeInfo.getJSONArray("authors").getString(i) + " ";
                    }

                    yearOfPubblication = volumeInfo.getString("publishedDate");
                    for (int i = 0; i < volumeInfo.getJSONArray("categories").length(); i++) {
                        type += volumeInfo.getJSONArray("categories").getString(i) + " ";
                    }

                    Log.d("Result", title + " ; " + authors + " ; " + yearOfPubblication);

                    int indexOf = yearOfPubblication.indexOf("-");
                    int year = 0;

                    if(indexOf != -1) {
                        year = Integer.parseInt(yearOfPubblication.substring(0, indexOf));
                    }
                    else{
                        year = Integer.parseInt(yearOfPubblication);
                    }

                    Log.d("year", "" + year);
                    Log.d("link" , volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail"));

                    sendBook = new Book(title, authors, year, 1, type, queryString);
                    ISBNScanFragment.setScannedBook(sendBook);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Move to the next item.
            }

            // If both are found, display the result.
            if (title != null && authors != "") {

            } else {
                // If none are found, update the UI to show failed results.
                Log.d("Result: ", "NO");
            }

        } catch (Exception e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            Log.d("Result: ", "NO");
            e.printStackTrace();
        }

        return sendBook;
    }

    public Book getSendBook() {
        return sendBook;
    }
}