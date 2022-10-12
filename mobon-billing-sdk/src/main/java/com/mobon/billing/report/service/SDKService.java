package com.mobon.billing.report.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mobon.billing.report.service.dao.PackageDao;

@Component
public class SDKService {

	@Autowired
	private PackageDao packageDao;
}
