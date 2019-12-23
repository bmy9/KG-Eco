package com.appleyk.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.appleyk.process.ModelProcess;
import com.appleyk.repository.QuestionRepository;
import com.appleyk.service.QuestionService;
import com.hankcs.hanlp.dictionary.CustomDictionary;

@Service
@Primary
public class QuestionServiceImpl implements QuestionService {

	@Value("D:\\data-for-1.7.5 (1)\\data")
	private String rootDictPath;

	@Value("${HanLP.CustomDictionary.path.movieDict}")
	private String companyDictPath;

	@Value("${HanLP.CustomDictionary.path.genreDict}")
	private String genreDictPath;

	@Value("${HanLP.CustomDictionary.path.scoreDict}")
	private String scoreDictPath;

	@Autowired
	private QuestionRepository questionRepository;

	@Override
	public void showDictPath() {
		System.out.println("HanLP分词字典及自定义问题模板根目录：" + rootDictPath);
		System.out.println("用户自定义扩展词库【公司】：" + companyDictPath);
	}

	@Override
	public String answer(String question) throws Exception {

		ModelProcess queryProcess = new ModelProcess(rootDictPath);

		/**
		 * 加载自定义的电影字典 == 设置词性 nm 0
		 */

		loadMovieDict(companyDictPath);

		/**
		 * 加载自定义的类型字典 == 设置词性 ng 0
		 */
		loadGenreDict(genreDictPath);

		/**
		 * 加载自定义的评分字典 == 设置词性 x 0
		 */
		loadScoreDict(scoreDictPath);
		ArrayList<String> reStrings = queryProcess.analyQuery(question);
		int modelIndex = Integer.valueOf(reStrings.get(0));
		String answer = null;
		String title = "";
		String name = "";
		String type = "";
		String num = "";
		/**
		 * 匹配问题模板
		 */
		switch (modelIndex) {
			case 0:
				/**
				 * nm 市场类型 == 市场类型
				 */
				title = reStrings.get(1);
				num = questionRepository.getMarkettype(title);
				if (num != null) {
					answer = num;
					if(answer.equals("4.0")){
						answer = "深圳A";
						System.out.println(answer);
					}
					else if (answer.equals("1.0")){
						answer = "上海A";
					}
					else if(answer.equals("2.0")){
						answer = "上海B";
					}
					else if(answer.equals("8.0")){
						answer = "深圳B";
					}
					else if(answer.equals("16.0")){
						answer = "创业板";
					}
					else if (answer.equals("32.0")){
						answer = "科创板";
					}
				}
				else {
					answer = null;
				}
				break;
			case 1:
				/**
				 * nm 股票代码 == 股票代码
				 */
				title = reStrings.get(1);
				String releaseDate = questionRepository.getCompanyCode(title);
				if (releaseDate != null) {
					answer = releaseDate;
					while(answer.length() < 6){
						answer = "0" + answer;
					}
				} else {
					answer = null;
				}
				break;
			case 2:
				/**
				 * nm 行业 == 公司行业
				 */
				title = reStrings.get(1);
				List<String> types = questionRepository.getCompanyIndustry(title);
				if (types.size() == 0) {
					answer = null;
				} else {
					answer = types.toString().replace("[", "").replace("]", "");
				}
				break;
			case 3:
				/**
				 * nm 公司全称 == 公司全称
				 */
				title = reStrings.get(1);
				answer = questionRepository.getCompanyFull(title);
				break;
			case 4:
				/**
				 * nm 交易所 == 公司上市地
				 */
				title = reStrings.get(1);
				List<String> actors = questionRepository.getCompanyTradePlace(title);
				if (actors.size() == 0) {
					answer = null;
				} else {
					answer = actors.toString().replace("[", "").replace("]", "");
					if(answer == "2"){
						answer = "深圳";
					}
					else {
						answer = "上海";
					}
				}
				break;
			case 5:
				/**
				 * nnt 后利润 == 报告后利润
				 */
				name = reStrings.get(1);
				answer = questionRepository.getCompanyMoney(name);
				break;

			case 6:
				/**
				 * nm 季度 == 最近一次报告在哪个季度
				 */
				name = reStrings.get(1);
				List<String> actorMovies = questionRepository.getCompanySeason(name);
				if (actorMovies.size() == 0) {
					answer = null;
				} else {
					answer = actorMovies.toString().replace("[", "").replace("]", "");
				}
				break;

			case 7:
				name = reStrings.get(1);
				Integer count = questionRepository.getReportNum(name);
				if (count == null) {
					answer = null;
				} else {
					answer = String.valueOf(count) + "个年报";
				}
				break;
			case 8:
				/**
				 * nnt 年报日期 == 公司年报日期
				 */
				name = reStrings.get(1);
				answer = questionRepository.getReportDate(name);
				break;
			default:
				break;
		}

		System.out.println(answer);
		if (answer != null && !answer.equals("") && !answer.equals("\\N")) {
			return answer;
		} else {
			return "sorry,我没有找到你要的答案";
		}
	}

	/**
	 * 加载自定义字典
	 *
	 * @param path
	 */
	public void loadMovieDict(String path) {

		File file = new File(path);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			addCustomDictionary(br, 0);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * 加载自定义类别字典
	 *
	 * @param path
	 */
	public void loadGenreDict(String path) {

		File file = new File(path);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			addCustomDictionary(br, 1);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	public static String getType(Object obj) {
		return obj.getClass().getName();
	}

	/**
	 * 加载自定义字典
	 *
	 * @param path
	 */
	public void loadScoreDict(String path) {

		File file = new File(path);
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
			addCustomDictionary(br, 2);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * 添加自定义分词及其词性，注意数字0表示频率，不能没有
	 *
	 * @param br
	 * @param type
	 */
	public void addCustomDictionary(BufferedReader br, int type) {

		String word;
		try {
			while ((word = br.readLine()) != null) {
				switch (type) {
					/**
					 * 设置公司名词词性 == nm 0
					 */
					case 0:
						CustomDictionary.add(word, "nm 0");
						break;
					/**
					 * 设置公司类型名词 词性 == ng 0
					 */
					case 1:
						CustomDictionary.add(word, "ng 0");
						break;
					/**
					 * 设置公司年份 词性 == x 0
					 */
					case 2:
						CustomDictionary.add(word, "x 0");
						break;
					default:
						break;
				}
			}
			br.close();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
