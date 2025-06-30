package org.gdkm.springbootxiangmushizhan.service;

import com.github.pagehelper.PageInfo;
import org.gdkm.springbootxiangmushizhan.modle.domain.Article;

import java.util.List;

public interface IArticleService {
//    分页查询文章列表
    public PageInfo<Article> selectArticleWithPage(Integer page,Integer count);
//    统计热度排名前十的文章信息
    public List<Article> getHeatArticles();
    // 根据文章id查询单个文章详情
    Article selectArticleWithId(Integer id);
//    发布文章
    public void publish(Article article);
//    根据主键更新文章
    public void updateArticleWithId(Article article);
//    根据主键删除文章
    public void deleteArticleWithId(int id);
}
