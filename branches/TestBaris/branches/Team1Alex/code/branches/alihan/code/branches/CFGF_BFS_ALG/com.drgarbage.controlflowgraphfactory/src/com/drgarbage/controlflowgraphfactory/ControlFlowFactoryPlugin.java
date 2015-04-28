/**
 * Copyright (c) 2008-2012, Dr. Garbage Community
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drgarbage.controlflowgraphfactory;

import org.eclipse.osgi.framework.internal.core.AbstractBundle;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.drgarbage.controlflowgraphfactory.actions.ExternalActionGenerateGraph;
import com.drgarbage.controlflowgraphfactory.preferences.ControlFlowFactoryPreferenceConstants;
import com.drgarbage.core.CorePlugin;

/**
 * The activator class controls the plug-in life cycle
 */
public class ControlFlowFactoryPlugin extends AbstractUIPlugin {

	/**
	 * The plug-in ID. 
	 */
	public static final String PLUGIN_ID = "com.drgarbage.controlflowgraphfactory";

	/**
	 * The plug.in version.
	 */
	public static String PLUGIN_VERSION;
	
	
	/**
	 * The shared instance.
	 */
	private static ControlFlowFactoryPlugin plugin;
		
	/**
	 * The constructor
	 */
	public ControlFlowFactoryPlugin() {
		plugin = this;
		
		/* install communication object to the bytecode vizualizer plugin */
		CorePlugin.getDefault().addExternalComunicationObject(PLUGIN_ID, new ExternalActionGenerateGraph());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		
		Bundle bundle = context.getBundle();
		if(bundle instanceof AbstractBundle){
			AbstractBundle aBundle = (AbstractBundle) bundle;
			PLUGIN_VERSION = aBundle.getVersion().toString();
		}

		// FIXME: use org.eclipse.core.runtime.preferences extension for this
		initPreferenceStore();
	}
	
	/**
	 * Initialize Defaults.
	 */
	private void initPreferenceStore(){
		/* set default */
		getPreferenceStore().setDefault(ControlFlowFactoryPreferenceConstants.USE_GRADIENT_FILL_COLOR, true);
		getPreferenceStore().setDefault(ControlFlowFactoryPreferenceConstants.GENERATE_START_NODE, true);
		getPreferenceStore().setDefault(ControlFlowFactoryPreferenceConstants.GENERATE_EXIT_NODE, true);

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static ControlFlowFactoryPlugin getDefault() {
		return plugin;
	}
}
