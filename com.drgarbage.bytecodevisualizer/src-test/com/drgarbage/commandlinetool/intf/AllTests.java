package com.drgarbage.commandlinetool.intf;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ IArgumentParserTest.class, IByteCodeConfigurationTest.class,
		ICommandLineToolTest.class, IGraphConfigurationTest.class })
public class AllTests {

}
