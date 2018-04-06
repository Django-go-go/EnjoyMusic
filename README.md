这是我参加一个比赛的App,下面是设计的文档.

一.选题背景:

1.1选题背景:

听音乐是一种很好的娱乐方式,但是自己喜欢的,好听的歌曲总是要去发掘,但是我们并没有那个时间和精力去一首首的听.如果聚集大家的力量的话会不会更好?每个人把自己认为好的歌曲分享出来,可能我们就会发现自己一直在等待的音乐...

1.2项目意义:

帮助爱好音乐的人,找到属于他(她)的音乐.

二.可行性分析和目标群体:

2.1可行性分析:

每个人只需花几分钟把自己认为不错的音乐分享出去即可.

2.2目标群体:

爱好音乐的人.

三.作品功能与原型设计:

3.1总体功能结构:

支持网络音乐与本地音乐的播放
支持下载
支持分享
有自己专属的歌单

3.2具体功能模块设计:

网络音乐的接口来源于百度,音乐都是免费的.功能基于retrofit+okhttp
音乐的播放使用后台服务,避免被进程杀死.
播放界面自定义唱片样式
自定义专属歌单,还可以将歌单分享出去
每天可以获取到最新分享的歌曲,如果喜欢可以下载.功能基于intentService.
你也可以自己每天去分享歌曲
整体的架构是基于MVP模式,辅助以模板方法模式
用fragment来管理播放界面,简化布局.
登录功能基于bmob后台
分享功能基于bmob后台
图片加载基于Picasso

3.3界面设计:

本应用的亮点在于自定义旋转唱片,高斯模糊背景,切换歌曲时不会有突兀的感觉.
主界面以viewPager+fragment搭配,配以懒加载,避免卡顿,丝滑体验.
用自定义的弹窗代替原生弹窗,用来实现各种操作.
播放界面用viewPager进行歌曲切换.无限轮播.

四.作品实现和难点

4.1作品实现:

参照三.

4.2难点

接口的不稳定性.
本来是用的网易云的接口,但是无法生成外链播放器.后来改成百度的接口,缺陷就是收费音乐获取不到,不过我们的目的是歌曲分享,对于音乐的版权问题显然已经超出开发者的能力范围.我们可以将自己认为好的音乐下载下来,再通过本平台分享个其他人.

六.今后的想法
我觉得这个软件还可以增加用户之间的交流功能,成为一个分享音乐的社交平台,不用于利益,只为了爱音乐的人们.因为开发时间与精力的限制,这个想法只能留在将来了.

图片:
https://github.com/Jkingone/EnjoyMusic/blob/master/screenshots/Screenshot_2017-09-25-12-19-25.png
https://github.com/Jkingone/EnjoyMusic/blob/master/screenshots/Screenshot_2017-09-25-12-19-35.png
https://github.com/Jkingone/EnjoyMusic/blob/master/screenshots/Screenshot_2017-09-25-12-19-44.png
https://github.com/Jkingone/EnjoyMusic/blob/master/screenshots/Screenshot_2017-09-25-12-19-50.png
https://github.com/Jkingone/EnjoyMusic/blob/master/screenshots/Screenshot_2017-09-25-12-19-57.png
https://github.com/Jkingone/EnjoyMusic/blob/master/screenshots/Screenshot_2017-09-25-12-20-04.png
https://github.com/Jkingone/EnjoyMusic/blob/master/screenshots/Screenshot_2017-09-25-12-20-11.png
https://github.com/Jkingone/EnjoyMusic/blob/master/screenshots/Screenshot_2017-09-25-12-20-23.png
https://github.com/Jkingone/EnjoyMusic/blob/master/screenshots/Screenshot_2017-09-25-12-20-30.png
https://github.com/Jkingone/EnjoyMusic/blob/master/screenshots/Screenshot_2017-09-25-12-20-41.png
