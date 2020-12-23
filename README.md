# DubboTest

#### 介绍
            DubboTest可以直接请求您的dubbo接口，在开发调试时希望能给您带来一些便利。
            开始使用：
            1.首先您需要先启动dubbo服务，暴漏您的接口。
            2.找到您需要调试的接口方法，右键选择RunDubboTest，这时你可以看到DubboTest的窗口弹出，
            而且随机生成了一些参数，你可以更改你的入参。参数确定好后点击run按钮就可以了。
            address输入框可以选择以下两种类型
            使用zookeeper地址：zookeeper://127.0.0.1:2181
            使用dubbo直连：dubbo://127.0.0.1:2288
            dubbo直连在初始化类时会快一些，不过您使用过的地址DubboTest会缓存起来以便下次使用时直接使用。
            在输入框后有save按钮，您可以选择保存当前使用地址用以下次可以选择，您也可以点击delete按钮来删除当前地址
            注意：
            methodType这个字段需要对应你的接口入参类型，一般是不需要更改，因为鼠标右键选择后会为你自动生成。
            当你的入参不是常规对象时，需要添加class字段来告诉dubbo你使用的是哪个类。
#### 软件架构
软件架构说明


#### 安装教程

1.  xxxx
2.  xxxx
3.  xxxx

#### 使用说明

1.  xxxx
2.  xxxx
3.  xxxx

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
