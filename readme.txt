JSONConverter
������:
1. yandexconverter
	������:
	1.1 FileHandler: �������� ������� ������ � ������ �� �����. ��������� ������
	� ���� ������ �����. ���������� ������ ����� � ���� ������ �����.
	1.2. JSONConverter: -����������� ������ ����� � json-������ (url, content, title);
			    -����������� ������ json-�������� (url, content, title) � 
			    � ������ json-�������� (url, snippet). ������� ���������
			    html-�������, title ������������ � content, ���������
			    ��������� �� 300 �������� (�� ����� � �����������).
			    -����������� ������ json-�������� (url, (��snippet) � ������ �����.
			    (��� ����������� ������������ ������� ���������� Jackson JSON (http://jackson.codehaus.org/)
	1.3. JSONTest: ����� ������������ ������ �������. �� ����� ���� "intern-task.json.filtered",
	� ������� �������� ������ json-�������� (url, content, title), ����� ���� ������
	������������� � ������ json-�������� (url, snippet), ������� ������������ � ���� 
	intern-task_modified.json".
2. jsonobject
	������:
	2.1. InputJSONObject: ������������ ����� �����, ���������� ��� ����:
			      url, content � title. ������������ ����� json-������.
			      ������ json-������� ���������� ������� �� ����� �
			      ������������� � json-������ ������ OutputJSONObject �
			      �������� � ����
	2.2. OutputJSONObject: ������������ ����� �����, ���������� ��� ����:
			       url, snippet (snippet - ��� ������������ title
			       � content, ��������� �� 300 ��������). ������ ������
			       json-�������� ������������ � ����.
				
	