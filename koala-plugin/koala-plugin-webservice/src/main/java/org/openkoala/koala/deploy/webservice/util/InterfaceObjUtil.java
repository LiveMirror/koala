package org.openkoala.koala.deploy.webservice.util;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.ImportDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.expr.AnnotationExpr;
import japa.parser.ast.expr.ArrayInitializerExpr;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.expr.NormalAnnotationExpr;
import japa.parser.ast.expr.SingleMemberAnnotationExpr;
import japa.parser.ast.expr.StringLiteralExpr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.openkoala.koala.deploy.webservice.pojo.InterfaceObj;
import org.openkoala.koala.deploy.webservice.pojo.MediaType;
import org.openkoala.koala.deploy.webservice.pojo.ParamType;
import org.openkoala.koala.deploy.webservice.pojo.RestWebServiceMethod;
import org.openkoala.koala.deploy.webservice.pojo.WebServiceMethod;
import org.openkoala.koala.deploy.webservice.pojo.WebServiceType;
import org.openkoala.koala.exception.KoalaException;
import org.openkoala.koala.java.JavaManagerUtil;
import org.openkoala.koala.java.JavaSaver;
import org.openkoala.koala.util.FileOperator;

/**
 * 对一个接口进行修改,加入WS的配置
 * @author lingen
 *
 */
public class InterfaceObjUtil {
	
	/**
	 * 
	 * @param interfaceObj
	 * @throws KoalaException 
	 */
	public static void updateInterfaceObj(InterfaceObj interfaceObj) throws KoalaException {
	    List<String> selectedMethods = interfaceObj.getSelectedMethods();
	    if(selectedMethods == null || selectedMethods.isEmpty()) {
	    	return;
	    }
	    
		String javasrc = interfaceObj.getQualifiedName();
		CompilationUnit cu;
		try {
			File file = new File(javasrc);
			cu = JavaParser.parse(file);
			if(cu.getImports()==null){
				cu.setImports(new ArrayList<ImportDeclaration>());
			}
			cu.getImports().add(new ImportDeclaration(new NameExpr("javax.ws.rs.*"),false,false));
			cu.getImports().add(new ImportDeclaration(new NameExpr("javax.ws.rs.core.*"),false,false));
			cu.getImports().add(new ImportDeclaration(new NameExpr("javax.ws.rs.ext.*"),false,false));
			cu.getImports().add(new ImportDeclaration(new NameExpr("org.openkoala.exception.extend.KoalaException"),false,false));
			
			List<MethodDeclaration> methods = JavaManagerUtil.getMethodDeclaration(cu);
			List<WebServiceMethod> webMethods = interfaceObj.getMethods();
			for(MethodDeclaration method : methods){
				
				RestWebServiceMethod webServiceMethod=null;
				for(WebServiceMethod webMethod : webMethods){
					if(webMethod.getName().equals(JavaManagerUtil.methodDescription(method))){
						webServiceMethod = (RestWebServiceMethod) webMethod;
						break;
					}
				}
				
				if(!selectedMethods.contains(JavaManagerUtil.methodDescription(method))) {
					continue;
				}
				
				addKoalaExceptionThrows(method);
				
				if(webServiceMethod.getType() == WebServiceType.GET){
					addAnnotationToMethodByConfig(method, webServiceMethod, new NormalAnnotationExpr(new NameExpr("GET"), null));
				} else if(webServiceMethod.getType().equals(WebServiceType.POST)){
					addAnnotationToMethodByConfig(method, webServiceMethod, new NormalAnnotationExpr(new NameExpr("POST"), null));
				} else if (webServiceMethod.getType().equals(WebServiceType.PUT)){
					addAnnotationToMethodByConfig(method, webServiceMethod, new NormalAnnotationExpr(new NameExpr("PUT"), null));
				} else if (webServiceMethod.getType().equals(WebServiceType.DELETE)){
					addAnnotationToMethodByConfig(method, webServiceMethod, new NormalAnnotationExpr(new NameExpr("DELETE"), null));
				}
			}
			JavaSaver.saveToFile(javasrc, cu);
			
			remveNoPublishMethod(javasrc, selectedMethods);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void addKoalaExceptionThrows(MethodDeclaration method){
		if(method.getThrows()==null){
			List<NameExpr> throwsList = new ArrayList<NameExpr>();
			throwsList.add(new NameExpr("KoalaException"));
			method.setThrows(throwsList);
		}
	}
	
	private static void addAnnotationToMethodByConfig(MethodDeclaration method, 
			RestWebServiceMethod restWebServiceMethod, AnnotationExpr httpMethodAnnotation) {
		
		addHttpMethodAnnotation(method, restWebServiceMethod, httpMethodAnnotation);
		addPahtUriAnnotation(method, restWebServiceMethod);
		addParameAnnotation(method, restWebServiceMethod);
		addProducesAnnotation(method, restWebServiceMethod);
		addConsumesAnnotation(method, restWebServiceMethod);
	}
	
	private static void addHttpMethodAnnotation(MethodDeclaration method, 
			RestWebServiceMethod restWebServiceMethod, AnnotationExpr httpMethodAnnotation) {
		if(JavaManagerUtil.containsAnnotation(method, restWebServiceMethod.getType().getAnnotationString()) == false) {
			if(method.getAnnotations()==null){
				method.setAnnotations(new ArrayList<AnnotationExpr>());
			}
			method.getAnnotations().add(httpMethodAnnotation);
		}
	}
	
	private static void addPahtUriAnnotation(MethodDeclaration method, RestWebServiceMethod restWebServiceMethod) {
		AnnotationExpr pathAnnotation = new SingleMemberAnnotationExpr(new NameExpr("Path"), new StringLiteralExpr(restWebServiceMethod.getUriPath()));
		if(JavaManagerUtil.containsAnnotation(method,"Path")== false) {
			if(method.getAnnotations()==null){
				method.setAnnotations(Collections.<AnnotationExpr> emptyList());
			}
			method.getAnnotations().add(pathAnnotation);
		}
	}
	
	private static void addParameAnnotation(MethodDeclaration method, RestWebServiceMethod restWebServiceMethod) {
		List<Parameter> parameters = method.getParameters();
		if (parameters != null) {
			for(Parameter parameter : parameters){
				ParamType paramType = restWebServiceMethod.getParamType();
				String paramName = "";
				if (!AutoRestRuleUtils.isReferenceType(parameter)) {
					paramName = parameter.getId().getName();
				}
				
				AnnotationExpr paramTypeAnnotation = new SingleMemberAnnotationExpr(
								new NameExpr(paramType.getParamTransferType()), new StringLiteralExpr(paramName));
				if(parameter.getAnnotations()==null){
					parameter.setAnnotations(new ArrayList<AnnotationExpr>());
				}
				parameter.getAnnotations().add(paramTypeAnnotation);
			}
		}
	}
	
	private static void addProducesAnnotation(MethodDeclaration method, RestWebServiceMethod restWebServiceMethod) {
		addDataTransferTypeAnnotation(method, restWebServiceMethod.getProducesMediaTypes(), true);
	}
	
	private static void addConsumesAnnotation(MethodDeclaration method, RestWebServiceMethod restWebServiceMethod) {
		addDataTransferTypeAnnotation(method, restWebServiceMethod.getConsumesMediaTypes(), false);
	}

	private static void addDataTransferTypeAnnotation(MethodDeclaration method,	Set<MediaType> mediaTypes, boolean isProduces) {
		if (mediaTypes == null || mediaTypes.isEmpty()) {
			return;
		}
		
		List<Expression> values = new ArrayList<Expression>();
		for (MediaType mediaType : MediaType.values()) {
			if (mediaTypes.contains(mediaType)) {
				values.add(new NameExpr("MediaType." + mediaType.name()));
			}
		}
		
		if (isProduces) {
			AnnotationExpr producesAnnotation = new SingleMemberAnnotationExpr(new NameExpr("Produces"),new ArrayInitializerExpr(values));
			if(JavaManagerUtil.containsAnnotation(method,"Produces") == false) {
				if(method.getAnnotations()==null){
					method.setAnnotations(new ArrayList<AnnotationExpr>());
				}
				method.getAnnotations().add(producesAnnotation);
			}
		} else {
			AnnotationExpr consumesAnnotation = new SingleMemberAnnotationExpr(new NameExpr("Consumes"),new ArrayInitializerExpr(values));
			if(JavaManagerUtil.containsAnnotation(method,"Consumes") == false) {
				if(method.getAnnotations()==null){
					method.setAnnotations(new ArrayList<AnnotationExpr>());
				}
				method.getAnnotations().add(consumesAnnotation);
			}
		}
	}

	private static void remveNoPublishMethod(String javaFilePath, List<String> selectedMethods) throws ParseException, IOException {
		File file = new File(javaFilePath);
		CompilationUnit compilationUnit = JavaParser.parse(file);
		List<MethodDeclaration> methods = JavaManagerUtil.getMethodDeclaration(compilationUnit);
		for(MethodDeclaration method : methods){
			if(!selectedMethods.contains(JavaManagerUtil.methodDescription(method))) {
				FileOperator.removeLinesFromFile(javaFilePath, method.getBeginLine(), method.getEndLine());
			}
		}
	}
	
	public static void updateSoapInterfaceObj(InterfaceObj interfaceObj) throws KoalaException {
		List<String> selectedMethods = interfaceObj.getSelectedMethods();
		
	    if(selectedMethods == null || selectedMethods.isEmpty()) {
	    	return;
	    }
	    
		String javasrc = interfaceObj.getQualifiedName();
		CompilationUnit compilationUnit;
		try {
			File file = new File(javasrc);
			compilationUnit = JavaParser.parse(file);
			ClassOrInterfaceDeclaration coi = JavaManagerUtil.getClassOrInterfaceDeclaration(compilationUnit);
			
			List<MethodDeclaration> methods = JavaManagerUtil.getMethodDeclaration(compilationUnit);
			for(MethodDeclaration method:methods){
				if(!selectedMethods.contains(JavaManagerUtil.methodDescription(method))) {
					continue;
				}
				addKoalaExceptionThrows(method);
			}
			
			compilationUnit.getImports().add(new ImportDeclaration(new NameExpr("javax.jws.WebService"),false,false));
			compilationUnit.getImports().add(new ImportDeclaration(new NameExpr("org.openkoala.exception.extend.KoalaException"),false,false));
			
			AnnotationExpr webServiceAnnotation = new SingleMemberAnnotationExpr(new NameExpr("WebService"), new NameExpr(""));
			if(JavaManagerUtil.containsAnnotation(coi,"WebService") == false) {
				if(coi.getAnnotations()==null){
					coi.setAnnotations(new ArrayList<AnnotationExpr>());
				}
				coi.getAnnotations().add(webServiceAnnotation);
			}
			
			JavaSaver.saveToFile(javasrc, compilationUnit);

			remveNoPublishMethod(javasrc, selectedMethods);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
