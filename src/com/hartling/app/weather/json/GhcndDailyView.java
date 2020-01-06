
package com.hartling.app.weather.json;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "stationId", "icaoId", "occurDate", "tmax", "tmin", "prcp", "snow", "snwd", "wsf2", "wsf5",
		"normalMonth", "normalDay", "normalFlag", "normalMax", "normalMin", "normalPrec" })
public class GhcndDailyView {

	@JsonProperty("id")
	private String id;
	@JsonProperty("stationId")
	private String stationId;
	@JsonProperty("icaoId")
	private String icaoId;
	@JsonProperty("occurDate")
	private String occurDate;
	@JsonProperty("tmax")
	private Double tmax;
	@JsonProperty("tmin")
	private Double tmin;
	@JsonProperty("prcp")
	private Double prcp;
	@JsonProperty("snow")
	private Double snow;
	@JsonProperty("snwd")
	private Double snwd;
	@JsonProperty("wsf2")
	private Double wsf2;
	@JsonProperty("wsf5")
	private Double wsf5;
	@JsonProperty("normalMonth")
	private Integer normalMonth;
	@JsonProperty("normalDay")
	private Integer normalDay;
	@JsonProperty("normalFlag")
	private String normalFlag;
	@JsonProperty("normalMax")
	private Double normalMax;
	@JsonProperty("normalMin")
	private Double normalMin;
	@JsonProperty("normalPrec")
	private Double normalPrec;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("id")
	public String getId() {
		return id;
	}

	@JsonProperty("id")
	public void setId(String id) {
		this.id = id;
	}

	@JsonProperty("stationId")
	public String getStationId() {
		return stationId;
	}

	@JsonProperty("stationId")
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}

	@JsonProperty("icaoId")
	public String getIcaoId() {
		return icaoId;
	}

	@JsonProperty("icaoId")
	public void setIcaoId(String icaoId) {
		this.icaoId = icaoId;
	}

	@JsonProperty("occurDate")
	public String getOccurDate() {
		return occurDate;
	}

	@JsonProperty("occurDate")
	public void setOccurDate(String occurDate) {
		this.occurDate = occurDate;
	}

	@JsonProperty("tmax")
	public Double getTmax() {
		return tmax;
	}

	@JsonProperty("tmax")
	public void setTmax(Double tmax) {
		this.tmax = tmax;
	}

	@JsonProperty("tmin")
	public Double getTmin() {
		return tmin;
	}

	@JsonProperty("tmin")
	public void setTmin(Double tmin) {
		this.tmin = tmin;
	}

	@JsonProperty("prcp")
	public Double getPrcp() {
		return prcp;
	}

	@JsonProperty("prcp")
	public void setPrcp(Double prcp) {
		this.prcp = prcp;
	}

	@JsonProperty("snow")
	public Double getSnow() {
		return snow;
	}

	@JsonProperty("snow")
	public void setSnow(Double snow) {
		this.snow = snow;
	}

	@JsonProperty("snwd")
	public Double getSnwd() {
		return snwd;
	}

	@JsonProperty("snwd")
	public void setSnwd(Double snwd) {
		this.snwd = snwd;
	}

	@JsonProperty("wsf2")
	public Double getWsf2() {
		return wsf2;
	}

	@JsonProperty("wsf2")
	public void setWsf2(Double wsf2) {
		this.wsf2 = wsf2;
	}

	@JsonProperty("wsf5")
	public Double getWsf5() {
		return wsf5;
	}

	@JsonProperty("wsf5")
	public void setWsf5(Double wsf5) {
		this.wsf5 = wsf5;
	}

	@JsonProperty("normalMonth")
	public Integer getNormalMonth() {
		return normalMonth;
	}

	@JsonProperty("normalMonth")
	public void setNormalMonth(Integer normalMonth) {
		this.normalMonth = normalMonth;
	}

	@JsonProperty("normalDay")
	public Integer getNormalDay() {
		return normalDay;
	}

	@JsonProperty("normalDay")
	public void setNormalDay(Integer normalDay) {
		this.normalDay = normalDay;
	}

	@JsonProperty("normalFlag")
	public String getNormalFlag() {
		return normalFlag;
	}

	@JsonProperty("normalFlag")
	public void setNormalFlag(String normalFlag) {
		this.normalFlag = normalFlag;
	}

	@JsonProperty("normalMax")
	public Double getNormalMax() {
		return normalMax;
	}

	@JsonProperty("normalMax")
	public void setNormalMax(Double normalMax) {
		this.normalMax = normalMax;
	}

	@JsonProperty("normalMin")
	public Double getNormalMin() {
		return normalMin;
	}

	@JsonProperty("normalMin")
	public void setNormalMin(Double normalMin) {
		this.normalMin = normalMin;
	}

	@JsonProperty("normalPrec")
	public Double getNormalPrec() {
		return normalPrec;
	}

	@JsonProperty("normalPrec")
	public void setNormalPrec(Double normalPrec) {
		this.normalPrec = normalPrec;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
