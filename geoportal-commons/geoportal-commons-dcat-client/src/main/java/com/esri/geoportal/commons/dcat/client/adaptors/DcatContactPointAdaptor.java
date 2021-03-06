/*
 * Copyright 2019 Esri.
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
package com.esri.geoportal.commons.dcat.client.adaptors;

import com.esri.geoportal.commons.dcat.client.dcat.DcatContactPoint;
import com.esri.geoportal.commons.dcat.client.json.JsonAttributes;

/**
 * DCAT contact point adaptor.
 */
public class DcatContactPointAdaptor extends DcatAdaptor implements DcatContactPoint {

  public DcatContactPointAdaptor(JsonAttributes attrs) {
    super(attrs);
  }

  @Override
  public String getType() {
    return getString("@type");
  }

  @Override
  public String getName() {
    return getString("contactPoint");
  }

  @Override
  public String getMBox() {
    return getString("mbox");
  }
  
}
