// import axios from "axios";
// import { useEffect, useState } from "react";
import styled from 'styled-components';
import RankItem from '../container/rank/RankItem';
import useGetRank from '../hooks/useGetRank';

interface dataProps {
  profileImageUrl: string;
  nickname: string;
  score: number;
  modifiedAt: string;
}

const RankPage = () => {
  const data: dataProps[] = useGetRank('/home/rank');
  const sortedData = data.sort((a: dataProps, b: dataProps) => b.score - a.score); //score 기준으로 내림차순 정렬
  return (
    <RankingWrapper>
      {sortedData.map(({ profileImageUrl, nickname, score }: dataProps, index: number) => (
        <RankItem key={index} index={index + 1} imgUrl={profileImageUrl === null ? '' : profileImageUrl} nickName={nickname} subText={`${score}`} />
      ))}
      <RankingDescription>시즌1 2023-03-29 ~</RankingDescription>
    </RankingWrapper>
  );
};

export default RankPage;

const RankingWrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  gap: 8px;
  user-select: none;
  padding: 0 16px;
`;

const RankingDescription = styled.span`
  padding-top: 1.2rem;
  padding-bottom: 1.2rem;
  width: 100%;
  text-align: center;
  font-size: var(--font-size14);
  color: var(--color-gray02);
  letter-spacing: var(--font-spacing-title);
`;
