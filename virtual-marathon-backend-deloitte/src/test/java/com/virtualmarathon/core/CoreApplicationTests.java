package com.virtualmarathon.core;

import com.virtualmarathon.core.service.EmailSenderServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * By using Suite we run all test cases in a single run.
 */
@Suite
@SelectClasses({UserServicesTest.class,TrackingServicesTest.class,RoleServiceTest.class,EmailSenderTest.class,ImageServiceTest.class,EventServicesTest.class})
@SpringBootTest
class CoreApplicationTests {

	@Test
	void contextLoads() {
	}

}
