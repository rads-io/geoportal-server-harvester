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
package com.esri.geoportal.harvester.support;

import com.esri.geoportal.harvester.api.ProcessInstance;
import com.esri.geoportal.harvester.api.defs.TaskDefinition;
import com.esri.geoportal.harvester.engine.utils.Statistics;
import java.util.UUID;

/**
 * Process statistics response.
 */
public final class ProcessStatisticsResponse {
  private final UUID uuid;
  private final TaskDefinition taskDefinition;
  private final ProcessInstance.Status status;
  private final Statistics statistics;

  /**
   * Creates instance of the process info.
   * @param uuid process uuid
   * @param taskDefinition task definition
   * @param status process status
   * @param statistics statistics
   */
  public ProcessStatisticsResponse(UUID uuid, TaskDefinition taskDefinition, ProcessInstance.Status status, Statistics statistics) {
    this.uuid = uuid;
    this.taskDefinition = taskDefinition;
    this.status = status;
    this.statistics = statistics;
  }

  /**
   * Gets process uuid.
   * @return process uuid
   */
  public UUID getUuid() {
    return uuid;
  }

  /**
   * Task definition.
   * @return task definition
   */
  public TaskDefinition getTaskDefinition() {
    return taskDefinition;
  }

  /**
   * Gets status.
   * @return status
   */
  public ProcessInstance.Status getStatus() {
    return status;
  }

  /**
   * Gets statistics.
   * @return statistics
   */
  public Statistics getStatistics() {
    return statistics;
  }
  
  @Override
  public String toString() {
    return String.format("PROCESS/STATISTICS :: id: %s, task: %s, status: %s, statistics: %s", uuid, taskDefinition, status, statistics);
  }
}
