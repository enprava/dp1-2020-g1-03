/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Gasto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration test of the Service and the Repository layer.
 * <p>
 * ClinicServiceSpringDataJpaTests subclasses benefit from the following
 * services provided by the Spring TestContext Framework:
 * </p>
 * <ul>
 * <li><strong>Spring IoC container caching</strong> which spares us unnecessary
 * set up time between test execution.</li>
 * <li><strong>Dependency Injection</strong> of test fixture instances, meaning
 * that we don't need to perform application context lookups. See the use of
 * {@link Autowired @Autowired} on the <code>{@link
 * ClinicServiceTests#clinicService clinicService}</code> instance variable,
 * which uses autowiring <em>by type</em>.
 * <li><strong>Transaction management</strong>, meaning each test method is
 * executed in its own transaction, which is automatically rolled back by
 * default. Thus, even if tests insert or otherwise change database state, there
 * is no need for a teardown or cleanup script.
 * <li>An {@link org.springframework.context.ApplicationContext
 * ApplicationContext} is also inherited and can be used for explicit bean
 * lookup if necessary.</li>
 * </ul>
 *
 * @author Ken Krebs
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 * @author Dave Syer
 */

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class GastoServiceTests {

	@Autowired
	private GastoService		gastoService;

	@Autowired
	private EconomistaService	economistaService;


	@Test
	void shouldFindGastoWithCorrectId() {
		Gasto gasto1 = this.gastoService.findGastoById(1);
		Assertions.assertEquals(gasto1.getDescription(), "Gasto correspondiente a la compra de material esterilizante para la clinica");
		Assertions.assertEquals(gasto1.getEconomista().getId(), 1);
	}

	@Test
	void shouldNotFindGastoWithIncorrectId() {
		Assertions.assertNull(this.gastoService.findGastoById(100));
	}

	@Test
	void shouldFindAllGastos() {
		Collection<Gasto> gastos = this.gastoService.findAllGastosS();

		Assertions.assertEquals(gastos.size(), 3);
	}


	@Test
	@Transactional
	public void shouldUpdateVisitDate() throws Exception {
		Gasto gasto2 = this.gastoService.findGastoById(2);

		LocalDate newDate = LocalDate.parse("2021-12-15");
		gasto2.setFecha(newDate);
		this.gastoService.saveGasto(gasto2);

		Gasto gastoRev = this.gastoService.findGastoById(2);
		Assert.assertTrue(gastoRev.getFecha().isEqual(newDate));
	}

}