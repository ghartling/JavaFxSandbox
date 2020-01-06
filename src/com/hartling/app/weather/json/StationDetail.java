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
@JsonPropertyOrder({ "stationId", "icaoId", "wbanId", "usaf", "wmoId", "networkCode", "latitude", "longitude",
		"elevation", "fipsCountryCode", "state", "stationName", "begin", "end" })
public class StationDetail {

	@JsonProperty("stationId")
	private String stationId;
	@JsonProperty("icaoId")
	private String icaoId;
	@JsonProperty("wbanId")
	private String wbanId;
	@JsonProperty("usaf")
	private String usaf;
	@JsonProperty("wmoId")
	private String wmoId;
	@JsonProperty("networkCode")
	private String networkCode;
	@JsonProperty("latitude")
	private String latitude;
	@JsonProperty("longitude")
	private String longitude;
	@JsonProperty("elevation")
	private String elevation;
	@JsonProperty("fipsCountryCode")
	private String fipsCountryCode;
	@JsonProperty("state")
	private String state;
	@JsonProperty("stationName")
	private String stationName;
	@JsonProperty("begin")
	private String begin;
	@JsonProperty("end")
	private String end;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

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

	@JsonProperty("wbanId")
	public String getWbanId() {
		return wbanId;
	}

	@JsonProperty("wbanId")
	public void setWbanId(String wbanId) {
		this.wbanId = wbanId;
	}

	@JsonProperty("usaf")
	public String getUsaf() {
		return usaf;
	}

	@JsonProperty("usaf")
	public void setUsaf(String usaf) {
		this.usaf = usaf;
	}

	@JsonProperty("wmoId")
	public String getWmoId() {
		return wmoId;
	}

	@JsonProperty("wmoId")
	public void setWmoId(String wmoId) {
		this.wmoId = wmoId;
	}

	@JsonProperty("networkCode")
	public String getNetworkCode() {
		return networkCode;
	}

	@JsonProperty("networkCode")
	public void setNetworkCode(String networkCode) {
		this.networkCode = networkCode;
	}

	@JsonProperty("latitude")
	public String getLatitude() {
		return latitude;
	}

	@JsonProperty("latitude")
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	@JsonProperty("longitude")
	public String getLongitude() {
		return longitude;
	}

	@JsonProperty("longitude")
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	@JsonProperty("elevation")
	public String getElevation() {
		return elevation;
	}

	@JsonProperty("elevation")
	public void setElevation(String elevation) {
		this.elevation = elevation;
	}

	@JsonProperty("fipsCountryCode")
	public String getFipsCountryCode() {
		return fipsCountryCode;
	}

	@JsonProperty("fipsCountryCode")
	public void setFipsCountryCode(String fipsCountryCode) {
		this.fipsCountryCode = fipsCountryCode;
	}

	@JsonProperty("state")
	public String getState() {
		return state;
	}

	@JsonProperty("state")
	public void setState(String state) {
		this.state = state;
	}

	@JsonProperty("stationName")
	public String getStationName() {
		return stationName;
	}

	@JsonProperty("stationName")
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@JsonProperty("begin")
	public String getBegin() {
		return begin;
	}

	@JsonProperty("begin")
	public void setBegin(String begin) {
		this.begin = begin;
	}

	@JsonProperty("end")
	public String getEnd() {
		return end;
	}

	@JsonProperty("end")
	public void setEnd(String end) {
		this.end = end;
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
