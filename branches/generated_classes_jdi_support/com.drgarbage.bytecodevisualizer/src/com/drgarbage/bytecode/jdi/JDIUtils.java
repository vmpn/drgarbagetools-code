/**
 * Copyright (c) 2008-2013, Dr. Garbage Community
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

package com.drgarbage.bytecode.jdi;

import com.sun.jdi.ReferenceType;

/**
 * The utility routings for JDI handling.
 * 
 * @author Sergej Alekseev
 * @version $Revision$
 * $Id$
 */
public class JDIUtils {

	/**
	 * Returns the class file content as a byte array for
	 * the given JDI ReferenceType object.
	 * 
	 * @param ref ReferenceType object
	 * @return byte array
	 */
	public static byte[] getClassFileContent(ReferenceType ref){
//		return new byte[0];
		
		/*
		ClassFile {
		u4             magic;
		u2             minor_version;
		u2             major_version;
		u2             constant_pool_count;
		cp_info        constant_pool[constant_pool_count-1];
		u2             access_flags;
		u2             this_class;
		u2             super_class;
		u2             interfaces_count;
		u2             interfaces[interfaces_count];
		u2             fields_count;
	    field_info     fields[fields_count];
	    u2             methods_count;
	    method_info    methods[methods_count];
	    u2             attributes_count;
	    attribute_info attributes[attributes_count];
	 }
	 */
		 
		
		
		short u4_magic_len = 4;
		short u2_minor_version_len = 2;
		short u2_major_version_len = 2;
		short constant_pool_count_len = 2;
		short u2_access_flags_len = 2;
		short u2_this_class_len = 2;
		short u2_super_class_len = 2;
		short u2_interfaces_count_len = 2;
		short u2_fields_count_len = 2;
		short u2_methods_count_len = 2;
		short u2_attributes_count_len = 2;

		byte[]  constantPool = ref.constantPool();
		
		int array_length = 
				u4_magic_len + 
				u2_minor_version_len + 
				u2_major_version_len +
				constant_pool_count_len +
				constantPool.length +
				u2_access_flags_len +
				u2_this_class_len +
				u2_super_class_len +
				u2_interfaces_count_len +
				u2_fields_count_len +
				u2_methods_count_len +
				u2_attributes_count_len
				; 
		
		byte[] classFile = new byte[array_length];
		
		int offset = 0;
		
		/* u4 magic; */
		classFile[offset++] = (byte) ((12 << 4) + 10);
		classFile[offset++] = (byte) ((15 << 4) + 14);
		classFile[offset++] = (byte) ((11 << 4) + 10);
		classFile[offset++] = (byte) ((11 << 4) + 14);
		
		/* u2 minor_version; */
		classFile[offset++] = 0x0;
		classFile[offset++] = 0x0; //TODO: assign minor version
		
		/* u2 major_version; */
		classFile[offset++] = 0x0;
		classFile[offset++] = 0x32; //TODO: assign major version
		
		/* u2 constant_pool_count; */
		classFile[offset++] = 0x0;
		classFile[offset++] = (byte)ref.constantPoolCount(); //TODO: assign count
		
		/* cp_info constant_pool[constant_pool_count-1]; */
		for(int i = 0; i < constantPool.length; i++){
			classFile[offset++] = constantPool[i];
		}
		
		/* u2             access_flags; */
		classFile[offset++] = 0x0;
		classFile[offset++] = (byte)ref.modifiers(); //TODO: assign access flags
		
		
		/* u2             this_class; */
		classFile[offset++] = 0x0;
		classFile[offset++] = 0x2;	//TODO: assign this class index
		
		
		/* u2             super_class; */
		classFile[offset++] = 0x0;
		classFile[offset++] = 0x4; //TODO: assign super class index
		
		/* u2             interfaces_count; */
		classFile[offset++] = 0x0;
		classFile[offset++] = 0x0;
		
		/* u2             interfaces[interfaces_count]; */
		//TODO: implement interfaces
		
		/* u2             fields_count; */
		classFile[offset++] = 0x0;
		classFile[offset++] = 0x0;
		
	    /* field_info     fields[fields_count]; */
		//TODO: implement field_info
	    
		/* u2             methods_count; */
		classFile[offset++] = 0x0;
		classFile[offset++] = 0x0;
		
	    /* method_info    methods[methods_count]; */
		//TODO: implement method_info
		
		/* u2             attributes_count; */
		classFile[offset++] = 0x0;
		classFile[offset++] = 0x0;
		
	    /* attribute_info attributes[attributes_count]; */
		//TODO: implement attribute_info
		
		return classFile;
	}
}
