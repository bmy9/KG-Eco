package com.appleyk.controller;

import java.util.List;

import com.appleyk.node.Company;
import com.appleyk.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appleyk.node.Company;
import com.appleyk.repository.CompanyRepository;
import com.appleyk.result.ResponseMessage;
import com.appleyk.result.ResponseResult;
import com.appleyk.result.ResultData;

@RestController
@RequestMapping("/rest/appleyk/company") //restful风格的api接口
public class CompanyController {

	@Autowired
	CompanyRepository companyRepository;

	/**
	 * 根据公司名查询公司实体
	 * @param title
	 * @return
	 */
	@RequestMapping("/get")
	public List<Company> getMovies(@RequestParam(value="title") String title){
		return companyRepository.findByTitle(title);
	}

	/**
	 * 创建一个公司节点
	 * @param company
	 * @return
	 */
	@PostMapping("/save")
	public ResponseResult saveMovie(@RequestBody Company company){
		companyRepository.save(company);
		return new ResponseResult(ResponseMessage.OK);
	}


	@RequestMapping("/query")
	public ResponseResult queryMovieTiles(){
		List<String> movieTiles = companyRepository.getCompanyTitles();
		ResultData<String> result = new ResultData<String>(ResponseMessage.OK, movieTiles);
		return new ResponseResult(result);
	}
}
