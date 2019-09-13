package com.bc.bookcrossing.src.requestManager;

import android.os.AsyncTask;
import android.util.Log;

import com.bc.bookcrossing.src.View.Fragment.Iteration_2.ISBNScanFragment;
import com.bc.bookcrossing.src.ClientModels.Book;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * In questa classe sono presenti le chiamate alle API
 * Goolge per ottenere le informazioni partendo dal codice ISBn scansionato.
 * Al suo interno andiamo inoltre a comporre un oggetto di tipo
 * Book parsando le informazioni da un file JSON.
 *
 * @author Paganessi Andrea - Piffari Michele - Villa Stefano
 * @version 1.0
 * @since 2018/2019
 */

public class FetchAndParseBook extends AsyncTask<String,Void,String> {

    private static Book parsedBook;
    private String queryString;

    /**
     * Fetch delle informazioni relative al codice ISBN appena scansionato
     * @param params
     * @return Informazioni del libro
     */
    @Override
    protected String doInBackground(String... params) {

        // Get the search string (the ISBN scanned from camera)
        queryString = params[0];
        // Set up variables for the try block that need to be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        // Result of Google's API
        String bookJSONString = null;

        // Attempt to query the Books API.
        try {
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
        parseBook(s);
    }


    private Book parseBook(String s) {
        try {

            // Field that we are interested on
            String title = null;
            String authors = "";
            String yearOfPubblication = null;
            String type = "";

            // Convert the response into a JSON object.
            JSONObject jsonObject = new JSONObject(s);
            // Get the JSONArray of book items.
            JSONArray itemsArray = jsonObject.getJSONArray("items");

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

                    int indexOf = yearOfPubblication.indexOf("-");
                    int year = 0;
                    if(indexOf != -1) {
                        year = Integer.parseInt(yearOfPubblication.substring(0, indexOf));
                    } else{
                        year = Integer.parseInt(yearOfPubblication);
                    }

                    // Debug print
                    Log.d("Result", title + " ; " + authors + " ; " + yearOfPubblication);
                    Log.d("year", "" + year);
                    Log.d("link" , volumeInfo.getJSONObject("imageLinks").getString("smallThumbnail"));

                    parsedBook = new Book(title, authors, year, 1, type, queryString);
                    ISBNScanFragment.setScannedBook(parsedBook);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // Move to the next item.
            }
        } catch (Exception e) {
            // If onPostExecute does not receive a proper JSON string,
            // update the UI to show failed results.
            Log.d("Result: ", "Parsing error");
            e.printStackTrace();
        }
        return parsedBook;
    }
}