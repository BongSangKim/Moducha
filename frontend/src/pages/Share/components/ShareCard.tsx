import { ShareListItem } from '../../../types/ShareType';
import { UserIcon } from '@heroicons/react/24/outline';
import { Link } from 'react-router-dom';

const ShareCard = ({
  boardId,
  title,
  maxParticipants,
  participants,
}: ShareListItem) => {
  return (
    <>
      <Link
        to={`${boardId}`}
        className="flex bg-base-100 overflow-hidden shadow rounded-lg transition ease-in-out hover:bg-[#E6F9E4] hover:shadow-lg duration-150"
      >
        <figure className="size-32 shrink-0">
          <img
            className=""
            src="https://the-chinese-tea-company.com/cdn/shop/files/Loose_Leaf_Tea_1600x.jpg?v=1613558529"
            alt="Album"
          />
        </figure>
        <article className="flex flex-col w-full p-2 h-32 overflow-hidden">
          <h2 className="font-bold truncate">{title}</h2>
          <section className="flex items-center pe-2 gap-1">
            <UserIcon className="inline size-6" />
            {/* progress바 -> value / max의 비율만큼 표시 */}
            <progress
              className="progress progress-success w-full"
              value={participants}
              max={maxParticipants}
            />
          </section>
        </article>
      </Link>
    </>
  );
};

export default ShareCard;
