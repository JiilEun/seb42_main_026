import { createGlobalStyle } from 'styled-components';
import 'styled-components';

export function style() {
  const GlobalStyle = createGlobalStyle`

main{
  position: relative;
  top: 48px;
  /* 92 - 44*/
  padding: 20px 0px 102px 0px;
  width: 100%;
  height: auto;
  min-height: 100vh;
  z-index: 0;
  background-color: var(--color-white01);
}

.App{
  position: relative;
  display: flex;
  justify-content: flex-start;
  flex-direction: column;
  max-width: 720px;
  min-width: 360px;
  width: 100%;
  box-shadow: 0 6px 20px rgba(0,0,0,.1)
}

#root {
  display: flex;
  justify-content: center;
  
}

html,body,div,span,object,iframe,h1,h2,h3,h4,h5,h6,p,blockquote,pre,abbr,address,cite,
code,del,dfn,em,img,ins,kbd,q,samp,small,strong,sub,sup,var,b,i,dl,dt,dd,ol,ul,li,fieldset,form,label,legend,table,caption,tbody,tfoot,thead,tr,th,td,article,aside,
canvas,details,figcaption,figure,footer,header,hgroup,menu,nav,section,summary,time,
mark,audio,video {
  margin: 0;
  padding: 0;
}
/*블록요소들을 이렇게 여백 0으로 초기화 시켜 모든 브라우저에서 앞으로 주는 여백이 동일한 결과로 적용되도록 함. 전체 선택자 *를 사용하는 것보다 이렇게 블록을 나열하면 성능이 올라감
*/
body {
  font-family: "Noto Sans KR", "Roboto",'Apple SD Gothic','맑은고딕','Nanum Gothic',sans-serif;;
}
html {
  /* ************************************************************
		종류:overflow(가로,세로)/overflow-x(가로)/overflow-y(세로)
		표현방식:auto/scroll/hidden 
	    ************************************************************ */
  overflow-y: scroll; /* 문서전체에 세로 스크롤바자국 항상 표시 */
  overflow-x: hidden; /* 문서전체에 가로 스크롤바자국 항상 숨김 */
  font-size: 16px;
  background-color: var(--color-white01);
}

article,aside,details,figcaption,figure,footer,header,hgroup,menu,nav,
section {
  display: block;
  /*시맨틱태그를 지원하지 않은 브라우저 
	-> 인식하지 못하는 태그를 인라인으로 인식하기때문에 강제로 블럭으로 설정함*/
}
li {
  list-style-type: none;
} /*목록에 점 없애기 */
a {
  text-decoration: none; /*하이퍼링크 밑줄 없애기*/
  color: inherit; /*하이퍼링크의 글자색 부모한테 상속받기*/
}

h1,h2,h3,h4,h5,h6 {
  font-size: 100%;
}
/*제목 태그들에 글자크기를 초기화하여 앞으로 부여하는 크기의 기준을 똑같이 설정함*/

body,input,textarea {
  font-size: 16px; /*모든글자 기본글자크기*/
  font-weight: 400; /* IE8 이하버전 대응 */
  letter-spacing: 0;
}
input,button {
  padding: 0;
  cursor: pointer;
}

img {
  vertical-align: top; /*이미지에 원치 않는 여백이 생성되는 브라우저 오류가 발생하는 경우가 있음*/
  font-size: 0; /*폰트 크기 초기화*/
  border: 0; /* 이미지 링크시 기본적으로 나타나는 테두리 제거 */
  user-select: none;
  -webkit-user-drag: none;
  -khtml-user-drag: none;
  -moz-user-drag: none;
  -o-user-drag: none;
}
table {
  border-collapse: collapse;
} /* 칸사이의 기본 여백 제거 및 중첩 테두리 상쇄 효과 */
input,
select {
  vertical-align: middle;
} /*문자와 나란히 입력박스를 배치하면 높낮이가 맞지않아 맞추기 위함 */
:root{
  --color-webMain: #FF4F6E;
  --color-mobMain: #FF607C;
  --color-white01: #ffffff;
  --color-black01:#212123;
  --color-gray01: #878B93;
  --color-gray02:#ABAEB4;
  --color-gray03: #D1D3D7;
  --color-gray04: #EEEEEF;
  --font-size20: 1.25rem;
  --font-size18: 1.125rem;
  --font-size16: 1rem;
  --font-size14: 0.875rem;
  --font-size12: 0.75rem;
  --font-size10: 0.625rem;
  --font-weight700: 700;
  --font-weight600: 600;
  --font-weight400: 400;
  --font-weight300: 300;
  --font-spacing-title: -0.05em;
  --font-spacing-bottom: -0.035em;
  }
  `;

  return { GlobalStyle };
}
