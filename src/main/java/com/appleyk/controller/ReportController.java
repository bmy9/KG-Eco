package com.appleyk.controller;

import java.util.List;

import com.appleyk.node.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appleyk.repository.ReportRepository;
import com.appleyk.result.ResponseMessage;
import com.appleyk.result.ResponseResult;

@RestController
@RequestMapping("/rest/appleyk/report")
public class ReportController {

	@Autowired
	ReportRepository reportRepository;

	/**
	 * 根据公司名查询Report实体
	 * 
	 * @param name
	 * @return
	 */
	@RequestMapping("/get")
	public List<Report> getPersons(@RequestParam(value = "name") String name) {
		return reportRepository.findByName(name);
	}

	/**
	 * 创建一个report节点
	 * 
	 * @param report
	 * @return
	 */
	@PostMapping("/save")
	public ResponseResult savePerson(@RequestBody Report report) {
		reportRepository.save(report);
		return new ResponseResult(ResponseMessage.OK);
	}

	
}
