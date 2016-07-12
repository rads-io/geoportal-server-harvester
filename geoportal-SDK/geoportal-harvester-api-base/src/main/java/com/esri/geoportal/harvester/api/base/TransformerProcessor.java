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
package com.esri.geoportal.harvester.api.base;

import com.esri.geoportal.harvester.api.DataReference;
import com.esri.geoportal.harvester.api.TransformerInstance;
import com.esri.geoportal.harvester.api.defs.EntityDefinition;
import com.esri.geoportal.harvester.api.ex.DataTransformerException;

/**
 * Transformer processor.
 */
/*package*/ final class TransformerProcessor implements LinkProcessor {
  
  final TransformerInstance transformer;

  public TransformerProcessor(TransformerInstance transformer) {
    this.transformer = transformer;
  }

  @Override
  public EntityDefinition getLinkDefinition() {
    return transformer.getTransformerDefinition();
  }

  @Override
  public DataReference process(DataReference dataReference) throws DataTransformerException {
    return transformer.transform(dataReference);
  }

  @Override
  public void close() throws Exception {
    transformer.close();
  }
  
}