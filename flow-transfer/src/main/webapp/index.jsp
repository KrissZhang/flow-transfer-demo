<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
    <form action="/upload" method="post" enctype="multipart/form-data">
        上传文件：<input type="file" name="upload" /><br>
        用户名：<input type="text" name="username"><br>
        密码：<input type="text" name="password"><br>
        <button type="submit">上传</button>
    </form>
  </body>
</html>
