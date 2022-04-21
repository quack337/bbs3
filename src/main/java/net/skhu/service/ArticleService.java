package net.skhu.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import net.skhu.config.ModelMapperConfig.MyModelMapper;
import net.skhu.entity.Article;
import net.skhu.model.ArticleDto;
import net.skhu.model.Pagination;
import net.skhu.repository.ArticleRepository;
import net.skhu.repository.BoardRepository;

@Service
public class ArticleService {

    @Autowired UserService userService;
    @Autowired ArticleRepository articleRepository;
    @Autowired BoardRepository boardRepository;
    @Autowired MyModelMapper modelMapper;

    private static Sort orderBy = Sort.by(Sort.Direction.DESC, "id");

    public ArticleDto findById(int id) {
        var article = articleRepository.findById(id).get();
        return modelMapper.map(article, ArticleDto.class);
    }

    public List<ArticleDto> findAll(Pagination pagination) {
        int pg = pagination.getPg() - 1, sz = pagination.getSz(),
            si = pagination.getSi(), bd = pagination.getBd();
        String st = pagination.getSt();
        var pageRequest = PageRequest.of(pg, sz, orderBy);
        Page<Article> page = null;
        if (si == 1)
            page = articleRepository.findByBoardIdAndTitleContains(bd, st, pageRequest);
        else if (si == 2)
            page = articleRepository.findByBoardIdAndUserNameStartsWith(bd, st, pageRequest);
        else
            page = articleRepository.findByBoardId(bd, pageRequest);
        pagination.setRecordCount((int)page.getTotalElements());
        List<Article> articleEntities = page.getContent();
        List<ArticleDto> articleDtos = modelMapper.mapList(articleEntities, ArticleDto.class);
        for (int i = 0; i < articleDtos.size(); ++i) {
            Article article = articleEntities.get(i);
            ArticleDto articleDto = articleDtos.get(i);
            articleDto.setUserName(article.getUser().getName());
        }
        return articleDtos;
    }

}
