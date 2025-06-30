package org.gdkm.springbootxiangmushizhan.service.impl;

import com.github.pagehelper.PageHelper;
import org.gdkm.springbootxiangmushizhan.dao.ArticleMapper;
import org.gdkm.springbootxiangmushizhan.dao.CommentMapper;
import org.gdkm.springbootxiangmushizhan.dao.StatisticMapper;
import org.gdkm.springbootxiangmushizhan.modle.ResponseData.StaticticsBo;
import org.gdkm.springbootxiangmushizhan.modle.domain.Article;
import org.gdkm.springbootxiangmushizhan.modle.domain.Comment;
import org.gdkm.springbootxiangmushizhan.modle.domain.Statistic;
import org.gdkm.springbootxiangmushizhan.service.ISiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class SiteServiceImpl implements ISiteService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<Comment> recentComments(int limit) {
        PageHelper.startPage(1, limit>10 || limit<1 ? 10:limit);
        List<Comment> byPage = commentMapper.selectNewComment();
        return byPage;
    }

    @Override
    public List<Article> recentArticles(int limit) {
        PageHelper.startPage(1, limit>10 || limit<1 ? 10:limit);
        List<Article> list = articleMapper.selectArticleWithPage();
        for (int i = 0; i < list.size(); i++){
            Article article = list.get(i);
            Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        return list;
    }

    @Override
    public StaticticsBo getStatistics() {
        StaticticsBo staticticsBo = new StaticticsBo();
        Integer articles = articleMapper.countArticle();
        Integer comments = commentMapper.countComment();
        staticticsBo.setArticles(articles);
        staticticsBo.setComments(comments);
        return staticticsBo;
    }

    @Override
    public void updateStatistics(Article article) {
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
        statistic.setHits(statistic.getHits()+1);
        statisticMapper.updateArticleHitsWithId(statistic);
    }


}
