package org.gdkm.springbootxiangmushizhan.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;
import org.gdkm.springbootxiangmushizhan.dao.ArticleMapper;
import org.gdkm.springbootxiangmushizhan.dao.CommentMapper;
import org.gdkm.springbootxiangmushizhan.dao.StatisticMapper;
import org.gdkm.springbootxiangmushizhan.modle.domain.Article;
import org.gdkm.springbootxiangmushizhan.modle.domain.Statistic;
import org.gdkm.springbootxiangmushizhan.service.IArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ArticleServiceImpl implements IArticleService {
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CommentMapper commentMapper;
    // 分页查询文章列表
    @Override
    public PageInfo<Article> selectArticleWithPage(Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<Article> articleList = articleMapper.selectArticleWithPage();
        // 封装文章统计数据
        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        PageInfo<Article> pageInfo=new PageInfo<>(articleList);
        return pageInfo;
    }

    // 统计前10的热度文章信息
    @Override
    public List<Article> getHeatArticles( ) {
        List<Statistic> list = statisticMapper.getStatistic();
        List<Article> articlelist=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Article article = articleMapper.selectArticleWithId(list.get(i).getArticleId());
            article.setHits(list.get(i).getHits());
            article.setCommentsNum(list.get(i).getCommentsNum());
            articlelist.add(article);
            if(i>=9){
                break;
            }
        }
        return articlelist;
    }
//    根据id查询单个文章详情，并使用redis缓存管理
    @Override
    public Article selectArticleWithId(Integer id){
        Article article = null;
        Object o = redisTemplate.opsForValue().get("article_"+id);
        if (o != null){
            article = (Article) o;
        }else {
            article = articleMapper.selectArticleWithId(id);
            if (article != null){
                redisTemplate.opsForValue().set("article_"+id,article);
            }
        }
        return article;
    }

//    发布文章

    @Override
    public void publish(Article article) {
//        去除表情
        article.setContent(EmojiParser.parseToAliases(article.getContent()));
        article.setCreated(new Date());
        article.setHits(0);
        article.setCommentsNum(0);
//        插入文章，同时插入文章统计数据
        articleMapper.publishArticle(article);
        statisticMapper.addStatistic(article);
    }
    //    更新文章

    @Override
    public void updateArticleWithId(Article article) {
        article.setModified(new Date());
        articleMapper.updateArticleWithId(article);
        redisTemplate.delete("article_"+article.getId());
    }

    @Override
    public void deleteArticleWithId(int id) {
        // 删除文章的同时，删除对应的缓存
        articleMapper.deleteArticleWithId(id);
        redisTemplate.delete("article_" + id);
        // 同时删除对应文章的统计数据
        statisticMapper.deleteStatisticWithId(id);
        // 同时删除对应文章的评论数据
        commentMapper.deleteCommentWithId(id);
    }
}
