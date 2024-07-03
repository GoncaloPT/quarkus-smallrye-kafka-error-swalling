package org.acme;

import io.quarkus.test.junit.QuarkusIntegrationTest;


/**
 * This test will run all the tests in GreetingResourceTest but in a containerized application context.
 */
@QuarkusIntegrationTest
class GreetingResourceIT extends GreetingResourceTest {

}
