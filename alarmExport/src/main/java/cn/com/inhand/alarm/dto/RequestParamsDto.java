/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cn.com.inhand.alarm.dto;

import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author zhangyf
 */
public class RequestParamsDto {

    private String access_token;
    private String reportName;
    private Integer language;
    private Integer online;
    private Long startTime;
    private Long endTime;
    private String businessState;
    private Integer month;
    private String alarmLevel;
    private String alarmState;
    private String logLevel;
    private ObjectId deviceId;
    private String siteName;
    private ObjectId oId;
    private List<ObjectId> gatewayIds;

    public List<ObjectId> getGatewayIds() {
        return this.gatewayIds;
    }

    public void setGatewayIds(List<ObjectId> gatewayIds) {
        this.gatewayIds = gatewayIds;
    }

    public String getAccess_token() {
        return this.access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getReportName() {
        return this.reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public Integer getLanguage() {
        return this.language;
    }

    public void setLanguage(Integer language) {
        this.language = language;
    }

    public Integer getOnline() {
        return this.online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }

    public Long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getBusinessState() {
        return this.businessState;
    }

    public void setBusinessState(String businessState) {
        this.businessState = businessState;
    }

    public Integer getMonth() {
        return this.month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public String getAlarmLevel() {
        return this.alarmLevel;
    }

    public void setAlarmLevel(String alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public String getAlarmState() {
        return this.alarmState;
    }

    public void setAlarmState(String alarmState) {
        this.alarmState = alarmState;
    }

    public String getLogLevel() {
        return this.logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public ObjectId getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(ObjectId deviceId) {
        this.deviceId = deviceId;
    }

    public ObjectId getoId() {
        return this.oId;
    }

    public void setoId(ObjectId oId) {
        this.oId = oId;
    }

    public String getSiteName() {
        return this.siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }
}
