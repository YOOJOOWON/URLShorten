# URLShorten

스마일게이트 서버개발캠프 3기 면접 과제 자료입니다.

# 소스 설명
main : 메인 함수
GUI : awt를 이용한 GUI 구현
RBTree : Red Black Tree
Base58 : binary-to-text encoding
URLShortner : URL 단축을 위한 클래스


# 목표

1. URL 입력 후 shortening 결과 출력
2. 동일한 URL을 입력 할 경우 항상 동일한 shortening 결과 값이 나와야 함
3. shortening 결과는 정확히 8글자가 되도록 구현함
4. (*)shortening가 가능한 한 입력 url을 연상할 수 있도록 비슷하게 구성


# 전개 과정

1) key 1
   1. URL을 파싱하여 host 주소를 골라낸다.
   2. www. 등을 제거하고 핵심이 되는 단어를 뽑아낸다.
   3. 해당 단어의 자음을 삭제하고
    (예 : "www.google.com -> gg", "www.youtube.com -> yt")
   4. 위 과정을 통해 얻은 2문자를 hashtable의 key로 활용
   
    (참조 : SHRT : 유사 단어를 활용한 URL 단축 기법 http://www.kics.or.kr/Home/UserContents/20130703/130703_142813081.pdf)

2) key 2
   1. hashtable은 RBTree의 배열을 value로 가진다.
   2. 사이트 주소 전체를 CRC32로 인코딩하여 16진법의 숫자를 도출한다.
   3. 해당 16진법의 숫자는 Base58로 인코딩한 후 뒤의 3문자를 따온다. 
   4. 이 숫자는 배열의 주소값으로도 쓰인다

3) key 3
   1. 해당 배열 주소에는 RedBlackTree가 있다.
   2. RBTree의 Node는 value와 name을 갖는다.
   3. 

4) 저장 및 출력
   1. 위의 자료는 이후 프로그램 종료 시 xml 형태로 저장된다.
   2. 다시 실행 시 xml을 파싱하여 불러오므로 table은 그대로 유지된다.
   3. 해당 입력의 결과는 "http://localhost/******" 형식으로 출력한다

5) 기타 추가 기능
   1. 8문자 이외의 입력, URL 형식이 아닌 입력 등을 차단
   2. localhost 외의 input 혹은 localhost 자신의 shortening 등을 차단
   3. 해당 key를 찾을 수 없는 경우 not found 표시
