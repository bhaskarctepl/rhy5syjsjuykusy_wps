
package com.hillspet.wearables.request;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author vvodyaram
 *
 */
@ApiModel(description = "Contains all information that needed to create a campaign activity", value = "PointTrackerActivityRequest")
@JsonInclude(value = Include.NON_NULL)
public class PointTrackerActivityRequest {

	@ApiModelProperty(value = "activitiesId", required = true)
	private Integer activitiesId;

	@ApiModelProperty(value = "points", required = true)
	private Integer points;

	private String startDate;
	private String endDate;

	private List<PointTrackerMetricRequest> pointTrackerMetricRequest;

	public List<PointTrackerMetricRequest> getPointTrackerMetricRequest() {
		return pointTrackerMetricRequest;
	}

	public void setPointTrackerMetricRequest(List<PointTrackerMetricRequest> pointTrackerMetricRequest) {
		this.pointTrackerMetricRequest = pointTrackerMetricRequest;
	}

	public Integer getActivitiesId() {
		return activitiesId;
	}

	public void setActivitiesId(Integer activitiesId) {
		this.activitiesId = activitiesId;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return activitiesId + "#" + points + "#" + startDate + "#" + endDate;
	}
}
