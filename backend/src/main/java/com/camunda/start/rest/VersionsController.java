/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.camunda.start.rest;

import com.camunda.start.update.VersionUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class VersionsController {

  @Autowired
  protected VersionUpdater versionUpdater;

  @GetMapping(value = "/versions.json")
  public @ResponseBody String getVersions() {
    String versionsAsJson = null;

    Path path = Paths.get("versions.json");
    try {
      versionsAsJson = readVersions(path);

    } catch (NoSuchFileException e) {
      versionUpdater.updateVersions();

      try {
        versionsAsJson = readVersions(path);

      } catch (IOException ex) {
        throw new RuntimeException(ex);

      }

    } catch (IOException e) {
      throw new RuntimeException(e);

    }

    return versionsAsJson;
  }

  protected String readVersions(Path path) throws IOException {
    return new String(Files.readAllBytes(path), Charset.defaultCharset());
  }

}
