package com.avbelyaev.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.LambdaLogger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class Handler implements RequestHandler<Map<String,String>, String>{

  private static final Logger logger = LoggerFactory.getLogger(Handler.class);

  Gson gson = new GsonBuilder().setPrettyPrinting().create();

  @Override
  public String handleRequest(Map<String,String> event, Context context)
  {
    // log execution details
    logger.log("ENVIRONMENT VARIABLES: " + gson.toJson(System.getenv()));
    logger.log("CONTEXT: " + gson.toJson(context));
    // process event
    var evt = gson.toJson(event);
    logger.log("EVENT: " + event);

    return "Hello lambda " + evt;
  }
}