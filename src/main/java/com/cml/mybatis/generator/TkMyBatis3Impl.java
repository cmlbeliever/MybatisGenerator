package com.cml.mybatis.generator;

import java.lang.reflect.Field;
import java.text.MessageFormat;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.codegen.mybatis3.IntrospectedTableMyBatis3Impl;

//MyBatis3 的实现
public class TkMyBatis3Impl extends IntrospectedTableMyBatis3Impl {

	private String originDomainObjectName;

	@Override
	protected String calculateMyBatis3XmlMapperFileName() {
		StringBuilder sb = new StringBuilder();
		if (stringHasValue(tableConfiguration.getMapperName())) {
			String mapperName = tableConfiguration.getMapperName();
			int ind = mapperName.lastIndexOf('.');
			if (ind != -1) {
				mapperName = mapperName.substring(ind + 1);
			}
			// sb.append(MessageFormat.format(mapperName,
			// fullyQualifiedTable.getDomainObjectName()));

			sb.append(formatMapperName(originDomainObjectName));
			sb.append(".sql.xml"); //$NON-NLS-1$
		} else {
			sb.append(fullyQualifiedTable.getDomainObjectName());
			sb.append("Mapper.xml"); //$NON-NLS-1$
		}
		return sb.toString();
	}

	private boolean stringHasValue(String mapperName) {
		return mapperName != null && mapperName.trim().length() > 0;
	}

	@Override
	public void setFullyQualifiedTable(FullyQualifiedTable fullyQualifiedTable) {
		try {
			originDomainObjectName = fullyQualifiedTable.getDomainObjectName();
			Field domainObjectNameField = fullyQualifiedTable.getClass().getDeclaredField("domainObjectName");
			domainObjectNameField.setAccessible(true);
			domainObjectNameField.set(fullyQualifiedTable, formatBeanBame(fullyQualifiedTable.getDomainObjectName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.setFullyQualifiedTable(fullyQualifiedTable);
	}

	@Override
	protected void calculateJavaClientAttributes() {
		if (context.getJavaClientGeneratorConfiguration() == null) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append(calculateJavaClientImplementationPackage());
		sb.append('.');
		sb.append(fullyQualifiedTable.getDomainObjectName());
		sb.append("DAOImpl"); //$NON-NLS-1$
		setDAOImplementationType(sb.toString());

		sb.setLength(0);
		sb.append(calculateJavaClientInterfacePackage());
		sb.append('.');
		sb.append(fullyQualifiedTable.getDomainObjectName());
		sb.append("DAO"); //$NON-NLS-1$
		setDAOInterfaceType(sb.toString());

		sb.setLength(0);
		sb.append(calculateJavaClientInterfacePackage());
		sb.append('.');
		if (stringHasValue(tableConfiguration.getMapperName())) {
			// 支持mapperName 格式化功能
			sb.append(formatMapperName(fullyQualifiedTable.getDomainObjectName()));
		} else {
			sb.append(fullyQualifiedTable.getDomainObjectName());
			sb.append("Mapper"); //$NON-NLS-1$
		}
		setMyBatis3JavaMapperType(sb.toString());

		sb.setLength(0);
		sb.append(calculateJavaClientInterfacePackage());
		sb.append('.');
		if (stringHasValue(tableConfiguration.getSqlProviderName())) {
			// 支持mapperName = "{0}SqlProvider" 等用法
			sb.append(MessageFormat.format(tableConfiguration.getSqlProviderName(), fullyQualifiedTable.getDomainObjectName()));
		} else {
			sb.append(fullyQualifiedTable.getDomainObjectName());
			sb.append("SqlProvider"); //$NON-NLS-1$
		}
		setMyBatis3SqlProviderType(sb.toString());
	}

	private String formatMapperName(String mapperName) {
		return MessageFormat.format(context.getProperty("mapperNameFormat"), mapperName);
	}

	private String formatBeanBame(String beanName) {
		return MessageFormat.format(context.getProperty("beanNameFormat"), beanName);
	}
}