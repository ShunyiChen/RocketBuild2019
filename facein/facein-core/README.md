# facematch
Face match in python using Facenet and their pretrained model

In Anaconda:
1.Open Anaconda prompt
2.activate tensorflow
3.Run python face_match_demo.py --img1=images/daniel-radcliffe_2.jpg --img2=images/daniel-radcliffe_4.jpg

In embeded Python
Install pip:
1.Download get-pip.py, then put this file into python folder
2.python get-pip.py
Prepare Tenserflow libs:
1.pip install scipy
2.pip install sklearn
3.pip install opencv-python
4.Run python face_match_demo.py --img1=images/daniel-radcliffe_2.jpg --img2=images/daniel-radcliffe_4.jpg


Cannot run program "python"问题解决
在dos中执行正常，在eclipse中报文中开头的错误；
调试发现，当指定Python 命令为全路径时，执行没问题；
最终解决：因为在windows下设置Python的path时eclipse是开启状态，导致path没有加载进去，所以无法执行；关闭eclipse，再次启动，执行正常