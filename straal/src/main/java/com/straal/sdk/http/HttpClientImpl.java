/*
 * HttpClientImpl.java
 * Created by Arkadiusz Różalski on 26.01.18
 * Straal SDK for Android
 * Copyright 2018 Straal Sp. z o. o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.straal.sdk.http;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class HttpClientImpl implements HttpClient {
    private final String baseUrl;
    private final Map<String, String> headers;

    HttpClientImpl(String baseUrl, Map<String, String> headers) {
        this.baseUrl = baseUrl;
        this.headers = headers;
    }

    @Override
    public HttpResponse post(String endpoint, String request) throws HttpRequestException {
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) new URL(baseUrl + endpoint).openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            for (Map.Entry<String, String> header : headers.entrySet()) {
                urlConnection.setRequestProperty(header.getKey(), header.getValue());
            }
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            outputStream.write(request.getBytes());
            outputStream.flush();

            HttpResponse response = createResponse(urlConnection);
            outputStream.close();
            urlConnection.disconnect();
            return response;
        } catch (Exception e) {
            throw new HttpRequestException(e);
        }
    }

    private HttpResponse createResponse(HttpURLConnection httpURLConnection) throws IOException {
        int responseCode = httpURLConnection.getResponseCode();
        InputStream inputStream = HttpResponse.isSuccessful(responseCode) ? httpURLConnection.getInputStream() : httpURLConnection.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder response = new StringBuilder();
        String currentLine;
        while ((currentLine = reader.readLine()) != null) {
            response.append(currentLine);
        }
        reader.close();
        return new HttpResponse(responseCode, response.toString());
    }
}
