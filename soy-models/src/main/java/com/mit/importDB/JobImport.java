package com.mit.importDB;

import com.mit.dao.salary.JobDAO;
import com.mit.entities.salary.Job;

public class JobImport {
	public static void main(String[] args) {
		Job job = new Job();
		job.setJobCategoryId(1L);
		job.setName("Hardware engineering");
		job.setStatus(1);
		JobDAO.getInstance().insert(job);
	}
}
