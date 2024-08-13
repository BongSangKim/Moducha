import { MutableRefObject, useEffect, useState } from 'react';
import { fetchReplyList } from '../api/fetchComment';
import { BoardType } from '../types/BoardType';
import { Comment } from '../types/CommentType';
import useIntersectionObserver from './useIntersectionObserver';

const useFetchReplyList = (
  boardType: BoardType,
  boardId: number,
  commentId: number,
  replyCount: number | undefined,
  sentinel: MutableRefObject<HTMLDivElement | null>
) => {
  const [replyList, setReplyList] = useState<Comment[]>([]);
  const [page, setPage] = useState(1);
  const [totalPage, setTotalPage] = useState(null);
  const handleObserver = () => {
    setPage((prev) => prev + 1);
  };
  const [observe, unobserve] = useIntersectionObserver(handleObserver);

  useEffect(() => {
    if (replyCount) {
      fetchReplyList({
        boardType: boardType,
        boardId,
        commentId,
        page,
        perPage: 10,
      }).then((res) => {
        if (res.status === 200) {
          setReplyList((prev) => [...prev, ...res.data.data]);
          setTotalPage(res.data.pagination.total);
        }
      });
    }
  }, [page]);

  useEffect(() => {
    if (!totalPage) {
      unobserve(sentinel.current);
    } else {
      observe(sentinel.current);
    }
    if (totalPage && page >= totalPage) {
      unobserve(sentinel.current);
    }
  });
  return { replyList, setReplyList };
};

export default useFetchReplyList;