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
package edu.mayo.cts2.framework.plugin.service.bprdf.profile.valueset;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.model.command.Page;
import edu.mayo.cts2.framework.model.command.ResolvedFilter;
import edu.mayo.cts2.framework.model.core.PredicateReference;
import edu.mayo.cts2.framework.model.core.PropertyReference;
import edu.mayo.cts2.framework.model.core.SortCriteria;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntry;
import edu.mayo.cts2.framework.model.valueset.ValueSetCatalogEntrySummary;
import edu.mayo.cts2.framework.plugin.service.bprdf.dao.RdfDao;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.AbstractQueryService;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.VariableQueryBuilder;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.VariableQueryBuilder.VariableQuery;
import edu.mayo.cts2.framework.plugin.service.bprdf.profile.VariableTiedPropertyReference;
import edu.mayo.cts2.framework.service.meta.StandardModelAttributeReference;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQuery;
import edu.mayo.cts2.framework.service.profile.valueset.ValueSetQueryService;

/**
 * The Class BioportalRdfCodeSystemQueryService.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class BioportalRdfValueSetQueryService extends AbstractQueryService implements
		ValueSetQueryService {
	
	private final static String VALUESET_NAMESPACE = "valueSet";
	private final static String GET_VALUESET_SUMMARIES = "getValueSetCatalogSummaries";
	
	private final static String LIMIT = "limit";
	private final static String OFFSET = "offset";
	
	@Resource
	private RdfDao rdfDao;

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.QueryService#getResourceSummaries(edu.mayo.cts2.framework.service.profile.ResourceQuery, edu.mayo.cts2.framework.model.core.SortCriteria, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<ValueSetCatalogEntrySummary> getResourceSummaries(
			ValueSetQuery query, 
			SortCriteria sortCriteria, 
			Page page) {
			
		Map<String,Object> parameters = new HashMap<String,Object>();
		parameters.put(LIMIT, page.getMaxToReturn()+1);
		parameters.put(OFFSET, page.getStart());
		
		VariableQueryBuilder builder = new VariableQueryBuilder();
		
		if(query != null){
			for(ResolvedFilter filter : query.getFilterComponent()){
				PropertyReference modelRef = filter.getPropertyReference();
				
				VariableTiedPropertyReference variableModelRef = this.findSupportedModelAttribute(modelRef);
				
				builder = builder.addQuery(variableModelRef.getVariable(), filter.getMatchValue());
			}
		}
		
		VariableQuery variableQuery = builder.build();
		
		parameters.put("filters", variableQuery);
		
		List<ValueSetCatalogEntrySummary> results = rdfDao.selectForList(
					VALUESET_NAMESPACE, 
					GET_VALUESET_SUMMARIES,
					parameters,
					ValueSetCatalogEntrySummary.class);
		
		boolean moreResults = results.size() > page.getMaxToReturn();
		
		if(moreResults){
			results.remove(results.size() - 1);
		}
		
		return new DirectoryResult<ValueSetCatalogEntrySummary>(results,!moreResults);
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.QueryService#getResourceList(edu.mayo.cts2.framework.service.profile.ResourceQuery, edu.mayo.cts2.framework.model.core.SortCriteria, edu.mayo.cts2.framework.model.command.Page)
	 */
	@Override
	public DirectoryResult<ValueSetCatalogEntry> getResourceList(
			ValueSetQuery query, SortCriteria sortCriteria, Page page) {
		throw new UnsupportedOperationException();
	}

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.profile.QueryService#count(edu.mayo.cts2.framework.service.profile.ResourceQuery)
	 */
	@Override
	public int count(ValueSetQuery query) {
		throw new UnsupportedOperationException();
	}

	
	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.plugin.service.bprdf.profile.AbstractQueryService#doAddSupportedModelAttributes(java.util.Set)
	 */
	@Override
	public void doAddSupportedModelAttributes(
			Set<VariableTiedPropertyReference> set) {
		VariableTiedPropertyReference name = 
				new VariableTiedPropertyReference(
						StandardModelAttributeReference.RESOURCE_NAME, "acronym");
		
		VariableTiedPropertyReference description = 
				new VariableTiedPropertyReference(
						StandardModelAttributeReference.RESOURCE_SYNOPSIS, "description");
		
		set.add(name);
		set.add(description);
	}

	@Override
	public Set<? extends PropertyReference> getSupportedSortReferences() {
		return null;
	}

	@Override
	public Set<PredicateReference> getKnownProperties() {
		return null;
	}	
}
