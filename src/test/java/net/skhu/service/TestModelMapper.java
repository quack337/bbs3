package net.skhu.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import net.skhu.entity.Article;
import net.skhu.entity.User;
import net.skhu.entity.UserRole;
import net.skhu.model.ArticleDto;
import net.skhu.model.UserDto;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TestModelMapper {

	@Autowired ModelMapper modelMapper;

	ArticleDto createArticleDTO() {
		var a = new ArticleDto();
		a.setId(1);
		a.setTitle("title");
		a.setBody("body");
		return a;
	}

	User createUserEntity() {
		var user = new User();
		user.setId(2);
		user.setLoginName("user1");
		user.setPassword("pass1");
		user.setEmail("user1@skhu.net");
		user.setName("홍길동");

		var roles = new ArrayList<UserRole>();
		var role1 = new UserRole();
		role1.setId(1);
		role1.setUser(user);
		role1.setRole("ROLE_USER");
		roles.add(role1);
		var role2 = new UserRole();
		role2.setId(2);
		role2.setUser(user);
		role2.setRole("ROLE_STUDENT");
		roles.add(role2);
		user.setUserRoles(roles);
		return user;
	}

	Article createArticleEntity() {
		var article = new Article();
		article.setId(3);
		article.setTitle("title");
		article.setBoardId(2);
		article.setModifiedTime(new Date());
		article.setUser(createUserEntity());
		article.setBody("body");
		article.setNo(4);
		return article;
	}

	@Test
	void test1() {
		assert(modelMapper != null);

		var articleEntity = createArticleEntity();
		var articleDto = modelMapper.map(articleEntity, ArticleDto.class);
		assertEquals(articleDto.getUserName(), articleEntity.getUser().getName());
	}

	@Test
	void test2() {
		var userEntity = createUserEntity();
		var userDto = modelMapper.map(userEntity, UserDto.class);
		System.out.println(Arrays.toString(userDto.getRoles()));
		assertEquals(userDto.getRoles().length, 2);
		assertEquals(userDto.getRoles()[0], "ROLE_USER");
		assertEquals(userDto.getRoles()[1], "ROLE_STUDENT");
	}

	@Test
	void test3() {
		List<Integer> list1 = new ArrayList<>();
		list1.add(1);
		list1.add(2);
		list1.add(3);
		List<Character> list2 = modelMapper.map(list1, new TypeToken<List<Character>>() {}.getType());
		assertEquals(list1.size(), list2.size());
	}

	@Test
	void test4() {
		List<Article> list1 = new ArrayList<>();
		list1.add(createArticleEntity());
		List<ArticleDto> list2 = modelMapper.map(list1, new TypeToken<List<ArticleDto>>() {}.getType());
		assertEquals(list1.size(), list2.size());
		assertEquals(list1.get(0).getNo(), list2.get(0).getNo());
		assertEquals(list1.get(0).getTitle(), list2.get(0).getTitle());
		assertEquals(list1.get(0).getBody(), list2.get(0).getBody());
	}

}
