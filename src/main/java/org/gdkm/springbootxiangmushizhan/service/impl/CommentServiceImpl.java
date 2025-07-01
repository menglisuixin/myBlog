package org.gdkm.springbootxiangmushizhan.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.gdkm.springbootxiangmushizhan.dao.CommentMapper;
import org.gdkm.springbootxiangmushizhan.dao.StatisticMapper;
import org.gdkm.springbootxiangmushizhan.modle.domain.Comment;
import org.gdkm.springbootxiangmushizhan.modle.domain.Statistic;
import org.gdkm.springbootxiangmushizhan.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private StatisticMapper statisticMapper;

//    根据文章id分页查询评论
    @Override
    public PageInfo<Comment> getComments(Integer aid, int page, int count) {
        PageHelper.startPage(page,count);
        List<Comment> commentList = commentMapper.selectCommentWithPage(aid);
        PageInfo<Comment> commentInfo = new PageInfo<>(commentList);
        return commentInfo;
    }
//用户发表评论
    @Override
    public void pushComment(Comment comment) {
        commentMapper.pushComment(comment);
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(comment.getArticleId());
        statistic.setCommentsNum(statistic.getCommentsNum()+1);
        statisticMapper.updateArticleCommentsWithId(statistic);
    }
 // 分页查询所有评论
    @Override
    public List<Comment> selectAllCommentsWithPage(int offset, int limit) {
        return commentMapper.selectAllCommentsWithPage(offset, limit);
    }

    // 根据评论id删除评论
    @Override
    public void deleteCommentById(Integer id) {
        commentMapper.deleteCommentById(id);
    }
}
