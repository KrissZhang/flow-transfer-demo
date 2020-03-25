-- 分页查询测试表数据
#sql("queryTestListPaging")
    SELECT B.ID,B.T_NAME,B.T_VALUE FROM (SELECT ROWNUM AS RN, A.* FROM (SELECT * FROM T_TABLE_1) A WHERE ROWNUM <= ?) B WHERE B.RN >= ?
#end