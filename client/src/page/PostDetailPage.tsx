import { useParams } from 'react-router-dom';
import PostDetail from '../container/postdetail/PostDetail';
import styled from 'styled-components';
import CountsBar from '../container/postdetail/CountsBar';
import Answer from '../container/postdetail/Answer';
import SubAnswer from '../container/postdetail/SubAnswer';
import axios from 'axios';
import { useEffect, useState } from 'react';
import getCookie from '../utils/cookieUtils';
import { usePage } from '../hooks/usePage';

type Post = {
  content: string;
  createdAt: Date;
  nickname: string;
  tag: string;
  title: string;
  answers: any;
  likeCount: number;
};

const PostDetailPage = () => {
  const { questionId } = useParams();
  const [post, setPost] = useState<Post | null>(null);
  const { setPostDetailHandler } = usePage();

  const postData = async () => {
    try {
      const response = await axios.get(`${process.env.REACT_APP_BASE_URL}/questions/${questionId}`);
      const { data } = response.data;
      console.log(data);
      console.log(data.likeCount);
      setPost(data); // 서버에서 발급한 토큰 등의 정보가 담긴 객체
      setPostDetailHandler(data.memberId, data.questionId);
    } catch (error) {
      console.error(error);
      return null;
    }
  };

  useEffect(() => {
    postData();
  }, []);

  return (
    <PostDetailWrapper>
      {post !== null && <PostDetail {...post} />}
      {post !== null && <CountsBar answer={post.answers.length} likeCount={post.likeCount} />}
      <AnswerWrapper>
        {post?.answers.length === 0 && <span>댓글이 없습니다.</span>}
        {post?.answers.map((el: { likeCount: number; answerStatus: string; content: string; createdAt: string; nickname: string; comment: [] }, index: number) => {
          return <Answer key={index} {...el} />;
        })}
        {/* <SubAnswer></SubAnswer> */}
      </AnswerWrapper>
    </PostDetailWrapper>
  );
};

export default PostDetailPage;

const PostDetailWrapper = styled.div`
  /* padding: 0 16px; */
`;

const AnswerWrapper = styled.div`
  padding: 0 16px;
  display: flex;
  flex-direction: column;
  gap: 30px;
`;
