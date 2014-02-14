package org.openkoala.bpm.designer.web.action.jbpm;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.inject.Inject;

import org.openkoala.bpm.application.vo.ProcessVO;
import org.openkoala.bpm.designer.application.BpmDesignerApplication;
import org.openkoala.bpm.designer.application.GunvorApplication;
import org.openkoala.bpm.designer.application.dto.Bpmn2;
import org.openkoala.bpm.designer.application.dto.PackageVO;
import org.openkoala.bpm.designer.application.dto.PublishURLVO;

public class JbpmAction {
	
	@Inject
	private GunvorApplication gunvorApplication;

	@Inject
	private BpmDesignerApplication publishURLApplication;

	private List<PackageVO> packages;

	private List<Bpmn2> bpmns;

	private String packageName;

	private String description;

	private String bpmnName;

	private int result;

	private String results;

	private List<String> names;

	private List<PublishURLVO> urls;

	private String wsdl;

	private List<ProcessVO> processes;

	private int id;

	private String errors;

	private String gunvorServerUrl;

	public String findPackages() {
		try {
			gunvorServerUrl = gunvorApplication.getGunvorServerUrl();
			packages = gunvorApplication.getPackages();
		} catch (Exception e) {
			this.errors = e.getMessage();
			e.printStackTrace();
		}
		return "json2";
	}

	public String createBpmn() {
		try {
			results = gunvorApplication.createBpm(
					URLEncoder.encode(packageName, "UTF-8"),
					URLEncoder.encode(bpmnName, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			this.errors = e.getMessage();
			e.printStackTrace();
		}
		return "json2";
	}

	public String findBpmns() {
		try {
			this.bpmns = gunvorApplication.getBpmn2s(packageName);
		} catch (Exception e) {
			this.errors = e.getMessage();
			e.printStackTrace();
		}
		return "json2";
	}

	public String createPackage() {
		try {
			gunvorApplication.createPackage(packageName, description);
			result = 1;
		} catch (Exception e) {
			this.errors = e.getMessage();
			e.printStackTrace();
		}
		return "json2";
	}

	public String deleteBpmn() {
		try {
			gunvorApplication.deleteBpmn(packageName, bpmnName);
			result = 1;
		} catch (Exception e) {
			this.errors = e.getMessage();
			e.printStackTrace();
		}
		return "json2";
	}

	public String deletePackage() {
		
		try {
			gunvorApplication.deletePackage(packageName);
			result = 1;
		} catch (Exception e) {
			this.errors = e.getMessage();
			e.printStackTrace();
		}
		return "json2";
	}

	public String getPublish() {
		try {
			this.urls = publishURLApplication.findAllPublishURL();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "json2";
	}

	public String publish() {
		try {
			this.gunvorApplication.publichJBPM(packageName, bpmnName, wsdl);
			result = 1;
		} catch (Exception e) {
			this.errors = e.getMessage();
			e.printStackTrace();
		}
		return "json2";
	}

	public List<PackageVO> getPackages() {
		return packages;
	}

	public void setPackages(List<PackageVO> packages) {
		this.packages = packages;
	}

	public List<Bpmn2> getBpmns() {
		return bpmns;
	}

	public void setBpmns(List<Bpmn2> bpmns) {
		this.bpmns = bpmns;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBpmnName() {
		return bpmnName;
	}

	public void setBpmnName(String bpmnName) {
		this.bpmnName = bpmnName;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getResults() {

		return results;
	}

	public void setResults(String results) {

		this.results = results;
	}

	public String processes() {
		// TODO 获取某个流程引擎中的所有流程信息
		return "json2";
	}

	public List<String> getNames() {
		return names;
	}

	public void setNames(List<String> names) {
		this.names = names;
	}

	public List<PublishURLVO> getUrls() {
		return urls;
	}

	public void setUrls(List<PublishURLVO> urls) {
		this.urls = urls;
	}

	public String getWsdl() {
		return wsdl;
	}

	public void setWsdl(String wsdl) {
		this.wsdl = wsdl;
	}

	public List<ProcessVO> getProcesses() {
		return processes;
	}

	public void setProcesses(List<ProcessVO> processes) {
		this.processes = processes;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getErrors() {
		return errors;
	}

	public void setErrors(String errors) {
		this.errors = errors;
	}

	public String getGunvorServerUrl() {
		return gunvorServerUrl;
	}

}
