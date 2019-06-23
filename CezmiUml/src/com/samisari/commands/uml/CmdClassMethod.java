package com.samisari.commands.uml;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.samisari.cezmi.component.CmdText;

@XmlRootElement
public class CmdClassMethod extends CmdText {
	private static final long				serialVersionUID	= -6236627071292774193L;
//	private MethodDeclaration				astMethodDeclaration;
	private String							cmdClassId;
	private String							name;
	private String							returnType;
	private boolean							_public;
	private boolean							_protected;
	private boolean							_private;
	private boolean							_abstract;
	private boolean							_static;
	private boolean							_final;
	private boolean							_transient;
	private boolean							_volatile;
	private boolean							_synchronized;
	private boolean							constructor;
	private List<CmdClassMethodArgument>	arguments;
	private CmdCodeBlock					code;
	private String							javaDoc;

	public String getCmdClassId() {
		return cmdClassId;
	}

	public void setCmdClassId(String cmdClassId) {
		this.cmdClassId = cmdClassId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		setText();
	}

	private void setText() {
		setText((is_static() ? "S " : "  ") + ((is_private() ? "- " : (is_public() ? "+ " : (is_protected() ? "P " : "  ")))) + getName());

	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public boolean is_abstract() {
		return _abstract;
	}

	public void set_abstract(boolean _abstract) {
		this._abstract = _abstract;
	}

	public boolean is_static() {
		return _static;
	}

	public void set_static(boolean _static) {
		this._static = _static;
		setText();
	}

	public boolean is_final() {
		return _final;
	}

	public void set_final(boolean _final) {
		this._final = _final;
	}

	public boolean is_transient() {
		return _transient;
	}

	public void set_transient(boolean _transient) {
		this._transient = _transient;
	}

	public boolean is_volatile() {
		return _volatile;
	}

	public void set_volatile(boolean _volatile) {
		this._volatile = _volatile;
	}

	public boolean is_synchronized() {
		return _synchronized;
	}

	public void set_synchronized(boolean _synchronized) {
		this._synchronized = _synchronized;
	}

	public boolean isConstructor() {
		return constructor;
	}

	public void setConstructor(boolean constructor) {
		this.constructor = constructor;
	}

	public List<CmdClassMethodArgument> getArguments() {
		return arguments;
	}

	public void setArguments(List<CmdClassMethodArgument> arguments) {
		this.arguments = arguments;
	}

	public CmdCodeBlock getCode() {
		return code;
	}

	public void setCode(CmdCodeBlock code) {
		this.code = code;
	}

	public String getJavaDoc() {
		return javaDoc;
	}

	public void setJavaDoc(String javaDoc) {
		this.javaDoc = javaDoc;
	}

	public boolean is_public() {
		return _public;
	}

	public void set_public(boolean _public) {
		this._public = _public;
	}

	public boolean is_protected() {
		return _protected;
	}

	public void set_protected(boolean _protected) {
		this._protected = _protected;
	}

	public boolean is_private() {
		return _private;
	}

	public void set_private(boolean _private) {
		this._private = _private;
	}

//	public MethodDeclaration getAstMethodDeclaration() {
//		return astMethodDeclaration;
//	}
//
//	public void setAstMethodDeclaration(MethodDeclaration astMethodDeclaration) {
//		this.astMethodDeclaration = astMethodDeclaration;
//	}
}
