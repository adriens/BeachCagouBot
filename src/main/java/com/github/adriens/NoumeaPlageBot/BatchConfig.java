/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.adriens.NoumeaPlageBot;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 *
 * @author salad74
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {
     
    @Autowired
    private JobBuilderFactory jobs;
 
    @Autowired
    private StepBuilderFactory steps;
    
    @Autowired
    private Environment env;
     
    @Bean
    public Step stepTwitter(){
        /*return steps.get("stepTwitter")
                .tasklet(new TwitterTasklet())
                .build();*/
        
        TwitterTasklet tasklet = new TwitterTasklet();
        tasklet.setEndpoint(env.getProperty("endpoint"));
        return steps.get("stepTwitter")
                .tasklet(tasklet)
                .build();
    }
     
    @Bean
    public Job demoJob(){
        System.out.println("ENV : <" + env.getProperty("endpoint") + ">");
        return jobs.get("demoJob")
                .incrementer(new RunIdIncrementer())
                .start(stepTwitter())
                .build();
    }
}
