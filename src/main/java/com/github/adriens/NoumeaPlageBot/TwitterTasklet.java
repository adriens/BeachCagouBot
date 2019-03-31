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
import com.vdurmont.emoji.EmojiParser;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;

/**
 *
 * @author salad74
 */
public class TwitterTasklet implements Tasklet {

    private String endpoint;

    public static final String MESSAGE_DRAPEAU_BLEU = "Drapeau bleu. Eau de bonne qualité : baignade autorisée.";
    public static final String MESSAGE_DRAPEAU_JAUNE = "Drapeau jaune. Eau de qualité médiocre : baignade déconseillée.";
    public static final String MESSAGE_DRAPEAU_ROUGE = "Drapeau rouge. Eau de mauvaise qualité : baignade interdite";

    public static final String URL_EAUX_BAIGNADES = "https://goo.gl/frSuC8";

    public static final String MESSAGE_CUSTO_BLEU = "Bonne :swimmer: :heart_eyes: :raised_hands:";
    public static final String MESSAGE_CUSTO_JAUNE = "Evitez de vous baigner :pray:";
    public static final String MESSAGE_CUSTO_ROUGE = "Pour ne pas tomber :sick:, prenez un :coffee: ou un :tea: et profitez de la vue :sunglasses:";

    public static final String HASHTAGS_TWEET = "#noumea #waterquality #baiedescitrons #cagougeek";

    public static final String composeStatusMessage(PlageStatus aPlageStatus) {
        String out = "[" + (new Date()) + "] " + aPlageStatus.getNomPlage() + "\n";
        if (aPlageStatus.getCouleurDrapeauEnglish().equalsIgnoreCase("blue")) {
            out += MESSAGE_DRAPEAU_BLEU + "\n";
            out += MESSAGE_CUSTO_BLEU + "\n";
        } else if (aPlageStatus.getCouleurDrapeauEnglish().equalsIgnoreCase("yellow")) {
            out += MESSAGE_DRAPEAU_JAUNE + "\n";
            out += MESSAGE_CUSTO_JAUNE + "\n";
        } else if (aPlageStatus.getCouleurDrapeauEnglish().equalsIgnoreCase("red")) {
            out += MESSAGE_DRAPEAU_ROUGE + "\n";
            out += MESSAGE_CUSTO_ROUGE + "\n";
        } else {
            out += "Couleur non reconnue.";
        }
        out += HASHTAGS_TWEET + "\n";
        out += URL_EAUX_BAIGNADES;
        return out;
    }

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        System.out.println("TwitterTask start..");

        // ... your code
        try {
            Client client = Client.create();
            // https://eaux-baignade-noumea.herokuapp.com/plages/{plageId}
            WebResource webResource = client.resource(getEndpoint());
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
            // get previous status
            List<Status> statuses = twitter.getHomeTimeline();
            if (statuses.size() > 0) {

                // Status status = twitter.updateStatus((new Date()).toString() + "\\u263A Tweet from Spring boot 2 ;-p See https://www.noumea.nc/actualites/qualite-des-eaux-de-baignade-0");
                //System.out.println("Successfully updated the status to [" + status.getText() + "].");
                // the latest tweet posted if the first of the list
                Status lastTwitterStatus = statuses.get(0);
                /*if(lastTwitterStatus.getCreatedAt()){
                    
                }*/
                System.out.println("Latest tweet : " + lastTwitterStatus.getText());
                // check previous flag color
                if (lastTwitterStatus.getText().contains(plageStatus.getCouleurDrapeau().toLowerCase())) {
                    System.out.println("No chnage in flag : do not tweet anything");
                } else {
                    System.out.println("Drapeau change : now <" + plageStatus.getCouleurDrapeau() + ">");
                    Status status = twitter.updateStatus(EmojiParser.parseToUnicode(TwitterTasklet.composeStatusMessage(plageStatus)));

                }
            } else {
                // the first status ! We post no matter previous status
                System.out.println("No previous tweet : inconditional tweet.");
                Status status = twitter.updateStatus(EmojiParser.parseToUnicode(TwitterTasklet.composeStatusMessage(plageStatus)));

            }

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

    public static final LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public static int getTweetAgeInDays(Status aStatus) {
        int out = 0;
        LocalDate tweetCreateDate = TwitterTasklet.convertToLocalDateViaInstant(aStatus.getCreatedAt());
        LocalDate now = TwitterTasklet.convertToLocalDateViaInstant(new Date());
        Period period = Period.between(now, tweetCreateDate);
        out = period.getDays();
        return out;
    }
}
