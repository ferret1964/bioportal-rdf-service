package edu.mayo.cts2.framework.plugin.service.bprdf.profile.codesystem;

import static org.junit.Assert.*

import javax.annotation.Resource
import javax.xml.transform.stream.StreamResult

import org.junit.Test

import edu.mayo.cts2.framework.core.xml.Cts2Marshaller
import edu.mayo.cts2.framework.model.codesystem.CodeSystemCatalogEntry
import edu.mayo.cts2.framework.model.util.ModelUtils

class BioportalRdfCodeSystemReadServiceByNameTestIT extends BioportalRdfCodeSystemReadServiceTestITBase {

	@Resource
	Cts2Marshaller marshaller
	
	@Override
	public CodeSystemCatalogEntry doRead() {
		def cs = read.read(ModelUtils.nameOrUriFromName("GO-1070"), null)
		
		cs
	}
	
	@Test
	void TestCodeSystemWithHyhpenName() {
		def cs = read.read(ModelUtils.nameOrUriFromName("FDA-MedDevice-1576"), null)
		
		assertNotNull cs
		
		assertEquals cs.codeSystemName, "FDA-MedDevice-1576"
	}

	@Test
	void TestDoRead() {
		def cs = doRead()
		
		assertNotNull cs
	}
	
	@Test
	void TestValidXml() {
		def cs = doRead()
		
		assertNotNull cs

		marshaller.marshal(cs, new StreamResult(new StringWriter()))
	}

	@Test
	void doReadByInvalid() {
		assertNull read.read(ModelUtils.nameOrUriFromName("TEST-INVALID"), null)
	}

}
