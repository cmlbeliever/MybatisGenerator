package com.cml.mybatis.generator;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Properties;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.springframework.util.ReflectionUtils;

public class MyPlugin extends PluginAdapter {

	public MyPlugin() {
	}

	public boolean validate(List<String> warnings) {
		// false 表示此plugin不执行
		return false;
	}

	@Override
	public void initialized(IntrospectedTable introspectedTable) {
	}

	@Override
	public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
		// 重命名bean
		Field field = ReflectionUtils.findField(topLevelClass.getClass(), "type");
		field.setAccessible(true);
		String fullyBeanName = null;
		try {
			FullyQualifiedJavaType oldType = topLevelClass.getType();
			fullyBeanName = oldType.getPackageName() + "." + oldType.getShortName();
			field.set(topLevelClass, new FullyQualifiedJavaType(fullyBeanName));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
		}

		// 重命名Mapper对应bean
		introspectedTable.setDAOImplementationType(fullyBeanName);
		return true;
	}

}
