import { useEffect, useState } from 'react';
import { useTeatime } from '../../../hooks/useTeatime';
import useAuthStore from '../../../stores/authStore';
import ApplyModal from '../../../components/Modal/ApplyModal';
import { useParams } from 'react-router-dom';

interface TeatimeParticipantButtonProps {
  boardType: string;
  title: string;
}

export default function TeatimeParticipantButton({
  boardType,
  title,
}: TeatimeParticipantButtonProps) {
  const { isLogin } = useAuthStore((state) => ({
    teatimeToken: state.teatimeToken,
    isLogin: state.isLoggedIn,
  }));
  const { boardId } = useParams<{ boardId: string }>();
  const [isApplied, setIsApplied] = useState<boolean>(false);
  const [isRoomOpen, setIsRoomOpen] = useState<boolean>(false);
  const { startTeatime, teatimeIsOpen, teatimeIsApplied } =
    useTeatime(boardType, title);

  useEffect(() => {
    async function getOpenData() {
      const isOpen = await teatimeIsOpen();
      setIsRoomOpen(isOpen);
    }
    async function getAppliedData() {
      const applied = await teatimeIsApplied();
      setIsApplied(applied);
    }
    getOpenData();
    getAppliedData();
  }, [isApplied, isRoomOpen]);

  const handleButtonClick = async () => {
    if (isApplied) {
      await startTeatime();
      setIsRoomOpen(true);
    } else {
      (document.getElementById('apply_modal') as HTMLDialogElement)?.showModal()
      setIsApplied(true);
    }
  };

  const checkDisableButton = () => {
    if (!isLogin) return true;
    if (isApplied && !isRoomOpen) return true;
    return false;
  };

  return (
    <>
      <button
        className="btn rounded bg-tea hover:bg-rose-400 text-white"
        onClick={handleButtonClick}
        disabled={checkDisableButton()}
      >
        {isApplied ? '티타임 시작하기' : '티타임 참여하기'}
      </button>
      <ApplyModal {...{ boardType, boardId: Number(boardId) }} />
    </>
  );
}
