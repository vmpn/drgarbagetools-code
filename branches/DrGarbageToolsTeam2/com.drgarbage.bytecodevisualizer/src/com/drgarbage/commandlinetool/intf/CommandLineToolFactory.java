package com.drgarbage.commandlinetool.intf;

import com.drgarbage.commandlinetool.impl.ByteCodeConfiguration;
import com.drgarbage.commandlinetool.impl.CommandLineToolImplementation;
import com.drgarbage.commandlinetool.impl.GraphConfiguration;

public class CommandLineToolFactory {
	public static ICommandLineTool createCommandLineToolInterface(){
		return new CommandLineToolImplementation();
	}
	public static IByteCodeConfiguration createByteCodeConfigurationInterface(String arg){
		return new ByteCodeConfiguration(arg);
	}
	public static IByteCodeConfiguration createByteCodeConfigurationInterface(){
		return createByteCodeConfigurationInterface(null);
	}
	public static IGraphConfiguration createGraphConfigurationInterface(String arg){
		return new GraphConfiguration();
	}
	public static IGraphConfiguration createGraphConfigurationInterface(){
		return createGraphConfigurationInterface(null);
	}
}
