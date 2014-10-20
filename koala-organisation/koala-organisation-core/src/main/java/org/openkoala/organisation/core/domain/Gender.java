package org.openkoala.organisation.core.domain;

/**
 * 性别枚举
 * 
 * @author xmfang
 * 
 */
public enum Gender {

	MALE("男"),
	FEMALE("女");

	private String label;

	private Gender(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}

	public static Gender getByLabel(String label) {
		for (Gender each : Gender.values()) {
			if (each.getLabel().equals(label)) {
				return each;
			}
		}
		return null;
	}

}
