import { BoardType } from './BoardType';

export type ImageList = {
  id: number;
  url: string;
}[];

export interface ArticleDetail {
  boardType: BoardType;
  boardId: number;
  title: string;
  content: string;
  createdDate: string;
  lastUpdated: string;
  broadcastDate?: string;
  endDate: string;
  maxParticipants: number;
  participants: number;
  nickName: string;
  viewCount: number;
}
