import { useEffect, useState } from 'react';
import { fetchArticleDetail } from '../../api/fetchArticle';
import { useParams } from 'react-router-dom';
import { TeatimeDetailItem } from '../../types/TeatimeType';
import { mockDetail } from '../../constants/teatimeMock';
import ArticleCard from '../../components/Article/ArticleCard';
import ArticleContent from '../../components/Article/ArticleContent';
import CommentList from '../../components/Comment/CommentList';

const TeatimeDetail = () => {
  const { boardId } = useParams();
  const [teatimeDetail, setsTeatimeDetail] = useState<TeatimeDetailItem>(
    mockDetail.data
  );

  useEffect(() => {
    if (boardId) {
      fetchArticleDetail({ boardType: 'teatimes', boardId })
        .then((res) => setsTeatimeDetail(res.data.data))
        .catch((err) => console.log(err));
    }
  }, [boardId]);
  // router 파라미터가 누락된 경우
  if (!boardId) return null;
  return (
    <div className="grid grid-cols-12">
      <aside className="hidden lg:flex col-span-2"></aside>
      <main className="col-span-12 lg:col-span-8 md:grid md:grid-cols-12">
        <section className="md:col-span-4 p-2">
          <ArticleCard {...{ boardType: 'teatimes', ...teatimeDetail }} />
        </section>
        <article className="md:col-span-8 p-2">
          <ArticleContent {...{ boardType: 'teatimes', ...teatimeDetail }}>
            <CommentList boardType="teatimes" boardId={parseInt(boardId)} />
          </ArticleContent>
        </article>
      </main>
      <aside className="hidden lg:flex col-span-2"></aside>
    </div>
  );
};

export default TeatimeDetail;