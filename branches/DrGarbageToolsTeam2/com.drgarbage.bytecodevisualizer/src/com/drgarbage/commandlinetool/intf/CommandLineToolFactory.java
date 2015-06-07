package com.drgarbage.commandlinetool.intf;

import com.drgarbage.commandlinetool.impl.CommandLineToolImplementation;

public class CommandLineToolFactory {
	public static ICommandLineTool createCommandLineToolInterface(){
		return new CommandLineToolImplementation();
	}
}
