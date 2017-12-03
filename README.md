# URLShorten

�����ϰ���Ʈ ��������ķ�� 3�� ���� ���� �ڷ��Դϴ�.

# �ҽ� ����
main : ���� �Լ�
GUI : awt�� �̿��� GUI ����
RBTree : Red Black Tree
Base58 : binary-to-text encoding
URLShortner : URL ������ ���� Ŭ����


# ��ǥ

1. URL �Է� �� shortening ��� ���
2. ������ URL�� �Է� �� ��� �׻� ������ shortening ��� ���� ���;� ��
3. shortening ����� ��Ȯ�� 8���ڰ� �ǵ��� ������
4. (*)shortening�� ������ �� �Է� url�� ������ �� �ֵ��� ����ϰ� ����


# ���� ����

1) key 1
   1. URL�� �Ľ��Ͽ� host �ּҸ� ��󳽴�.
   2. www. ���� �����ϰ� �ٽ��� �Ǵ� �ܾ �̾Ƴ���.
   3. �ش� �ܾ��� ������ �����ϰ�
    (�� : www.google.com -> gg, www.youtube.com -> yt)
   4. �� ������ ���� ���� 2���ڸ� hashtable�� key�� Ȱ��
   
    (���� : SHRT : ���� �ܾ Ȱ���� URL ���� ��� http://www.kics.or.kr/Home/UserContents/20130703/130703_142813081.pdf)

2) key 2
   1. hashtable�� RBTree�� �迭�� value�� ������.
   2. ����Ʈ �ּ� ��ü�� CRC32�� ���ڵ��Ͽ� 16������ ���ڸ� �����Ѵ�.
   3. �ش� 16������ ���ڴ� Base58�� ���ڵ��� �� ���� 3���ڸ� ���´�. 
   4. �� ���ڴ� �迭�� �ּҰ����ε� ���δ�

3) key 3
   1. �ش� �迭 �ּҿ��� RedBlackTree�� �ִ�.
   2. RBTree�� Node�� value�� name�� ���´�.
   3. 

4) ���� �� ���
   1. ���� �ڷ�� ���� ���α׷� ���� �� xml ���·� ����ȴ�.
   2. �ٽ� ���� �� xml�� �Ľ��Ͽ� �ҷ����Ƿ� table�� �״�� �����ȴ�.
   3. �ش� �Է��� ����� http://localhost/****** �������� ����Ѵ�

5) ��Ÿ �߰� ���
   1. 8���� �̿��� �Է�, URL ������ �ƴ� �Է� ���� ����
   2. localhost ���� input Ȥ�� localhost �ڽ��� shortening ���� ����
   3. �ش� key�� ã�� �� ���� ��� not found ǥ��