package com.mit.models;

public class CommentModel {
	public static final CommentModel Instance = new CommentModel();

	private CommentModel() {

	}

//	public void likeCmt(long userId, long cmtId, int type) {
////		Map<String, Object> rs = new HashMap<String, Object>();
//		int err = FeedErrCode.SUCCESS;
//		Comment comment = CommentDAO.getInstance().getById(cmtId);
//		if (comment != null) {
//			boolean isUpdate = true;
//			boolean isFirstLike = false;
//			if (comment.isLiked(userId) && type == DISLIKE) {
//				comment.removeLikeUserIds(userId);
//			} else if (!comment.isLiked(userId) && type == LIKE) {
//				comment.addLikeUserIds(userId);
//				isFirstLike = !comment.isDisLiked(userId);
//			} else {
//				isUpdate = false;
//			}
//			if (isUpdate) {
//				CommentDAO.getInstance().update(comment);
//			}
//			if (err >= 0) {
//				err = FeedErrCode.SUCCESS;
//				comment = CommentDAO.getInstance().getById(cmtId);
////				rs.put("isLiked", comment.isLiked(userId));
////				rs.put("likes", comment.getNumLike());
//				if (comment instanceof FeedComment && isFirstLike) {
//					FeedComment fcmt = (FeedComment) comment;
//					Feed feed = FeedDAO.getInstance().getById(fcmt.getFeedId());
//					int socialType = fcmt.getCommentParentId() > 0 ? SocialNews.LIKE_REPLY_TYPE : SocialNews.LIKE_COMMENT_TYPE;
//					NewsModel.Instance.notifyFeed(fcmt.getFeedId(), fcmt.getFeedItemId(), userId,
//							socialType, feed.getFeedType(), cmtId, fcmt.getUserId());
//				}
//			}
//		}
////		rs.put("err", err);
//
////		return rs;
//	}
//	
//	public Map<String, Object> likeCommentCmn(long userId, long cmtId, int type) {
//		Map<String, Object> rs = new HashMap<>();
//		int err = FeedErrCode.SUCCESS; 
//		Comment cmt = CommentDAO.getInstance().getById(cmtId);
//		if (cmt != null) {
//			int offset = cmt.isLiked(userId) && type == DISLIKE ? -1 : !cmt.isLiked(userId) && type == LIKE ? 1 : 0;
//			int likes = cmt.getNumLike() + offset;
//			rs.put("likes", likes);
//			rs.put("isLiked", type == LIKE);
//		} else {
//			err = FeedErrCode.COMMENT_NOT_EXIST;
//		}
//		rs.put("err", err);
//		ProducerPush.send(ProducerTopic.COMMENT_LIKE, userId + "\t" + cmtId + "\t" + type);
//		
//		return rs;
//	}
//
//	public Map<String, Object> editComment(long userId, long commentId, String newContent) {
//		Map<String, Object> rs = new HashMap<String, Object>();
//		int err = FeedErrCode.SUCCESS;
//		Comment comment = CommentDAO.getInstance().getById(commentId);
//		if (comment == null) {
//			err = FeedErrCode.COMMENT_NOT_EXIST;
//		} else if (comment.getUserId() != userId) {
//			err = FeedErrCode.PERMISSION_LIMIT;
//		} else {
//			comment.setContent(newContent);
//			err = CommentDAO.getInstance().update(comment);
//			if (err >= 0) {
//				err = 0;
//				comment = CommentDAO.getInstance().getById(comment.getId());
//				rs.put("comment", comment.buildBaseCommentView(userId));
//			} else {
//				err = FeedErrCode.SERVER_ERROR;
//			}
//		}
//		rs.put("err", err);
//
//		return rs;
//	}
//
//	public Map<String, Object> getSliceCmtLikeUser(long commentId, long viewUserId, int count, int from) {
//		Map<String, Object> rs = new HashMap<String, Object>();
//		int err = FeedErrCode.SUCCESS;
//		boolean hasMore = false;
//		List<UserInfo> userInfos = null;
//		Comment comment = CommentDAO.getInstance().getById(commentId);
//		if (comment != null) {
//			List<Long> likeUserIds = comment.getLikeUserIds();
//			if (likeUserIds != null && likeUserIds.size() > from) {
//				if (likeUserIds.size() > from + count) {
//					hasMore = true;
//					likeUserIds = likeUserIds.subList(from, from + count);
//				} else {
//					likeUserIds = likeUserIds.subList(from, likeUserIds.size());
//				}
//				Map<Integer, UserInfo> userMap = UserInfoDAO.getInstance().getMapFromIdLList(likeUserIds);
//				userInfos = new ArrayList<>();
//				for (long id : likeUserIds) {
//					if (userMap.get(((int) id)) != null) {
//						userInfos.add(userMap.get((int) id));
//					}
//				}
//			}
//		} else {
//			err = FeedErrCode.COMMENT_NOT_EXIST;
//		}
//		rs.put("err", err);
//		rs.put("hasMore", hasMore);
//		rs.put("users", UserInfo.buildListSocialView(userInfos, viewUserId));
//		rs.put("totalUser", userInfos != null ? userInfos.size() : 0);
//
//		return rs;
//	}
//
//	public Map<String, Object> getSliceReplyUser(long commentId, long viewUserId, int count, int from) {
//		Map<String, Object> rs = new HashMap<String, Object>();
//		int err = FeedErrCode.SUCCESS;
//		boolean hasMore = false;
//		List<UserInfo> userInfos = null;
//		List<Long> userIds = CommentDAO.getInstance().getAllReplyUser(commentId, "createTime", -1);
//		if (userIds != null && !userIds.isEmpty()) {
//			Map<Long, UserInfo> userMap = UserInfoDAO.getInstance().getSliceFromIds(userIds, count + 1, from);
//			userInfos = new ArrayList<>();
//			if (userMap != null) {
//				for (long id : userIds) {
//					if (userMap.get(id) != null) {
//						userInfos.add(userMap.get(id));
//					}
//				}
//			}
//			if (userInfos.size() > count) {
//				hasMore = true;
//				userInfos = userInfos.subList(0, count);
//			}
//		}
//		rs.put("err", err);
//		rs.put("hasMore", hasMore);
//		rs.put("users", UserInfo.buildListSocialView(userInfos, viewUserId));
//		rs.put("totalUser", userInfos != null ? userInfos.size() : 0);
//
//		return rs;
//	}

	public static void main(String[] args) {

	}
}
