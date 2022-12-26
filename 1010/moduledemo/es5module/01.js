/*
 * @Date: 2022-12-26 19:55:51
 * @LastEditors: wwz
 * @LastEditTime: 2022-12-26 20:32:41
 * @FilePath: \1010\moduledemo\es5module\01.js
 */
//创建js方法
// 定义成员：
 const sum = function(a,b){
     return parseInt(a) + parseInt(b)
 }
 const subtract = function(a,b){
     return parseInt(a) - parseInt(b)
 }

 //设置哪些方法可以被其他js调用
 module.exports = {
        sum,
        subtract
    }