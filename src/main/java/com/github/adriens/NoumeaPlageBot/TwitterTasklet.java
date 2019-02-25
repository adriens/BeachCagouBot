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
import java.util.Date;
import javax.ws.rs.core.MediaType;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

/**
 *
 * @author salad74
 */
public class TwitterTasklet implements Tasklet {

    private String endpoint;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("TwitterTask start..");

        // ... your code
        try {
            Client client = Client.create();
            // https://eaux-baignade-noumea.herokuapp.com/plages/{plageId}
            WebResource webResource = client
                    .resource("https://eaux-baignade-noumea.herokuapp.com/plages/0");
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
            //System.out.println("ENV : <" + env.getProperty("env") + ">");

            // the twitter part
            Twitter twitter = TwitterFactory.getSingleton();
            Status status = twitter.updateStatus((new Date()).toString() + "\\u263A Tweet from Spring boot 2 ;-p See https://www.noumea.nc/actualites/qualite-des-eaux-de-baignade-0");
            System.out.println("Successfully updated the status to [" + status.getText() + "].");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.out.println("TwitterTask done..");
        return RepeatStatus.FINISHED;
    }

    /**
     * @return the endpoint
     */
    public String getEndpoint() {
        return endpoint;
    }

    /**
     * @param endpoint the endpoint to set
     */
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}
