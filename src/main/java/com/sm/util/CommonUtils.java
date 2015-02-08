package com.sm.util;

import java.util.UUID;

public class CommonUtils {
	
	public static String genUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}

}
