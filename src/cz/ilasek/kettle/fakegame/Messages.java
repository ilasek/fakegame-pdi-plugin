package cz.ilasek.kettle.fakegame;

import org.pentaho.di.i18n.BaseMessages;

public class Messages
{
	public static final String packageName = Messages.class.getPackage().getName();

	public static String getString(String key, String...parameters)
	{
		return BaseMessages.getString(packageName, key, Messages.class, parameters);
	}

//	public static String getString(String key, String param1)
//	{
//		return BaseMessages.getString(packageName, key, Messages.class, param1);
//	}
//
//	public static String getString(String key, String param1, String param2)
//	{
//		return BaseMessages.getString(packageName, key, Messages.class, param1, param2);
//	}
//
//	public static String getString(String key, String param1, String param2, String param3)
//	{
//		return BaseMessages.getString(packageName, key, Messages.class, param1, param2, param3);
//	}
//
//	public static String getString(String key, String param1, String param2, String param3, String param4)
//	{
//		return BaseMessages.getString(packageName, key, Messages.class, param1, param2, param3, param4);
//	}
//
//	public static String getString(String key, String param1, String param2, String param3, String param4, String param5)
//	{
//		return BaseMessages.getString(packageName, key, Messages.class, param1, param2, param3, param4, param5);
//	}
//
//	public static String getString(String key, String param1, String param2, String param3, String param4, String param5, String param6)
//	{
//		return BaseMessages.getString(packageName, key, Messages.class, param1, param2, param3, param4, param5, param6);
//	}
}