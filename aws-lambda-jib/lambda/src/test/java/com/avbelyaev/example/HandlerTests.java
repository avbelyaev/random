package com.avbelyaev.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

class HandlerTests {
  private static final Logger logger = LoggerFactory.getLogger(HandlerTests.class);

  @Test
  void invokeTest() {
    // given
    HashMap<String,String> event = new HashMap<String,String>();
    Context context = new TestContext();
    Handler handler = new Handler();

    // when
    String result = handler.handleRequest(event, context);

    // then
    assertTrue(result.contains("Hello lambda"));
  }

}