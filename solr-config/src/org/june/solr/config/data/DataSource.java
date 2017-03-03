package org.june.solr.config.data;
/**
 * 数据源信息
 * @author lwp
 *
 */
public class DataSource {
	private static String template=
			"<dataSource "
				+ "name=\"%s\" "
				+ "driver=\"%s\" "
				+ "password=\"%s\" "
				+ "type=\"%s\" "
				+ "url=\"%s\" "
				+ "user=\"%s\" "
				+ "batchSize=\"%s\" "
			+ "/>";
	private String name;
	private String driver;
	private String password;
	private String type = "JdbcDataSource";
	private String url;
	private String user;
	//批量提交
	private String batchSize;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDriver() {
		return driver;
	}
	public void setDriver(String driver) {
		this.driver = driver;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getType() {
		return type;
	}
//	public void setType(String type) {
//		this.type = type;
//	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	@Override
	public String toString(){
		return String.format(template, name,driver,password,type,url,user,batchSize);
	}
	public void parse(String xmlString){
		
	}
	public static void main(String[] args) {
		DataSource ds = new DataSource();
		ds.setDriver("oracle.jdbc.driver.OracleDriver");
		ds.setName("outer");
		ds.setPassword("dms");
		ds.setUrl("jdbc:oracle:thin:@10.110.1.12:1521:jcdb");
		ds.setUser("dms");
		System.out.println(ds);
	}
	public String getBatchSize() {
		return batchSize;
	}
	/**
	 * 在数据量较大时应配置该参数
	 * @param batchSize
	 */
	public void setBatchSize(String batchSize) {
		this.batchSize = batchSize;
	}
}
