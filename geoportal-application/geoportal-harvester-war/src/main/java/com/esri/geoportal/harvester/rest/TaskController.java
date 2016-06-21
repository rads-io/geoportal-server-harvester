/*
 * Copyright 2016 Esri, Inc..
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

import com.esri.geoportal.harvester.api.defs.EntityDefinition;
import com.esri.geoportal.harvester.support.TaskResponse;
import com.esri.geoportal.harvester.beans.EngineBean;
import com.esri.geoportal.harvester.api.defs.TaskDefinition;
import com.esri.geoportal.harvester.api.defs.TriggerInstanceDefinition;
import com.esri.geoportal.harvester.api.ex.DataProcessorException;
import com.esri.geoportal.harvester.api.ex.InvalidDefinitionException;
import com.esri.geoportal.harvester.engine.support.ProcessReference;
import com.esri.geoportal.harvester.engine.support.TriggerReference;
import com.esri.geoportal.harvester.support.ProcessResponse;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Task controller.
 * <p>
 * Provides access to tasks.
   <pre><code>
   GET /rest/harvester/tasks                      - gets all the tasks
   GET /rest/harvester/tasks/{taskId}             - gets task information by task id
   DELETE /rest/harvester/tasks/{taskId}          - deletes existing task by task id
   PUT /rest/harvester/tasks                      - creates a new task (task definition in the request body)
   POST /rest/harvester/tasks/{taskId}            - updates a task by task id (task definition in the request body)
   
   PUT /rest/harvester/tasks/execute              - executes a task (task definition in the request body)
   PUT /rest/harvester/tasks/schedule             - schedules a task (trigger instance definition in the request body)
   PUT /rest/harvester/tasks/{taskId}/execute     - executes immediatelly a task by task id
   PUT /rest/harvester/tasks/{taskId}/schedule    - schedule a task by task id (trigger definition in the request body)
   </code></pre>
 */
@RestController
public class TaskController {
  private static final Logger LOG = LoggerFactory.getLogger(TaskController.class);
  
  @Autowired
  private EngineBean engine;
  
  /**
   * Lists all available tasks.
   * @return array of task informations
   */
  @RequestMapping(value = "/rest/harvester/tasks", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TaskResponse[]> listTasks() {
    try {
      LOG.debug(String.format("GET /rest/harvester/tasks"));
      return new ResponseEntity<>(engine.selectTaskDefinitions(null).stream().map(d->new TaskResponse(d.getKey(), d.getValue())).collect(Collectors.toList()).toArray(new TaskResponse[0]),HttpStatus.OK);
    } catch (DataProcessorException ex) {
      LOG.error(String.format("Error listing tasks."), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  /**
   * Gets task by id.
   * @param taskId task id
   * @return task info or <code>null</code> if no task found
   */
  @RequestMapping(value = "/rest/harvester/tasks/{taskId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TaskResponse> getTask(@PathVariable UUID taskId) {
    try {
      LOG.debug(String.format("GET /rest/harvester/tasks/%s", taskId));
      TaskDefinition taskDefinition = engine.readTaskDefinition(taskId);
      return new ResponseEntity<>(taskDefinition!=null? new TaskResponse(taskId, taskDefinition): null,HttpStatus.OK);
    } catch (DataProcessorException ex) {
      LOG.error(String.format("Error getting task: %s", taskId), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  /**
   * Deletes task by id.
   * @param taskId task id
   * @return task info of the deleted task or <code>null</code> if no tasks have been deleted
   */
  @RequestMapping(value = "/rest/harvester/tasks/{taskId}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TaskResponse> deleteTask(@PathVariable UUID taskId) {
    try {
      LOG.debug(String.format("DELETE /rest/harvester/tasks/%s", taskId));
      TaskDefinition taskDefinition = engine.readTaskDefinition(taskId);
      if (taskDefinition!=null) {
        engine.deleteTaskDefinition(taskId);
      }
      return new ResponseEntity<>(taskDefinition!=null? new TaskResponse(taskId, taskDefinition): null,HttpStatus.OK);
    } catch (DataProcessorException ex) {
      LOG.error(String.format("Error deleting task: %s", taskId), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  /**
   * Adds a new task.
   * @param taskDefinition task definition
   * @return task info of the newly created task
   */
  @RequestMapping(value = "/rest/harvester/tasks", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TaskResponse> addTask(@RequestBody TaskDefinition taskDefinition) {
    try {
      LOG.debug(String.format("PUT /rest/harvester/tasks <-- %s", taskDefinition));
      UUID id = engine.addTaskDefinition(taskDefinition);
      return new ResponseEntity<>(new TaskResponse(id, taskDefinition),HttpStatus.OK);
    } catch (DataProcessorException ex) {
      LOG.error(String.format("Error adding task: %s", taskDefinition), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  /**
   * Updates task by id.
   * @param taskDefinition task definition
   * @param taskId task id
   * @return task info of the deleted task or <code>null</code> if no tasks have been deleted
   */
  @RequestMapping(value = "/rest/harvester/tasks/{taskId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TaskResponse> updateTask(@RequestBody TaskDefinition taskDefinition, @PathVariable UUID taskId) {
    try {
      LOG.debug(String.format("POST /rest/harvester/tasks/%s <-- %s", taskId, taskDefinition));
      TaskDefinition oldTaskDef = engine.updateTaskDefinition(taskId, taskDefinition);
      return new ResponseEntity<>(oldTaskDef!=null? new TaskResponse(taskId, oldTaskDef): null, HttpStatus.OK);
    } catch (DataProcessorException ex) {
      LOG.error(String.format("Error updating task: %s <-- %s", taskId, taskDefinition), ex);
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  /**
   * Executes task by id.
   * @param taskId task id
   * @return task info of the deleted task or <code>null</code> if no tasks have been deleted
   */
  @RequestMapping(value = "/rest/harvester/tasks/{taskId}/execute", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProcessResponse> executeTask(@PathVariable UUID taskId) {
    try {
      LOG.debug(String.format("PUT /rest/harvester/tasks/%s/execute", taskId));
      TaskDefinition taskDefinition = engine.readTaskDefinition(taskId);
      ProcessReference ref = engine.submitTaskDefinition(taskDefinition);
      ref.getProcess().begin();
      return new ResponseEntity<>(new ProcessResponse(ref.getProcessId(), ref.getProcess().getTitle(), ref.getProcess().getStatus()), HttpStatus.OK);
    } catch (InvalidDefinitionException ex) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (DataProcessorException ex) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  /**
   * Executes task immediately using task definition.
   * @param taskDefinition task definition
   * @return task info of the deleted task or <code>null</code> if no tasks have been deleted
   */
  @RequestMapping(value = "/rest/harvester/tasks/execute", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProcessResponse> executeTask(@RequestBody TaskDefinition taskDefinition) {
    try {
      LOG.debug(String.format("PUT /rest/harvester/tasks/execute <-- %s", taskDefinition));
      ProcessReference ref = engine.submitTaskDefinition(taskDefinition);
      ref.getProcess().begin();
      return new ResponseEntity<>(new ProcessResponse(ref.getProcessId(), ref.getProcess().getTitle(), ref.getProcess().getStatus()), HttpStatus.OK);
    } catch (InvalidDefinitionException ex) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (DataProcessorException ex) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  /**
   * Schedules task by id.
   * @param triggerDefinition trigger definition
   * @param taskId task id
   * @return task info of the deleted task or <code>null</code> if no tasks have been deleted
   */
  @RequestMapping(value = "/rest/harvester/tasks/{taskId}/schedule", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TriggerResponse> scheduleTask(@RequestBody EntityDefinition triggerDefinition, @PathVariable UUID taskId) {
    try {
      LOG.debug(String.format("PUT /rest/harvester/tasks/%s/schedule <-- %s", taskId, triggerDefinition));
      TaskDefinition taskDefinition = engine.readTaskDefinition(taskId);
      TriggerInstanceDefinition triggerInstanceDefinition = new TriggerInstanceDefinition();
      triggerInstanceDefinition.setType(triggerDefinition.getType());
      triggerInstanceDefinition.setTaskDefinition(taskDefinition);
      triggerInstanceDefinition.setProperties(triggerDefinition.getProperties());
      TriggerReference trigRef = engine.scheduleTask(triggerInstanceDefinition);
      return new ResponseEntity<>(new TriggerResponse(trigRef.getUuid(), trigRef.getTriggerInstanceDefinition()),HttpStatus.OK);
    } catch (InvalidDefinitionException ex) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (DataProcessorException ex) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
  
  /**
   * Schedules task using trigger definition.
   * @param trigDef trigger definition
   * @return task info of the deleted task or <code>null</code> if no tasks have been deleted
   */
  @RequestMapping(value = "/rest/harvester/tasks/schedule", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TriggerResponse> scheduleTask(@RequestBody TriggerInstanceDefinition trigDef) {
    try {
      LOG.debug(String.format("PUT /rest/harvester/tasks/schedule <-- %s", trigDef));
      TriggerReference trigRef = engine.scheduleTask(trigDef);
      return new ResponseEntity<>(new TriggerResponse(trigRef.getUuid(), trigRef.getTriggerInstanceDefinition()),HttpStatus.OK);
    } catch (InvalidDefinitionException ex) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    } catch (DataProcessorException ex) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
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
