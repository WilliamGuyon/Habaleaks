package net.habaleaks;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.*;

public class ApiClient {
    private final OkHttpClient client;
    private final Gson gson;
    private static final String BASE_URL = "http://172.16.33.1:8000/api/";

    /**
     * Constructs a new ApiClient object.
     */
    public ApiClient() {
        this.client = new OkHttpClient();
        this.gson = new Gson();
    }

    /**
     * Executes a GET request to retrieve a list of objects from the specified endpoint.
     *
     * @param endpoint The API endpoint to send the request to.
     * @param type     The type of the list elements.
     * @param <T>      The type of the list elements.
     * @return A list of objects retrieved from the API.
     * @throws IOException if an I/O error occurs during the HTTP request.
     */
    private <T> List<T> executeGetRequest(String endpoint, Type type) throws IOException {
        return executeRequest("GET", endpoint, null, type);
    }

    /**
     * Executes a POST request with the given request body to the specified endpoint.
     *
     * @param endpoint     The API endpoint to send the request to.
     * @param requestBody The request body to send with the POST request.
     * @return true if the request was successful, false otherwise.
     * @throws IOException if an I/O error occurs during the HTTP request.
     */
    private boolean executePostRequest(String endpoint, RequestBody requestBody) throws IOException {
        return executeRequest("POST", endpoint, requestBody, Boolean.class);
    }

    /**
     * Executes an HTTP request to the specified endpoint with the given method and request body.
     *
     * @param method      The HTTP method of the request (e.g., GET, POST).
     * @param endpoint    The API endpoint to send the request to.
     * @param requestBody The request body to send with the request.
     * @param type        The expected response type.
     * @param <T>         The type of the response.
     * @return The response object of the specified type.
     * @throws IOException if an I/O error occurs during the HTTP request.
     */
    private <T> T executeRequest(String method, String endpoint, RequestBody requestBody, Type type) throws IOException {
        Request request = buildRequest(method, endpoint, requestBody);
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Error : " + response);
            if (type == Boolean.class) {
                return (T) Boolean.TRUE; // For POST requests
            } else {
                String responseBody = response.body().string();
                return gson.fromJson(responseBody, type);
            }
        }
    }

    /**
     * Builds an HTTP request with the given method, endpoint, and request body.
     *
     * @param method      The HTTP method of the request (e.g., GET, POST).
     * @param endpoint    The API endpoint to send the request to.
     * @param requestBody The request body to send with the request.
     * @return The constructed HTTP request.
     */
    private Request buildRequest(String method, String endpoint, RequestBody requestBody) {
        Request.Builder builder = new Request.Builder()
                .url(BASE_URL + endpoint)
                .method(method, requestBody);
        return requestBody != null ? builder.post(requestBody).build() : builder.build();
    }

    /**
     * Retrieves a list of all Lieux from the API.
     *
     * @return A list of Lieu objects.
     * @throws IOException if an I/O error occurs during the HTTP request.
     */
    public List<Lieu> GetAllLieux() throws IOException {
        Type lieuListType = new TypeToken<List<Lieu>>() {}.getType();
        return executeGetRequest("lieux/", lieuListType);
    }

    /**
     * Retrieves a list of all Defauts from the API.
     *
     * @return A list of Defaut objects.
     * @throws IOException if an I/O error occurs during the HTTP request.
     */
    public List<Defaut> GetAllDefauts() throws IOException {
        Type defautListType = new TypeToken<List<Defaut>>() {}.getType();
        return executeGetRequest("defauts/", defautListType);
    }

    /**
     * Adds a new Signalement to the API.
     *
     * @param signalement The Signalement object to add.
     * @return true if the Signalement was successfully added, false otherwise.
     * @throws IOException if an I/O error occurs during the HTTP request.
     */
    public Boolean AddSignalement(Signalement signalement) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("defaut", String.valueOf(signalement.getidDefaut()))
                .addFormDataPart("lieu", String.valueOf(signalement.getidLieu()))
                .addFormDataPart("personnel", String.valueOf(signalement.getidPersonnel()))
                .addFormDataPart("gravite", String.valueOf(signalement.getgravite()))
                .addFormDataPart("commentaire", signalement.getcommentaire());

        if (signalement.getphoto() != null) {
            builder.addFormDataPart("photo", signalement.getphoto());
        }
        if (signalement.getlatitude() != null) {
            builder.addFormDataPart("latitude", signalement.getlatitude());
        }
        if (signalement.getlongitude() != null) {
            builder.addFormDataPart("longitude", signalement.getlongitude());
        }

        RequestBody body = builder.build();
        return executePostRequest("signalements/", body);
    }
}