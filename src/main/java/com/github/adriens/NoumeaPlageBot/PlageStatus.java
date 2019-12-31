/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adriens.NoumeaPlageBot;

/**
 *
 * @author salad74
 */
public class PlageStatus {

    /**
     * @return the videoStreamURL
     */
    public String getVideoStreamURL() {
        return videoStreamURL;
    }

    /**
     * @param videoStreamURL the videoStreamURL to set
     */
    public void setVideoStreamURL(String videoStreamURL) {
        this.videoStreamURL = videoStreamURL;
    }

    /**
     * @return the couleurDrapeau
     */
    private String couleurDrapeau;
    private String nomPlage;
    private String urlIconeDrapeau;
    private int plageId;
    private String baignadeMessage;
    private String couleurDrapeauEnglish;
    private String videoStreamURL; 
    public PlageStatus(){
        
        
        
    }
    public String getCouleurDrapeau() {
        return couleurDrapeau;
    }

    /**
     * @param couleurDrapeau the couleurDrapeau to set
     */
    public void setCouleurDrapeau(String couleurDrapeau) {
        this.couleurDrapeau = couleurDrapeau;
    }

    /**
     * @return the nomPlage
     */
    public String getNomPlage() {
        return nomPlage;
    }

    /**
     * @param nomPlage the nomPlage to set
     */
    public void setNomPlage(String nomPlage) {
        this.nomPlage = nomPlage;
    }

    /**
     * @return the urlIconeDrapeau
     */
    public String getUrlIconeDrapeau() {
        return urlIconeDrapeau;
    }

    /**
     * @param urlIconeDrapeau the urlIconeDrapeau to set
     */
    public void setUrlIconeDrapeau(String urlIconeDrapeau) {
        this.urlIconeDrapeau = urlIconeDrapeau;
    }

    /**
     * @return the plageId
     */
    public int getPlageId() {
        return plageId;
    }

    /**
     * @param plageId the plageId to set
     */
    public void setPlageId(int plageId) {
        this.plageId = plageId;
    }

    /**
     * @return the baignadeMessage
     */
    public String getBaignadeMessage() {
        return baignadeMessage;
    }

    /**
     * @param baignadeMessage the baignadeMessage to set
     */
    public void setBaignadeMessage(String baignadeMessage) {
        this.baignadeMessage = baignadeMessage;
    }

    /**
     * @return the couleurDrapeauEnglish
     */
    public String getCouleurDrapeauEnglish() {
        return couleurDrapeauEnglish;
    }

    /**
     * @param couleurDrapeauEnglish the couleurDrapeauEnglish to set
     */
    public void setCouleurDrapeauEnglish(String couleurDrapeauEnglish) {
        this.couleurDrapeauEnglish = couleurDrapeauEnglish;
    }
    
}
