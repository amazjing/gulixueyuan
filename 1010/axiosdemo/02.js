/*
 * @Date: 2022-11-30 20:35:24
 * @LastEditors: wwz
 * @LastEditTime: 2022-11-30 20:36:34
 * @FilePath: \1010\axiosdemo\02.js
 */
const http = require('http');
http.createServer(function (request, response) {
    // 发送 HTTP 头部
    // HTTP 状态值: 200 : OK
    // 内容类型: text/plain
    response.writeHead(200, {'Content-Type': 'text/plain'});
    // 发送响应数据 "Hello World"
    response.end('Hello Server');
    
 }).listen(8888);
 // 终端打印如下信息
console.log('Server running at http://127.0.0.1:8888/');