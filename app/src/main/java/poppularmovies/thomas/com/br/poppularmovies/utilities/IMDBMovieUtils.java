/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package poppularmovies.thomas.com.br.poppularmovies.utilities;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import poppularmovies.thomas.com.br.poppularmovies.data.Movie;

/**
 * Utility functions to handle IMDB Movies JSON data.
 */
public final class IMDBMovieUtils {

    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the movies posters.
     * <p/>
     *
     * @param moviesJsonStr JSON response from server
     *
     * @return Array of Strings describing the posters
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static ArrayList<Movie> getMoviesPosters(Context context, String moviesJsonStr)
            throws JSONException {

        /* Movies information. Each movie's info is an element of the "results" array */
        final String MV_LIST = "results";

        /* id and backdrop_path */
        final String MV_ID = "id";
        final String MV_RATING = "vote_average";
        final String MV_TITLE = "title";
        final String MV_POSTER = "backdrop_path";
        final String MV_SYNOPSIS = "overview";
        final String MV_RELEASE_DATE= "release_date";
        final String MV_MESSAGE_CODE = "status_code";

        ArrayList<Movie> lsMovies = null;

        JSONObject moviesJson = new JSONObject(moviesJsonStr);

        /* Is there an error? */
        if (moviesJson.has(MV_MESSAGE_CODE)) {
            int errorCode = moviesJson.getInt(MV_MESSAGE_CODE);

            switch (errorCode) {
                case HttpURLConnection.HTTP_OK:
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    /* Location invalid */
                    return null;
                default:
                    /* Server probably down */
                    return null;
            }
        }

        JSONArray moviesArray = moviesJson.getJSONArray(MV_LIST);

        lsMovies = new ArrayList<>(moviesArray.length());
        for (int i = 0; i < moviesArray.length(); i++) {

            JSONObject jsonMovie = moviesArray.getJSONObject(i);
            Movie item = new Movie();
            item.setID(jsonMovie.getInt(MV_ID));
            item.setTitle(jsonMovie.getString(MV_TITLE));
            item.setPoster(jsonMovie.getString(MV_POSTER));
            item.setRating(jsonMovie.getInt(MV_RATING));
            item.setRelease_date(jsonMovie.getString(MV_RELEASE_DATE));
            item.setSynopsis(jsonMovie.getString(MV_SYNOPSIS));
            lsMovies.add(item);
        }
        return lsMovies;
    }
}