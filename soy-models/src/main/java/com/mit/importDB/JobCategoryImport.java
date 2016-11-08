package com.mit.importDB;

import com.mit.dao.salary.JobCategoryDAO;
import com.mit.entities.salary.JobCategory;

public class JobCategoryImport {
	public static void main(String[] args) {
		JobCategory jc = new JobCategory();
		jc.setName("Cong nghe thong tin");
		jc.setDesc("sortware and hardware");
		jc.setStatus(1);
		JobCategoryDAO.getInstance().insert(jc);
	}
}
