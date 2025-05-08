/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.contrib.jmxmetrics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ConfigParser {

  public static Map<String, Properties> parseConfig(String filePath) throws IOException {
    Map<String, Properties> configMap = new HashMap<>();
    BufferedReader reader = new BufferedReader(new FileReader(filePath));
    String line;
    String currentSection = null;
    Properties currentProps = null;

    while ((line = reader.readLine()) != null) {
      line = line.trim();
      if (line.isEmpty() || line.startsWith("#")) {
        continue; // Skip empty lines and comments
      }

      if (line.endsWith(":")) {
        // Start of a new section
        currentSection = line.substring(0, line.length() - 1).trim();
        currentProps = new Properties();
        configMap.put(currentSection, currentProps);
      } else if (currentSection != null) {
        // Parse key-value pairs within the current section
        String[] parts = line.split("=", 2);
        if (parts.length == 2) {
          String key = parts[0].trim();
          String value = parts[1].trim();
          currentProps.setProperty(key, value);
        }
      }
    }

    reader.close();
    return configMap;
  }
}
