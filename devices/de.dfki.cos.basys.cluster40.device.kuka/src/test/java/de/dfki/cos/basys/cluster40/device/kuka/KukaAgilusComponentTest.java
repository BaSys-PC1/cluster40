package de.dfki.cos.basys.cluster40.device.kuka;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import de.dfki.cos.basys.cluster40.device.kuka.KukaAgilusComponent;
import de.dfki.cos.basys.platform.model.runtime.component.ComponentConfiguration;
import de.dfki.cos.basys.platform.model.runtime.component.State;
import de.dfki.cos.basys.platform.model.runtime.component.impl.ComponentConfigurationImpl;
import de.dfki.cos.basys.platform.model.runtime.component.impl.PropertyImpl;
import de.dfki.cos.basys.platform.runtime.component.ComponentContext;
import de.dfki.cos.basys.platform.runtime.component.ComponentException;
import de.dfki.cos.basys.platform.runtime.component.device.DeviceComponent;

public class KukaAgilusComponentTest {

	ComponentConfiguration componentConfig;
	ComponentContext emptyContext = new ComponentContext.Builder().build();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		componentConfig = new ComponentConfigurationImpl.Builder().componentId("scale-component").componentName("scale-component")
				.externalConnectionString("opc.tcp://127.0.0.1:12686/ScaleController").build();
		componentConfig.getProperties().add(new PropertyImpl.Builder().key("unittesting").value("true").build());
	}

	@After
	public void tearDown() throws Exception {
	}

	private void sleep(long seconds) {
		try {
			TimeUnit.MILLISECONDS.sleep(seconds*1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Test
	@Ignore
	public void testConnection() throws ComponentException {
		DeviceComponent component = new KukaAgilusComponent(componentConfig);
		assertTrue(!component.isConnectedToExternal());

		component.activate(emptyContext);
		assertTrue(component.isConnectedToExternal());
		
		component.deactivate();		
		assertTrue(!component.isConnectedToExternal());
	}

	@Test
	public void testComponentLifecycleComplete() throws ComponentException {
		DeviceComponent component = new KukaAgilusComponent(componentConfig);
		assertTrue(!component.isConnectedToExternal());

		component.activate(emptyContext);		
		assertTrue(component.isConnectedToExternal());		
		assertEquals(State.STOPPED, component.getState(true));
		
		component.reset();		
		assertEquals(State.RESETTING, component.getState(true));
		assertEquals(State.IDLE, component.getState(true));
				
		component.start();	
		assertEquals(State.STARTING, component.getState(true));			
		assertEquals(State.EXECUTE, component.getState(true));		
		
		sleep(10);
		
		component.signalExecuteComplete();
		assertEquals(State.COMPLETING, component.getState(true));
		assertEquals(State.COMPLETE, component.getState(true));
		
		component.deactivate();
		assertTrue(!component.isConnectedToExternal());
	}
	
}
