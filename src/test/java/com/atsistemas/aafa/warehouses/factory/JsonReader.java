package com.atsistemas.aafa.warehouses.factory;

import static org.assertj.core.api.Assertions.fail;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

public class JsonReader {

  private JsonReader() {
  }

  private static final String basePath = "src/test/resources";

  public static <T> T stubFromJson(String fileName, Class<T> valueType) {
    try {
      return getObjectMapper().readValue(getFile(fileName), valueType);
    } catch (IOException e) {
      return stubFromJsonResource(fileName, valueType);
    }
  }

  private static ObjectMapper getObjectMapper() {
    return new ObjectMapper();
  }

  private static File getFile(String fileName) {
    return Paths.get(basePath, fileName).toFile();
  }

  public static <T> T stubFromJsonResource(String resourceName, Class<T> valueType) {
    try {
      return getObjectMapper().readValue(getResourceStream(resourceName), valueType);
    } catch (IOException e) {
      fail("Error reading resource ".concat(resourceName));
      throw new RuntimeException();
    }
  }

  private static InputStream getResourceStream(String fileName) {
    return JsonReader.class.getClassLoader().getResourceAsStream(fileName);
  }
}