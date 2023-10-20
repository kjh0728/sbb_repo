package com.mysite.sbb;

import com.mysite.sbb.Model.Entity.Question;
import com.mysite.sbb.Model.Repository.AnswerRepository;
import com.mysite.sbb.Model.Repository.QuestionRepository;
import com.mysite.sbb.Service.QuestionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionService questionService;

	@Test
	void testJpa()
	{
		for(int i = 1; i <= 300; i++)
		{
			String subject = String.format("테스트 데이터 입니다. : [%03d]", i);
			String content = "내용무";
			//this.questionService.create(subject,content);
		}
	}

}
