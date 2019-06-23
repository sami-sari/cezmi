package com.samisari.cezmi.desktop;

import java.util.List;

public class CezmiRule implements ICezmiRuleComponent {
	private String							ruleName;
	private List<List<ICezmiRuleComponent>>	ruleValue;
	private Runnable						action;

}
