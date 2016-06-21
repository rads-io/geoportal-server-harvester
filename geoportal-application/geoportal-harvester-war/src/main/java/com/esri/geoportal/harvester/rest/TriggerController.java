/*
 * Copyright 2016 Esri, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.esri.geoportal.harvester.rest;

import com.esri.geoportal.harvester.api.defs.UITemplate;
import com.esri.geoportal.harvester.api.ex.DataProcessorException;
import com.esri.geoportal.harvester.api.ex.InvalidDefinitionException;
import com.esri.geoportal.harvester.beans.EngineBean;
import com.esri.geoportal.harvester.engine.support.TriggerReference;
import com.esri.geoportal.harvester.support.TriggerResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Trigger controller.
 */
@RestController
public class TriggerController {
  private static final Logger LOG = LoggerFactory.getLogger(TriggerController.class);
  
  @Autowired
  private EngineBean engine;
  
  
  /**
   * Lists all triggers.
   * @return array of trigger templates
   */
  @RequestMapping(value = "/rest/harvester/triggers/types", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public UITemplate[] listTriggerTypes() {
    LOG.debug(String.format("GET /rest/harvester/triggers"));
    return engine.getTriggers().toArray(new UITemplate[0]);
  }
  
  /**
   * List all active triggers.
   * @return list of all activated triggers.
   */
  @RequestMapping(value = "/rest/harvester/triggers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<TriggerResponse>> listTriggers() {
    LOG.debug(String.format("GET /rest/harvester/triggers"));
    List<TriggerResponse> triggerResponses = engine.listActivatedTriggers().stream()
            .map(t->new TriggerResponse(t.getUuid(), t.getTriggerInstanceDefinition()))
            .collect(Collectors.toList());
    return new ResponseEntity<>(triggerResponses,HttpStatus.OK);
  }
  
  /**
   * Deactivates trigger.
   * @param triggerId trigger id
   * @return trigger response
   */
  @RequestMapping(value = "/rest/harvester/triggers/{triggerId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TriggerResponse> deactivateTrigger(@PathVariable UUID triggerId) {
    try {
      LOG.debug(String.format("DELETE /rest/harvester/triggers/%s", triggerId));
      TriggerReference trigRef = engine.deactivateTriggerInstance(triggerId);
      return new ResponseEntity<>(new TriggerResponse(trigRef.getUuid(), trigRef.getTriggerInstanceDefinition()),HttpStatus.OK);
    } catch (InvalidDefinitionException ex) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (DataProcessorException ex) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
}
