import BoardItem from "../container/naggingboard/BoardItem";

const NaggingBoardPage = () => {
  const dummyData = [
    { title: "잔소리 오지게 해줄 사람", nickname: "황금올리브닭다리", likeCount: 220, createdAt: "03/07", answerCount: 20 },
    { title: "잔소리 오지게 해줄 사람", nickname: "고양킹", likeCount: 100, createdAt: "03/07", answerCount: 20 },
    { title: "잔소리 오지게 해줄 사람", nickname: "강아지는귀여워", likeCount: 660, createdAt: "03/07", answerCount: 20 },
    { title: "잔소리 오지게 해줄 사람", nickname: "자르반 4세", likeCount: 603, createdAt: "03/07", answerCount: 20 },
    { title: "잔소리 오지게 해줄 사람", nickname: "피그마로 밥먹는 사람", likeCount: 610, createdAt: "03/07", answerCount: 20 },
    { title: "잔소리 오지게 해줄 사람", nickname: "백엔드의 황제", likeCount: 500, createdAt: "03/07", answerCount: 20 },
    { title: "잔소리 오지게 해줄 사람", nickname: "당신누구요", likeCount: 620, createdAt: "03/07", answerCount: 20 },
    { title: "잔소리 오지게 해줄 사람", nickname: "연진아너X됐어", likeCount: 699, createdAt: "03/07", answerCount: 20 },
    { title: "잔소리 오지게 해줄 사람", nickname: "리액트가뭔가요", likeCount: 601, createdAt: "03/07", answerCount: 20 },
    { title: "잔소리 오지게 해줄 사람", nickname: "코드로 밥먹기", likeCount: 654, createdAt: "03/07", answerCount: 20 },
    // 동점이면 날짜순으로 하는 코드 작성해야함 백엔드와 상의
  ];

  return (
    <div>
      {dummyData.map(({ title, nickname, likeCount, createdAt, answerCount }, index) => (
        <BoardItem key={index} title={title} likeCount={likeCount} nickname={nickname} createdAt={createdAt} answerCount={answerCount} />
      ))}
    </div>
  );
};

export default NaggingBoardPage;
