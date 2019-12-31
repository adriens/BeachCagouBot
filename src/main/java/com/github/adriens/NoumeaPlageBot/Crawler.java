/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adriens.NoumeaPlageBot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author salad74
 */
public class Crawler {

    public static void main(String[] args) {
        try {
            Client client = Client.create();
            // https://plages-noumea.herokuapp.com/plages/{plageId}
            WebResource webResource = client
                    .resource("https://plages-noumea.herokuapp.com/plages/0");
            ClientResponse response = webResource.accept(MediaType.APPLICATION_JSON).
                    header("content-type", MediaType.APPLICATION_JSON).
                    get(ClientResponse.class);

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + response.getStatus());
            }
            String output = response.getEntity(String.class);

            System.out.println("Output from Server .... \n");
            System.out.println(output);
            ObjectMapper mapper = new ObjectMapper();
            PlageStatus plageStatus = mapper.readValue(output, PlageStatus.class);
            System.out.println("Plage couleur : <" + plageStatus.getCouleurDrapeau() + ">");
            

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
