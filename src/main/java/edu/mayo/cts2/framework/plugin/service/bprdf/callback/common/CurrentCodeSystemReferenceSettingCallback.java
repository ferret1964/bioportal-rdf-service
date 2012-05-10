/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
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
package edu.mayo.cts2.framework.plugin.service.bprdf.callback.common;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntrySummary;
import edu.mayo.cts2.framework.model.core.CodeSystemVersionReference;
import edu.mayo.cts2.framework.plugin.service.bprdf.common.CodeSystemVersionReferenceFactory;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.id.IdService;
import edu.mayo.twinkql.result.callback.AfterResultBinding;
import edu.mayo.twinkql.result.callback.CallbackContext;

/**
 * The Class CurrentCodeSystemReferenceSettingCallback.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component("currentCodeSystemReferenceCallback")
public class CurrentCodeSystemReferenceSettingCallback implements AfterResultBinding<CodeSystemCatalogEntrySummary> {

	@Resource
	private CodeSystemVersionReferenceFactory codeSystemVersionReferenceFactory;
	@Resource
	private IdService idService;	
	/* (non-Javadoc)
	 * @see edu.mayo.twinkql.result.callback.AfterResultBinding#afterBinding(java.lang.Object)
	 */
	@Override
	public void afterBinding(
			CodeSystemCatalogEntrySummary bindingResult,
			CallbackContext callbackContext) {
		String virtualId= callbackContext.getCallbackIds().get("virtualId").toString();
		String id= idService.getCurrentIdForOntologyId(virtualId);
		CodeSystemVersionReference currentVersion= codeSystemVersionReferenceFactory.getCodeSystemVersionReferenceFor(id);
		bindingResult.setCurrentVersion(currentVersion);
	}

}
