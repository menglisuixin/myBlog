package org.gdkm.springbootxiangmushizhan.service;

import com.github.pagehelper.PageInfo;
import org.gdkm.springbootxiangmushizhan.modle.domain.Comment;
import java.util.List;

public interface ICommentService {
//    获取文章下的评论
    public PageInfo<Comment> getComments(Integer aid,int page,int count);
//    用户发表评论
    public void pushComment(Comment comment);
 // 分页查询所有评论
    public List<Comment> selectAllCommentsWithPage(int offset, int limit);

    // 根据评论id删除评论
    public void deleteCommentById(Integer id);
}
