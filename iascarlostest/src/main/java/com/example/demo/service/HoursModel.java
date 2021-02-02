package com.example.demo.service;

public class HoursModel {

	public long totalHours;
	public long normalHours;
	public long nightHours;
	public long normalExtraHours;
	public long nightExtraHours;
	public long dominicalExtraHours;
	public long dominicalHours;

	public HoursModel() {

	}

	public HoursModel(long totalH, long normalH, long nightH, long normalEH, long nightEH, long dominicalH, long dominicalEH) {
		this.totalHours = totalH;
		this.normalHours = normalH;
		this.nightHours = nightH;
		this.normalExtraHours = normalEH;
		this.nightExtraHours = nightEH;
		this.dominicalExtraHours = dominicalEH;
		this.dominicalHours = dominicalH;
	}
}
