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
package com.esri.geoportal.harvester.api.defs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Task definition.
 * Holds all the information needed to create instance of the 
 * {@link com.esri.geoportal.harvester.api.defs.Task}.
 */
public final class TaskDefinition implements Serializable {
  private String name;
  private EntityDefinition processor;
  private EntityDefinition source;
  private List<LinkDefinition> destinations;
  private List<String> keywords = new ArrayList<>();
  private boolean incremental;
  private boolean ignoreRobotsTxt;
  private String ref;

  /**
   * Gets task name.
   * @return name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets task name.
   * @param name name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets processor definition.
   * @return processor definition or <code>null</code> if default processor shall be used
   */
  public EntityDefinition getProcessor() {
    return processor;
  }

  /**
   * Sets processor definition.
   * @param processor processor definition or <code>null</code> for default processor
   */
  public void setProcessor(EntityDefinition processor) {
    this.processor = processor;
  }

  /**
   * Gets source definition.
   * @return source definition
   */
  public EntityDefinition getSource() {
    return source;
  }

  /**
   * Sets source definition.
   * @param source source definition
   */
  public void setSource(EntityDefinition source) {
    this.source = source;
  }

  /**
   * Gets destinations.
   * @return destinations
   */
  public List<LinkDefinition> getDestinations() {
    return destinations;
  }

  /**
   * Sets destinations.
   * @param destinations destinations
   */
  public void setDestinations(List<LinkDefinition> destinations) {
    this.destinations = destinations;
  }

  /**
   * Gets keywords.
   * @return keywords
   */
  public List<String> getKeywords() {
    return keywords;
  }

  /**
   * Sets keywords.
   * @param keywords keywords
   */
  public void setKeywords(List<String> keywords) {
    this.keywords = keywords!=null? keywords: new ArrayList<>();
  }

  /**
   * Checks if is incremental harvest.
   * @return <code>true</code> for incremental
   */
  public boolean isIncremental() {
    return incremental;
  }

  /**
   * Sets incremental harvest.
   * @param incremental <code>true</code> for incremental
   */
  public void setIncremental(boolean incremental) {
    this.incremental = incremental;
  }

  /**
   * Checks if ignore robots.txt
   * @return <code>true</code> to ignore robots.txt
   */
  public boolean isIgnoreRobotsTxt() {
    return ignoreRobotsTxt;
  }

  /**
   * Sets to ignore robots.txt
   * @param ignoreRobotsTxt <code>true</code> to ignore robots.txt
   */
  public void setIgnoreRobotsTxt(boolean ignoreRobotsTxt) {
    this.ignoreRobotsTxt = ignoreRobotsTxt;
  }

  /**
   * Gets database record reference.
   * @return database record reference or <code>null</code> if no such reference
   */
  public String getRef() {
    return ref;
  }

  /**
   * Sets database record reference.
   * @param ref database record reference or <code>null</code> if no such reference
   */
  public void setRef(String ref) {
    this.ref = ref;
  }
  
  @Override
  public String toString() {
    return String.format("NAME: %s, PROCESSOR: %s, SOURCE: %s, DESTINATIONS: %s, INCREMENTAL: %b, IGNOREROBOTSTXT: %b", name, processor, source, destinations!=null? destinations: null, incremental, ignoreRobotsTxt);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    if (obj instanceof TaskDefinition) {
      TaskDefinition td = (TaskDefinition)obj;
      return ((getProcessor()!=null && td.getProcessor()!=null && getProcessor().equals(td.getProcessor())) || (getProcessor()==null && td.getProcessor()==null)) &&
             ((getSource()!=null && td.getSource()!=null && getSource().equals(td.getSource())) || (getSource()==null && td.getSource()==null)) &&
             ((getDestinations()!=null && td.getDestinations()!=null && getDestinations().equals(td.getDestinations())) || (getDestinations()==null && td.getDestinations()==null)) ;
    }
    
    return false;
  }
}
