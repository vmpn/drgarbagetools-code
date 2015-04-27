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

package com.drgarbage.core.preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Link;

import com.drgarbage.core.jface.LinkFactory;

public class PreferencePageLinkField extends HttpPathLinkField {

	public PreferencePageLinkField(String name, String labelText,
			Composite parent) {
		super(name, labelText, parent);
	}

	@Override
	protected Link createLink(Composite parent) {
		return LinkFactory.createPreferencePageLink(parent, SWT.NONE);
	}

}
