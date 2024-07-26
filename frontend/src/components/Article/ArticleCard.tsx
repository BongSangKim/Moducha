import { ShareDetailItem } from '../../types/ShareType';
const ArticleCard = ({
  title,
  content,
  nickname,
  createdDate,
  endDate,
  viewCount,
  participants,
  maxParticipants,
}: ShareDetailItem) => {
  return (
    <div className="flex flex-col p-2 overflow-clip border shadow gap-4">
      <figure
        className="bg-contain bg-center bg-no-repeat h-48"
        style={{ backgroundImage: `url(/logo.png)` }}
      />
      <div id="card-body">
        <header>
          <div className="flex gap-1">
            <span>{nickname}</span>|<span>조회 {viewCount}</span>
          </div>
        </header>
        <div className="pl-0 flex flex-col">
          <span>작성 : {createdDate}</span>
          <span>신청 : {participants + ' / ' + maxParticipants}</span>
          <span id="share-end_date">기간 : {endDate}</span>
        </div>
      </div>
      <button className="btn bg-success hover:bg-rose-500 text-white">
        나눔 참여하기
      </button>
    </div>
  );
};

export default ArticleCard;
