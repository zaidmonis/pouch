package com.example.pouch.client.app;

import com.example.pouch.client.lib.ApiException;
import com.example.pouch.client.lib.DynamicApiClient;
import com.example.pouch.client.model.UserAdminView;
import com.example.pouch.client.model.UserContact;
import com.example.pouch.client.model.UserSummary;
import com.example.pouch.client.request.GetUserRequest;

public class ClientApplication {
    public static void main(String[] args) throws Exception {
        System.out.println("Run the server first: mvn -pl server spring-boot:run");
        System.out.println("---");

        DynamicApiClient client = new DynamicApiClient();

        call(client, new GetUserRequest("u-100", "USER"), UserSummary.class, "UserSummary (USER)");
        call(client, new GetUserRequest("u-200", "USER"), UserContact.class, "UserContact (USER)");
        call(client, new GetUserRequest("u-300", "USER"), UserAdminView.class, "UserAdminView (USER)");
        call(client, new GetUserRequest("u-300", "ADMIN"), UserAdminView.class, "UserAdminView (ADMIN)");
    }

    private static <T> void call(DynamicApiClient client, GetUserRequest request, Class<T> modelClass, String label)
            throws Exception {
        System.out.println("Request: " + label + " -> " + request);
        try {
            T response = client.send(request, modelClass);
            System.out.println("Success: " + response);
        } catch (ApiException ex) {
            System.out.println("Error status: " + ex.getStatusCode());
            System.out.println("Error body: " + ex.getResponseBody());
        }
        System.out.println("---");
    }
}
