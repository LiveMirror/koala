package org.openkoala.bpm.core;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

import org.dayatang.domain.AbstractEntity;
import org.dayatang.domain.CriteriaQuery;
/**
 *
 * @author lingen
 *
 */
@Entity
public class KoalaProcessInfo extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 包名，如defaultPackage
	 */
	private String packageName;
	
	/**
	 * 流程定义 ID，包括版本号的，如defaultPackage.Trade@2
	 */
	private String processId;
	
	/**
	 * 流程定义 ID，不包括版本号，如defaultPackage.Trade
	 */
	private String processName;
	
	
	private Date createDate;
	
	/**
	 * 版本号
	 */
	private int versionNum;
	
	private String description;
	
	private boolean isActive;
	
	/**
	 * 流程定义XML 的二进制存储
	 */
	
	private byte[] data;
	
	/**
	 * 图片的二进制存储
	 */
	
	private byte[] png;
	
	public KoalaProcessInfo() {
		super();
	}

	public KoalaProcessInfo(String porcessName,int version,String xmlData,byte[] png){
		this.processId = porcessName + "@"+version;
		this.processName = porcessName;
		this.versionNum = version;
		this.data = xmlData.getBytes();
		this.png = png;
	}
	
	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getVersionNum() {
		return versionNum;
	}

	public void setVersionNum(int versionNum) {
		this.versionNum = versionNum;
	}

	@Lob
	@Column(length=2147483647)
	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + Arrays.hashCode(data);
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((processId == null) ? 0 : processId.hashCode());
		result = prime * result
				+ ((processName == null) ? 0 : processName.hashCode());
		result = prime * result + this.versionNum;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KoalaProcessInfo other = (KoalaProcessInfo) obj;
		if (processId == null) {
			if (other.processId != null)
				return false;
		} else if (!processId.equals(other.processId))
			return false;
		if (processName == null) {
			if (other.processName != null)
				return false;
		} else if (!processName.equals(other.processName))
			return false;
		if (this.versionNum != other.versionNum)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "KoalaProcessInfo [processId=" + processId + ", processName="
				+ processName + ", createDate=" + createDate + ", version="
				+ versionNum + ", description=" + description + ", data="
				+ Arrays.toString(data) + "]";
	}

	@Lob
	@Column(length=2147483647)
	public byte[] getPng() {
		return png;
	}

	public void setPng(byte[] png) {
		this.png = png;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	/**
	 * 返回激活的流程
	 * @return
	 */
	public static List<KoalaProcessInfo> getActiveProcess(){
		String hql = "from KoalaProcessInfo k where k.active is true";
		List<KoalaProcessInfo> processes = getRepository().createJpqlQuery(hql).list();
		return processes;
	}
	
	/**
	 * 返回所有的包
	 * @return
	 */
	public static List<String> getPackages(){
		String hql = "select distinct k.packageName from KoalaProcessInfo k";
		List<String> packages = getRepository().createJpqlQuery(hql).list();
		return packages;
	}
	
	/**
	 * 返回一个包下的所有流程
	 * @param packageName
	 * @return
	 */
	public static List<String> getProcessByPackage(String packageName){
		String hql = "select distinct k.processName from KoalaProcessInfo k where k.packageName = :packageName";
		List<String> packages = getRepository().createJpqlQuery(hql).addParameter("packageName", packageName).list();
		return packages;
	}
	
	public static List<String> getProcess(){
		String hql = "select distinct k.processName from KoalaProcessInfo k";
		List<String> packages = getRepository().createJpqlQuery(hql).list();
		return packages;
	}
	
	/**
	 * 按流程名下的所有版本的流程
	 * @param processName
	 * @return
	 */
	public static List<KoalaProcessInfo> getProcessByProcessName(String processName){
		String hql = "from KoalaProcessInfo k where k.processName = :processName order by versionNum";
		List<KoalaProcessInfo> processes = getRepository().createJpqlQuery(hql).addParameter("processName", processName).list();
		return processes;
	}
	
	/**
	 * 返回一个流程下部署的所有版本
	 * @param processId
	 * @return
	 */
	public static List<KoalaProcessInfo> getProcessVersionByProcessId(String processId){
		return getRepository().createJpqlQuery("from KoalaProcessInfo k where k.processName =:processName").addParameter("processName", processId).list();
	}
	
	public static KoalaProcessInfo getProcessInfoByProcessNameAndVersion(String processName,int versionNum){
		String jpql = "from KoalaProcessInfo k where k.processName = :processName and k.versionNum = :versionNum and k.active is true";
		KoalaProcessInfo info =  getRepository().createJpqlQuery(jpql).addParameter("processName", processName).addParameter("versionNum", versionNum).singleResult();
		return info;
	}
	
	public void publishProcess(){
		//如果当前流程在数据库中没有激活的流程，则这一次始终为激活的流程
		//getRepository().executeUpdate("update KoalaProcessInfo k set k.isActive = false where k.processName = ?",  new Object[]{processName});
		setActive(true);
		setCreateDate(new Date());
	}

	@Override
	public String[] businessKeys() {
		return new String[]{};
	}

	public static KoalaProcessInfo findKoalaProcessInfo(
			Map<String, Object> params) {
		
		CriteriaQuery query =  getRepository().createCriteriaQuery(KoalaProcessInfo.class);
		Set<String> keys = params.keySet();
		for(String key:keys){
			query.eq(key, params.get(key));
		}
		return query.singleResult();
	}
	
}
