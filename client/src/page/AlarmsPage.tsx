import AlarmsItem from "../components/AlarmsItem";

const AlarmsPage = () => {
  return (
    <div>
      <span>Alarms!</span>
      <AlarmsItem
        title="🚀 새로운 잔소리 요청 글과, 댓글을 남기면 활동점수🎯가 올라갑니다 ! "
        icon="광고"
        contents="새로운 잔소리, 댓글을 남겨 TOP 랭킹에 들어보세요!"
        createdAt="03/07"
      />
      <AlarmsItem
        title="📢 “잔소리 오지게 해줄사람” 글에 인생은 혼자야님이 새 댓글을 달았어요."
        icon="댓글"
        contents="댓글을 확인해 보세요"
        createdAt="03/07"
      />
      <AlarmsItem
        title="활동점수를 올려 상위 랭크🥇에 도전해보세요! 엄청난 보상이 있을지도...?"
        icon="광고"
        contents="활동점수얻고 티어올려 상품 받자!"
        createdAt="03/07"
      />
      <AlarmsItem
        title="📢 “잔소리 오지게 해줄사람” 글에 인생은 혼자야님이 새 댓글을 달았어요."
        icon="댓글"
        contents="댓글을 확인해 보세요"
        createdAt="03/07"
      />
      <AlarmsItem
        title="📢 “잔소리 오지게 해줄사람” 글에 인생은 혼자야님이 새 댓글을 달았어요."
        icon="댓글"
        contents="댓글을 확인해 보세요"
        createdAt="03/07"
      />
    </div>
  );
};

export default AlarmsPage;
