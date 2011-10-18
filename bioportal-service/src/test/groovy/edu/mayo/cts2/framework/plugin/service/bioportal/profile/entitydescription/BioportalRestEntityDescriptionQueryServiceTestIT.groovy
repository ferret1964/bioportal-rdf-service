package edu.mayo.cts2.framework.plugin.service.bioportal.profile.entitydescription

import static org.junit.Assert.*

import javax.annotation.Resource

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import edu.mayo.cts2.framework.model.core.FilterComponent
import edu.mayo.cts2.framework.model.core.MatchAlgorithmReference
import edu.mayo.cts2.framework.model.core.URIAndEntityName
import edu.mayo.cts2.framework.service.command.Page

@RunWith(SpringJUnit4ClassRunner)
@ContextConfiguration(locations="/bioportal-test-context-non-webapp.xml")
public class BioportalRestEntityDescriptionQueryServiceTestIT {
	
	@Resource
	private BioportalRestEntityDescriptionQueryService service

	@Test
	public void testGetPageCorrectlyCallBioportal(){
			MatchAlgorithmReference matchAlgorithm = new MatchAlgorithmReference(content:"contains")
		
		FilterComponent filter = 
			new FilterComponent(
				referenceTarget: new URIAndEntityName(),
				matchAlgorithm: matchAlgorithm, 
				matchValue: "test")

		def result = 
			service.getAllEntityDescriptions(null, filter, new Page(page:2,maxtoreturn:10))
			
		assertEquals 10, result.getEntries().size()
		
	}
}
