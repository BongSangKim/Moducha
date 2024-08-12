import ShareCard from './components/ShareCard';
import ShareHeader from './components/ShareHeader';
import TitleCard from '../../components/Title/TitleCard';
import Pagination from '../../components/Pagination/Pagination';
import { ShareListItem } from '../../types/ShareType';
import { Link } from 'react-router-dom';
import SideLayout from '../../components/Layout/SideLayout';
import MainLayout from '../../components/Layout/MainLayout';
import ArticleNotFound from '../../components/Article/ArticleNotFound';
import useFetchList from '../../hooks/useFetchList';

const Share = () => {
  const {
    articleList: shareList,
    sort,
    setSort,
    pageData,
    isLoading,
  } = useFetchList('shares');

  return (
    <div className="grid grid-cols-10">
      {/* 좌측 사이드바 영역 */}
      <SideLayout></SideLayout>
      <MainLayout>
        <header>
          <TitleCard>
            <div className="flex justify-between items-center">
              <span className="text-disabled">나눔</span>
              <Link to={'write'} className="btn btn-sm text-wood bg-papaya">
                글쓰기
              </Link>
            </div>
          </TitleCard>
          <div className="divider"></div>
        </header>

        {!shareList.length && !isLoading && <ArticleNotFound />}
        
        {!!shareList.length && (
          <>
            <div className="flex justify-between">
              <ShareHeader {...{ sort, setSort }} />
            </div>

            <section
              id="share-list"
              className="my-4 grid gap-4 sm:grid-cols-2 2xl:grid-cols-3"
            >
              <ShareCardList shareItems={shareList} />
            </section>

            <footer className="flex justify-center">
              <Pagination {...pageData} />
            </footer>
          </>
        )}
      </MainLayout>
      {/* 우측 사이드바 영역 */}
      <SideLayout></SideLayout>
    </div>
  );
};

export default Share;

const ShareCardList = ({ shareItems }: { shareItems: ShareListItem[] }) => {
  return shareItems.map((shareItem) => (
    <ShareCard key={shareItem.boardId} {...shareItem} />
  ));
};
