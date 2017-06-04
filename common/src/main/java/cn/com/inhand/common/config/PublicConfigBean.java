package cn.com.inhand.common.config;

import java.io.Serializable;

public class PublicConfigBean implements Serializable {

	@Override
	public String toString() {
		return "PublicConfigBean [apiServerIP=" + apiServerIP
				+ ", apiServerPort=" + apiServerPort + ", timeoutHttp="
				+ timeoutHttp + ", oauth2Server=" + oauth2Server
				+ ", fileServer=" + fileServer + ", dbConnNoAct=" + dbConnNoAct
				+ ", dbConnCheckInterval=" + dbConnCheckInterval
				+ ", taskStatusInterval=" + taskStatusInterval
				+ ", mongodbHost=" + mongodbHost + ", mongodbPort="
				+ mongodbPort + ", mongodbUsername=" + mongodbUsername
				+ ",mongodbReplicaSet=" + mongodbReplicaSet
				+ ", mongodbPassword=" + mongodbPassword
				+ ", mongodbWriteMode=" + mongodbWriteMode
				+ ", mongodbReadMode=" + mongodbReadMode
				+ ", mongodbRsName=" + mongodbRsName
				+ ", mqLoadBalancerHost=" + mqLoadBalancerHost
				+ ", mqLoadBalancerPort=" + mqLoadBalancerPort
				+ ", mqHost=" + mqHost
				+ ", mqPort=" + mqPort + ", mqUsername=" + mqUsername
				+ ", mqPassword=" + mqPassword + "]";
	}

	private String apiServerIP;
	private int apiServerPort;
	private int timeoutHttp;
	private String oauth2Server;
	private String fileServer;
	private int dbConnNoAct;
	private int dbConnCheckInterval;
	private int taskStatusInterval;
	private String mongodbHost;
	private int mongodbPort;
	private String mongodbReplicaSet;
	private String mongodbUsername;
	private String mongodbPassword;
	private String mongodbWriteMode;
	private String mongodbReadMode;
	private String mongodbRsName;
	private String mqLoadBalancerHost;
	private int mqLoadBalancerPort;
	private String mqHost;
	private int mqPort;
	private String mqUsername;
	private String mqPassword;
	private String logLevel;

	public String getApiServerIP() {
		return apiServerIP;
	}

	public void setApiServerIP(String apiServerIP) {
		this.apiServerIP = apiServerIP;
	}

	public int getApiServerPort() {
		return apiServerPort;
	}

	public void setApiServerPort(int apiServerPort) {
		this.apiServerPort = apiServerPort;
	}

	public int getTimeoutHttp() {
		return timeoutHttp;
	}

	public void setTimeoutHttp(int timeoutHttp) {
		this.timeoutHttp = timeoutHttp;
	}

	public String getOauth2Server() {
		return oauth2Server;
	}

	public void setOauth2Server(String oauth2Server) {
		this.oauth2Server = oauth2Server;
	}

	public String getFileServer() {
		return fileServer;
	}

	public void setFileServer(String fileServer) {
		this.fileServer = fileServer;
	}

	public int getDbConnNoAct() {
		return dbConnNoAct;
	}

	public void setDbConnNoAct(int dbConnNoAct) {
		this.dbConnNoAct = dbConnNoAct;
	}

	public int getDbConnCheckInterval() {
		return dbConnCheckInterval;
	}

	public void setDbConnCheckInterval(int dbConnCheckInterval) {
		this.dbConnCheckInterval = dbConnCheckInterval;
	}

	public int getTaskStatusInterval() {
		return taskStatusInterval;
	}

	public void setTaskStatusInterval(int taskStatusInterval) {
		this.taskStatusInterval = taskStatusInterval;
	}

	public String getMongodbHost() {
		return mongodbHost;
	}

	public void setMongodbHost(String mongodbHost) {
		this.mongodbHost = mongodbHost;
	}

	public int getMongodbPort() {
		return mongodbPort;
	}

	public void setMongodbPort(int mongodbPort) {
		this.mongodbPort = mongodbPort;
	}

	public String getMongodbReplicaSet() {
		return mongodbReplicaSet;
	}

	public void setMongodbReplicaSet(String mongodbReplicaSet) {
		this.mongodbReplicaSet = mongodbReplicaSet;
	}

	public String getMongodbUsername() {
		return mongodbUsername;
	}

	public void setMongodbUsername(String mongodbUsername) {
		this.mongodbUsername = mongodbUsername;
	}

	public String getMongodbPassword() {
		return mongodbPassword;
	}

	public void setMongodbPassword(String mongodbPassword) {
		this.mongodbPassword = mongodbPassword;
	}

	public String getMongodbWriteMode() {
		return mongodbWriteMode;
	}

	public void setMongodbWriteMode(String mongodbWriteMode) {
		this.mongodbWriteMode = mongodbWriteMode;
	}

	public String getMongodbReadMode() {
		return mongodbReadMode;
	}

	public void setMongodbReadMode(String mongodbReadMode) {
		this.mongodbReadMode = mongodbReadMode;
	}

	public String getMongodbRsName() {
		return mongodbRsName;
	}

	public void setMongodbRsName(String mongodbRsName) {
		this.mongodbRsName = mongodbRsName;
	}

	public String getMqLoadBalancerHost() {
		return mqLoadBalancerHost;
	}

	public void setMqLoadBalancerHost(String mqLoadBalancerHost) {
		this.mqLoadBalancerHost = mqLoadBalancerHost;
	}

	public int getMqLoadBalancerPort() {
		return mqLoadBalancerPort;
	}

	public void setMqLoadBalancerPort(int mqLoadBalancerPort) {
		this.mqLoadBalancerPort = mqLoadBalancerPort;
	}

	public String getMqHost() {
		return mqHost;
	}

	public void setMqHost(String mqHost) {
		this.mqHost = mqHost;
	}

	public int getMqPort() {
		return mqPort;
	}

	public void setMqPort(int mqPort) {
		this.mqPort = mqPort;
	}

	public String getMqUsername() {
		return mqUsername;
	}

	public void setMqUsername(String mqUsername) {
		this.mqUsername = mqUsername;
	}

	public String getMqPassword() {
		return mqPassword;
	}

	public void setMqPassword(String mqPassword) {
		this.mqPassword = mqPassword;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

}
