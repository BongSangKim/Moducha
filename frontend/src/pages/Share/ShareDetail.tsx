import { useEffect, useState } from 'react';
import { fetchArticleDetail } from '../../api/fetchArticle';
import { useParams } from 'react-router-dom';
import { ShareDetailItem } from '../../types/ShareType';
import ArticleCard from '../../components/Article/ArticleCard';
import ArticleContent from '../../components/Article/ArticleContent';
import CommentList from '../../components/Comment/CommentList';
import SideLayout from '../../components/Layout/SideLayout';
import ArticleLoading from '../../components/Loading/ArticleLoading';

const ShareDetail = () => {
  const { boardId } = useParams();
  const [shareDetail, setsShareDetail] = useState<ShareDetailItem | null>(null);

  useEffect(() => {
    if (boardId) {
      fetchArticleDetail({ boardType: 'shares', boardId })
        .then((res) => setsShareDetail(res.data.data))
        .catch((err) => console.log(err));
    }
  }, [boardId]);
  // router 파라미터가 누락된 경우
  if (!boardId) return null;
  if (!shareDetail) return <ArticleLoading />;
  return (
    <div className="grid grid-cols-10">
      <SideLayout></SideLayout>
      <main className="col-span-10 lg:col-span-6 md:grid md:grid-cols-12">
        <section className="md:col-span-4 p-2">
          <ArticleCard {...{ boardType: 'shares', ...shareDetail }} />
        </section>
        <article className="md:col-span-8 p-2">
          <ArticleContent {...{ ...shareDetail, boardType: 'shares' }}>
            <CommentList boardType="shares" boardId={parseInt(boardId)} />
          </ArticleContent>
        </article>
      </main>
      <SideLayout></SideLayout>
    </div>
  );
};

export default ShareDetail;
