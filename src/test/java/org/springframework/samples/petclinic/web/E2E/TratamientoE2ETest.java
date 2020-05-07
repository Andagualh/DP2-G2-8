package org.springframework.samples.petclinic.web.E2E;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TratamientoE2ETest {
	
	@Autowired
	private MockMvc tratamientoService;
	
	@WithMockUser(username="alvaroMedico",authorities= {"admin"})
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		//mockMvc.perform
	}

}
