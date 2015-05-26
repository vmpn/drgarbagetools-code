package testcommandlinetool;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ testCommandLineTool.class, testCommandLineToolCore.class,
		testParseArguments.class })
public class AllTestSuite {

}
