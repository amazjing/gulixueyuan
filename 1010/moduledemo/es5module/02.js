/*
 * @Date: 2022-12-26 20:06:43
 * @LastEditors: wwz
 * @LastEditTime: 2022-12-26 20:34:06
 * @FilePath: \1010\moduledemo\es5module\02.js
 */
//调用01js方法
//1: 引入01.js文件
const m = require('./01.js')

//2: 调用01.js中的方法
console.log(m.sum(1,2))
console.log(m.subtract(10,3))
